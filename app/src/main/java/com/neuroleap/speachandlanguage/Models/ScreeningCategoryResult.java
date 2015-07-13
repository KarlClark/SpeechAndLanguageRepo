package com.neuroleap.speachandlanguage.Models;

import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 5/12/2015.
 */
public class ScreeningCategoryResult {
    private long mScreeningCategoryId;
    private String mScreeningCategoryNameEg;
    private String mScreeningCategoryNameSp;
    private boolean mIsCompleted;
    private float mNumberCorrectAnswers;
    private float mNumberOfQuestions;
    private float mPercentCorrect;
    private boolean mPassed;

    public ScreeningCategoryResult (long screeningCategoryId, String screeningCategoryNameEg, String screeningCategoryNameSp,
                                    boolean isCompleted, float numberCorrectAnswers, float numberOfQuestions){
        mScreeningCategoryId = screeningCategoryId;
        mScreeningCategoryNameEg = screeningCategoryNameEg;
        mScreeningCategoryNameSp = screeningCategoryNameSp;
        mIsCompleted = isCompleted;
        mNumberCorrectAnswers = numberCorrectAnswers;
        mNumberOfQuestions =  numberOfQuestions;
        mPassed = numberCorrectAnswers/numberOfQuestions >= Utilities.PASSING_FRACTION;
        mPercentCorrect = mNumberCorrectAnswers/mNumberOfQuestions * 100.0f;
    }

    public String getScreeningCategoryNameEg() {
        return mScreeningCategoryNameEg;
    }

    public String getScreeningCategoryNameSp() {
        return mScreeningCategoryNameSp;
    }

    public boolean isCompleted() {
        return mIsCompleted;
    }

    public float getNumberCorrectAnswers() {
        return mNumberCorrectAnswers;
    }

    public float getNumberOfQuestions() {
        return mNumberOfQuestions;
    }

    public float getPercentCorrect() {
        return mPercentCorrect;
    }

    public boolean isPassed() {
        return mPassed;
    }

    public long getScreeningCategoryId() {
        return mScreeningCategoryId;
    }
}
