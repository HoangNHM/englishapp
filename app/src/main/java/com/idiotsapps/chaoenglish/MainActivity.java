package com.idiotsapps.chaoenglish;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.idiotsapps.chaoenglish.baseclass.ActivityBase;

public class MainActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fmTrans = getSupportFragmentManager().beginTransaction();
        SlidingTabsFragment fragment = new SlidingTabsFragment();
        fmTrans.replace(R.id.fragment_content, fragment);
        fmTrans.commit();
//        DBHandler dbHandler = new DBHandler(this);
//        for (int i = 0; i < 20; i++) {
//            dbHandler.insertClassPercent(new Random().nextInt((100) + 1));
//        }
    }

    @Override
    protected void setActionBar() {
        setTitle("Main Activity Action Bar");
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayOptions(
                    ActionBar.DISPLAY_SHOW_TITLE |
                    ActionBar.DISPLAY_USE_LOGO |
                    ActionBar.DISPLAY_SHOW_HOME);

            actionBar.setLogo(R.drawable.earth);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
