package com.idiotsapps.chaoenglish;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xtruhuy on 05/09/2015.
 */
public class Unit {
    private ArrayList<Word> words = new ArrayList<Word>();
    private String unitName;// unit_<unitInt>
    private int unitId; //order of unit
    private int grade;
    private float vocabPercent;
    private float listenPercent;
    private float grammarPercent;
    private int numOfWord;
    private int cWordIndex = 0;
    private Date date; // last time user do the test

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
    public String getUnitName(){
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
        return words;
    }

    public void addWord(Word word) {
        words.add(word);
    }

    public Unit(int grade, int unitId,int unitName,int numOfWord, float vPercent, float gPercent, float lPercent) {
        this.unitId= unitId;
        this.numOfWord = numOfWord;
        this.unitName = "unit_" + this.unitName;
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
}
