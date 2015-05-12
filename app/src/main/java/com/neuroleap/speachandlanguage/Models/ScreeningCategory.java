package com.neuroleap.speachandlanguage.Models;

/**
 * Created by Karl on 5/9/2015.
 */
public class ScreeningCategory {
    private long mScreeningCategoryId;
    private String mName_Eg;
    private String mName_Sp;

    public ScreeningCategory(long screeningCategoryId, String name_Eg, String name_Sp){
        mScreeningCategoryId = screeningCategoryId;
        mName_Eg = name_Eg;
        mName_Sp = name_Sp;
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
}
