package com.frca.purtges;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.frca.purtges.tunnel.EndpointTunnel;

public abstract class AppActivity extends FragmentActivity {

    protected EndpointTunnel tunnel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tunnel = EndpointTunnel.invokeTunnel(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tunnel.setActive(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        tunnel.setActive(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        tunnel.doUnbindService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EndpointService.REQUEST_ACCOUNT_PICKER:
                if (data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        tunnel.getService().selectedAccount(accountName);
                    } else {
                        Log.e("AppActivity", "ERROR:: No accountName selected!");
                    }
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getIntExtra(EndpointService.IDENTIFIER, 0) == EndpointService.GCM_SERVICE) {
            if (intent.getBooleanExtra(EndpointService.ERROR, true)) {
                Log.e("AppActivity", "Registration to GCM failed");
            } else {
                Log.e("AppActivity", "Registration to GCM was successfull");
                tunnel.getService().onRegistered();
            }
        }
    }

    public abstract void appendText(String text);
}
