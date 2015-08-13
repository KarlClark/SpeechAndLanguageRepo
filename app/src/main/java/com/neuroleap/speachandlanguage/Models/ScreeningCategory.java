package com.neuroleap.speachandlanguage.Models;

/**
 * Created by Karl on 5/9/2015.
 */
public class ScreeningCategory {
    private long mScreeningCategoryId;
    private String mName_Eg;
    private String mName_Sp;
    private int mLowCutoffAge, mHighCutoffAge;

    public ScreeningCategory(long screeningCategoryId, String name_Eg, String name_Sp, int lowCutoffAge, int highCutoffAge){
        mScreeningCategoryId = screeningCategoryId;
        mName_Eg = name_Eg;
        mName_Sp = name_Sp;
        mLowCutoffAge = lowCutoffAge;
        mHighCutoffAge = highCutoffAge;
    }

    public long getScreeningCategoryId() {
        return mScreeningCategoryId;
    }

    public String getName_Eg() {
        return mName_Eg;
    }

    public String getName_Sp() {
        return mName_Sp;
    }

    public int getLowCutoffAge() {
        return mLowCutoffAge;
    }

    public int getHighCutoffAge() {
        return mHighCutoffAge;
    }
}
