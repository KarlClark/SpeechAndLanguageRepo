package com.neuroleap.speachandlanguage.Utility;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Karl on 3/6/2015.
 */
public class Utilities {

    public static final String PREFS_NAME = "pref_name";
    public static final String PREFS_LANGUAGE = "prefs_language";
    public static final int ENGLISH = 0;
    public static final int SPANISH = 1;
    private static int mLanguage= ENGLISH;
    private static SQLiteDatabase mDB;

    public static SQLiteDatabase getDatabase() {
        return mDB;
    }

    public static void setDatabase(SQLiteDatabase db) {
        mDB = db;
    }

    public static int getLanguage() {
        return mLanguage;

    }

    public static void setLanguage(int language) {
        mLanguage = language;
    }
}
