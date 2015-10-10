package com.idiotsapps.chaoenglish;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by xtruhuy on 08/10/2015.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    private ArrayList<Grade> grades = new ArrayList<>();
    private AssetManager assetManager = null;
    private SQLiteDatabase db = null;
    public static final String TABLE_CLASSES = "classes";
    public static final String TABLE_UNIT = "unit";
    public static final String TABLE_TEST = "test";

    private static final String DATABASE_NAME = "englishstudy.db";
    private static final int DATABASE_VERSION = 1;
    private static final String VOCA_TEST = "vocabulary";
    private static final String GRAM_TEST = "grammar";
    private static final String LIST_TESt = "listening";
    private String tag = this.getClass().getSimpleName();
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CLASSES + " (class_id integer primary key autoincrement,class_name integer not null);";
    private static final String UNIT_CREATE = "create table " + TABLE_UNIT +
            " (unit_id integer primary key autoincrement,class_id integer,unit_name integer not null," +
            "number_of_words integer);";
    private static final String TEST_CREATE = "create table " + TABLE_TEST + " (test_id integer primary " +
            "key autoincrement, unit_id integer, vocabulary float, grammar float,listening float);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.assetManager = context.getAssets();
        getJsonDatabase();
//        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(tag, "create db helper");
        database.execSQL(DATABASE_CREATE);//Create classes table
        database.execSQL(UNIT_CREATE);//create unit table
        database.execSQL(TEST_CREATE);//create test table
        this.db = database;
        insertData(this.grades);
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
//        db.close();
        return rowId;
    }

    /**
     * Insert a new row for new class
     * @param className
     * @return
     */
    public int insertClass(int className) {
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("class_name", className);
        int rowId = (int) db.insert(TABLE_CLASSES, null, contentValues);
//        db.close();
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
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("class_id", classId);
        contentValues.put("unit_name", unitName);
        contentValues.put("number_of_words", numberOfWord);
        int rowId = (int) db.insert(TABLE_UNIT, null, contentValues);
//        db.close();
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
    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = assetManager.open("database");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * Analyse JSON string into classes, grades, units
     */
    private void getJsonDatabase() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray cArray = obj.getJSONArray("classes");
//            this.percent = obj.getInt("classes_percent");
//            Log.d(tag, "class_percent" + percent);

            for (int i = 0; i < cArray.length(); i++) {
                JSONObject gradeObj = cArray.getJSONObject(i);//Grade
                int grade = gradeObj.getInt("grade");
                int gradePercent = gradeObj.getInt("grade_percent");
                JSONArray vocabArray = gradeObj.getJSONArray("vocabulary");
                Log.d(tag, "grade:" + grade);
                Grade newGrade = new Grade(grade, gradePercent);
                for (int j = 0; j < vocabArray.length(); j++) {
                    JSONObject unitObj = vocabArray.getJSONObject(j); //Unit
                    int unit = unitObj.getInt("unit");
                    int unitPercent = unitObj.getInt("unit_percent");
                    JSONArray wordArray = unitObj.getJSONArray("words");
                    Unit newUnit = new Unit(grade, unit, unitPercent);
                    Log.d(tag, "Unit:" + unit);

                    for (int k = 0; k < wordArray.length(); k++) {
                        String word = wordArray.getString(k);
                        Word newWord = new Word(word);
                        newUnit.addWord(newWord);
                    }
                    newGrade.addUnit(newUnit);
                }
                Log.d(tag, "add grade:" + i);
                this.grades.add(newGrade);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Insert data into database
     *
     * @param grades
     */
    public void insertData(ArrayList<Grade> grades) {
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
                int testId = insertTestUnit(unitId, 0, 0, 0);
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
