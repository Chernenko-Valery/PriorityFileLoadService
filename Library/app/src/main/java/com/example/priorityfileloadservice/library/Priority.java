package com.example.priorityfileloadservice.library;

import android.os.Parcel;
import android.os.Parcelable;

/** Priority Class */
public class Priority implements Parcelable {

    public static int NO_PRIORITY = -1;
    public static int LOW_PRIORITY = 1;
    public static int NORMAL_PRIORITY = 2;
    public static int HIGH_PRIORITY = 3;

    /** Field - Priority */
    protected int mPriority;

    /** Class's constructor */
    public Priority(int aPriority) {
        if(aPriority==LOW_PRIORITY || aPriority== NORMAL_PRIORITY || aPriority==HIGH_PRIORITY) {
            this.mPriority = aPriority;
        } else {
            this.mPriority = NORMAL_PRIORITY;
        }
    }

    public Priority(Parcel in) {
        mPriority = Integer.parseInt(in.readString());
    }

    public int getPriority() {
        return mPriority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(String.valueOf(mPriority));
    }

    public static final Parcelable.Creator<Priority> CREATOR = new Parcelable.Creator<Priority>(){
        @Override
        public Priority createFromParcel(Parcel source) {
            return new Priority(source);
        }

        @Override
        public Priority[] newArray(int size) {
            return new Priority[0];
        }
    };
}
