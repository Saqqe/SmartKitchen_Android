package com.example.saqibfredrik.smartkitchen;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Saqib on 2015-09-07.
 */
public class ParseReceiver extends ParsePushBroadcastReceiver {

    private final String TAG = "Parse Notification";
    private String msg = "";

    public ParseReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);


        try {
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
            Iterator itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                Log.d(TAG, "..." + key + " => " + json.getString(key));
                if(key.equals("string")){
                    msg = json.getString(key);
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }

        Intent launchActivity = new Intent(context, MainActivity.class);
        launchActivity.putExtra("url", msg);
        PendingIntent pi = PendingIntent.getActivity(context, 0, launchActivity, 0);

        Notification noti = new NotificationCompat.Builder(context)
                .setContentTitle("PUSH RECEIVED")
                .setContentText(msg)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, noti);
    }
}
