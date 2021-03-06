package com.frca.purtges.services;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.frca.purtges.gcm.Gcm;
import com.frca.purtges.helpers.Result;
import com.frca.purtges.requests.RequestManager;
import com.frca.purtges.requests.callbacks.ResultCallback;
import com.frca.purtges.tunnel.EndpointTunnel;
import com.frca.purtges.userdataendpoint.model.UserData;

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

    private Gcm gcm = null;

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
            gcm = new Gcm(this, new Runnable() {
                @Override
                public void run() {
                    onGCMResult(false);
                }
            });
        }
    }

    public void onGCMResult(boolean error) {

        if (error) {
            appendText("Error when registering into GCM");
            return;
        }

        appendText("Registering device in endpoints");
        requestMgr.getOwnUserData(new ResultCallback() {
            @Override
            public void handleDone(Object result) {
                if (result == Result.ERROR) {
                    appendText("User not yet created, creting new one");
                    appendText("User must select displayName");
                    handleCreatingOfUser();
                } else {
                    appendText("User exists, registering now");
                    registerDevice((UserData) result);
                }
            }
        });
    }

    protected void registerDevice(UserData userData) {
        String registration = gcm.getRegid();

        List<String> deviceIds = userData.getDeviceIds();
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
                    appendText("Display Name selected, registring to endpoint!");

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
        Log.d("LOG_APPEND", tunnel.isActive() ? text : "'Tunnel down!': " + text);
        if (tunnel.isActive())
            tunnel.getActivity().appendText(text);
    }

    public void onDeviceRegSaved() {

    }

    public void finish() {
        appendText("FINISH !!!");
        Toast.makeText(tunnel.getActivity(), "User registered ... I guess ...", Toast.LENGTH_LONG).show();
    }
}
