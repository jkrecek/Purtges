package com.frca.purtges.requests;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.frca.purtges.BackgroundTask;
import com.frca.purtges.Const.Ids;
import com.frca.purtges.GCMIntentService;
import com.frca.purtges.RegisterActivity;
import com.frca.purtges.devicedataendpoint.model.DeviceData;
import com.frca.purtges.requests.EndpointHolder;
import com.frca.purtges.userdataendpoint.model.UserData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.io.IOException;

/**
 * Created by KillerFrca on 15.7.13.
 */
public class RequestManager {

    private GoogleAccountCredential credential;

    private EndpointHolder endpoints;

    public RequestManager(Activity activity, String selectedAccount) {
        credential = GoogleAccountCredential.usingAudience(activity, Ids.AUDIENCE_SCOPE);
        endpoints = new EndpointHolder(credential);

        if (selectedAccount != null) {
            credential.setSelectedAccountName(selectedAccount);
        }
        else
            activity.startActivityForResult(credential.newChooseAccountIntent(), RegisterActivity.REQUEST_ACCOUNT_PICKER);
    }

    public boolean isPrepared() {
        if (TextUtils.isEmpty(credential.getSelectedAccountName()))
            return false;

        return true;
    }
    public void setAccountName(String selectedAccount) {
        credential.setSelectedAccountName(selectedAccount);
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }

    public EndpointHolder getEndpoints() {
        return endpoints;
    }

    private boolean isValid(com.google.api.client.json.GenericJson result) {
        if (result == null)
            return false;

        if (result.get("error_message") != null && result.get("kind") != null)
            return false;

        return true;
    }

    public void insertDeviceData(final DeviceData deviceData, BackgroundTask.ForegroundCallback callback) {
        new BackgroundTask(new BackgroundTask.BackgroundCallback() {
            @Override
            public Object run() {
                try {
                    DeviceData result = endpoints.deviceData().insertDeviceData(deviceData).execute();
                    if (!isValid(result))
                        return BackgroundTask.Result.ERROR;
                    else
                        return result;
                } catch (IOException e) {
                    Log.e("REQUEST_MANAGER", "Error at insertDeviceData: " + e.getClass().getName() + ": " + e.getMessage());
                    return BackgroundTask.Result.ERROR;
                }
            }
        }, callback).execute();
    }

    public void getDeviceData(final String id, BackgroundTask.ForegroundCallback callback) {
        new BackgroundTask(new BackgroundTask.BackgroundCallback() {
            @Override
            public Object run() {
                try {
                    DeviceData result = endpoints.deviceData().getDeviceData(id).execute();
                    if (!isValid(result))
                        return BackgroundTask.Result.ERROR;
                    else
                        return result;
                } catch (IOException e) {
                    Log.e("REQUEST_MANAGER", "Error at getDeviceData: " + e.getClass().getName() + ": " + e.getMessage());
                    return BackgroundTask.Result.ERROR;
                }
            }
        }, callback).execute();
    }

    public void removeDeviceData(final String id, BackgroundTask.ForegroundCallback callback) {
        new BackgroundTask(new BackgroundTask.BackgroundCallback() {
            @Override
            public Object run() {
                try {
                    DeviceData result = endpoints.deviceData().removeDeviceData(id).execute();
                    if (!isValid(result))
                        return BackgroundTask.Result.ERROR;
                    else
                        return result;
                } catch (IOException e) {
                    Log.e("REQUEST_MANAGER", "Error at removeDeviceData: " + e.getClass().getName() + ": " + e.getMessage());
                    return BackgroundTask.Result.ERROR;
                }
            }
        }, callback).execute();
    }

    public void insertUserData(final String displayName, BackgroundTask.ForegroundCallback callback) {
        new BackgroundTask(new BackgroundTask.BackgroundCallback() {
            @Override
            public Object run() {
                try {
                    UserData result = endpoints.userData().insertUserData(displayName).execute();
                    if (!isValid(result))
                        return BackgroundTask.Result.ERROR;
                    else
                        return result;
                } catch (IOException e) {
                    Log.e("REQUEST_MANAGER", "Error at insertUserData: " + e.getClass().getName() + ": " + e.getMessage());
                    return BackgroundTask.Result.ERROR;
                }
            }
        }, callback).execute();
    }


}
