package com.idiotsapps.chaoenglish.helper;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.idiotsapps.chaoenglish.stardict.StarDict;

import bolts.AppLinks;

/**
 * Created by vantuegia on 10/17/2015.
 */
public class HelperApplication extends Application {

    public static StarDict sStarDict;
    public static MySQLiteHelper sMySQLiteHelper;
    public static SoundHelper sSoundHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
    }
}
