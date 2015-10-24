package com.idiotsapps.chaoenglish;
import android.os.Parcel;
import android.os.Parcelable;

import com.idiotsapps.chaoenglish.helper.HelperApplication;


import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xtruhuy on 05/09/2015.
 */
public class Unit implements Parcelable{
    private ArrayList<Word> words = new ArrayList<Word>();
    private int unitName;//1,2,3...
    private int unitId; //rowId of Unit
    private int grade;
    private float vocabPercent;
    private float listenPercent;
    private float grammarPercent;
    private int numOfWord;
    private int cWordIndex = 0;
    private Date date; // last time user do the test

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
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
//        if(words.size() == 0){
        words = HelperApplication.sMySQLiteHelper.getWords(this.grade,this.unitName);
//        }
        return words;
    }

    public void addWord(Word word) {
        words.add(word);
    }

    public Unit(int grade, int unitId,int unitName,int numOfWord, float vPercent, float gPercent, float lPercent) {
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
}
