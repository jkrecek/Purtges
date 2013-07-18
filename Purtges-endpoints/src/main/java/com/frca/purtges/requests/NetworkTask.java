package com.frca.purtges.requests;

import android.util.Log;

import com.frca.purtges.helpers.Result;
import com.frca.purtges.requests.callbacks.QueryTask;
import com.frca.purtges.requests.callbacks.ResultCallback;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.GenericJson;

public class NetworkTask {

    private final QueryTask queryTask;
    private final ResultCallback resultCallback;

    private Object result;

    private String taskName;

    public NetworkTask(QueryTask queryTask, ResultCallback resultCallback) {
        this.queryTask = queryTask;
        this.resultCallback = resultCallback;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public NetworkTask execute() {
        try {
            try {
                result = queryTask.query();
            } catch (GoogleJsonResponseException e) {

                // appengine random error workaround
                if (e.getStatusCode() == 404) {
                    Log.d("NetworkTask", "Received 404 in method `" + getTaskName() + "`, trying again in a few secs");
                    Thread.sleep(100);
                    result = queryTask.query();
                } else
                    throw e;
            }
        } catch (Exception e) {
            Log.e("NetworkTask", "Error in method `" + getTaskName() + "`: " + e.getClass().getName() + ": " + e.getMessage());
        }

        result = validateResult(result);

        return this;
    }

    public void callback() {
        if (resultCallback != null)
            resultCallback.handleDone(result);
        else
            Log.w("NetworkTask", "null callback");
    }

    private Object validateResult(Object result) {
        if (result == null)
            return Result.ERROR;

        if (result instanceof GenericJson) {
            GenericJson entity = (GenericJson) result;
            if (entity.get("error_message") != null && entity.get("kind") != null)
                return Result.ERROR;
        }

        return result;
    }


}
