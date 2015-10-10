package com.idiotsapps.englishpicturedictionary4vietnam;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xtruhuy on 05/09/2015.
 */
public class Unit {
    private ArrayList<Word> words = new ArrayList<Word>();
    private String unitName;// unit_<unitInt>
    private int unitInt; //order of unit
    private int grade;
    private int percent;
    private int cWordIndex = 0;
    private Date date; // last time user do the test

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUnit() {
        return unitInt;
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

    public Unit(int grade, int unit, int percent) {
        this.unitInt = unit;
        this.unitName = "unit_" + this.unitInt;
        this.cWordIndex = 0;
        this.grade = grade;
        this.percent = percent;
    }
    public boolean isLastWord(){
        if(this.cWordIndex == (words.size() - 1)){
            return true;
        }else{
            return false;
        }
    }
}
