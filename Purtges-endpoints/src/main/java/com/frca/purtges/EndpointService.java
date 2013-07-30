package com.frca.purtges;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frca.purtges.helpers.Result;
import com.frca.purtges.requests.RequestManager;
import com.frca.purtges.requests.callbacks.ResultCallback;
import com.frca.purtges.tunnel.EndpointTunnel;
import com.frca.purtges.userdataendpoint.model.UserData;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EndpointService extends Service {

    public static final int REQUEST_ACCOUNT_PICKER = 2;
    static final String PREF_ACCOUNT_NAME = "accountName";
    static final String DEVICE_REG_ID = "deviceId";
    static final String SPECIAL = "special";
    static final int SPECIAL_ID = 5;
    static final int GCM_SERVICE = 6;
    static final String ERROR = "error";
    static final String IDENTIFIER = "identifier";

    private SharedPreferences settings = null;

    private RequestManager requestMgr = null;

    public class LocalBinder extends Binder {
        public EndpointService getService() {
            return EndpointService.this;
        }
    }

    private EndpointTunnel tunnel;

    private final IBinder binder = new LocalBinder();

    public void binding(EndpointTunnel tunnel) {
        this.tunnel = tunnel;
        onBinded();
    }


    private void onBinded() {
        appendText("Starting '" + getClass().getName() + "'");

        settings = getSharedPreferences("test", 0);

        requestMgr = new RequestManager(tunnel.getActivity(), settings.getString(PREF_ACCOUNT_NAME, null));
        if (requestMgr.isPrepared())
            authAccount();
    }

    /*@Override
    public void onCreate() {
        super.onCreate();

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();

        GCMIntentService.onDestroy(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void selectedAccount(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, accountName);
        editor.commit();
        requestMgr.setAccountName(accountName);
        authAccount();
    }


    public void authAccount() {
        String deviceId = settings.getString(DEVICE_REG_ID, null);
        if (deviceId != null) {
            appendText("Device is already registred");
            finish();
        } else {
            appendText("Registering into GCM");
            if (GCMIntentService.register(this))
                onRegistered();
        }
    }

    public void onRegistered() {

        appendText("Registering device in endpoints");
        requestMgr.getOwnUserData(new ResultCallback() {
            @Override
            public void handleDone(Object result) {
                if (result == Result.ERROR) {
                    appendText("User not yet created, creting new one");
                    appendText("User must select displayName");
                } else {
                    appendText("User exists, registering now");
                    registerDevice((UserData) result);
                }
            }
        });
    }

    protected void registerDevice(UserData userData) {
        List<String> deviceIds = userData.getDeviceIds();
        String registration = GCMIntentService.getDeviceId(this);

        if (deviceIds == null)
            deviceIds = new ArrayList<String>();

        if (deviceIds.isEmpty() || !deviceIds.contains(registration)) {
            deviceIds.add(registration);
            userData.setDeviceIds(deviceIds);

            requestMgr.updateUserData(userData, new ResultCallback() {
                @Override
                public void handleDone(Object result) {
                    if (result == Result.ERROR) {
                        appendText("ERROR");
                    } else {
                        appendText("Everything went fine");
                        finish();
                    }
                }
            });
        } else {
            appendText("Device is already registred");
            finish();
        }
    }

    private void registerDeviceToData(UserData userData) {

    }

    protected void handleCreatingOfUser() {
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        final AlertDialog.Builder builder = new AlertDialog.Builder(tunnel.getActivity());
        builder.setTitle("Set display name")
            .setMessage("Please, set your display name")
            .setView(input)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EndpointService.this.appendText("Display Name selected, registring to endpoint!");

                    UserData userData = new UserData();
                    userData.setDisplayName(input.getText().toString());
                    userData.setDeviceIds(Collections.<String>emptyList());

                    requestMgr.insertUserData(userData, new ResultCallback() {
                        @Override
                        public void handleDone(Object result) {
                            if (result == Result.ERROR)
                                appendText("Something went bad when creating user");
                            else
                                registerDevice((UserData) result);
                        }
                    });

                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        tunnel.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.create().show();
            }
        });
    }

    public void appendText(final String text) {
        if (tunnel == null || tunnel.isActive())
            return;

        Log.d("LOG_APPEND", text);
        tunnel.getActivity().appendText(text);
    }

    public void onDeviceRegSaved() {

    }

    public void finish() {
        appendText("FINISH !!!");
        Toast.makeText(tunnel.getActivity(), "User registered ... I guess ...", Toast.LENGTH_LONG).show();
    }
}
