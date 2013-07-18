package com.frca.purtges;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frca.purtges.helpers.Result;
import com.frca.purtges.requests.RequestManager;
import com.frca.purtges.requests.callbacks.ResultCallback;
import com.frca.purtges.userdataendpoint.model.UserData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RegisterActivity extends Activity {

    public static final int REQUEST_ACCOUNT_PICKER = 2;
    static final String PREF_ACCOUNT_NAME = "accountName";
    static final String DEVICE_REG_ID = "deviceId";
    static final String SPECIAL = "special";
    static final int SPECIAL_ID = 5;
    static final int GCM_SERVICE = 6;
    static final String ERROR = "error";
    static final String IDENTIFIER = "identifier";

    private SharedPreferences settings = null;

    private TextView mStatus = null;
    private TextView mLog = null;

    public static RegisterActivity instance = null;

    private RequestManager requestMgr = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        mStatus = (TextView) findViewById(R.id.text);
        ((View)mStatus.getParent()).setVisibility(View.GONE);

        mLog = (TextView) findViewById(R.id.text_content);
        instance = this;

        appendText("Starting '" + getClass().getName() + "'");

        settings = getSharedPreferences("test", 0);

        requestMgr = new RequestManager(this, settings.getString(PREF_ACCOUNT_NAME, null));
        if (requestMgr.isPrepared())
            authAccount();
    }

    @Override
    protected void onResume() {
        super.onResume();

        instance = this;
    }

    @Override
    protected void onPause() {
        super.onPause();

        instance = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        GCMIntentService.onDestroy(this);
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
            finishActivityTimed(10);
        } else {
            appendText("Registering into GCM");
            if (GCMIntentService.register(this))
                onRegistered();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        selectedAccount(accountName);
                    } else {
                        appendText("ERROR:: No accountName selected!");
                    }
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getIntExtra(IDENTIFIER, 0) == GCM_SERVICE) {
            if (intent.getBooleanExtra(ERROR, true)) {
                appendText("Registration to GCM failed");
            } else {
                appendText("Registration to GCM was successfull");
                onRegistered();
            }
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleCreatingOfUser();
                        }
                    });

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
                        finishActivityTimed(10);
                    }
                }
            });
        } else {
            appendText("Device is already registred");
            finishActivityTimed(10);
        }
    }

    private void registerDeviceToData(UserData userData) {

    }

    protected void handleCreatingOfUser() {
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set display name")
            .setMessage("Please, set your display name")
            .setView(input)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    RegisterActivity.appendText("Display Name selected, registring to endpoint!");

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
        }).create().show();
    }

    public static void appendText(final String text) {
        if (instance == null)
            return;

        Log.d("LOG_APPEND", text);
        instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.mLog.setText(instance.mLog.getText().toString() + "\n --" + text);
                instance.mStatus.setText(text);
            }
        });
    }

    public void onDeviceRegSaved(/*DeviceData deviceData*/) {
        /*SharedPreferences.Editor editor = settings.edit();
        editor.putString(DEVICE_REG_ID, deviceData.getRegistrationId());
        editor.commit();
        finishActivityTimed(10);*/
    }

    /*private void runChecker(final String accountName) {
        appendText("Building checker");

        requestMgr = new RequestManager(this, accountName);

        final Checker checker = new Checker(new String[] {Ids.ANDROID_CLIENT_ID, Ids.WEB_CLIENT_ID}, Ids.ANDROID_AUDIENCE);

        new NetworkTask(new NetworkTask.BackgroundCallback() {
            @Override
            public Result run() {
                try {
                    appendText("Acquiring token for account " + accountName);
                    String token = requestMgr.getCredential().getToken();
                    appendText("Token acquired, checking it");
                    checker.check(token);
                    return Result.OK;
                } catch (IOException e) {
                    checker.setProblem("Network IO problem: " + e.getLocalizedMessage());
                } catch (GoogleAuthException e) {
                    checker.setProblem("Google Auth problem: " + e.getLocalizedMessage());
                }
                return Result.ERROR;
            }
        }, new NetworkTask.ForegroundCallback() {
            @Override
            public void run(Object result) {
                if (result == Result.OK)
                    appendText("Everything OK! Payload: " + checker.getPayload().toString());
                else if (result == Result.ERROR)
                    appendText("Checking failed! ERROR: " + checker.problem());
            }
        }).execute();
    }*/

    final Handler mHandler = new Handler();
    int steps = 0;

    private void finishActivityTimed(final int secs) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                steps = secs;
                mStatus.setText("Finishing activity");
                instance.mLog.setText(instance.mLog.getText().toString() + "\n ");
                tink();
            }
        });
    }

    private void tink() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int curSteps = steps--;
                if (curSteps > 0) {
                    instance.mLog.setText(instance.mLog.getText().toString() + String.valueOf(curSteps) + " ");
                    tink();
                } else {
                    finish();
                }
            }
        }, 1000);
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
