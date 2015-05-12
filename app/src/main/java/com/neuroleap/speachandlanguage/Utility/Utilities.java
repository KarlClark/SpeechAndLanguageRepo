package com.neuroleap.speachandlanguage.Utility;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.neuroleap.speachandlanguage.R;

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
    private static  int mAppLanguage= ENGLISH;
    private static  int mQuestionsLanguage = ENGLISH;
    private static  SimpleDateFormat mDateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    private static String mPackageName;
    //private static int mTotalQuestions;
    public static final String PREFS_TEST_MODE = "prefs_test_mode";
    //public static final int SCORING_BUTTONS_ONLY= 0;
    public static final int TEXT_INPUT_ONLY = 1;
    public static final int BOTH_SCORING_BUTTONS_AND_TEXT = 2;
    private static int mTestMode = BOTH_SCORING_BUTTONS_AND_TEXT;
    public static final int SCREENING_NOT_STARTED = 0;
    public static final int SCREENING_NOT_COMPLETE = 1;
    public static final int SCREENING_COMPLETED = 2;
    public static final int GROUP_UNTOUCHED_COLOR = R.color.white;
    public static final int GROUP_PASSING_COLOR = R.color.light_green;
    public static final int GROUP_FAILING_COLOR = R.color.light_red;
    public static final int CHILD_DEFAULT_COLOR = R.color.lighter_blue;
    public static final int CHILD_COMPLETED_COLOR = R.color.light_blue;
    public static final int CHILD_HIGHLIGHT_COLOR = R.color.yellow;
    public static final int SCREENINGS = 20;
    public static final int RESULTS = 21;


   /* public static int getTotalQuestions() {
        return mTotalQuestions;
    }

    public static void setTotalQuestions(int totalQuestions) {
        mTotalQuestions = totalQuestions;
    }*/

    public static String getPackageName() {
        return mPackageName;
    }

    public static void setPackageName(String packageName) {
        mPackageName = packageName;
    }

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

    public static String toLowerCaseAndTrim(String s) {
        String newS = s.trim();
        return newS.toLowerCase();
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
