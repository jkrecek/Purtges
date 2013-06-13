package com.frca.gamingscheduler;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

/*import com.frca.gamingscheduler.messageEndpoint.MessageEndpoint;
import com.frca.gamingscheduler.messageEndpoint.model.CollectionResponseMessageData;
import com.frca.gamingscheduler.messageEndpoint.model.MessageData;
import com.frca.gamingscheduler.teaminfoendpoint.Teaminfoendpoint;
import com.frca.gamingscheduler.teaminfoendpoint.model.TeamInfo;*/
import com.frca.gamingscheduler.Const.Ids;
import com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public class RegisterActivity extends Activity {

    static final int REQUEST_ACCOUNT_PICKER = 2;
    static final String PREF_ACCOUNT_NAME = "accountName";
    static final String DEVICE_REG_ID = "deviceId";

    enum State {
        REGISTERED, REGISTERING, UNREGISTERED, UNREGISTERING
    }

    private State curState = State.UNREGISTERED;
    //private MessageEndpoint messageEndpoint = null;
    //private Teaminfoendpoint teaminfoEndpoint = null;
    private Button mSendButton = null;

    private SharedPreferences settings = null;
    private GoogleAccountCredential credential = null;

    private TextView mStatus = null;
    private TextView mLog = null;

    public static RegisterActivity instance = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        mStatus = (TextView) findViewById(R.id.text);
        mLog = (TextView) findViewById(R.id.text_content);
        instance = this;

        appendText("Starting '" + getClass().getName() + "'");

        appendText("Creating initial data");

        settings = getSharedPreferences("test", 0);
        credential = GoogleAccountCredential.usingAudience(this, "server:client_id:" + Ids.ANDROID_AUDIENCE);

        checkAccount();

        /*mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curState == State.REGISTERED) {
                    EditText text = (EditText) findViewById(R.id.textEdit);
                    new InsertMessageTask(RegisterActivity.this, messageEndpoint, text.getText().toString()).execute();
                } else
                    Toast.makeText(RegisterActivity.this, "Register first", Toast.LENGTH_LONG).show();
            }
        });*/

    }

    /*private void setEndpoints() {
        MessageEndpoint.Builder endpointBuilder = new MessageEndpoint.Builder(
            AndroidHttp.newCompatibleTransport(),
            new JacksonFactory(),
            GCMIntentService.getCredential());

        messageEndpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();

        Teaminfoendpoint.Builder teamInfoBuilder = new Teaminfoendpoint.Builder(
            AndroidHttp.newCompatibleTransport(),
            new JacksonFactory(),
            GCMIntentService.getCredential());

        teaminfoEndpoint = CloudEndpointUtils.updateBuilder(teamInfoBuilder).build();
    }*/

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

    private void checkAccount() {
        appendText("Checking account");
        String accName = settings.getString(PREF_ACCOUNT_NAME, null);
        if (accName != null) {
            credential.setSelectedAccountName(accName);
            appendText("Account name already set");
            checkDeviceId();
        } else {
            appendText("Waiting for selecting your account");
            startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
        }

    }

    private void checkDeviceId() {
        GCMIntentService.setCredential(credential);

        appendText("Account selected, checking registration");
        String deviceId = settings.getString(DEVICE_REG_ID, null);
        if (deviceId != null) {
            appendText("Device is already registred");
            finishActivityTimed(10);
            //finish();
        } else {
            updateState(State.REGISTERING);
            try {
                appendText("Registering into GCM");
                GCMIntentService.register(this);
            } catch (Exception e) {
                appendText("ERROR:: " + RegisterActivity.class.getName() + ": Unable to register");
                updateState(State.UNREGISTERED);
            }
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
                        setAccountName(accountName);
                        checkDeviceId();
                    } else {
                        appendText("ERROR:: No accountName selected!");
                    }
                }
                break;
        }
    }

    private void setAccountName(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, accountName);
        editor.commit();
        credential.setSelectedAccountName(accountName);
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

    public void onRegistered(UserDevice userDevice) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(DEVICE_REG_ID, userDevice.getDeviceRegID());
        editor.commit();
        finishActivityTimed(10);
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

    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getBooleanExtra("gcmIntentServiceMessage", false)) {

            showDialog(intent.getStringExtra("message"));

            if (intent.getBooleanExtra("registrationMessage", false)) {

                if (intent.getBooleanExtra("error", false)) {

                    if (curState == State.REGISTERING) {
                        updateState(State.UNREGISTERED);
                    } else {
                        updateState(State.REGISTERED);
                    }
                } else {

                    if (curState == State.REGISTERING) {
                        updateState(State.REGISTERED);
                    } else {
                        updateState(State.UNREGISTERED);
                    }
                }
            } else {
                new QueryMessagesTask(this, messageEndpoint).execute();
            }
        }
    }*/

    private void updateState(State newState) {
        curState = newState;
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



    /*private class QueryMessagesTask
            extends AsyncTask<Void, Void, CollectionResponseMessageData> {
        Exception exceptionThrown = null;
        MessageEndpoint messageEndpoint;

        public QueryMessagesTask(Activity activity, MessageEndpoint messageEndpoint) {
            this.messageEndpoint = messageEndpoint;
        }

        @Override
        protected CollectionResponseMessageData doInBackground(Void... params) {
            try {
                CollectionResponseMessageData messages =
                        messageEndpoint.listMessages().setLimit(5).execute();
                return messages;
            } catch (IOException e) {
                exceptionThrown = e;
                return null;
                //Handle exception in PostExecute
            }
        }

        protected void onPostExecute(CollectionResponseMessageData messages) {
            // Check if exception was thrown
            if (exceptionThrown != null) {
                Log.e(RegisterActivity.class.getName(),
                        "Exception when listing Messages", exceptionThrown);
                showDialog("Failed to retrieve the last 5 messages from " +
                        "the endpoint at " + messageEndpoint.getBaseUrl() +
                        ", check log for details");
            } else {
                TextView messageView = (TextView) findViewById(R.id.msgView);
                messageView.setText("Last 5 Messages read from " +
                        messageEndpoint.getBaseUrl() + ":\n");
                for (MessageData message : messages.getItems()) {
                    messageView.append(message.getMessage() + "\n");
                }
            }
        }
    }

    private class InsertMessageTask
        extends AsyncTask<Void, Void, Void> {
        Exception exceptionThrown = null;
        MessageEndpoint messageEndpoint;
        String message;

        public InsertMessageTask(Activity activity, MessageEndpoint messageEndpoint, String message) {
            this.messageEndpoint = messageEndpoint;
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                messageEndpoint.sendMessage(message).execute();
                //Toast.makeText(RegisterActivity.this, "Message2 '" + info.getTeamName() + "' saved", Toast.LENGTH_LONG).show();
                return null;
            } catch (IOException e) {
                exceptionThrown = e;
                return null;
                //Handle exception in PostExecute
            }
        }

        protected void onPostExecute(Void messages) {
            RegisterActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (exceptionThrown == null)
                        Toast.makeText(RegisterActivity.this, "Message2 '" + message + "' saved", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(RegisterActivity.this, "Message2: '" + exceptionThrown.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

            return;
        }
    }

    private class InsertTeaminfoTask
        extends AsyncTask<Void, Void, Void> {
        Exception exceptionThrown = null;
        Teaminfoendpoint teaminfoEndpoint;
        TeamInfo info;

        public InsertTeaminfoTask(Activity activity, Teaminfoendpoint teaminfoEndpoint, TeamInfo info) {
            this.teaminfoEndpoint = teaminfoEndpoint;
            this.info = info;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                teaminfoEndpoint.insertTeamInfo(info).execute();
                //Toast.makeText(RegisterActivity.this, "Message2 '" + info.getTeamName() + "' saved", Toast.LENGTH_LONG).show();
                return null;
            } catch (IOException e) {
                exceptionThrown = e;
                return null;
                //Handle exception in PostExecute
            }
        }

        protected void onPostExecute(Void messages) {
            RegisterActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (exceptionThrown == null)
                        Toast.makeText(RegisterActivity.this, "Message2 '" + info.getTeamName() + "' saved", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(RegisterActivity.this, "Message2: '" + exceptionThrown.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

            return;
        }
    }*/
}
