package com.idiotsapps.chaoenglish.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.idiotsapps.chaoenglish.Grade;
import com.idiotsapps.chaoenglish.helper.HelperApplication;
import com.idiotsapps.chaoenglish.helper.SoundHelper;
import com.idiotsapps.chaoenglish.ui.dialog.InfoDialogFragment;
import com.idiotsapps.chaoenglish.R;
import com.idiotsapps.chaoenglish.Unit;
import com.idiotsapps.chaoenglish.Word;
import com.idiotsapps.chaoenglish.stardict.StarDict;
import com.idiotsapps.chaoenglish.util.PublishQuestion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

public class QuesActivity extends AppCompatActivity
        implements InfoDialogFragment.NoticeDialogListener
{
    private ArrayList<Grade> grades = new ArrayList<Grade>();
    private Unit currentUnit;
    private Word currentWord;
    private Grade  currentGrade;
    private ImageView imageView;
    private Button[] aBtn = new Button[4];
    private ImageView[] iBtn = new ImageView[4];
    private ImageView[] heartBtn = new ImageView[3];
    private StarDict starDict = null;
    private int lifeCount = 3;
    private String tag = this.getClass().getSimpleName();
    private Word[] mWords;
    private Handler mHandlerRialog;
    private RelativeLayout mViewRightChoice;
    private TextView mTvWord;
    private SoundHelper mSoundHelper;
    private Button mTvUnit;
    private ProgressBar progressBar;
    private InterstitialAd interstitial;
    private LinearLayout playGround;
    private boolean isAdLoaded = false;
    private boolean isPopUp = false;
    private DialogFragment appIdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setContentView(R.layout.activity_ques);
        playGround = (LinearLayout) findViewById(R.id.playGround);
        // Sound Helper
        mSoundHelper = HelperApplication.sSoundHelper;
        // add view RightChoice
        mViewRightChoice = (RelativeLayout) findViewById(R.id.vRightChoice);
        mViewRightChoice.setOnClickListener(mOnDismissRightChoiceView);
        mTvWord = (TextView) findViewById(R.id.tvWord);

        Intent intent = getIntent();
        this.grades = HelperApplication.sMySQLiteHelper.getClasses();
        int pos = intent.getIntExtra("position", 0);
        getStarDict(); //initalize StarDict stuff

        /**
         * Init current status of user
         * This below snippet code will be replace
         * by other initialization
         */
        this.currentGrade = grades.get(pos);//list starts from 0
        this.currentUnit = this.currentGrade.getCurrentUnit();
        this.currentUnit.getWords();
        this.currentWord = this.currentUnit.getCurrentWord();
        this.imageView = (ImageView) findViewById(R.id.wordImage);

        // Question image size
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = 0;
        if (width >= 900) {
            width = 900;
            height = 900;
        } else {
            height = width;
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        lp.gravity = Gravity.CENTER;
        this.imageView.setLayoutParams(lp);

        // Answers buttons
        this.aBtn[0] = (Button) findViewById(R.id.button_A);
        this.aBtn[1] = (Button) findViewById(R.id.button_B);
        this.aBtn[2] = (Button) findViewById(R.id.button_C);
        this.aBtn[3] = (Button) findViewById(R.id.button_D);
        addAnswBtnListener();

        // Information buttons
        this.iBtn[0] = (ImageView) findViewById(R.id.button_S);
        this.iBtn[1] = (ImageView) findViewById(R.id.button_X);
        this.iBtn[2] = (ImageView) findViewById(R.id.button_Y);
        this.iBtn[3] = (ImageView) findViewById(R.id.button_Z);
        addInfoBtnListener();
        findViewById(R.id.btnQuit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Come back MainActivity
                comeBackMain();
            }
        });

        //heart buttons
        this.heartBtn[0] = (ImageView) findViewById(R.id.heart1);
        this.heartBtn[1] = (ImageView) findViewById(R.id.heart2);
        this.heartBtn[2] = (ImageView) findViewById(R.id.heart3);

        mTvUnit = (Button) findViewById(R.id.tvUnit);
        setTvUnit(this.currentUnit.getUnitName());

        progressBar = (ProgressBar) findViewById(R.id.progressBarQues);
        progressBar.setMax(100);

        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(this.getString(R.string.admob_interstitial));

        // Create ad request.
        AdRequest interstitialAdRequest = new AdRequest.Builder().build();

//        // Begin loading your interstitial.
//        interstitial.loadAd(interstitialAdRequest);
//        interstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                displayInterstitial();
//            }
//        });

        //Starting game
        playVocabTest();
    }

    /**
     * && (getRandNumber(1) == 1)
     */
    public void displayInterstitial() {
        Log.d(tag, "load add");
        AdRequest interstitialAdRequest = new AdRequest.Builder().build();
        // Begin loading your interstitial.
        interstitial.loadAd(interstitialAdRequest);
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                interstitial.show();
            }
        });
    }


    private void setTvUnit(int unit) {
        mTvUnit.setText("Unit " + unit);
    }

    private void setProgressBar(int percent){
        progressBar.setProgress(percent);
    }

    private void comeBackMain(){
        this.onBackPressed();
    }

    private void hideActionBar() {
        ActionBar bar = getSupportActionBar();
        if (null != bar) {
            bar.hide();
        }
    }

    /**
     * when RightChoice view isShow, click will cause it's dismissed
     */
    private View.OnClickListener mOnDismissRightChoiceView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mHandlerRialog = null;
            mViewRightChoice.setVisibility(View.GONE);
            playVocabTest();
        }
    };

    /**
     * - Copy the dict database from asset for sdcard
     * - Create new StarDict object for app
     */
    private void getStarDict() {
        Log.d(tag, "enter getStarDict");
        if (null != HelperApplication.sStarDict) {
            this.starDict = HelperApplication.sStarDict;
            Log.d("testing", "StarDict = HelperApplication.sStarDict");
        } else {
            Log.d("testing", "null == HelperApplication.sStarDict");
        }
        Log.d("testing", "done getStarDict");
    }

    /**
     *  Add listener for every answer buttons
     */
    private void addAnswBtnListener() {
        for (int i = 0; i < this.aBtn.length; i++) {
            this.aBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b = (Button) view;
                    String word = b.getText().toString();
                    if(verifyAnswer(word)){ // correct answer
                        QuesActivity.this.currentUnit.increaseRightCount();
                        updateState(); //update state of game
                        mSoundHelper.playSound(SoundHelper.SoundId.SOUND_CHOOSE_RIGHT);
                    }else{
                        mSoundHelper.playSound(SoundHelper.SoundId.SOUND_CHOOSE_WRONG);
                        decrLifeCount(word);
                    }
                }
            });
        }
    }

    /**
     * Handle the heart buttons when you answer questions
     * @param word
     */
    private void decrLifeCount(String word){
        lifeCount--;
        isPopUp = true;
        if (lifeCount >= 0) {
            heartBtn[lifeCount].setImageResource(R.drawable.ic_heart_null);
        }
        String htmlText = this.starDict.lookupWord(word);
        showFailedDialog(htmlText);
        if(lifeCount < 0){
            String message = "Congratulation! " +
                    "Corrected " + this.currentUnit.getRightCount() + " words per "
                    + this.currentUnit.getWords().size();
            showPopUp(message, 3000, POPUP_CALLBACK_STATE.GO_NEXT_CLASS);
        }else{
            heartBtn[lifeCount].setImageResource(R.drawable.ic_heart_null);
        }
    }

    /**
     * Share question
     * @param view
     */
    public void shareQuestion(View view) {
        PublishQuestion.publish(this, playGround);
    }

    private enum POPUP_CALLBACK_STATE{
        GO_NEXT_WORD(0),
        GO_NEXT_UNIT(1),
        GO_NEXT_CLASS(3);
        POPUP_CALLBACK_STATE(int i) {
        }
    }

    private void showPopUp(String text, int mSecond, final POPUP_CALLBACK_STATE nextState) {
        mTvWord.setText(text);
        mViewRightChoice.setVisibility(View.VISIBLE);
        mHandlerRialog = new Handler();
        mHandlerRialog.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (View.GONE != mViewRightChoice.getVisibility()) {
                    mViewRightChoice.setVisibility(View.GONE);
                    switch (nextState) {
                        case GO_NEXT_WORD:
                            //Update current word
                            QuesActivity.this.currentWord = QuesActivity.this.currentUnit.getNextWord();
                            playVocabTest();
                            break;
                        case GO_NEXT_UNIT:
                            goNextUnit();
                            break;
                        case GO_NEXT_CLASS:
                            goNextClass();
                            break;
                        default:
                            break;
                    }
                }
            }
        }, mSecond);
    }

    private void goNextClass() {
        comeBackMain();
    }

    private void goNextUnit() {
        this.currentUnit = this.currentGrade.getNextUnit();
        this.currentUnit.getWords(); //Get words from database
        this.currentWord = this.currentUnit.getCurrentWord();
        setTvUnit(this.currentUnit.getUnitName());
        setProgressBar(0);//Reset progress
        playVocabTest();
    }

    /**
     *  Verify if user's answer is correct or not
     *  Will show advertisement if it is corrected
     * @param word
     */
    private boolean verifyAnswer(String word){
        String currWord = this.currentWord.getWord();
        if (currWord.equals(word)) {
            return true;
        }else {
            return false;

        }
    }

    @Override
    public void onBackPressed() {
        Log.d(this.tag, "back presssed");
        if (isPopUp && appIdialog != null) {
            isPopUp = false;
            appIdialog.dismiss();
            if(lifeCount < 0){
                String message = "Congratulation! " +
                        "Corrected " + this.currentUnit.getRightCount() + " words per "
                        + this.currentUnit.getWords().size();
                showPopUp(message, 3000, POPUP_CALLBACK_STATE.GO_NEXT_CLASS);
            }else{
                //Do not thing
            }
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Log.d("testing", "clossing Dialog");
        String tag = dialog.getTag();
        appIdialog = dialog;
        if (tag.equals("failedDialog")) {
            dialog.dismiss();
//            if(lifeCount < 0){
//                String message = "Congratulation! " +
//                        "Corrected " + this.currentUnit.getRightCount() + " words per "
//                        + this.currentUnit.getWords().size();
//                showPopUp(message, true, 3000, POPUP_CALLBACK_STATE.GO_NEXT_CLASS);
//            }else{
//                heartBtn[lifeCount].setImageResource(R.drawable.ic_heart_null);
//            }
            if(lifeCount < 0){
                String message = "Congratulation! " +
                        "Corrected " + this.currentUnit.getRightCount() + " words per "
                        + this.currentUnit.getWords().size();
                showPopUp(message, 3000, POPUP_CALLBACK_STATE.GO_NEXT_CLASS);
            }else{
                //Do not thing
            }

        } else {
            dialog.dismiss();//Close dialog
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
    /**
     * Show definition, pronunciation, example for a word
     * This word user want to know more about its meaning
     * @param word
     */
    public void showHelpInfo(String word){
        this.isAdLoaded = true;
        this.displayInterstitial();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        final InfoDialogFragment newFragment = new InfoDialogFragment();
        String htmlText = this.starDict.lookupWord(word);
        String htmlBody = null;
        try {
            htmlBody = new ParseHtlm().parseHtlm(htmlText);
        } catch (IOException e) {
            e.printStackTrace();
            htmlBody = "";
        }
        newFragment.setWord(htmlBody);
        newFragment.show(ft, "infoDialog");
    }

     class ParseHtlm {

        private static final char MARK_TITLE = '@';
        private static final char MARK_TYPE = '*';
        private static final char MARK_MEANING = '-';
        private static final char MARK_EX = '=';

        private static final String OPEN_TITLE = "<div class=\"title\"><span>";
        private static final String OPEN_TYPE = "<div>\n" +
                "\t\t<div class=\"c-meaning\">\n" +
                "\t\t\t<span class=\"type\">";
        private static final String OPEN_MEANING = "<div class=\"meaning\">";
        private static final String OPEN_EX = "<div class=\"example\">";

        private static final String CLOSE_TITLE = "</div>";
        private static final String CLOSE_TYPE = "</div>\n" +
                "\t\t</div>";
        private static final String CLOSE_MEANING = "</div>";
        private static final String CLOSE_EX = "</div>";

        private  String parseHtlm(String strInput) throws IOException {
            String res = "";
            BufferedReader reader = new BufferedReader(new StringReader(strInput));
            String line = ""; // read line by line
            char[] mark = new char[1]; // @, -, ...

            String openTitle = "";
            String openType = "";
            String openMeaning = "";
            String openEx = "";

            String closeTitle = "";
            String closeType = "";
            String closeMeaning = "";
            String closeEx = "";

            while (null != (line = reader.readLine())) {
                line.getChars(0, 1, mark, 0);
                switch (mark[0]) {
                    case MARK_TITLE:
                        //close
                        res += closeEx; closeEx = "";
                        res += closeMeaning; closeMeaning = "";
                        res += closeType; closeType = "";
                        res += closeTitle; closeTitle = "";
                        //open
                        openTitle = OPEN_TITLE;
                        //close
                        closeTitle = CLOSE_TITLE;
                        //res
                        //process
                        String[] temp = new String[9];
                        line = line.substring(1); // remove @
                        if (line.contains("/")) {
                            temp = line.split("/");
                            res += openTitle + temp[0] + "</span> /<b>" + temp[1] + "</b>/</div>";
                            openTitle = "";
                        } else {
                            res += openTitle + line + "</b></div>";
                            openTitle = "";
                        }
                        break;

                    case MARK_TYPE:
                        //close
                        res += closeEx; closeEx = "";
                        res += closeMeaning; closeMeaning = "";
                        res += closeType; closeType = "";
                        //open
                        openType = OPEN_TYPE;
                        //close
                        closeType = CLOSE_TYPE;
                        //res
                        line = line.substring(1); // remove *
                        res += openType + line + "</span>\n" +
                                "\t\t\t<div>"; openType = "";
                        break;

                    case MARK_MEANING:
                        //close
                        res += closeEx; closeEx = "";
                        res += closeMeaning; closeMeaning = "";
                        //open
                        openMeaning = OPEN_MEANING;
                        //close
                        closeMeaning = CLOSE_MEANING;
                        //res
                        line = line.substring(1); // remove -
                        res += openMeaning + line + "\n"; openMeaning = "";
                        break;

                    case MARK_EX:
                        //close
                        res += closeEx; closeEx = "";
                        res += closeMeaning; closeMeaning = "";
                        //open
                        openEx = OPEN_EX;
                        //close
                        closeEx = CLOSE_EX;
                        //res
                        line = line.substring(1); // remove =
                        String[] ex_meaning = new String[2];
                        ex_meaning = line.split("\\+");
                        res += openEx + ex_meaning[0] + "</div>";
                        res += "<div class=\"ex-meaning\">" + ex_meaning[1] + "</div>";
                        openEx = "";

                        break;

                    default:
                        res += line;
                        break;
                }
            } // end while
            //close
            res += closeEx; closeEx = "";
            res += closeMeaning; closeMeaning = "";
            res += closeType; closeType = "";
            res += closeTitle; closeTitle = "";

            String HEADER = "<html>\n" +
                    "<head>\n" +
                    "\t\t<meta charset=\"UTF-8\">\n" +
                    "</head>\n" +
                    "<body style=\"padding: 0px; margin-top: 0px;\">\n" +
                    "<style type=\"text/css\">\n" +
                    "\t.title{\n" +
                    "\t\tbackground-color: #94C500;\n" +
                    "\t\tmargin: 0px -236px 4px -28px;\n" +
                    "\t\tpadding: 7px 236px 4px 28px;\n" +
                    "\t\tline-height: 28px;\n" +
                    "\t\tfont-size: 21px;\n" +
                    "\t\tfont-weight: bold;\n" +
                    "\t\tfont-family: Arial,sans-serif;\n" +
                    "\t\tcolor: #FFF;\n" +
                    "\t}\n" +
                    "\t.c-meaning{\n" +
                    "\t\tmargin-top: 2px;\n" +
                    "\t}\n" +
                    "\t.type{\n" +
                    "\t\tfont-size: 18px;\n" +
                    "\t\ttext-decoration: none;\n" +
                    "\t\tcolor: #FF8A00;\n" +
                    "\t\tfont-weight: bold;\n" +
                    "\n" +
                    "\t}\n" +
                    "\t.meaning{\n" +
                    "\t\tdisplay: block;\n" +
                    "\t\tpadding-top: 5px;\n" +
                    "\t\tmargin-top: 2px;\n" +
                    "\t\tpadding-left: 20px;\n" +
                    "\t\tfont-weight: bold;\n" +
                    "\t\tfont-style: italic;\n" +
                    "\t}\n" +
                    "\t.meaning::before{\n" +
                    "\t\tcontent: \"* \";\n" +
                    "\t}\n" +
                    "\t.example{\n" +
                    "\t\tborder-left: 1px solid #CCC;\n" +
                    "\t    padding-bottom: 5px;\n" +
                    "\t    padding-left: 40px;\n" +
                    "\t    font-style: italic;\n" +
                    "\t}\n" +
                    "\t.example::before{\n" +
                    "\t\tcontent: \"-\";\n" +
                    "\t\tpadding-right: 5px;\n" +
                    "\t}\n" +
                    "\t.ex-meaning{\n" +
                    "\t\tborder-left: 1px solid #CCC;\n" +
                    "\t    padding-bottom: 5px;\n" +
                    "\t    padding-left: 40px;\n" +
                    "\t    font-style: italic;\n" +
                    "\t    color: #3434E0\n" +
                    "\t}\n" +
                    "\t.ex-meaning::before{\n" +
                    "\t\tcontent: \" \";\n" +
                    "\t\tpadding-right: 5px;\n" +
                    "\t}\n" +
                    "\t.title b{\n" +
                    "\t\tfont-style: italic;\n" +
                    "\t}\n" +
                    "</style>" +
                    "<div id=\"column-content\">\t\t\t\t\t\t\t\t\n";
            String FOOTER = "\t</div>\n" +
                    "</body>\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                    "</html>";
            return HEADER + res + FOOTER;
        }
    }
    /**
     * Show definition, pronunciation, example for click word
     * @param word
     */
    public void showFailedDialog(String word) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        final InfoDialogFragment newFragment = new InfoDialogFragment();
        String htmlBody = null;
        try {
            htmlBody = new ParseHtlm().parseHtlm(word);
        } catch (IOException e) {
            e.printStackTrace();
            htmlBody = "";
        }
        newFragment.setWord(htmlBody);
        newFragment.show(ft, "failedDialog");
    }

    /**
     * Add listener for info buttons
     */
    private void addInfoBtnListener() {
        for (int i = 0; i < this.iBtn.length; i++) {
            final int finalI = i;
            this.iBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String word = mWords[finalI].getWord();
                    showHelpInfo(word);
                }
            });
        }
    }

    /**
     * Show four word and one image
     */
    private void playVocabTest() {
        mWords = get4Words();
        // Set an image into view
        String fileName = getFilePath(this.currentWord.getWord());
        imageViewSet(imageView,fileName + ".jpg");
        for (int i = 0; i < 4; i++) {
            this.aBtn[i].setText(mWords[i].getWord());
        }
    }

    /**
     * Get the path of image file
     * @param word
     * @return
     */
    private String getFilePath(String word) {
        String out = word.replace(' ', '_');
        String grade = Integer.toString(this.currentGrade.getGrade());
        out = "out/" + "grade_" + grade + "/unit_" + this.currentUnit.getUnitName() + "/" + out;
        Log.d(tag, "out:" + out);
        return out;
    }

    /**
     * Set an image into the image view
     * @param imageView
     * @param filename
     */
    public void imageViewSet(ImageView imageView, String filename) {
        try {
            // get input stream
            InputStream ims = getAssets().open(filename);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            imageView.setImageDrawable(d);
        }
        catch(IOException ex) {
            return;
        }
    }

    /**
     * Create array of three integer numbers
     * The random numbers are not larger than max
     * and is different with curr
     * @param curr
     * @param max
     * @return Integer number array
     */
    private int[] getThreeRanNum(int curr, int max) {
        int[] dumpNums = new int[3];
        Random r = new Random();
        Log.d(tag,"curr: " + curr);
        int rNum = 0;
        for(int i = 0; i < 3; i++){
            boolean flag = true;
            while(flag) {
                flag = false;
                rNum = r.nextInt(max);
                for(int j = 0; j < i; j++) {
                    if(dumpNums[j] == rNum) {
                        flag = true;
                        break;
                    }
                }
                if (!flag && (rNum == curr)) {
                    flag = true;
                }
            }
            dumpNums[i] = rNum;
            Log.d(tag, "random:" + rNum);
        }
        return dumpNums;
    }

    /**
     * Create  a random number smaller than max
     * @param max
     * @return
     */
    private int getRandNumber(int max) {
        Random r = new Random();
        return r.nextInt(max);
    }

    /**
     * Update state of current word.
     * This function will be refactor later
     * when connecting with a database
     */
    private void updateState(){
        this.currentUnit.updateVocabPercent();
        this.setProgressBar((int)this.currentUnit.getVocabPercent());
        Log.d("testing", "getVocabPercent:" + (int) this.currentUnit.getVocabPercent());
        if(this.currentUnit.isLastWord()){
            if(this.currentGrade.isLastUnit()){
                //show dialog finish class
                String message = "Congratulation! " +
                        "You have studied " + this.currentGrade.getUnits().size() + " words!";
                showPopUp(message, 3000, POPUP_CALLBACK_STATE.GO_NEXT_CLASS);
            }else{
                //move to next unit
                String message = "Congratulation! " +
                        "You have studied " + this.currentUnit.getWords().size() + " words!";
                showPopUp(message, 3000,POPUP_CALLBACK_STATE.GO_NEXT_UNIT);
            }
        }else{
            //move to next word
            showPopUp(this.currentWord.getWord(), 1500, POPUP_CALLBACK_STATE.GO_NEXT_WORD);
        }
    }

    /**
     *  Get four random words
     * @return Words array
     */
    private Word[] get4Words() {
        Word[] playWord = new Word[4];
        Log.d(tag, "current Grade:" + this.currentGrade.getGrade());
        Log.d(tag, "current Unit:" + this.currentUnit.getUnit());
        Log.d(tag, "current word:" + this.currentWord.getWord());
        // Get array of 3 random words
        ArrayList<Word> words = this.currentUnit.getWords();
        int cWordIndex = this.currentUnit.getcWordIndex();
        int[] dumpNum = getThreeRanNum(cWordIndex, words.size());
        for (int k = 0; k < 3; k++) {
            playWord[k] = words.get(dumpNum[k]);
            Log.d(tag, "add rand word:" + playWord[k].getWord());
        }

        // Random position for answer's word
        int x = getRandNumber(4);
        if(x == 3){
            playWord[3] = this.currentWord;
        }else{
            // Exchange position
            Word tmp = playWord[x];
            playWord[x] = this.currentWord;
            playWord[3] = tmp;
        }
        return playWord;
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
