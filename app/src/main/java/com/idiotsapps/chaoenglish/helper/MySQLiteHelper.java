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
    public static final String TABLE_TEST = "test";

    private static final String DATABASE_NAME = "chaoenglish.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String VOCA_TEST = "vocab_test";
    private static final String GRAM_TEST = "grammar_test";
    private static final String LIST_TEST = "listen_test";
    private static final String UNIT_ID = "unit_id";
    private static final String UNIT_NAME = "unit_name";
    private static final String CLASS_ID = "class_id";
    private static final String NUMBER_OF_WORDS = "number_of_words";
    private Context myContext = null;
    private static MySQLiteHelper mInstance = null;
    private String tag = this.getClass().getSimpleName();

    // Database creation sql statement
//    private static final String DATABASE_CREATE = "create table "
//            + TABLE_CLASSES + " (class_id integer primary key autoincrement,class_name integer not null);";
//    private static final String UNIT_CREATE = "create table " + TABLE_UNIT +
//            " (unit_id integer primary key autoincrement,class_id integer,unit_name integer not null," +
//            "number_of_words integer);";
//    private static final String TEST_CREATE = "create table " + TABLE_TEST + " (test_id integer primary " +
//            "key autoincrement, unit_id integer, vocabulary float, grammar float,listening float);";

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
     * This function will return the percentage of every classes
     * 1. Get list of class id from database
     * 2. Get list of unit of class
     * 3. Calculate average of percentages
     */
    public ArrayList<Grade> getClasses(){
        String collum[] = {"rowid",this.CLASS_ID};
        ArrayList<Grade> classes = new ArrayList<>();
        Log.d(tag, "get classes");
        Cursor cursor = this.db.query(this.TABLE_CLASSES, collum, null, null, null, null,null);
        if (cursor.moveToFirst()) {
            do {
                // unit_id,unit_name, number_of_words,vocab_test,grammar_test,listen_test
                int rowId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("rowid")));
                int classId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(this.CLASS_ID)));
                ArrayList<Unit> units = getUnitList(classId);
                Grade grade = new Grade(classId);
                grade.setUnits(units);
                classes.add(grade);
            } while (cursor.moveToNext());
        }
        Log.d(tag, "leave get classes");
        return classes;
    }

    public ArrayList<Unit> getUnitList(int class_id) {
        String collum[] = {this.UNIT_ID,this.UNIT_NAME,this.NUMBER_OF_WORDS, this.VOCA_TEST,this.LIST_TEST,this.GRAM_TEST};
        ArrayList<Unit> units = new ArrayList<>();
        String selection = "class_id =" + class_id;
        Log.d(tag, "get Unit list");
        Cursor unitCursor = this.db.query(this.TABLE_UNIT, collum, selection,null, null, null, null);
        if (unitCursor.moveToFirst()) {
            do {
                // unit_id,unit_name, number_of_words,vocab_test,grammar_test,listen_test
                int unitId = Integer.parseInt(unitCursor.getString(unitCursor.getColumnIndex(this.UNIT_ID)));
                int unitName = Integer.parseInt(unitCursor.getString(unitCursor.getColumnIndex(this.UNIT_NAME)));
                int numOfWord = Integer.parseInt(unitCursor.getString(unitCursor.getColumnIndex(this.NUMBER_OF_WORDS)));
                float vocabTest = Float.parseFloat(unitCursor.getString(unitCursor.getColumnIndex(this.VOCA_TEST)));
                float gramTest = Float.parseFloat(unitCursor.getString(unitCursor.getColumnIndex(this.GRAM_TEST)));
                float listTest = Float.parseFloat(unitCursor.getString(unitCursor.getColumnIndex(this.LIST_TEST)));
                units.add(new Unit(class_id,unitId,unitName,numOfWord,vocabTest,gramTest,listTest));
                Log.d(tag, "classid: " + class_id + " unitName:" + unitName + " numOfWord:" + numOfWord
                        + " vocabTest:" + vocabTest + " gramTest:" + gramTest + " listTest:" + listTest);
            } while (unitCursor.moveToNext());
        }
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
//		File sqliteFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/sinhhuynh.game.quiz/files/data/" + DB_NAME);
//		InputStream mInput = new FileInputStream(sqliteFile);

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
    public int insertTestUnit(int unitId, float vocaRes, float gramRes, float listRes) {
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("unit_id", unitId);
        contentValues.put("vocabulary", vocaRes);
        contentValues.put("grammar", gramRes);
        contentValues.put("listening", listRes);
        int rowId = (int) db.insert(TABLE_TEST, null, contentValues);
        return rowId;
    }

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


    /**
     * Load Json file of list words into a string
     *
     * @return
     */
//    private String loadJSONFromAsset() {
//        String json = null;
//        try {
//            InputStream is = assetManager.open("database");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }

//    /**
//     * Analyse JSON string into classes, grades, units
//     */
//    private void getJsonDatabase() {
//        try {
//            JSONObject obj = new JSONObject(loadJSONFromAsset());
//            JSONArray cArray = obj.getJSONArray("classes");
////            this.percent = obj.getInt("classes_percent");
////            Log.d(tag, "class_percent" + percent);
//
//            for (int i = 0; i < cArray.length(); i++) {
//                JSONObject gradeObj = cArray.getJSONObject(i);//Grade
//                int grade = gradeObj.getInt("grade");
//                int gradePercent = gradeObj.getInt("grade_percent");
//                JSONArray vocabArray = gradeObj.getJSONArray("vocabulary");
//                Log.d(tag, "grade:" + grade);
//                Grade newGrade = new Grade(grade, gradePercent);
//                for (int j = 0; j < vocabArray.length(); j++) {
//                    JSONObject unitObj = vocabArray.getJSONObject(j); //Unit
//                    int unit = unitObj.getInt("unit");
//                    int unitPercent = unitObj.getInt("unit_percent");
//                    JSONArray wordArray = unitObj.getJSONArray("words");
//                    Unit newUnit = new Unit(grade, unit, unitPercent);
//                    Log.d(tag, "Unit:" + unit);
//
//                    for (int k = 0; k < wordArray.length(); k++) {
//                        String word = wordArray.getString(k);
//                        Word newWord = new Word(word);
//                        newUnit.addWord(newWord);
//                    }
//                    newGrade.addUnit(newUnit);
//                }
//                Log.d(tag, "add grade:" + i);
//                this.grades.add(newGrade);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * Insert data into database
     *
     * @param grades
     */
    public void insertData(ArrayList<Grade> grades) {
        int percent[] = new int[]{0,10,30,75,80,90,100 }; //TODO: dumpy data, remove later
        for (int i = 0; i < grades.size(); i++) {
            Grade grade = grades.get(i);
            int className = grade.getGrade();
            int classId = insertClass(className);//add row for class
            Log.d(tag, "class id: " + classId);
            ArrayList<Unit> units = grade.getUnits();
            for (int j = 0; j < units.size(); j++) {
                Unit unit = units.get(i);
                int unitName = unit.getUnit();
                int numOfWords = unit.getWords().size();
                int unitId = insertUnit(unitName, classId, numOfWords);
                Log.d(tag, "unit id: " + unitId);
                int testId = insertTestUnit(unitId, percent[i],percent[i], percent[i]);
                Log.d(tag, "test id: " + testId);
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        onCreate(db);
    }
}
