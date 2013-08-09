package com.frca.purtges.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.frca.purtges.Const.Ids;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by KillerFrca on 9.8.13.
 */
public class Gcm {
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTimeMs";

    public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;


    private GoogleCloudMessaging gcm;
    private AtomicInteger msgId = new AtomicInteger();
    private SharedPreferences prefs;
    private Context context;

    private String regid;

    private final Runnable registerCallback;

    public Gcm(Context context, Runnable registerCallback) {
        regid = getRegistrationId(context);
        this.registerCallback = registerCallback;

        if (regid.length() == 0) {
            registerBackground();
        } else
            onRegistered();

        gcm = GoogleCloudMessaging.getInstance(context);
    }

    public String getRegid() {
        return regid;
    }

    private void onRegistered() {
        registerCallback.run();
    }

    private String getRegistrationId(Context context) {
        if (!TextUtils.isEmpty(regid))
            return regid;

        final SharedPreferences prefs = getGCMPreferences(context);
        regid = prefs.getString(PROPERTY_REG_ID, "");
        if (regid.length() == 0)
            Log.v(this.getClass().getName(), "Registration not found.");
        else {
            int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
            int currentVersion = getAppVersion(context);
            if (registeredVersion != currentVersion || isRegistrationExpired())
                Log.v(this.getClass().getName(), "App version changed or registration expired.");
        }

        return regid;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(this.getClass().getSimpleName(), Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager() .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private boolean isRegistrationExpired() {
        final SharedPreferences prefs = getGCMPreferences(context);
        long expirationTime = prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
        return System.currentTimeMillis() > expirationTime;
    }

    private void registerBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg;
                try {
                    if (gcm == null)
                        gcm = GoogleCloudMessaging.getInstance(context);

                    regid = gcm.register(Ids.PROJECT_ID);
                    msg = "Device registered, registration id=" + regid;

                    setRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.v(this.getClass().getName(), "Device registered!");
                onRegistered();
            }

        }.execute(null, null, null);
    }

    private void setRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.v(this.getClass().getName(), "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        long expirationTime = System.currentTimeMillis() + REGISTRATION_EXPIRY_TIME_MS;

        Log.v(this.getClass().getName(), "Setting registration expiry time to " + new Timestamp(expirationTime));
        editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
        editor.commit();
    }


}
