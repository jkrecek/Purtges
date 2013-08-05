package com.frca.purtges.activities;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.frca.purtges.services.EndpointService;
import com.frca.purtges.tunnel.EndpointTunnel;

//public abstract class AppActivity extends FragmentActivity {
public abstract class AppActivity extends ActionBarActivity {

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

    public abstract void appendText(String text);
}
