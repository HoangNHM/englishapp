package com.idiotsapps.englishpicturedictionary4vietnam;

/**
 * Created by xtruhuy on 05/09/2015.
 */
public class Word {
    private String word;
    private String[] defintions;
    private String[] example;
    private String[] pronounciation;

    public Word(String word) {
        this.word = word;
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
