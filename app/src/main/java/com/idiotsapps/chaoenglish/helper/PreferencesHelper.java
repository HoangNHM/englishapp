package com.idiotsapps.chaoenglish.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vantuegia on 10/13/2015.
 */
public class PreferencesHelper {

    private static final String FIRST_RUN = "first_run";
    private static final String DICT_EXTER_PATH = "dict_exter_path";

    private SharedPreferences mPref;

    public static final String PREF_FILE_NAME = "chaoenglish_pref_file";


    public PreferencesHelper(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().commit();
    }

    public boolean isFirstStart() {
        return mPref.getBoolean(FIRST_RUN, true);
    }

    public void setNotFirstStart() {
        mPref.edit().putBoolean(FIRST_RUN, false).commit();
    }

    public void setDictExterPath(String path) {
        mPref.edit().putString(DICT_EXTER_PATH, path).commit();
    }

    public String getDictExterPath() {
        return mPref.getString(DICT_EXTER_PATH, "");
    }

}
