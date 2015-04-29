package com.neuroleap.speachandlanguage.Models;

/**
 * Created by Karl on 4/28/2015.
 */
public class PressedIcon {
    long mAnswerIconId;
    int mAnswerNumber;

    public PressedIcon(long answerIconId, int answerNumber){
        mAnswerNumber = answerNumber;
        mAnswerIconId = answerIconId;
    }

    public long getAnswerIconId() {
        return mAnswerIconId;
    }

    public int getAnswerNumber() {
        return mAnswerNumber;
    }
}
