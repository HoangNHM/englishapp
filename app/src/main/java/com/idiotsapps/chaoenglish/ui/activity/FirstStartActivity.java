package com.idiotsapps.chaoenglish.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.idiotsapps.chaoenglish.helper.MySQLiteHelper;
import com.idiotsapps.chaoenglish.R;
import com.idiotsapps.chaoenglish.helper.HelperApplication;
import com.idiotsapps.chaoenglish.helper.PreferencesHelper;
import com.idiotsapps.chaoenglish.stardict.StarDict;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FirstStartActivity extends AppCompatActivity {

    private static final String TAG = FirstStartActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private String dictExterPath = null;
    private static final String DICT_ASSEST_PATH = "stardictvn";
    // Check if app first launch, save path of database
    private PreferencesHelper prefsHelper;
    private boolean mIsFirstStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefsHelper = new PreferencesHelper(getApplicationContext());
        setContentView(R.layout.activity_first_start);
        mIsFirstStart = prefsHelper.isFirstStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        HelperApplication.sMySQLiteHelper = new MySQLiteHelper(getApplicationContext());
//        Log.d(TAG, "new MySQLiteHelper");
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
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
//            Log.d(TAG, "onPause dismiss mProgressDialog");
        }
    }

    private class SetDataBase extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(FirstStartActivity.this);
            mProgressDialog.setTitle("Setup DataBase in First Start");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
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
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
//                Log.d(TAG, "onPostExecute dismiss mProgressDialog");
            } else {
//                Log.d(TAG, "onPostExecute NOT dismiss mProgressDialog");
            }
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
