package com.neuroleap.speachandlanguage.Models;

import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 3/20/2015.
 */
public class Question {
    private int mId;
    private int mCategoryId;
    private long mScreeningCategoryId;
    private int mColor=Utilities.CHILD_DEFAULT_COLOR;
    private String mText;
    private boolean mDone = false;
    private String mFragmentName;
    private int mViewPagerPosition;
    private int mChildPosition;
    private int mGroupPosition;
    private boolean mCorrect;

    public boolean isCorrect() {
        return mCorrect;
    }

    public void setCorrect(boolean correct) {
        mCorrect = correct;
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
        if (done) {
            setColor(Utilities.CHILD_COMPLETED_COLOR);
        }
    }

    public long getScreeningCategoryId() {
        return mScreeningCategoryId;
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

    public String getFragmentName(){
        return mFragmentName;
    }

    public int getViewPagerPosition () {
        return mViewPagerPosition;
    }

    public int getChildPosition() {
        return mChildPosition;
    }

    public int getGroupPosition() {
        return mGroupPosition;
    }

    public Question(int id, int categoryId, long screeningCategoryId,  String text, int color, String fragmentName,
                    int viewPagerPosition, int childPosition, int groupPosition, boolean done, boolean correct){
        mId = id;
        mCategoryId = categoryId;
        mText = text;
        mColor = color;
        mFragmentName = fragmentName;
        mViewPagerPosition = viewPagerPosition;
        mChildPosition = childPosition;
        mGroupPosition = groupPosition;
        mDone = done;
        if (done) {
            setColor(Utilities.CHILD_COMPLETED_COLOR);
        }
        mCorrect = correct;
        mScreeningCategoryId = screeningCategoryId;

    }
}
