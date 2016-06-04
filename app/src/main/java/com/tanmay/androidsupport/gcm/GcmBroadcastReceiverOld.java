package com.tanmay.androidsupport.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by TaNMay on 3/3/2016.
 */
public class GcmBroadcastReceiverOld extends WakefulBroadcastReceiver {

    public static String TAG = "GCM Broadcast Receiver ==>";

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentServiceOld.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        Log.d(TAG, "Log");
        setResultCode(Activity.RESULT_OK);

    }

}
