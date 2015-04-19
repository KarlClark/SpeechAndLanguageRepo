package com.neuroleap.speachandlanguage.Models;

import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 3/20/2015.
 */
public class Question {
    private int mId;
    private int mCategoryId;
    private int mCategoryType;
    private int mColor=Utilities.CHILD_DEFAULT_COLOR;
    private String mText;
    private boolean mDone = false;
    private String mFragmentName;
    private int mViewPagerPosition;
    private int mChildPosition;
    private int mGroupPosition;

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

    public int getCategoryType() {
        return mCategoryType;
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

    public Question(int id, int categoryId, int categoryType,  String text, int color, String fragmentName,
                    int viewPagerPosition, int childPosition, int groupPosition, boolean done){
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
        mCategoryType = categoryType;
    }
}
