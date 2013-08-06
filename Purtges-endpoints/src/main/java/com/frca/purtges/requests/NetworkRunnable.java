package com.frca.purtges.requests;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NetworkRunnable implements Runnable {

    private List<NetworkTask> tasks = Collections.synchronizedList(new ArrayList<NetworkTask>());

    private final long SLEEP_INTERVAL = 500;
    private final long CALLBACK_SLEEP_INTERVAL = 50;

    @Override
    public void run() {
        while(true) {
            if (tasks.isEmpty()) {
                try {
                    Thread.sleep(SLEEP_INTERVAL);
                } catch (InterruptedException e) {
                    return;
                }
                continue;
            }

            final List<Future<NetworkTask>> futures = new ArrayList<Future<NetworkTask>>();

            synchronized (tasks) {
                Iterator<NetworkTask> itr = tasks.iterator();

                // add tasks
                while (itr.hasNext()) {
                    final NetworkTask task = itr.next();
                    itr.remove();
                    futures.add(
                        Executors.newSingleThreadExecutor().submit(new Callable<NetworkTask>() {
                            @Override
                            public NetworkTask call() throws Exception {
                                Log.d("TASK_MGMNT", "Executing task `" + task.getTaskName() + "` from queue.");
                                return task.execute();
                            }
                        }));
                }

                // call callbacks
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Iterator<Future<NetworkTask>> callbackItr;

                        do {
                            callbackItr = futures.iterator();

                            while (callbackItr.hasNext()) {
                                Future<NetworkTask> future = callbackItr.next();
                                if (future.isDone() || future.isCancelled()) {
                                    callbackItr.remove();
                                    try {
                                        if (future.isDone()) {
                                            Log.d("TASK_MGMNT", "Callbacking task `" + future.get().getTaskName() + "` from queue.");
                                            future.get().callback();
                                        }
                                    } catch (Exception e) {
                                        Log.e(e.getClass().getName(), e.getMessage() != null ? e.getMessage() : "<empty>");
                                    }
                                }
                            }

                            try {
                                Thread.sleep(CALLBACK_SLEEP_INTERVAL);
                            } catch (InterruptedException e) {
                                return;
                            }

                        } while(!futures.isEmpty());
                    }
                }).start();
            }
        }
    }

    public void addTask(NetworkTask task) {
        tasks.add(task);
    }
}
