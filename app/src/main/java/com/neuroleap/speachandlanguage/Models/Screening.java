package com.neuroleap.speachandlanguage.Models;

import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 4/2/2015.
 */
public class Screening {
    private int mId;
    private long mLongDate;
    private String mDisplayDate;
    private String mFirstName;
    private String mLastName;
    private String mTeacher;
    private int mCompletionState;
    private int mAge;

    public Screening(int id, String firstName, String lastName, int age, String teacher, long longDate, int completionState){
        mId = id;
        mFirstName = firstName;
        mLastName = lastName;
        mTeacher = teacher;
        mLongDate = longDate;
        mCompletionState = completionState;
        mAge = age;
        mDisplayDate = Utilities.getDisplayDate(longDate);
    }

    public String getDisplayDate() {
        return mDisplayDate;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getTeacher() {
        return mTeacher;
    }

    public int getCompletionState() {
        return mCompletionState;
    }

    public long getLongDate() {
        return mLongDate;
    }

    public int getId() { return mId; }

    public int getAge() { return mAge;}
}


