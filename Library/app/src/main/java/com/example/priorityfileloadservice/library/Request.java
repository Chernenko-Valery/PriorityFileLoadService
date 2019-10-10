package com.example.priorityfileloadservice.library;

import java.net.URL;

/** Request class */
public class Request {

    public static int LOW_PRIORITY = 1;
    public static int NORMAL_PRIORITY = 2;
    public static int HIGH_PRIORITY = 3;

    /** Field - URL to File */
    private URL mUrl;
    /** Field - Priority (Low, Medium, High) */
    private int mPriority;
    /** Field - Time to limit */
    private int mTtl;

    /** Class's Constructor */
    public Request(URL aUrl, int aPriority, int aTtl) {
        this.mUrl = aUrl;
        if(aPriority == LOW_PRIORITY || aPriority == NORMAL_PRIORITY || aPriority == HIGH_PRIORITY) {
            this.mPriority = aPriority;
        } else {
            this.mPriority = NORMAL_PRIORITY;
        }
        this.mTtl = aTtl;
    }

    public URL getUrl() {
        return mUrl;
    }

    public int getPriority() {
        return mPriority;
    }

    public int getTtl() {
        return mTtl;
    }
}
