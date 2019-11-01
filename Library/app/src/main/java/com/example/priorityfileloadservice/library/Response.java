package com.example.priorityfileloadservice.library;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/** Response Class */
public class Response implements Parcelable {

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

    public Response(Parcel in) {
        mCode = in.readInt();
        mUri = in.readParcelable(Uri.class.getClassLoader());
        mSize = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCode);
        dest.writeParcelable(mUri, Uri.PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeInt(mSize);
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

    public static final Parcelable.Creator<Response> CREATOR = new Parcelable.Creator<Response>(){
        @Override
        public Response createFromParcel(Parcel source) {
            return new Response(source);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[0];
        }
    };
}
