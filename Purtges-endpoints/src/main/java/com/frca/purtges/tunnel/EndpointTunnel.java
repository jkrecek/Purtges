package com.frca.purtges.tunnel;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.frca.purtges.activities.AppActivity;
import com.frca.purtges.services.EndpointService;

/**
 * Created by Frca on 26.7.13.
 */
public class EndpointTunnel {

    private AppActivity activity;

    private EndpointService service;

    private boolean isActive = false;

    private boolean isBound = false;

    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            service = ((EndpointService.LocalBinder)binder).getService();
            service.binding(EndpointTunnel.this);
            Log.e("LOG", "binded");
        }

        public void onServiceDisconnected(ComponentName className) {
            service = null;
            Log.e("LOG", "disconnected");
        }
    };

    private static EndpointTunnel instance;

    public static EndpointTunnel invokeTunnel(AppActivity activity) {
        if (instance == null)
            instance = new EndpointTunnel();

        instance.activity = activity;
        if (!instance.isBound())
            instance.doBindService();

        return instance;
    }

    // usage should be avoided
    public static EndpointTunnel getInstance() {
        return instance;
    }

    public void doBindService() {
        activity.bindService(new Intent(activity,
                EndpointService.class), connection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    public void doUnbindService() {
        if (isBound) {
            activity.unbindService(connection);
            isBound = false;
        }
    }

    public AppActivity getActivity() {
        return activity;
    }

    public EndpointService getService() {
        return service;
    }

    public boolean isActive() {
        return activity != null && service != null && isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isBound() {
        return isBound;
    }
}
