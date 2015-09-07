package com.example.saqibfredrik.smartkitchen;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;


/**
 * Created by Saqib on 2015-09-07.
 */
public class ParseConnect extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        String appKey    = getString(R.string.ParseAppKey);
        String clientKey = getString(R.string.ParseClientKey);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, appKey, clientKey);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
