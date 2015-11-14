package com.idiotsapps.chaoenglish.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.idiotsapps.chaoenglish.baseclass.ActivityBase;
import com.idiotsapps.chaoenglish.helper.MySQLiteHelper;
import com.idiotsapps.chaoenglish.R;
import com.idiotsapps.chaoenglish.helper.HelperApplication;
import com.idiotsapps.chaoenglish.helper.PreferencesHelper;
import com.idiotsapps.chaoenglish.helper.SoundHelper;
import com.idiotsapps.chaoenglish.stardict.StarDict;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import bolts.AppLinks;

public class FirstStartActivity extends ActivityBase {

    private static final String TAG = FirstStartActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private String dictExterPath = null;
    private static final String DICT_ASSEST_PATH = "stardictvn";
    // Check if app first launch, save path of database
    private PreferencesHelper prefsHelper;
    private boolean mIsFirstStart = false;

    private WebView wvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("AppLinks", "App Link Target URL: " + targetUrl.toString());
        } else {
            AppLinkData.fetchDeferredAppLinkData(
                    this,
                    new AppLinkData.CompletionHandler() {
                        @Override
                        public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                            //process applink data
                            Log.i("AppLinks", "process applink data");
                        }
                    });
        }
        prefsHelper = new PreferencesHelper(getApplicationContext());
        setContentView(R.layout.activity_first_start);
        wvWelcome = (WebView) findViewById(R.id.wvWelcome);
        wvWelcome.loadDataWithBaseURL("file:///android_asset/","<html><center><img src=\"loading.gif\"></html>","text/html","utf-8","");
//        wvWelcome.loadDataWithBaseURL("file:///android_asset/", "<html><center><style type='text/css'>@-webkit-keyframes uil-default-anim { 0% { opacity: 1} 100% {opacity: 0} }@keyframes uil-default-anim { 0% { opacity: 1} 100% {opacity: 0} }.uil-default-css > div:nth-of-type(1){-webkit-animation: uil-default-anim 1s linear infinite;animation: uil-default-anim 1s linear infinite;-webkit-animation-delay: -0.5s;animation-delay: -0.5s;}.uil-default-css { position: relative;background:none;width:200px;height:200px;}.uil-default-css > div:nth-of-type(2){-webkit-animation: uil-default-anim 1s linear infinite;animation: uil-default-anim 1s linear infinite;-webkit-animation-delay: -0.4166666666666667s;animation-delay: -0.4166666666666667s;}.uil-default-css { position: relative;background:none;width:200px;height:200px;}.uil-default-css > div:nth-of-type(3){-webkit-animation: uil-default-anim 1s linear infinite;animation: uil-default-anim 1s linear infinite;-webkit-animation-delay: -0.33333333333333337s;animation-delay: -0.33333333333333337s;}.uil-default-css { position: relative;background:none;width:200px;height:200px;}.uil-default-css > div:nth-of-type(4){-webkit-animation: uil-default-anim 1s linear infinite;animation: uil-default-anim 1s linear infinite;-webkit-animation-delay: -0.25s;animation-delay: -0.25s;}.uil-default-css { position: relative;background:none;width:200px;height:200px;}.uil-default-css > div:nth-of-type(5){-webkit-animation: uil-default-anim 1s linear infinite;animation: uil-default-anim 1s linear infinite;-webkit-animation-delay: -0.16666666666666669s;animation-delay: -0.16666666666666669s;}.uil-default-css { position: relative;background:none;width:200px;height:200px;}.uil-default-css > div:nth-of-type(6){-webkit-animation: uil-default-anim 1s linear infinite;animation: uil-default-anim 1s linear infinite;-webkit-animation-delay: -0.08333333333333331s;animation-delay: -0.08333333333333331s;}.uil-default-css { position: relative;background:none;width:200px;height:200px;}.uil-default-css > div:nth-of-type(7){-webkit-animation: uil-default-anim 1s linear infinite;animation: uil-default-anim 1s linear infinite;-webkit-animation-delay: 0s;animation-delay: 0s;}.uil-default-css { position: relative;background:none;width:200px;height:200px;}.uil-default-css > div:nth-of-type(8){-webkit-animation: uil-default-anim 1s linear infinite;animation: uil-default-anim 1s linear infinite;-webkit-animation-delay: 0.08333333333333337s;animation-delay: 0.08333333333333337s;}.uil-default-css { position: relative;background:none;width:200px;height:200px;}.uil-default-css > div:nth-of-type(9){-webkit-animation: uil-default-anim 1s linear infinite;animation: uil-default-anim 1s linear infinite;-webkit-animation-delay: 0.16666666666666663s;animation-delay: 0.16666666666666663s;}.uil-default-css { position: relative;background:none;width:200px;height:200px;}.uil-default-css > div:nth-of-type(10){-webkit-animation: uil-default-anim 1s linear infinite;animation: uil-default-anim 1s linear infinite;-webkit-animation-delay: 0.25s;animation-delay: 0.25s;}.uil-default-css { position: relative;background:none;width:200px;height:200px;}.uil-default-css > div:nth-of-type(11){-webkit-animation: uil-default-anim 1s linear infinite;animation: uil-default-anim 1s linear infinite;-webkit-animation-delay: 0.33333333333333337s;animation-delay: 0.33333333333333337s;}.uil-default-css { position: relative;background:none;width:200px;height:200px;}.uil-default-css > div:nth-of-type(12){-webkit-animation: uil-default-anim 1s linear infinite;animation: uil-default-anim 1s linear infinite;-webkit-animation-delay: 0.41666666666666663s;animation-delay: 0.41666666666666663s;}.uil-default-css { position: relative;background:none;width:200px;height:200px;}</style><div class='uil-default-css' style='-webkit-transform:scale(1)'><div style='top:80px;left:93px;width:14px;height:40px;background:#00b2ff;-webkit-transform:rotate(0deg) translate(0,-60px);transform:rotate(0deg) translate(0,-60px);border-radius:10px;position:absolute;'></div><div style='top:80px;left:93px;width:14px;height:40px;background:#00b2ff;-webkit-transform:rotate(30deg) translate(0,-60px);transform:rotate(30deg) translate(0,-60px);border-radius:10px;position:absolute;'></div><div style='top:80px;left:93px;width:14px;height:40px;background:#00b2ff;-webkit-transform:rotate(60deg) translate(0,-60px);transform:rotate(60deg) translate(0,-60px);border-radius:10px;position:absolute;'></div><div style='top:80px;left:93px;width:14px;height:40px;background:#00b2ff;-webkit-transform:rotate(90deg) translate(0,-60px);transform:rotate(90deg) translate(0,-60px);border-radius:10px;position:absolute;'></div><div style='top:80px;left:93px;width:14px;height:40px;background:#00b2ff;-webkit-transform:rotate(120deg) translate(0,-60px);transform:rotate(120deg) translate(0,-60px);border-radius:10px;position:absolute;'></div><div style='top:80px;left:93px;width:14px;height:40px;background:#00b2ff;-webkit-transform:rotate(150deg) translate(0,-60px);transform:rotate(150deg) translate(0,-60px);border-radius:10px;position:absolute;'></div><div style='top:80px;left:93px;width:14px;height:40px;background:#00b2ff;-webkit-transform:rotate(180deg) translate(0,-60px);transform:rotate(180deg) translate(0,-60px);border-radius:10px;position:absolute;'></div><div style='top:80px;left:93px;width:14px;height:40px;background:#00b2ff;-webkit-transform:rotate(210deg) translate(0,-60px);transform:rotate(210deg) translate(0,-60px);border-radius:10px;position:absolute;'></div><div style='top:80px;left:93px;width:14px;height:40px;background:#00b2ff;-webkit-transform:rotate(240deg) translate(0,-60px);transform:rotate(240deg) translate(0,-60px);border-radius:10px;position:absolute;'></div><div style='top:80px;left:93px;width:14px;height:40px;background:#00b2ff;-webkit-transform:rotate(270deg) translate(0,-60px);transform:rotate(270deg) translate(0,-60px);border-radius:10px;position:absolute;'></div><div style='top:80px;left:93px;width:14px;height:40px;background:#00b2ff;-webkit-transform:rotate(300deg) translate(0,-60px);transform:rotate(300deg) translate(0,-60px);border-radius:10px;position:absolute;'></div><div style='top:80px;left:93px;width:14px;height:40px;background:#00b2ff;-webkit-transform:rotate(330deg) translate(0,-60px);transform:rotate(330deg) translate(0,-60px);border-radius:10px;position:absolute;'></div></div></html>","text/html","utf-8","");
        mIsFirstStart = prefsHelper.isFirstStart();
        hideActionBar(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);

        AsyncTask<Void, Void, Boolean> task;
        task = new SetDataBase().execute();
        // not first start, call MainActivity
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(FirstStartActivity.this, MainActivity.class);
        startActivity(intent);
        FirstStartActivity.this.finish();
    }

    private void setHelper() {
        HelperApplication.sStarDict = new StarDict(dictExterPath);
//        Log.d(TAG, "new StarDict");
        HelperApplication.sMySQLiteHelper = MySQLiteHelper.getInstance(getApplicationContext());
        HelperApplication.sMySQLiteHelper.createDataBase();
        HelperApplication.sMySQLiteHelper.openDataBase();
        HelperApplication.sMySQLiteHelper.getWritableDatabase();
//        Log.d(TAG, "new MySQLiteHelper");
        // Sound Helper
        HelperApplication.sSoundHelper = new SoundHelper(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_start, menu);
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

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
//        if (mProgressDialog.isShowing()) {
//            mProgressDialog.dismiss();
////            Log.d(TAG, "onPause dismiss mProgressDialog");
//        }
    }

    private class SetDataBase extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressDialog = new ProgressDialog(FirstStartActivity.this);
//            mProgressDialog.setTitle("Setup DataBase in First Start");
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (mIsFirstStart) {
//                    Log.d(TAG, "mIsFirstStart copy database");
                    copyDictToSdCard();
                    prefsHelper.setNotFirstStart();
                    prefsHelper.setDictExterPath(dictExterPath);
                } else {
//                    Log.d(TAG, "not mIsFirstStart getDictExterPath");
                    dictExterPath = prefsHelper.getDictExterPath();
                }
//                Log.d(TAG, "setHelper");
                setHelper();
                return true;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
//                Toast.makeText(FirstStartActivity.this, "Done", Toast.LENGTH_SHORT).show();
                gotoMainActivity();
            } else {
                Toast.makeText(FirstStartActivity.this, "Error\nPlease start app again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * It will copy asset dictionary files to an external location
     */
    private void copyDictToSdCard() throws IOException {
        AssetManager assetManager = getAssets();
        this.dictExterPath = Environment.getExternalStorageDirectory().toString() + "/" + this.DICT_ASSEST_PATH;
        File starDict = new File(this.dictExterPath);

        if(!starDict.exists()){
            starDict.mkdirs();
            String[] files = null;
            try {
                files = assetManager.list(this.DICT_ASSEST_PATH);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }

            for(String filename : files) {
                Log.d(TAG, "file: " + filename);
                InputStream in = null;
                OutputStream out = null;
                    in = assetManager.open(this.DICT_ASSEST_PATH + "/" + filename);
                    Log.d(TAG, "file path: " + this.dictExterPath + "/" + filename);
                    out = new FileOutputStream(this.dictExterPath + "/" + filename);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
            }
        }
        else {
            //TODO: Verify exist files
            Log.d(TAG, "dir. already exists");
        }
    }

    /**
     * It will write data from input stream to an output stream as a file
     * @param in Input stream
     * @param out Output stream of a file
     * @throws IOException
     */
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
