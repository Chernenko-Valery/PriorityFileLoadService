package com.example.priorityfileloadservice.library;

import android.os.Messenger;

import java.net.URL;

/** Request with Messenger Class */
public class RequestWithMessenger extends Request {

    /** Field - Messenger */
    private Messenger mMessenger;

    /** Class's constructor */
    public RequestWithMessenger(int aPriority, URL aUrl, int aTtl, Messenger aMessenger) {
        super(aPriority, aUrl, aTtl);
        this.mMessenger = aMessenger;
    }
}
