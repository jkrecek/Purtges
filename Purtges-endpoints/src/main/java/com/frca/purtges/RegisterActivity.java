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
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frca.purtges.Const.Ids;
import com.frca.purtges.devicedataendpoint.model.DeviceData;
import com.frca.purtges.requests.EndpointHolder;
import com.frca.purtges.requests.RequestManager;
import com.google.android.gms.auth.GoogleAuthException;

import java.io.IOException;

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

        //getAccount();

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

    public void selectedAccount(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, accountName);
        editor.commit();
        requestMgr.setAccountName(accountName);
        authAccount();
    }


    public void authAccount() {
        String deviceId = null;//settings.getString(DEVICE_REG_ID, null);
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
        appendText("DAFUQ?!?!");
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                appendText("end");
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

        if (intent.getIntExtra(IDENTIFIER, 0)  == GCM_SERVICE) {
            if (intent.getBooleanExtra(ERROR, true)) {
                appendText("Registration to GCM failed");
            } else {
                appendText("Registration to GCM was successfull");
                onRegistered();
            }
        }
    }

    public void onRegistered() {
        final String registrationId = GCMIntentService.getDeviceId(this);
        final Context context = this;
        requestMgr.getDeviceData(registrationId, new BackgroundTask.ForegroundCallback() {
            @Override
            public void run(Object object) {
                if (object != null && object instanceof DeviceData) {
                    appendText("Device registration found");
                    DeviceData deviceData = (DeviceData) object;
                    if (registrationId.equals(deviceData.getRegistrationId())) {
                        appendText("User device registration matches: " + deviceData.getRegistrationId());
                        onDeviceRegSaved(deviceData);
                        // TODO
                        // callX
                        return;
                    } else
                        appendText("User device registration mismatch");
                }

                final DeviceData deviceData = new DeviceData();
                deviceData.setRegistrationId(registrationId);

                requestMgr.insertDeviceData(deviceData, new BackgroundTask.ForegroundCallback() {
                    @Override
                    public void run(Object object) {
                        if (object == BackgroundTask.Result.OK) {
                            appendText("Registration successfully pushed!");
                            onDeviceRegSaved(deviceData);
                            // TODO
                            // callX
                        } else if (object == BackgroundTask.Result.ERROR) {
                            appendText("Device registration error, creting new user");
                            final EditText input = new EditText(context);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);

                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Set display name")
                                .setMessage("Please, set your display name")
                                .setView(input)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        RegisterActivity.appendText("Display Name selected, registring to endpoint!");
                                        final String displayName = input.getText().toString();
                                        requestMgr.insertUserData(displayName, new BackgroundTask.ForegroundCallback() {
                                            @Override
                                            public void run(Object object) {
                                                if (object == BackgroundTask.Result.ERROR) {
                                                    appendText("Something went terribly wrong :(");
                                                } else {
                                                    appendText("User registered successfully, registering device again");
                                                    requestMgr.insertDeviceData(deviceData, new BackgroundTask.ForegroundCallback() {
                                                        @Override
                                                        public void run(Object object) {
                                                            if (object == BackgroundTask.Result.ERROR) {
                                                                appendText("Something went terribly wrong :(");
                                                            } else {
                                                                appendText("Device registered successfully");
                                                                onDeviceRegSaved(deviceData);
                                                                // TODO
                                                                // callX
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();

                        } else {
                            appendText("Unexpected result when inserting device data " + object.toString());
                        }
                    }
                });
            }
        });
    }

    public static void appendText(final String text) {
        if (instance == null)
            return;

        instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.mLog.setText(instance.mLog.getText().toString() + "\n --" + text);
                instance.mStatus.setText(text);
            }
        });
    }

    public void onDeviceRegSaved(DeviceData deviceData) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(DEVICE_REG_ID, deviceData.getRegistrationId());
        editor.commit();
        finishActivityTimed(10);
    }

    private void runChecker(final String accountName) {
        appendText("Building checker");

        requestMgr = new RequestManager(this, accountName);

        final Checker checker = new Checker(new String[] {Ids.ANDROID_CLIENT_ID, Ids.WEB_CLIENT_ID}, Ids.ANDROID_AUDIENCE);

        new BackgroundTask(new BackgroundTask.BackgroundCallback() {
            @Override
            public BackgroundTask.Result run() {
                try {
                    appendText("Acquiring token for account " + accountName);
                    String token = requestMgr.getCredential().getToken();
                    appendText("Token acquired, checking it");
                    checker.check(token);
                    return BackgroundTask.Result.OK;
                } catch (IOException e) {
                    checker.setProblem("Network IO problem: " + e.getLocalizedMessage());
                } catch (GoogleAuthException e) {
                    checker.setProblem("Google Auth problem: " + e.getLocalizedMessage());
                }
                return BackgroundTask.Result.ERROR;
            }
        }, new BackgroundTask.ForegroundCallback() {
            @Override
            public void run(Object result) {
                if (result == BackgroundTask.Result.OK)
                    appendText("Everything OK! Payload: " + checker.getPayload().toString());
                else if (result == BackgroundTask.Result.ERROR)
                    appendText("Checking failed! ERROR: " + checker.problem());
            }
        }).execute();
    }

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
