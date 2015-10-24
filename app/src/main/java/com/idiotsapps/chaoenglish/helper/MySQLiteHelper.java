package com.idiotsapps.chaoenglish.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.idiotsapps.chaoenglish.Grade;
import com.idiotsapps.chaoenglish.Unit;
import com.idiotsapps.chaoenglish.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private ArrayList<Grade> grades = new ArrayList<>();
    private AssetManager assetManager = null;
    private SQLiteDatabase db = null;
    public static final String TABLE_CLASSES = "classes";
    public static final String TABLE_UNIT = "unit";
    public static final String TABLE_WORD = "words";

    private static final String DATABASE_NAME = "chaoenglish.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String VOCA_TEST = "vocab_test";
    private static final String GRAM_TEST = "grammar_test";
    private static final String LIST_TEST = "listen_test";
    private static final String UNIT_ID = "unit_id";
    private static final String UNIT_NAME = "unit_name";
    private static final String CLASS_ID = "class_id";
    private static final String NUMBER_OF_WORDS = "number_of_words";
    private static final String NUMBER_OF_CORRECT = "number_of_correct_time";
    private static final String NUMBER_OF_WRONG = "number_of_wrong_time";
    private static final String WORD = "word";
    private Context myContext = null;
    private static MySQLiteHelper mInstance = null;
    private String tag = this.getClass().getSimpleName();

    public static MySQLiteHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new MySQLiteHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
//        this.assetManager = context.getAssets();
        //TODO: copy file database for use


    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(tag, "create db helper");
        this.db = database;
//        openDataBase();
//        createDataBase();
////        insertData(this.grades);
    }

    /**
     *
     * @param classId
     * @param unitName
     * @return
     */
    public ArrayList<Word> getWords(int classId, int unitName){
        String column[] = {MySQLiteHelper.NUMBER_OF_CORRECT,MySQLiteHelper.NUMBER_OF_WRONG,MySQLiteHelper.WORD};
        ArrayList<Word> words = new ArrayList<>();
        String selection = "class_id =" + classId + " and unit_name=" + unitName;
        Log.d(tag, "get word list");
        Cursor unitCursor = this.db.query(MySQLiteHelper.TABLE_WORD, column, selection,null, null, null, null);
        if (unitCursor.moveToFirst()) {
            do {
                // unit_id,unit_name, number_of_words,vocab_test,grammar_test,listen_test
                String word = unitCursor.getString(unitCursor.getColumnIndex(MySQLiteHelper.WORD));
                int numOfCorr = Integer.parseInt(unitCursor.getString(unitCursor.getColumnIndex(MySQLiteHelper.NUMBER_OF_CORRECT)));
                int numOfWrong = 0;
                String temp = unitCursor.getString(unitCursor.getColumnIndex(MySQLiteHelper.NUMBER_OF_WRONG));
                if(temp != null) {
                    numOfWrong = Integer.parseInt(temp);
                }
                words.add(new Word(word,classId,unitName,numOfCorr,numOfWrong));
                Log.d(tag, "classid: " + classId + " unitName:" + unitName + " Word:" + word
                        + " numOfCorr:" + numOfCorr + " numOfWrong:" + numOfWrong);
            } while (unitCursor.moveToNext());
        }
        unitCursor.close();
        return words;
    }
    /**
     * This function will return the percentage of every classes
     * 1. Get list of class id from database
     * 2. Get list of unit of class
     * 3. Calculate average of percentages
     */
    public ArrayList<Grade> getClasses(){
        String column[] = {"rowid",MySQLiteHelper.CLASS_ID};
        ArrayList<Grade> classes = new ArrayList<>();
        Log.d(tag, "get classes");
        Cursor cursor = this.db.query(MySQLiteHelper.TABLE_CLASSES, column, null, null, null, null,null);
        if (cursor.moveToFirst()) {
            do {
                // unit_id,unit_name, number_of_words,vocab_test,grammar_test,listen_test
                int rowId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("rowid")));
                int classId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.CLASS_ID)));
                ArrayList<Unit> units = getUnitList(classId);
                Grade grade = new Grade(classId);
                grade.setUnits(units);
                classes.add(grade);
            } while (cursor.moveToNext());
        }
        Log.d(tag, "leave get classes");
        cursor.close();
        return classes;
    }

    public ArrayList<Unit> getUnitList(int class_id) {
        String column[] = {MySQLiteHelper.UNIT_ID,MySQLiteHelper.UNIT_NAME,MySQLiteHelper.NUMBER_OF_WORDS,
                MySQLiteHelper.VOCA_TEST,MySQLiteHelper.LIST_TEST,MySQLiteHelper.GRAM_TEST};
        ArrayList<Unit> units = new ArrayList<>();
        String selection = "class_id =" + class_id;
        Log.d(tag, "get Unit list");
        Cursor unitCursor = this.db.query(MySQLiteHelper.TABLE_UNIT, column, selection,null, null, null, null);
        if (unitCursor.moveToFirst()) {
            do {
                // unit_id,unit_name, number_of_words,vocab_test,grammar_test,listen_test
                int unitId = Integer.parseInt(unitCursor.getString(unitCursor.getColumnIndex(MySQLiteHelper.UNIT_ID)));
                int unitName = Integer.parseInt(unitCursor.getString(unitCursor.getColumnIndex(MySQLiteHelper.UNIT_NAME)));
                int numOfWord = Integer.parseInt(unitCursor.getString(unitCursor.getColumnIndex(MySQLiteHelper.NUMBER_OF_WORDS)));
                float vocabTest = Float.parseFloat(unitCursor.getString(unitCursor.getColumnIndex(MySQLiteHelper.VOCA_TEST)));
                float gramTest = Float.parseFloat(unitCursor.getString(unitCursor.getColumnIndex(MySQLiteHelper.GRAM_TEST)));
                float listTest = Float.parseFloat(unitCursor.getString(unitCursor.getColumnIndex(MySQLiteHelper.LIST_TEST)));
                units.add(new Unit(class_id,unitId,unitName,numOfWord,vocabTest,gramTest,listTest));
                Log.d(tag, "classid: " + class_id + " unitName:" + unitName + " numOfWord:" + numOfWord
                        + " vocabTest:" + vocabTest + " gramTest:" + gramTest + " listTest:" + listTest);
            } while (unitCursor.moveToNext());
        }
        unitCursor.close();
        return units;
    }

    public void createDataBase() {
        // If database does not exist, then copy it from the assets
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                // Copy the database from assets
                copyDataBase();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }

    }
    public boolean openDataBase()  {
        String mPath = "/data/data/" + myContext.getPackageName() + "/databases/" + DATABASE_NAME;
//		Log.v("mPath", mPath);
        this.db = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.CREATE_IF_NECESSARY);
//		mDataBase = SQLiteDatabase.openDatabase(mPath, null,SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return this.db != null;
    }

    private boolean checkDataBase() {
        File dbFile = new File("/data/data/" + myContext.getPackageName() + "/databases/" + DATABASE_NAME);
        return dbFile.exists();
    }

    // Copy the database from assets
    private void copyDataBase() throws IOException {
        InputStream mInput = myContext.getAssets().open(DATABASE_NAME);
        String outFileName = "/data/data/" + myContext.getPackageName() + "/databases/" + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //TODO
    public int updateTestUnit(int unitId, String testType, float result) {

        return 0;
    }

    /**
     * Insert a new row of new test of new unit
     * @param unitId
     * @param vocaRes
     * @param gramRes
     * @param listRes
     * @return
     */
//    public int insertTestUnit(int unitId, float vocaRes, float gramRes, float listRes) {
////        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("unit_id", unitId);
//        contentValues.put("vocabulary", vocaRes);
//        contentValues.put("grammar", gramRes);
//        contentValues.put("listening", listRes);
//        int rowId = (int) db.insert(TABLE_TEST, null, contentValues);
//        return rowId;
//    }

    /**
     * Insert a new row for new class
     * @param className
     * @return
     */
    public int insertClass(int className) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("class_name", className);
        int rowId = (int) db.insert(TABLE_CLASSES, null, contentValues);
        return rowId;
    }

    /**
     * Insert a new row for a new unit
     * @param unitName
     * @param classId
     * @param numberOfWord
     * @return
     */
    public int insertUnit(int unitName, int classId, int numberOfWord) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("class_id", classId);
        contentValues.put("unit_name", unitName);
        contentValues.put("number_of_words", numberOfWord);
        int rowId = (int) db.insert(TABLE_UNIT, null, contentValues);
        return rowId;
    }

    /**
     * Get asset manager of context
     * @param assetManager
     */
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public ArrayList<Grade> getGrades() {
        return grades;
    }

//    /**
//     * Insert data into database
//     *
//     * @param grades
//     */
//    public void insertData(ArrayList<Grade> grades) {
//        int percent[] = new int[]{0,10,30,75,80,90,100 }; //TODO: dumpy data, remove later
//        for (int i = 0; i < grades.size(); i++) {
//            Grade grade = grades.get(i);
//            int className = grade.getGrade();
//            int classId = insertClass(className);//add row for class
//            Log.d(tag, "class id: " + classId);
//            ArrayList<Unit> units = grade.getUnits();
//            for (int j = 0; j < units.size(); j++) {
//                Unit unit = units.get(i);
//                int unitName = unit.getUnit();
//                int numOfWords = unit.getWords().size();
//                int unitId = insertUnit(unitName, classId, numOfWords);
//                Log.d(tag, "unit id: " + unitId);
//                int testId = insertTestUnit(unitId, percent[i],percent[i], percent[i]);
//                Log.d(tag, "test id: " + testId);
//            }
//        }
//
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        onCreate(db);
    }
}
