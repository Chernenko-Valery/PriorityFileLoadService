package com.example.priorityfileloadservice.library;

import android.net.Uri;

/** Response Class */
public class Response {

    public static int OK_CODE = 0;
    public static int ERROR_CODE = 1;

    /** Field - Response Code (OK or ERROR) */
    private int mCode;
    /** Field - Uri with File */
    private Uri mUri;
    /** Field - File size */
    private int mSize;

    /** Class's Constructor */
    public Response(int aCode, Uri aUri, int aSize) {
        if(aCode == OK_CODE || aCode == ERROR_CODE) {
            this.mCode = aCode;
        } else {
            this.mCode = ERROR_CODE;
        }
        this.mUri = aUri;
        this.mSize = aSize;
    }

    public int getCode() {
        return mCode;
    }

    public int getSize() {
        return mSize;
    }

    public Uri getUri() {
        return mUri;
    }
}
