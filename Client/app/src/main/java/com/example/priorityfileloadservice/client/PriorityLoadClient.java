package com.example.priorityfileloadservice.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.Printer;

import androidx.annotation.NonNull;

import com.example.priorityfileloadservice.library.INTERACTION_CONSTANTS;
import com.example.priorityfileloadservice.library.Request;
import com.example.priorityfileloadservice.library.Response;

/** Class for interaction with PriorityLoadService */
public class PriorityLoadClient implements ServiceConnection {

    private static final String TAG = "PriorityLoadClient";

    private Messenger mMessenger = null;
    private boolean mBind = false;
    private Context mContext;

    public PriorityLoadClient(Context mContext) {
        this.mContext = mContext;
    }

    public boolean bindToService() {
        if(!mBind) {
            Intent intent = INTERACTION_CONSTANTS.createBindIntent(mContext);
            if (mContext.bindService(intent, this, mContext.BIND_AUTO_CREATE)) {
                Log.d(TAG, "bindToService: OK");
                mBind = true;
            } else {
                Log.d(TAG, "bindToService: ERROR");
            }
        }
        return mBind;
    }

    public boolean isBind() {
        return mBind;
    }

    public void sendRequest(Request request, PriorityLoadServiceCallback callback) {
        if (mBind) {
            InteractionHandler handler = new InteractionHandler(callback);
            Messenger messenger = new Messenger(handler);
            try {
                Message message = new Message();
                message.what = INTERACTION_CONSTANTS.REQUEST_WHAT;
                Bundle data = new Bundle();
                data.putParcelable(INTERACTION_CONSTANTS.REQUEST_EXTRA, request);
                message.setData(data);
                message.replyTo = messenger;
                mMessenger.send(message);
            } catch (RemoteException aE) {
                aE.printStackTrace();
                return;
                //TODO
            }
        }
    }

    class InteractionHandler extends Handler {

        PriorityLoadServiceCallback callback;

        public InteractionHandler(PriorityLoadServiceCallback callback) {
            this.callback = callback;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == INTERACTION_CONSTANTS.REQUEST_ANSWER_WHAT) {
                callback.onAnswerRequest(msg.arg1);
            } else if (msg.what == INTERACTION_CONSTANTS.RESPONSE_WHAT) {
                msg.getData().setClassLoader(Response.class.getClassLoader());
                callback.onRecieveResponse((Response) msg.getData().getParcelable(INTERACTION_CONSTANTS.RESPONSE_EXTRA));
            }
        }
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mMessenger = new Messenger(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mMessenger = null;
        mBind = false;
    }
}
