package com.idiotsapps.chaoenglish;

/**
 * Created by xtruhuy on 05/09/2015.
 */
public class Word {
    private String word;
    private int classId;
    private int unitName;
    private int numOfWrong;
    private int numOfCorr;
    private String[] defintions;
    private String[] example;
    private String[] pronounciation;

    public Word(String word,int classId, int unitName,int numOfCorr, int numOfWrong) {
        this.word = word;
        this.classId = classId;
        this.unitName = unitName;
        this.numOfCorr = numOfCorr;
        this.numOfWrong = numOfWrong;
    }

    public String[] getDefintions() {

        return defintions;
    }

    public void setDefintions(String[] defintions) {
        this.defintions = defintions;
    }

    public String[] getExample() {
        return example;
    }

    public void setExample(String[] example) {
        this.example = example;
    }

    public String[] getPronounciation() {
        return pronounciation;
    }

    public void setPronounciation(String[] pronounciation) {
        this.pronounciation = pronounciation;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
