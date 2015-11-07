package com.idiotsapps.chaoenglish.helper;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.idiotsapps.chaoenglish.Grade;
import com.idiotsapps.chaoenglish.Unit;
import com.idiotsapps.chaoenglish.Word;

/**
 * Created by vantuegia on 11/1/2015.
 */
public class QuizHelper {

    private ImageView mImgQues;         // Question imgae

    private SoundHelper mSoundHelper = HelperApplication.sSoundHelper;   // Sound effect

    private ProgressBar progressBar;    // Progress Bar

    private RelativeLayout mChoseBoard; // Board show after choose

    private int mLifeRemain; // Life remain
    private ImageView[] mImgLife = new ImageView[3];    // Life remain icons

    private ImageView[] mImgChoice = new ImageView[4];  // 4 Choice Option
    private Button[] mBtnMeaning = new Button[4];       // 4 Help show meaning

    private Grade mCurrentGrade; // Current Grade
    private Unit mCurrentUnit;   // Current Unit
    private Word mCurrentWord;   // Current Word

    private QuizHelper(QuizBuilder builder) {
        this.mImgQues = builder.mImgQues;
        this.progressBar = builder.mProgressBar;
        this.mChoseBoard = builder.mChoseBoard;
        this.mLifeRemain = builder.mImgLife.length; // max life remain as begin
        this.mImgLife = builder.mImgLife;
        this.mImgChoice = builder.mImgChoice;
        this.mBtnMeaning = builder.mBtnMeaning;
        this.mCurrentGrade = builder.mCurrentGrade;
        this.mCurrentUnit = builder.mCurrentUnit;
    }

    public static class QuizBuilder {

        public QuizBuilder() {
        }

        public QuizHelper build() {
            return new QuizHelper(this);
        }

        private ImageView mImgQues;         // Question imgae

        private ProgressBar mProgressBar;    // Progress Bar

        private RelativeLayout mChoseBoard; // Board show after choose

        private ImageView[] mImgLife = new ImageView[3];    // Life remain icons

        private ImageView[] mImgChoice = new ImageView[4];  // 4 Choice Option
        private Button[] mBtnMeaning = new Button[4];       // 4 Help show meaning

        private Grade mCurrentGrade; // Current Grade
        private Unit mCurrentUnit;   // Current Unit

        // builder methods for setting property
        public QuizBuilder imgQues(ImageView imageView) {
            this.mImgQues = imageView;
            return this;
        }

        public QuizBuilder progressBoard(ProgressBar progressBar) {
            this.mProgressBar = progressBar;
            return this;
        }

        public QuizBuilder choseBoard(RelativeLayout layout) {
            this.mChoseBoard = layout;
            return this;
        }

        public QuizBuilder imgChoice(ImageView[] views) {
            this.mImgChoice = views;
            return this;
        }

        public QuizBuilder btnMeaning(Button[] buttons) {
            this.mBtnMeaning = buttons;
            return this;
        }

        public QuizBuilder currentGrade(Grade grade) {
            this.mCurrentGrade = grade;
            return this;
        }

        public QuizBuilder currentUnit(Unit unit) {
            this.mCurrentUnit = unit;
            return this;
        }
    }
}
