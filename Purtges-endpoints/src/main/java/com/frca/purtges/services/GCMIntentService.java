package com.frca.purtges.services;

import android.content.Context;
import android.content.Intent;

import com.frca.purtges.Const.Ids;


import com.frca.purtges.tunnel.EndpointTunnel;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

/**
 * This class is started up as a service of the Android application. It listens
 * for Google Cloud Messaging (GCM) messages directed to this device.
 * <p/>
 * When the device is successfully registered for GCM, a message is sent to the
 * App Engine backend via Cloud Endpoints, indicating that it wants to receive
 * broadcast messages from the it.
 * <p/>
 * Before registering for GCM, you have to create a project in Google's Cloud
 * Console (https://code.google.com/apis/console). In this project, you'll have
 * to enable the "Google Cloud Messaging for Android" Service.
 * <p/>
 * Once you have set up a project and enabled GCM, you'll have to set the
 * PROJECT_NUMBER field to the project number mentioned in the "Overview" page.
 * <p/>
 * See the documentation at
 * http://developers.google.com/eclipse/docs/cloud_endpoints for more
 * information.
 */
public class GCMIntentService extends GCMBaseIntentService {

    public GCMIntentService() {
        super(Ids.PROJECT_NUMBER);
    }

    public static boolean register(Context mContext) {
        if (!GCMRegistrar.isRegistered(mContext)) {
            GCMRegistrar.checkDevice(mContext);
            GCMRegistrar.checkManifest(mContext);
            GCMRegistrar.register(mContext, Ids.PROJECT_NUMBER);
            return false;
        } else
            return true;
    }

    public static void unregister(Context mContext) {
        GCMRegistrar.unregister(mContext);
    }

    public static void onDestroy(Context mContext) {
        GCMRegistrar.onDestroy(mContext);
    }


    public static String getDeviceId(Context mContext) {
        return GCMRegistrar.getRegistrationId(mContext);
    }

    @Override
    public void onError(Context context, String errorId) {
        //EndpointService.appendText("App error");
        EndpointTunnel.getInstance().getService().onGCMResult(true);
    }

    @Override
    public void onMessage(Context context, Intent intent) {

    }

    @Override
    public void onRegistered(Context context, String registration) {
        //EndpointService.appendText("App registered");
        EndpointTunnel.getInstance().getService().onGCMResult(false);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {

        /*if (registrationId != null && registrationId.length() > 0) {

            try {
                EndpointHolder.deviceData().removeDeviceData(registrationId).execute();
            } catch (IOException e) {
                Log.e(GCMIntentService.class.getName(),
                        "Exception received when attempting to unregister with server at "
                                + EndpointHolder.deviceData().getRootUrl(), e);

                return;
            }
        }*/
    }

    /*private void sendResultToUI(Context context, boolean isError) {
        EndpointTunnel.getInstance().getService().onRegistered();
        Intent notificationIntent = new Intent(context, EndpointService.class);
        notificationIntent.putExtra(EndpointService.IDENTIFIER, EndpointService.GCM_SERVICE);
        notificationIntent.putExtra(EndpointService.ERROR, isError);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(notificationIntent);
    }*/
}