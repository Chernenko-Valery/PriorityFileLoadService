package com.example.priorityfileloadservice.library;

import android.os.Parcel;
import android.os.Parcelable;

/** Request class */
public class Request extends Priority{

    public static final int TTL_STANDART = 15;

    /** Field - URL to File */
    private String mUrl;
    /** Field - Time to limit */
    private int mTtl;

    /** Class's Constructor */
    public Request(int aPriority, String aUrl, int aTtl) {
        super(aPriority);
        this.mUrl = aUrl;
        this.mTtl = aTtl;
    }

    public Request(Parcel in) {
        super(in);
        mUrl = in.readString();
        mTtl = Integer.parseInt(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(String.valueOf(mPriority));
        dest.writeString(mUrl);
        dest.writeString(String.valueOf(mTtl));
    }

    public String getUrl() {
        return mUrl;
    }


    public int getTtl() {
        return mTtl;
    }

    public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>(){
        @Override
        public Request createFromParcel(Parcel source) {
            return new Request(source);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[0];
        }
    };
}
