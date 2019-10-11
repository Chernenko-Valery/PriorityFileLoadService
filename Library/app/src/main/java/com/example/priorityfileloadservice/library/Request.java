package com.example.priorityfileloadservice.library;

import java.net.URL;

/** Request class */
public class Request extends Priority{

    /** Field - URL to File */
    private URL mUrl;
    /** Field - Time to limit */
    private int mTtl;

    /** Class's Constructor */
    public Request(int aPriority, URL aUrl, int aTtl) {
        super(aPriority);
        this.mUrl = aUrl;
        this.mTtl = aTtl;
    }

    public URL getUrl() {
        return mUrl;
    }


    public int getTtl() {
        return mTtl;
    }
}
