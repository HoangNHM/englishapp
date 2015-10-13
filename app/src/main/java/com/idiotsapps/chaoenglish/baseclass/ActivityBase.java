package com.idiotsapps.chaoenglish.baseclass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by vantuegia on 10/11/2015.
 */
public abstract class ActivityBase extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
    }

    protected abstract void setActionBar();
}
