package com.neuroleap.speachandlanguage.Utility;

import android.content.Context;
import android.content.res.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Karl on 3/6/2015.
 */
public class Utilities {

    public static final String PREFS_NAME = "pref_name";
    public static final String PREFS_APP_LANGUAGE = "prefs_app_language";
    public static final String PREFS_QUESTIONS_LANGUAGE = "prefs_app_language";
    public static final int ENGLISH = 0;
    public static final int SPANISH = 1;
    private static int mAppLanguage= ENGLISH;
    private static int mQuestionsLanguage = ENGLISH;
    private static SimpleDateFormat mDateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    public static final String PREFS_TEST_MODE = "prefs_test_mode";
    public static final int SCORING_BUTTONS_ONLY= 0;
    public static final int TEXT_INPUT_ONLY = 1;
    public static final int BOTH_SCORING_BUTTONS_AND_TEXT = 2;
    private static int mTestMode = BOTH_SCORING_BUTTONS_AND_TEXT;
    public static final int SCREENING_NOT_STARTED = 0;
    public static final int SCREENING_NOT_COMPLETE = 1;
    public static final int SCREENING_COMPLETED = 2;


    public static int getAppLanguage() {
        return mAppLanguage;
    }

    public static void setAppLanguage(int language) {
        mAppLanguage = language;
    }

    public static int getQuestionsLanguage() {
        return mQuestionsLanguage;
    }

    public static void setQuestionsLanguage(int questionsLanguage) {
        mQuestionsLanguage = questionsLanguage;
    }

    public static int getTestMode() {
        return mTestMode;
    }

    public static void setTestMode(int testMode) {
        mTestMode = testMode;
    }

    public static void setLocale(Context context, String languageCode){
        Locale locale;
        locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static String getDisplayDate(long longDate){
        Date date = new Date(longDate);
        return mDateFormatter.format(date);
    }

    public static long getLongDate(String displayDate){
        try {
            Date date = mDateFormatter.parse(displayDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
