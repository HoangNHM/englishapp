package com.idiotsapps.chaoenglish.helper;

import android.app.Application;

import com.idiotsapps.chaoenglish.stardict.StarDict;

/**
 * Created by vantuegia on 10/17/2015.
 */
public class HelperApplication extends Application {

    public static StarDict sStarDict;

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
