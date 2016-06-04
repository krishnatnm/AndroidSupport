package com.tanmay.androidsupport.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tanmay.androidsupport.view.activities.HomeActivity;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.utils.ConstantClass;

/**
 * Created by TaNMay on 3/3/2016.
 */
public class GcmIntentServiceOld extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    public static String TAG = "GcmIntentServiceOld ==>";
    public static GcmInterfaceOld gcmInterfaceOld;
    static NotificationManager mNotificationManager;

    public GcmIntentServiceOld() {
        super("GcmIntentServiceOld");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // The getMessageType() intent parameter must be the intent you received in your BroadcastReceiver

        String messageType = gcm.getMessageType(intent);
        Log.d(TAG, "Intent: " + messageType);
        if (extras != null && !extras.isEmpty()) {
          /*
          * filter message based on message Type. Since it is likely that GCM
          * will be extended in the future with new message types, just
          * ignore any message types you're not interested in, or that you
          * don't recognize
          */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send Error: " + extras.toString());
                Log.d(TAG, messageType);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messagess on server: " + extras.toString());
                Log.d(TAG, messageType);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.d(TAG, "Extras: " + extras.getString("notification_type"));
                if (extras != null) {
                    String notification_type = extras.getString("notification_type");
                    Log.d(TAG, "Notification: " + notification_type);
                    sendNotification(notification_type);
                }
            }
        }
        GcmBroadcastReceiverOld.completeWakefulIntent(intent);
    }

    private void sendNotification(String message) {

        String titleText = ConstantClass.APP_NAME;
        String contentText = message;
        PendingIntent pendingIntent = null;

        String notification_type = message;

        if (gcmInterfaceOld != null) {
            gcmInterfaceOld.notificationReceived(true, notification_type);
        }

        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(titleText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
