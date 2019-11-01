package com.example.priorityfileloadservice.library;

import android.os.Messenger;

/** Request with Messenger Class */
public class RequestWithMessenger extends Request {

    /** Field - Messenger */
    private Messenger mMessenger;

    /** Class's constructor */
    public RequestWithMessenger(int aPriority, String aUrl, int aTtl, Messenger aMessenger) {
        super(aPriority, aUrl, aTtl);
        this.mMessenger = aMessenger;
    }

    public RequestWithMessenger(Request aRequest, Messenger aMessenger) {
        super(aRequest.getPriority(), aRequest.getUrl(), aRequest.getTtl());
        this.mMessenger = aMessenger;
    }

    public Messenger getMessenger() {
        return mMessenger;
    }
}
