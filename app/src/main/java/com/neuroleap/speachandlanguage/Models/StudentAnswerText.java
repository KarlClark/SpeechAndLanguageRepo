package com.neuroleap.speachandlanguage.Models;

/**
 * Created by Karl on 4/27/2015.
 */
public class StudentAnswerText {

    private int mAnswerNumber;
    private String mText;

    public StudentAnswerText(int answerNumber, String text){
        mAnswerNumber = answerNumber;
        mText = text;
    }

    public int getAnswerNumber() {
        return mAnswerNumber;
    }

    public String getText() {
        return mText;
    }
}
