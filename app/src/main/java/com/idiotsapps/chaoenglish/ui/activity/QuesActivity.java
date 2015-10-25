package com.idiotsapps.chaoenglish.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idiotsapps.chaoenglish.Grade;
import com.idiotsapps.chaoenglish.helper.HelperApplication;
import com.idiotsapps.chaoenglish.helper.SoundHelper;
import com.idiotsapps.chaoenglish.ui.dialog.InfoDialogFragment;
import com.idiotsapps.chaoenglish.helper.MySQLiteHelper;
import com.idiotsapps.chaoenglish.R;
import com.idiotsapps.chaoenglish.Unit;
import com.idiotsapps.chaoenglish.Word;
import com.idiotsapps.chaoenglish.stardict.StarDict;

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
    private int lifeCount = 3;
    private Word currentWord;
    private Grade  currentGrade;
    private int currGradeIndex = 0;
    private int percent = 0;
    private ImageView imageView;
    private Button[] aBtn = new Button[4];
    private ImageView[] iBtn = new ImageView[4];
    private ImageView[] heartBtn = new ImageView[3];
    private StarDict starDict = null;
    private String tag = this.getClass().getSimpleName();
    private MySQLiteHelper mySQLiteHelper = null;
    //HoangNHM
    private Word[] mWords;
    private Handler mHandlerRialog;
    private RelativeLayout mViewRightChoice;
    private TextView mTvWord;
    private SoundHelper mSoundHelper;
    private TextView mTvUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setContentView(R.layout.activity_ques);

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

        mTvUnit = (TextView) findViewById(R.id.tvUnit);
        setTvUnit(this.currentUnit.getUnitName());

        //Starting game
        goNextWord();
    }

    private void setTvUnit(int unit) {
        mTvUnit.setText("UNIT" + unit);
    }

    private void comeBackMain(){
//        Intent intentQues = new Intent(QuesActivity.this, MainActivity.class);
//        startActivity(intentQues);
        onBackPressed();
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
            goNextWord();
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
                    verifyAnswer(word);
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
        String htmlText = this.starDict.lookupWord(word);
        showFailedDialog(htmlText);
        if(lifeCount < 0){
            //TODO: show adv
            //TODO: show a dialog "number correct word per total
            comeBackMain();
        }else{
            heartBtn[lifeCount].setImageResource(R.drawable.ic_heart_null);
        }

    }
    /**
     *  Verify if user's answer is correct or not
     *  Will show advertisement if it is corrected
     * @param word
     */
    private void verifyAnswer(String word){
        String currWord = this.currentWord.getWord();
        if (currWord.equals(word)) {
            // show Right Choice dialog - then goNextWord
            mTvWord.setText(word);
            mViewRightChoice.setVisibility(View.VISIBLE);
//            final DialogFragment dialog = showRightChoiceDialog(word);
            mHandlerRialog = new Handler();
            mHandlerRialog.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (View.GONE != mViewRightChoice.getVisibility()) {
                        mViewRightChoice.setVisibility(View.GONE);
                        goNextWord();
                    }
                }
            }, 2000);
            // TODO sound
            mSoundHelper.playSound(SoundHelper.SoundId.SOUND_CHOOSE_RIGHT);
        }else {
            //show ads
            mSoundHelper.playSound(SoundHelper.SoundId.SOUND_CHOOSE_WRONG);
            decrLifeCount(word);

        }
    }

//    private DialogFragment showRightChoiceDialog(String word) {
//        DialogFragment newFragment = CustomDialogFragment.newInstance(word);
//        newFragment.show(getSupportFragmentManager(), "Rialog");
//        return newFragment;
//    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Log.d("testing", "clossing Dialog");
        //start new activity
        String tag = dialog.getTag();
        if (tag.equals("failedDialog")) {
            dialog.dismiss();
            //Comeback to screen 1
            //TODO ViewMoreActivity
//            Intent intent = new Intent(this, ReportActivity.class);
//            startActivity(intent);
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
        //TODO: show ads randomly here
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
                            res += openTitle + line + "</b>/</div>";
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
     * Move to next word
     */
    private void goNextWord() {
//        Word[] words = get4Words();
        mWords = get4Words();
        // Set an image into view
        String fileName = getFilePath(this.currentWord.getWord());
        imageViewSet(imageView,fileName + ".jpg");
        for (int i = 0; i < 4; i++) {
            this.aBtn[i].setText(mWords[i].getWord());
//            this.iBtn[i].setText((words[i].getWord()));
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
        if(this.currentUnit.isLastWord()){
            if(this.currentGrade.isLastUnit()){
                //move to next grade
                //move to first of unit
                if (this.currGradeIndex < this.grades.size()) {
                    this.currGradeIndex++;
                    this.currentGrade = grades.get(currGradeIndex);
                    this.currentUnit = this.currentGrade.getCurrentUnit();
                    this.currentWord = this.currentUnit.getCurrentWord();
                }else{
                    Log.d(tag, "finished game");
                    return;
                }
            }else{
                //move to next unit
                this.currentUnit = this.currentGrade.getNextUnit();
                this.currentWord = this.currentUnit.getCurrentWord();
            }
        }else{
            //move to next word
            this.currentWord = this.currentUnit.getNextWord();
        }
    }

    /**
     *  Get four random words
     * @return Words array
     */
    private Word[] get4Words() {
        Word[] playWord = new Word[4];
        updateState(); //update state of game
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
