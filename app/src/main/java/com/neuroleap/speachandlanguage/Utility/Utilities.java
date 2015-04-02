package com.neuroleap.speachandlanguage.Utility;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by Karl on 3/6/2015.
 */
public class Utilities {

    public static final String PREFS_NAME = "pref_name";
    public static final String PREFS_LANGUAGE = "prefs_language";
    public static final int ENGLISH = 0;
    public static final int SPANISH = 1;
    private static int mLanguage= ENGLISH;
    public static final int SCORING_BUTTONS_ONLY= 0;
    public static final int TEXT_INPUT_ONLY = 1;
    public static final int BOTH_SCORING_BUTTONS_AND_TEXT = 2;


    public static int getLanguage() {
        return mLanguage;

    }

    public static void setLanguage(int language) {
        mLanguage = language;
    }

    public static void setLocale(Context context, String languageCode){
        Locale locale;
        locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
