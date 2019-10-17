package com.example.priorityloadservice.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.priorityfileloadservice.library.INTERACTION_CONSTANTS;
import com.example.priorityfileloadservice.library.Request;
import com.example.priorityfileloadservice.library.RequestWithMessenger;

/** Service for priority load file */
public class PriorityLoadService extends IntentService {

    private static final String TAG = "PriorityLoadService";

    private static final String EXTRA_MESSAGE = "Extra Message";

    /** Field - Handler for request message from users */
    private final Handler requestHandler = new Handler(getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message aMsg) {
            Intent intent = new Intent(PriorityLoadService.this, PriorityLoadService.class);
            intent.putExtra(EXTRA_MESSAGE, aMsg);
            startService(intent);
        }
    };

    /** Field - Messenger for Binder */
    private final Messenger requestMessenger = new Messenger(requestHandler);

    /** Field - Fair Queue */
    private final LnhProirityFairQueue requestQueue = new LnhProirityFairQueue();

    public PriorityLoadService() {
        super(TAG);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent aIntent) {
        return requestMessenger.getBinder();
    }

    @Override
    protected void onHandleIntent(Intent aIntent) {
        if(aIntent!=null) {
            Message msg = aIntent.getParcelableExtra(EXTRA_MESSAGE);
            //Message contain Request
            if (msg.what == INTERACTION_CONSTANTS.REQUEST_WHAT) {
                RequestWithMessenger requestWithMessenger = new RequestWithMessenger((Request) msg.obj, msg.replyTo);

                Log.d(TAG, "New Request with Priority: " + requestWithMessenger.getPriority());

                Message answer = new Message();
                if(isOnline()) {
                    //add Request in requestQueue
                    requestQueue.add(requestWithMessenger);
                    answer.what = INTERACTION_CONSTANTS.REQUEST_ANSWER_ACCEPTANCE_WHAT;
                } else {
                    answer.what = INTERACTION_CONSTANTS.REQUEST_ANSWER_ERROR_WHAT;
                }
                //send answer
                if(msg.replyTo != null) {
                    try {
                        msg.replyTo.send(answer);
                    } catch (RemoteException aE) {
                        Log.d(TAG, "" + aE.getMessage());
                    }
                }
            }
            //TODO other what
        }
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
