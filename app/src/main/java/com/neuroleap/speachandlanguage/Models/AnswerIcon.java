package com.neuroleap.speachandlanguage.Models;

/**
 * Created by Karl on 4/28/2015.
 */
public class AnswerIcon {
    private long mAnswerIconId;
    private String mFilename;

    public AnswerIcon(long answerIconId, String filename){
        mAnswerIconId = answerIconId;
        mFilename = filename;
    }

    public long getAnswerIconId() {
        return mAnswerIconId;
    }

    public String getFilename() {
        return mFilename;
    }
}
