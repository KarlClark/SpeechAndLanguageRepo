package com.neuroleap.speachandlanguage.Models;

/**
 * Created by Karl on 3/20/2015.
 */
public class QuestionCategory {
    private int mId;
    private int mScreeningCategoryId;
    int mColor;
    private String mText;
    private boolean mDone = false;
    private int mPosition;
    private int mNumberOfChildren;

    public int getNumberOfChildren() {
        return mNumberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        mNumberOfChildren = numberOfChildren;
    }

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

    public int  getScreeningCategoryId(){
        return mScreeningCategoryId;
    }

    public int getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }

    public int getPosition() {
        return mPosition;
    }

    public QuestionCategory(int id, int screeningCategoryId, String text, int color, int position){
        mId = id;
        mText = text;
        mColor = color;
        mPosition = position;
        mScreeningCategoryId = screeningCategoryId;
    }
}
