package com.neuroleap.speachandlanguage.Models;

import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 3/20/2015.
 */
public class QuestionCategory {
    private int mId;
    int mColor = Utilities.GROUP_DEFAULT_COLOR;
    private String mText;
    private boolean mDone = false;
    private int mPosition;

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
        if (done){
            setColor(Utilities.GROUP_COMPLETED_COLOR);
        }
    }

    public int getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }

    private int getPosition() {
        return mPosition;
    }

    public QuestionCategory(int id, String text, int color, int position){
        mId = id;
        mText = text;
        mColor = color;
        mPosition = position;
    }
}
