package com.idiotsapps.chaoenglish.ui.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.idiotsapps.chaoenglish.Grade;
import com.idiotsapps.chaoenglish.helper.HelperApplication;
import com.idiotsapps.chaoenglish.ui.dialog.InfoDialogFragment;
import com.idiotsapps.chaoenglish.helper.MySQLiteHelper;
import com.idiotsapps.chaoenglish.R;
import com.idiotsapps.chaoenglish.Unit;
import com.idiotsapps.chaoenglish.Word;
import com.idiotsapps.chaoenglish.helper.PreferencesHelper;
import com.idiotsapps.chaoenglish.stardict.StarDict;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class QuesActivity extends Activity implements InfoDialogFragment.NoticeDialogListener
{
    //Hoang test push
    //HoangNHM_br
    private ArrayList<Grade> grades = new ArrayList<Grade>();
    private Unit currentUnit;
    private Word currentWord;
    private Grade  currentGrade;
    private int currGradeIndex = 0;
    private int percent = 0;
    private ImageView imageView;
    private Button[] aBtn = new Button[4];
    private Button[] iBtn = new Button[4];
    private StarDict starDict = null;
    private String tag = this.getClass().getSimpleName();
    private MySQLiteHelper mySQLiteHelper = null;

    // Check if app first launch, save path of database
    private PreferencesHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ques);
//        Intent intent = getIntent();
//        this.grade = intent.getIntExtra("grade", 6);
        prefsHelper = new PreferencesHelper(getApplicationContext());


//        mySQLiteHelper = new MySQLiteHelper(this); //intialize sqlite helper
        mySQLiteHelper = HelperApplication.sMySQLiteHelper; //intialize sqlite helper
        this.grades = mySQLiteHelper.getGrades();
        mySQLiteHelper.getWritableDatabase();
        getStarDict(); //initalize StarDict stuff

        /**
         * Init current status of user
         * This below snippet code will be replace
         * by other initialization
         */
        this.currentGrade = grades.get(0);//list starts from 0
        this.currentUnit = this.currentGrade.getCurrentUnit();
        this.currentWord = this.currentUnit.getCurrentWord();
        this.imageView = (ImageView) findViewById(R.id.wordImage);

        // Answers buttons
        this.aBtn[0] = (Button) findViewById(R.id.button_A);
        this.aBtn[1] = (Button) findViewById(R.id.button_B);
        this.aBtn[2] = (Button) findViewById(R.id.button_C);
        this.aBtn[3] = (Button) findViewById(R.id.button_D);
        addAnswBtnListener();

        // Information buttons
        this.iBtn[0] = (Button) findViewById(R.id.button_S);
        this.iBtn[1] = (Button) findViewById(R.id.button_X);
        this.iBtn[2] = (Button) findViewById(R.id.button_Y);
        this.iBtn[3] = (Button) findViewById(R.id.button_Z);
        addInfoBtnListener();

        //Starting game
        goNextWord();
    }

    //++del HoangNHM 20151013 move to FirstStartActivity
//    /**
//     * It will copy asset dictionary files to an external location
//     */
//    private void copyDictToSdCard() {
//        AssetManager assetManager = getAssets();
//        this.dictExterPath = Environment.getExternalStorageDirectory().toString() + "/" + this.dictAssestPath;
//        File starDict = new File(this.dictExterPath);
//
//        if(!starDict.exists()){
//            starDict.mkdirs();
//            String[] files = null;
//            try {
//                files = assetManager.list(this.dictAssestPath);
//            } catch (IOException e) {
//                Log.e(tag, e.getMessage());
//            }
//
//            for(String filename : files) {
//                Log.d(tag, "file: " + filename);
//                InputStream in = null;
//                OutputStream out = null;
//                try {
//                    in = assetManager.open(this.dictAssestPath + "/" + filename);
//                    Log.d(tag, "file path: " + this.dictExterPath + "/" + filename);
//                    out = new FileOutputStream(this.dictExterPath + "/" + filename);
//                    copyFile(in, out);
//                    in.close();
//                    in = null;
//                    out.flush();
//                    out.close();
//                    out = null;
//                } catch (Exception e) {
//                    Log.e(tag, e.getMessage());
//                }
//            }
//        }
//        else {
//            //TODO: Verify exist files
//            Log.d(tag, "dir. already exists");
//        }
//    }
//
//    /**
//     * It will write data from input stream to an output stream as a file
//     * @param in Input stream
//     * @param out Output stream of a file
//     * @throws IOException
//     */
//    private void copyFile(InputStream in, OutputStream out) throws IOException {
//        byte[] buffer = new byte[1024];
//        int read;
//        while((read = in.read(buffer)) != -1){
//            out.write(buffer, 0, read);
//        }
//    }
    //--del HoangNHM 20151013 move to FirstStartActivity

    /**
     * - Copy the dict database from asset for sdcard
     * - Create new StarDict object for app
     */
    private void getStarDict() {
        Log.d(tag, "enter getStarDict");
        //++del HoangNHM 20151013 move to FirstStartActivity
//        copyDictToSdCard();
        //--del HoangNHM 20151013 move to FirstStartActivity
        // get path from pref
        if (null != HelperApplication.sStarDict) {
            this.starDict = HelperApplication.sStarDict;
            Log.d("testing", "new StarDict");
        } else {
            Log.d("testing", "some thing wrong");
        }
        Log.d("testing", "leave getStarDict");
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
     *  Verify if user's answer is correct or not
     *  Will show advertisement if it is corrected
     * @param word
     */
    private void verifyAnswer(String word){
        String currWord = this.currentWord.getWord();
        if (currWord.equals(word)) {
            goNextWord();
        }else {
            //show ads
            String htmlText = this.starDict.lookupWord(word);
            showFailedDialog(htmlText);
        }
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
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
        // User touched the dialog's negative button
    }

    /**
     * Show definition, pronunciation, example for a word
     * This word user want to know more about its meaning
     * @param word
     */
    public void showHelpInfo(String word){
        //TODO: show ads randomly here
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        final InfoDialogFragment newFragment = new InfoDialogFragment();
        String htmlText = this.starDict.lookupWord(word);
        newFragment.show(ft, "infoDialog");
        newFragment.setWord(htmlText);
    }

    /**
     * Show definition, pronunciation, example for click word
     * @param word
     */
    public void showFailedDialog(String word) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        final InfoDialogFragment newFragment = new InfoDialogFragment();
        newFragment.show(ft, "failedDialog");
        newFragment.setWord(word);
    }

    /**
     * Add listener for info buttons
     */
    private void addInfoBtnListener() {
        for (int i = 0; i < this.iBtn.length; i++) {
            this.iBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b = (Button) view;
                    String word = b.getText().toString();
                    showHelpInfo(word);
                }
            });
        }
    }

    /**
     * Move to next word
     */
    private void goNextWord() {
        Word[] words = get4Words();
        // Set an image into view
        String fileName = getFilePath(this.currentWord.getWord());
        imageViewSet(imageView,fileName + ".jpg");
        for (int i = 0; i < 4; i++) {
            this.aBtn[i].setText(words[i].getWord());
            this.iBtn[i].setText((words[i].getWord()));
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
        out = "out/" + "grade_" + grade + "/" + this.currentUnit.getUnitName() + "/" + out;
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