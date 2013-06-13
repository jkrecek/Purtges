package com.frca.gamingscheduler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.frca.gamingscheduler.userdeviceendpoint.Userdeviceendpoint;
import com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;

import java.io.IOException;
import java.net.URLEncoder;

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
    private final Userdeviceendpoint endpoint;

    protected static final String PROJECT_NUMBER = "428547134262";

    private static GoogleAccountCredential credential = null;

    public static void register(Context mContext) {
        GCMRegistrar.checkDevice(mContext);
        GCMRegistrar.checkManifest(mContext);
        GCMRegistrar.register(mContext, PROJECT_NUMBER);
    }

    public static void unregister(Context mContext) {
        GCMRegistrar.unregister(mContext);
    }

    public GCMIntentService() {
        super(PROJECT_NUMBER);
        if (credential == null) {
            Log.e(GCMIntentService.class.getName(), "No credentials set");
            stopSelf();
        } else if (credential.getSelectedAccountName() == null) {
            Log.e(GCMIntentService.class.getName(), "No accountname set");
            stopSelf();
        }

        Userdeviceendpoint.Builder endpointBuilder = new Userdeviceendpoint.Builder(
            AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
            credential);
        endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
    }

    public static GoogleAccountCredential getCredential() {
        return credential;
    }

    public static void setCredential(GoogleAccountCredential credential) {
        GCMIntentService.credential = credential;
    }

    @Override
    public void onError(Context context, String errorId) {

        sendNotificationIntent(
                context,
                "Registration with Google Cloud Messaging...FAILED!\n\n"
                        + "A Google Cloud Messaging registration error occured (errorid: "
                        + errorId
                        + "). "
                        + "Do you have your project number ("
                        + ("".equals(PROJECT_NUMBER) ? "<unset>"
                        : PROJECT_NUMBER)
                        + ")  set correctly, and do you have Google Cloud Messaging enabled for the "
                        + "project?", true, true);
    }

    /**
     * Called when a cloud message has been received.
     */
    @Override
    public void onMessage(Context context, Intent intent) {
        sendNotificationIntent(
                context,
                "Message received via Google Cloud Messaging:\n\n"
                        + intent.getStringExtra("message"), true, false);
    }

    /**
     * Called back when a registration token has been received from the Google
     * Cloud Messaging service.
     *
     * @param context the Context
     */
    @Override
    public void onRegistered(Context context, String registration) {
        UserDevice userDevice = null;

        try {
            RegisterActivity.appendText("Probing for existing UserDevice");
            userDevice = endpoint.getUserDevice(registration).execute();

            if (userDevice != null && registration.equals(userDevice.getDeviceRegID())) {
                RegisterActivity.appendText("UserDevice: " + userDevice.toString());
            } else {
                userDevice = null;
                RegisterActivity.appendText("Existing UserDevice not found");
            }

        } catch (IOException e) {
            // Ignore
        }

        try {
            if (userDevice == null) {
                RegisterActivity.appendText("Starting registration push to DataStore");

                userDevice = endpoint.insertUserDevice(registration).execute();
                RegisterActivity.appendText("Registration pushed");
            }
        } catch (final IOException e) {
            RegisterActivity.appendText("Exception " + e.getClass().getName() + ": " + e.getMessage() + "|\n   " + "Cause: " + e.getCause());

            Log.e(GCMIntentService.class.getName(),
                    "Exception received when attempting to register with server at "
                            + endpoint.getRootUrl(), e);

            return;
        }

        RegisterActivity.appendText("SUCCESS");
        RegisterActivity.instance.onRegistered(userDevice);
    }

    /**
     * Called back when the Google Cloud Messaging service has unregistered the
     * device.
     *
     * @param context the Context
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {

        if (registrationId != null && registrationId.length() > 0) {

            try {
                endpoint.removeUserDevice(registrationId).execute();
            } catch (IOException e) {
                Log.e(GCMIntentService.class.getName(),
                        "Exception received when attempting to unregister with server at "
                                + endpoint.getRootUrl(), e);
                sendNotificationIntent(
                        context,
                        "1) De-registration with Google Cloud Messaging....SUCCEEDED!\n\n"
                                + "2) De-registration with Endpoints Server...FAILED!\n\n"
                                + "We were unable to de-register your device from your Cloud "
                                + "Endpoints server running at "
                                + endpoint.getRootUrl() + "."
                                + "See your Android log for more information.",
                        true, true);
                return;
            }
        }

        sendNotificationIntent(
                context,
                "1) De-registration with Google Cloud Messaging....SUCCEEDED!\n\n"
                        + "2) De-registration with Endpoints Server...SUCCEEDED!\n\n"
                        + "Device de-registration with Cloud Endpoints server running at  "
                        + endpoint.getRootUrl() + " succeeded!", false, true);
    }

    /**
     * Generate a notification intent and dispatch it to the RegisterActivity.
     * This is how we get information from this service (non-UI) back to the
     * activity.
     * <p/>
     * For this to work, the 'android:launchMode="singleTop"' attribute needs to
     * be set for the RegisterActivity in AndroidManifest.xml.
     *
     * @param context               the application context
     * @param message               the message to send
     * @param isError               true if the message is an error-related message; false
     *                              otherwise
     * @param isRegistrationMessage true if this message is related to registration/unregistration
     */
    private void sendNotificationIntent(Context context, String message,
                                        boolean isError, boolean isRegistrationMessage) {
        /*Intent notificationIntent = new Intent(context, RegisterActivity.class);
        notificationIntent.putExtra("gcmIntentServiceMessage", true);
        notificationIntent.putExtra("registrationMessage",
                isRegistrationMessage);
        notificationIntent.putExtra("error", isError);
        notificationIntent.putExtra("message", message);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(notificationIntent);*/
    }

    private String getWebSampleUrl(String endpointUrl) {
        // Not the most elegant solution; we'll improve this in the future
        if (CloudEndpointUtils.LOCAL_ANDROID_RUN) {
            return CloudEndpointUtils.LOCAL_APP_ENGINE_SERVER_URL
                    + "index.html";
        }
        return endpointUrl.replace("/_ah/api/", "/index.html");
    }
}
