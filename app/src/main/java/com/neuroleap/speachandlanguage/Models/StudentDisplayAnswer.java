package com.neuroleap.speachandlanguage.Models;

import com.neuroleap.speachandlanguage.Utility.DbCRUD;

/**
 * Created by Karl on 7/15/2015.
 */
public class StudentDisplayAnswer implements Comparable<StudentDisplayAnswer>{
    private int mAnswerNumber;
    private int mAnswerType;
    private String mAnswerText;
    public static final int ICON = 1;
    public static final int TEXT = 2;

    public StudentDisplayAnswer(String answerText, int answerNumber){
        mAnswerText = answerText;
        mAnswerNumber = answerNumber;
        mAnswerType = TEXT;
    }

    public StudentDisplayAnswer(long answerIconId, int answerNumber){
        mAnswerText = DbCRUD.getIconDescription(answerIconId);
        mAnswerNumber = answerNumber;
        mAnswerType = ICON;
    }

    public StudentDisplayAnswer(){
        mAnswerText = "Not Answered";
        mAnswerNumber = 1;
    }

    public int getAnswerNumber() {
        return mAnswerNumber;
    }

    public int getAnswerType() {
        return mAnswerType;
    }

    public String getAnswerText() {
        return mAnswerText;
    }

    @Override
    public int compareTo(StudentDisplayAnswer sda){
        return Integer.compare(this.mAnswerNumber,sda.getAnswerNumber());
    }
}
