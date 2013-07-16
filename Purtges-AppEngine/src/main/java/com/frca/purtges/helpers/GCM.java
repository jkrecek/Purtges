package com.frca.purtges.helpers;

import com.frca.purtges.Const.Ids;
import com.frca.purtges.DeviceData;
import com.frca.purtges.DeviceDataEndpoint;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by KillerFrca on 9.7.13.
 */
public class GCM {

    public static Result doSend(DeviceData deviceData, String message, Sender sender, DeviceDataEndpoint endpoint) throws IOException {

        if (endpoint == null)
            endpoint = new DeviceDataEndpoint();

        if (message.length() > 1000)
            message = message.substring(0, 1000) + "[...]";

        Message msg = new Message.Builder().addData("message", message).build();
        Result result = sender.send(msg, deviceData.getRegistrationId(), 5);
        if (result.getMessageId() != null) {
            String canonicalRegId = result.getCanonicalRegistrationId();
            if (canonicalRegId != null) {
                endpoint.removeDeviceData(deviceData.getRegistrationId());
                deviceData.setRegistrationId(canonicalRegId);
                //endpoint.insertDeviceData(deviceData);
            }
        } else {
            String error = result.getErrorCodeName();
            if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                endpoint.removeDeviceData(deviceData.getRegistrationId());
            }
        }

        return result;
    }

    public static Result doSend(DeviceData deviceData, String message, Sender sender) throws IOException {
        return doSend(deviceData, message, sender, null);
    }

    public static void doSend(Collection<DeviceData> deviceDatas, String message, DeviceDataEndpoint endpoint) throws IOException {
        if (endpoint == null)
            endpoint = new DeviceDataEndpoint();

        Sender sender = new Sender(Ids.API_KEY);
        for (DeviceData deviceUser : deviceDatas)
            doSend(deviceUser, message, sender, endpoint);
    }

    public static void doSend(Collection<DeviceData> deviceDatas, String message) throws IOException {
        doSend(deviceDatas, message, null);
    }


}
