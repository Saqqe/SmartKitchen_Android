package com.example.saqibfredrik.smartkitchen;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saqib on 2015-09-07.
 */
public class ParseReceiver extends BroadcastReceiver {

    private final String TAG = ParseReceiver.class.getName();
    private String alert = "";
    public String URLTAG = "url";

    private NotificationCompat.Builder mBuilder;
    private Uri notifySound;
    private Intent resultIntent;
    private int mNotificationId = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        //Get JSON data and put them into variables
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            //JSONArray jsonArray = json.getJSONArray("");



            alert = json.getString("url");

            Log.d(TAG, "Got it!:"+ alert + "\nThis is whole json: " + json);

        } catch (JSONException e) {

        }

        //specify sound
        notifySound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentText(alert);
        mBuilder.setContentTitle("Notification");
        mBuilder.setSound(notifySound);
        mBuilder.setAutoCancel(true);

        // this is the activity that we will send the user
        resultIntent = new Intent(context, ImageHandler.class);

        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra(URLTAG, alert);// info to send!

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(mNotificationId, mBuilder.build());

    }

}
