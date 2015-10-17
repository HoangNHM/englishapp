package com.idiotsapps.chaoenglish.baseclass;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.idiotsapps.chaoenglish.R;

/**
 * Created by vantuegia on 10/11/2015.
 */
public abstract class ActivityBase extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setActionBar(int resIc, String title, boolean canBack) {
        setTitle("");
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            if (canBack) {
                actionBar.setDisplayOptions(
                        ActionBar.DISPLAY_SHOW_CUSTOM |
                                ActionBar.DISPLAY_SHOW_TITLE |
                                ActionBar.DISPLAY_USE_LOGO |
                                ActionBar.DISPLAY_HOME_AS_UP |
                                ActionBar.DISPLAY_SHOW_HOME);
            } else {
                actionBar.setDisplayOptions(
                        ActionBar.DISPLAY_SHOW_CUSTOM |
                                ActionBar.DISPLAY_SHOW_TITLE |
                                ActionBar.DISPLAY_USE_LOGO |
                                ActionBar.DISPLAY_SHOW_HOME);
            }
            LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.actionbar_custom, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            v.setLayoutParams(params);
            ImageView abIc = (ImageView) v.findViewById(resIc);
            abIc.setImageResource(R.drawable.ic_app);
            TextView abTitle = (TextView) v.findViewById(R.id.abTitle);
            abTitle.setText(" " + title);
            abTitle.setTextSize(25);
            actionBar.setCustomView(v);
        }

    }
}
