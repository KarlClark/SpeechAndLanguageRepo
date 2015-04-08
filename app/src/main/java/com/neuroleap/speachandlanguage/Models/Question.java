package com.neuroleap.speachandlanguage.Models;

/**
 * Created by Karl on 3/20/2015.
 */
public class Question {
    private int mId;
    private int mCategoryId;
    private int mColor=0;
    private String mText;
    private boolean mDone = false;

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }

    public int getId() {
        return mId;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public String getText() {
        return mText;
    }

    public Question(int id, int categoryId, String text, int color){
        mId = id;
        mCategoryId = categoryId;
        mText = text;
        mColor = color;
    }
}
