package com.example.priorityfileloadservice.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.priorityfileloadservice.library.INTERACTION_CONSTANTS;
import com.example.priorityfileloadservice.library.Priority;
import com.example.priorityfileloadservice.library.Request;
import com.example.priorityfileloadservice.library.RequestWithMessenger;
import com.example.priorityfileloadservice.library.Response;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** Service for priority load file */
public class PriorityLoadService extends IntentService {

    private static final String TAG = "PriorityLoadService";
    private static final String EXTRA_MESSAGE = "Extra Message";

    /** Field - Handler for request message from users */
    private Handler mRequestHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message aMsg) {
            Log.d(TAG, "handleMessage: ");
            Intent intent = new Intent(PriorityLoadService.this, PriorityLoadService.class);
            intent.putExtra(EXTRA_MESSAGE, aMsg);
            startService(intent);
        }
    };
    /** Field - Messenger for Binder */
    private final Messenger mRequestMessenger = new Messenger(mRequestHandler);
    /** Field - Fair Queue */
    private final LnhProirityFairQueue mRequestQueue = new LnhProirityFairQueue();
    /** Field - Storage */
    private final Storage mStorage = new Storage(getApplicationContext());
    /** Field - Channel */
    private final Channel mChannel = new Channel();

    /** Class - QueueHandler Thread */
    private class QueueHandler extends Thread {

        /** Field - Lock */
        private final Object lock = new Object();

        @Override
        public void run() {
            while(!isInterrupted()) {
                try {
                    while (mRequestQueue.isEmpty()) {
                        //LOCK this Thread. It unlock when Request put in Queue
                        lock.wait();
                    }
                    RequestWithMessenger request = (RequestWithMessenger) mRequestQueue.poll();
                    Response response = mChannel.downloadFile(request, mStorage);
                    //Get Answer
                    Message answer = new Message();
                    answer.what = INTERACTION_CONSTANTS.RESPONSE_WHAT;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(INTERACTION_CONSTANTS.RESPONSE_EXTRA, response);
                    answer.setData(bundle);
                    try {
                        request.getMessenger().send(answer);
                    } catch (RemoteException aE) {
                        aE.printStackTrace();
                        //TODO exception
                    }
                } catch (InterruptedException aE) {
                    aE.printStackTrace();
                }
            }
        }
    }

    /** Field - QueueHandler */
    private QueueHandler mQueueHandler = new QueueHandler();

    public PriorityLoadService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent aIntent) {
        Log.d(TAG, "onBind: ");
        return mRequestMessenger.getBinder();
    }

    @Override
    protected void onHandleIntent(Intent aIntent) {
        Log.d(TAG, "onHandleIntent: ");
        if(aIntent!=null) {
            Message msg = aIntent.getParcelableExtra(EXTRA_MESSAGE);
            //Message contain Request
            if (msg!= null && msg.what == INTERACTION_CONSTANTS.REQUEST_WHAT) {
                //Get Request from Message
                msg.getData().setClassLoader(Request.class.getClassLoader());
                Request request = msg.getData().getParcelable(INTERACTION_CONSTANTS.REQUEST_EXTRA);
                //Request is null
                if(request == null) request = new Request(Priority.NO_PRIORITY, "", 0);
                RequestWithMessenger requestWithMessenger = new RequestWithMessenger(request, msg.replyTo);

                Log.d(TAG, "New Request with Priority: " + requestWithMessenger.getPriority() + " and URL: " + requestWithMessenger.getUrl());

                //Answer
                Message answer = new Message();
                answer.what = INTERACTION_CONSTANTS.REQUEST_ANSWER_WHAT;
                if(isOnline()) {
                    //add Request in requestQueue
                    mRequestQueue.add(requestWithMessenger);
                    mQueueHandler.lock.notify();
                    answer.arg1 = INTERACTION_CONSTANTS.REQUEST_ANSWER_ACCEPTANCE;
                } else {
                    answer.arg1 = INTERACTION_CONSTANTS.REQUEST_ANSWER_ERROR;
                }
                //Send answer
                if(msg.replyTo != null) {
                    try {
                        msg.replyTo.send(answer);
                    } catch (RemoteException aE) {
                        Log.d(TAG, "" + aE.getMessage());
                    }
                }
            }
            //TODO other Message
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    /**
     * isOnline - Check if there is a NetworkConnection
     *
     * @return boolean - check result
     */
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
