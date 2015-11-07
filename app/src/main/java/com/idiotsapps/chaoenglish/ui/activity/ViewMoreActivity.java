package com.idiotsapps.chaoenglish.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.SendButton;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.idiotsapps.chaoenglish.Grade;
import com.idiotsapps.chaoenglish.R;
import com.idiotsapps.chaoenglish.Unit;
import com.idiotsapps.chaoenglish.baseclass.ActivityBase;
import com.idiotsapps.chaoenglish.helper.HelperApplication;
import com.idiotsapps.chaoenglish.helper.MySQLiteHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ViewMoreActivity extends ActivityBase {

    private static final String TAG = "facebook";
    private HorizontalBarChart mChart;
    private int mClassName;
    private ScrollView mScrollViewChart;
    private ArrayList<Unit> mUnits; //list of units of clicked_class
    private CallbackManager callbackManager;
    private ImageView studyBtn;
    private int position; //position index of class
    private ArrayList<Grade> grades = new ArrayList<Grade>();

    // facebook
    private LoginButton mLoginButton;
    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more);
        // facebook
//        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        callbackManager = CallbackManager.Factory.create();
        mLoginButton = (LoginButton) findViewById(R.id.login_button);
//        mLoginButton.setReadPermissions("user_friends");
        // Other app specific specialization
        if (null != Profile.getCurrentProfile()) {
            ((ProfilePictureView)findViewById(R.id.profilePic)).setProfileId(Profile.getCurrentProfile().getId());
        }
        // Callback registration
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "logged in");
                if (null != Profile.getCurrentProfile()) {
                    ((ProfilePictureView)findViewById(R.id.profilePic)).setProfileId(Profile.getCurrentProfile().getId());
                }
            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "Error");
            }
        });
        mLoginButton.setOnClickListener(loginListener);
        mScrollViewChart = (ScrollView) findViewById(R.id.scrollViewChart);
//        ShareLinkContent content = new ShareLinkContent.Builder()
//                .setContentUrl(Uri.parse("https://fb.me/749567541854476"))
//                .build();
//        SendButton sendButton = (SendButton)findViewById(R.id.fb_send_button);
//        sendButton.setShareContent(content);
//        sendButton.registerCallback();

        mChart = (HorizontalBarChart) findViewById(R.id.horBarChart);
        // get caller intent
        Intent callerIntent = getIntent();
        position = callerIntent.getIntExtra("position", 0);
        this.grades = HelperApplication.sMySQLiteHelper.getClasses();
        mUnits = this.grades.get(position).getUnits();
        Collections.sort(mUnits);
        int[] YVals = getYVals(mUnits);
        // get bundle from intent
        Bundle packageFromCaller = callerIntent.getBundleExtra("ClassPackage");
        // get bundle info
        mClassName = packageFromCaller.getInt("ClassName");
        setActionBar(R.id.abIc, "Class " + mClassName, true); //true: back to parent
        setupChart(mChart, YVals);
        mScrollViewChart.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollViewChart.smoothScrollTo(0, mScrollViewChart.getBottom());
            }
        }, 1000);

        studyBtn = (ImageView) findViewById(R.id.StudyNow);
        studyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentQues = new Intent(ViewMoreActivity.this, QuesActivity.class);
                intentQues.putExtra("position",position);
                startActivity(intentQues);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.grades = HelperApplication.sMySQLiteHelper.getClasses();
        mUnits = this.grades.get(position).getUnits();
        Collections.sort(mUnits);
        int[] YVals = getYVals(mUnits);
        setActionBar(R.id.abIc, "Class " + mClassName, true); //true: back to parent
        setupChart(mChart, YVals);
    }

    private int[] getYVals(ArrayList<Unit> units) {
        int length = units.size();
        int[] YVals = new int[length];
        for (int i = 0; i < length; i++) {
            YVals[i] = (int) units.get(i).getVocabPercent();
        }
        return YVals;
    }

    private void setupChart(HorizontalBarChart horBarChart, int[] YVals) {
        horBarChart.getLegend().setEnabled(false);
        // set max value 108, to see 100% display percent
        horBarChart.getAxisLeft().setAxisMaxValue(108f);
        horBarChart.getAxisRight().setAxisMaxValue(108f);
//        horBarChart.setOnChartValueSelectedListener(this);
        horBarChart.animateY(2000);

        // draw bar name at bottom
        XAxis xl = horBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        horBarChart.setDrawBarShadow(false);
        horBarChart.setDrawValueAboveBar(true);

        horBarChart.setDescription("");
//        int density = (int) getResources().getDisplayMetrics().density;
//        horBarChart.setDescriptionPosition(horBarChart.getWidth() - (10 * density), horBarChart.getHeight());

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        horBarChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        horBarChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // horBarChart.setDrawBarShadow(true);

        // horBarChart.setDrawXLabels(false);

        horBarChart.setDrawGridBackground(false);

        // Random YVals
//        int count = new Random().nextInt((20) + 1);
//        int count = 16;
//        int[] mYVals = new int[count];
//        for (int i = 0; i < count; i++) {
//            mYVals[i] = new Random().nextInt((100) + 1);
//        }

        setData(horBarChart, YVals);
    }

    private void setData(BarChart barChart, int[] YVals) {

        ArrayList<String> xVals = new ArrayList<String>();
        int count = YVals.length;
        // draw backward
        for (int i = 0; i < count; i++) {
            xVals.add("unit " + mUnits.get(i).getUnitName());
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        // draw backward
        for (int i = 0; i < count; i++) {
//            yVals1.add(new BarEntry(YVals[count - 1 - i], i));
            yVals1.add(new BarEntry(YVals[i], i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet");
        set1.setColors(ColorTemplate.JOYFUL_COLORS);
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
//        data.setValueTypeface(mTf);

        // set chart.height
        barChart.getLayoutParams().height = 100 * count;

        barChart.setData(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_more, menu);
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

    public void invite(View view) {
        {
            String appLinkUrl, previewImageUrl;

            appLinkUrl = "https://fb.me/749567541854476";
//        previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";

            if (AppInviteDialog.canShow()) {
                AppInviteContent content = new AppInviteContent.Builder()
                        .setApplinkUrl(appLinkUrl)
//                    .setPreviewImageUrl(previewImageUrl)
                        .build();
                AppInviteDialog appInviteDialog = new AppInviteDialog(this);
                appInviteDialog.registerCallback(callbackManager, new FacebookCallback<AppInviteDialog.Result>() {
                    @Override
                    public void onSuccess(AppInviteDialog.Result result) {
                        Log.d("Facebook", "onSuccess " + result.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Facebook", "onCancel");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Log.d("Facebook", "onError " + e.toString());
                    }
                });
                appInviteDialog.show(content);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("Facebook", "onActivityResult\n" + requestCode + "\n" + resultCode + "\n" + data.toString());
    }

}
