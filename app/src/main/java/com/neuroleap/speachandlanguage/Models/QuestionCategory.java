package com.neuroleap.speachandlanguage.Models;

/**
 * Created by Karl on 3/20/2015.
 */
public class QuestionCategory {
    private int mId;
    private String mText;

    public int getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }

    public QuestionCategory(int id, String text){
        mId = id;
        mText = text;

    }
}
