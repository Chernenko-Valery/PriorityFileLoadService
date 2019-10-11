package com.example.priorityfileloadservice.library;

import androidx.core.app.NotificationCompat;

/** Priority Class */
public class Priority {

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

    public int getPriority() {
        return mPriority;
    }
}
