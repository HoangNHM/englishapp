package com.idiotsapps.chaoenglish;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.idiotsapps.chaoenglish.helper.HelperApplication;


import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * Created by xtruhuy on 05/09/2015.
 */
public class Unit implements Comparable, Parcelable{
    private ArrayList<Word> words = new ArrayList<Word>();
    private int unitName;//1,2,3...
    private int unitId; //rowId of Unit
    private int grade;
    private float vocabPercent;
    private float listenPercent;
    private float grammarPercent;
    private int numOfWord;
    private int cWordIndex = 0;
    private int mRightCount = 0;

    public int getRightCount() {
        return mRightCount;
    }

    public void increaseRightCount() {
        this.mRightCount += 1;
    }

    protected Unit(Parcel in) {
        unitName = in.readInt();
        unitId = in.readInt();
        grade = in.readInt();
        vocabPercent = in.readFloat();
        listenPercent = in.readFloat();
        grammarPercent = in.readFloat();
        numOfWord = in.readInt();
        cWordIndex = in.readInt();
    }

    public static final Creator<Unit> CREATOR = new Creator<Unit>() {
        @Override
        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };

    public float getVocabPercent() {
        return vocabPercent;
    }
    public int getUnit() {
        return unitId;
    }
    public int getUnitName(){
        return unitName;
    }
    public Word getCurrentWord() {
        return words.get(cWordIndex);
    }
    public Word getNextWord(){
        if(cWordIndex < (words.size() - 1)){
            cWordIndex++;
            return words.get(cWordIndex);
        }else
            return null;
    }
    public int getcWordIndex(){
        return cWordIndex;
    }
    public ArrayList<Word> getWords() {
        //If words have not retrieve from database
        if(words.size() == 0){
            words = HelperApplication.sMySQLiteHelper.getWords(this.grade,this.unitName);
            long seed = System.nanoTime();
            Collections.shuffle(words, new Random(seed));
        }
        return words;
    }

    public void updateVocabPercent() {
        Log.d("testing", "cWordIndex:" + mRightCount + "size:" + words.size());
        this.vocabPercent = mRightCount * 100 / words.size();
        HelperApplication.sMySQLiteHelper.updateTestResult(this.unitId,
                Integer.toString((int) this.vocabPercent), Integer.toString((int) this.grammarPercent),
                Integer.toString((int) this.listenPercent));
    }

    public Unit(int grade, int unitId,int unitName,int numOfWord, float vPercent,
                float gPercent, float lPercent) {
        this.unitId= unitId;
        this.numOfWord = numOfWord;
        this.unitName = unitName;
        this.cWordIndex = 0;
        this.grade = grade;
        this.grammarPercent = gPercent;
        this.vocabPercent = vPercent;
        this.listenPercent = lPercent;
    }

    public boolean isLastWord(){
        if(this.cWordIndex == (words.size() - 1)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(unitName);
        dest.writeInt(unitId);
        dest.writeInt(grade);
        dest.writeFloat(vocabPercent);
        dest.writeFloat(listenPercent);
        dest.writeFloat(grammarPercent);
        dest.writeInt(numOfWord);
        dest.writeInt(cWordIndex);
    }


    @Override
    public int compareTo(Object o) {

        return this.unitName - ((Unit)o).getUnitName();
    }
}
