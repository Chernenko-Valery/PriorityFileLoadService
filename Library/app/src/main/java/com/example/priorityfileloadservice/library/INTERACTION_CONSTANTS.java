package com.example.priorityfileloadservice.library;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public final class INTERACTION_CONSTANTS {

    public final static int REQUEST_WHAT = 1;
    public final static int REQUEST_ANSWER_WHAT = 11;

    public final static int REQUEST_ANSWER_ACCEPTANCE = 111;
    public final static int REQUEST_ANSWER_ERROR = 112;

    public final static String REQUEST_EXTRA = "REQUEST";

    public final static int RESPONSE_WHAT = 2;

    public final static int RESPONSE_STATUS_OK = 21;
    public final static int RESPONSE_STATUS_ERROR = 22;

    public final static String RESPONSE_EXTRA = "RESPONSE";

    private final static String BIND_INTENT_PACKAGE = "com.example.priorityfileloadservice.service";
    private final static String BIND_INTENT_CLASS_NAME = "com.example.priorityfileloadservice.service.PriorityLoadService";

    public static final Intent createBindIntent(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(BIND_INTENT_PACKAGE, BIND_INTENT_CLASS_NAME));
        return intent;
    }
}
