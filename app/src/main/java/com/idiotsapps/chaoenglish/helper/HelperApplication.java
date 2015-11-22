package com.idiotsapps.chaoenglish.helper;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.idiotsapps.chaoenglish.stardict.StarDict;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        // facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.idiotsapps.chaoenglish",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
