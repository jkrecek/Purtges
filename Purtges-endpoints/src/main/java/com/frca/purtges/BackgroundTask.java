package com.frca.purtges;

import android.os.AsyncTask;

//import com.frca.purtges.deviceuserendpoint.Deviceuserendpoint;

import java.io.IOException;

public class BackgroundTask extends AsyncTask<Void, Void, Void> {

    public enum Result {
        OK,
        ERROR
    }

    private final BackgroundCallback backgroundCallback;
    private final ForegroundCallback foregroundCallback;

    public BackgroundTask(BackgroundCallback backgroundCallback, ForegroundCallback foregroundCallback) {
        this.backgroundCallback = backgroundCallback;
        this.foregroundCallback = foregroundCallback;
    }

    private Object result = BackgroundTask.Result.ERROR;

    @Override
    protected Void doInBackground(Void... voids) {
        if (backgroundCallback != null)
            result = backgroundCallback.run();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (foregroundCallback != null)
            foregroundCallback.run(result);
    }

    public static interface BackgroundCallback {
        Object run();

    }

    public static interface ForegroundCallback {
        void run(Object object);
    }
}
