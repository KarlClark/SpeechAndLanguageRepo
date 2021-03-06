package com.neuroleap.speachandlanguage.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.util.Locale;

public class SettingsActivity extends ActionBarActivity {

    private Spinner mSpnAppLanguage, mSpnQuestionsLanguage, mSpnTestMode, mSpnAudioRecord;
    private TextView mTvAppLanguage, mTvQuestionsLanguage, mTvTestMode, mTvAudioRecord;
    private SharedPreferences mPrefs;
    private ArrayAdapter<String> mAppLanguageAdapter, mQuestionsLanguageAdapter, mTestModeAdapter, mAudioRecordAdapter;
    private String[] mAppLanguageChoices, mQuestionsLanguageChoices, mTestModeChoices, mAudioRecordChoices;
    private int mCallCount = 0;
    private int mAppLanguage, mQuestionsLanguage, mTestMode, mAudioRecordMode;
    private static final String TAG = "## My Info ##";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"Settings Activity onCreate called");
        setContentView(R.layout.activity_settings_2);

        //Get current settings from shared preferences so we can display them.
        mPrefs = getSharedPreferences(Utilities.PREFS_NAME, Activity.MODE_PRIVATE);
        mAppLanguage = mPrefs.getInt(Utilities.PREFS_APP_LANGUAGE, Utilities.ENGLISH);
        mQuestionsLanguage = mPrefs.getInt(Utilities.PREFS_QUESTIONS_LANGUAGE, Utilities.ENGLISH);
        mTestMode = mPrefs.getInt(Utilities.PREFS_TEST_MODE, Utilities.BOTH_SCORING_BUTTONS_AND_TEXT);
        mAudioRecordMode = mPrefs.getInt(Utilities.PREFS_RECORD_AUDIO_MODE, Utilities.ON);

        getViews();
        setupAppLanguageSpinner();
        setupQuestionsLanguageSpinner();
        setupTestModeSpinner();
        setupAudioRecordSpinner();
    }

    private void getViews(){
        mTvAppLanguage = (TextView)findViewById(R.id.tvAppLanguage);
        mTvQuestionsLanguage = (TextView)findViewById(R.id.tvQuestionsLanguage);
        mTvTestMode = (TextView)findViewById(R.id.tvTestMode);
        mTvAudioRecord = (TextView)findViewById(R.id.tvAudioRecord);
        mSpnAppLanguage= (Spinner)findViewById(R.id.spnAppLanguage);
        mSpnQuestionsLanguage = (Spinner)findViewById(R.id.spnQuestionsLanguage);
        mSpnTestMode = (Spinner)findViewById(R.id.spnTestMode);
        mSpnAudioRecord = (Spinner)findViewById(R.id.spnAudioRecord);

    }

    private void setupTestModeSpinner(){
        //Spinner for choosing test mode
        mTvTestMode.setText(getResources().getString(R.string.test_mode_spn));
        mTestModeChoices = getResources().getStringArray(R.array.test_mode);
        mTestModeAdapter = new ArrayAdapter<String>(this,R.layout.custom_spinner, mTestModeChoices);
        mTestModeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        mSpnTestMode.setAdapter(mTestModeAdapter);
        if (mTestMode == Utilities.TEXT_INPUT_ONLY){
            mSpnTestMode.setSelection(0);
        }else{
            if (mTestMode == Utilities.BOTH_SCORING_BUTTONS_AND_TEXT){
                mSpnTestMode.setSelection(1);
            }
        }
        mSpnTestMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                if (position == 0) {
                    prefsEditor.putInt(Utilities.PREFS_TEST_MODE, Utilities.TEXT_INPUT_ONLY).commit();
                    Utilities.setTestMode(Utilities.TEXT_INPUT_ONLY);
                }else{
                    prefsEditor.putInt(Utilities.PREFS_TEST_MODE, Utilities.BOTH_SCORING_BUTTONS_AND_TEXT).commit();
                    Utilities.setTestMode(Utilities.BOTH_SCORING_BUTTONS_AND_TEXT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupAudioRecordSpinner(){
        mTvAudioRecord.setText(getResources().getString(R.string.audio_record));
        mAudioRecordChoices = getResources().getStringArray(R.array.audio_record);
        mAudioRecordAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, mAudioRecordChoices);
        mAudioRecordAdapter.setDropDownViewResource((R.layout.custom_spinner_dropdown));
        mSpnAudioRecord.setAdapter(mAudioRecordAdapter);
        if (mAudioRecordMode == Utilities.ON){
            mSpnAudioRecord.setSelection(0);
        }else{
            mSpnAudioRecord.setSelection(1);
        }
        mSpnAudioRecord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                if (position == 0){
                    prefsEditor.putInt(Utilities.PREFS_RECORD_AUDIO_MODE, Utilities.ON).commit();
                    Utilities.setAudioRecordMode(Utilities.ON);
                }else{
                    prefsEditor.putInt(Utilities.PREFS_RECORD_AUDIO_MODE, Utilities.OFF).commit();
                    Utilities.setAudioRecordMode(Utilities.OFF);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupQuestionsLanguageSpinner(){
        //Spinner for choosing which language to use for the questions.
        mTvQuestionsLanguage.setText(getResources().getString(R.string.questions_language));
        mQuestionsLanguageChoices = getResources().getStringArray(R.array.languages);
        mQuestionsLanguageAdapter = new ArrayAdapter<String>(this,R.layout.custom_spinner, mQuestionsLanguageChoices);
        mQuestionsLanguageAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        mSpnQuestionsLanguage.setAdapter(mQuestionsLanguageAdapter);
        if (mQuestionsLanguage == Utilities.SPANISH){
            mSpnQuestionsLanguage.setSelection(1);
        }
        mSpnQuestionsLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                if (position == 0) {
                    prefsEditor.putInt(Utilities.PREFS_QUESTIONS_LANGUAGE , Utilities.ENGLISH).commit();
                    Utilities.setQuestionsLanguage(Utilities.ENGLISH);
                }else{
                    prefsEditor.putInt(Utilities.PREFS_QUESTIONS_LANGUAGE , Utilities.SPANISH).commit();
                    Utilities.setQuestionsLanguage(Utilities.SPANISH);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupAppLanguageSpinner(){
        //Spinner to choose the language to use for the app.  Also redo this screen
        //so that all text matches the language chosen here.
        mTvAppLanguage.setText(getResources().getString(R.string.app_language));
        mAppLanguageChoices = getResources().getStringArray(R.array.languages);
        mAppLanguageAdapter = new ArrayAdapter<String>(this,R.layout.custom_spinner, mAppLanguageChoices);
        mAppLanguageAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        mSpnAppLanguage.setAdapter(mAppLanguageAdapter);
        mSpnAppLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG ,  "onItemSelected called call count= " + mCallCount + "  Position= " + position);
                Locale locale;
                if (mCallCount == 0) {
                    if (mAppLanguage == Utilities.SPANISH){
                        mSpnAppLanguage.setSelection(1);
                    }
                    mCallCount++;
                    return;
                }
                Log.i(TAG, "Didn't return");
                SharedPreferences.Editor prefsEditor = mPrefs.edit();

                if (position == 0) {
                    prefsEditor.putInt(Utilities.PREFS_APP_LANGUAGE , Utilities.ENGLISH).commit();
                    Utilities.setAppLanguage(Utilities.ENGLISH);
                    Utilities.setLocale(getBaseContext(),"en");
                }else{
                    prefsEditor.putInt(Utilities.PREFS_APP_LANGUAGE , Utilities.SPANISH).commit();
                    Utilities.setAppLanguage(Utilities.SPANISH);
                    Utilities.setLocale(getBaseContext(),"es");
                }

                String[] s = getResources().getStringArray(R.array.languages);
                mAppLanguageChoices[0] = s[0];
                mAppLanguageChoices[1] = s[1];
                mAppLanguageAdapter.notifyDataSetChanged();
                mTvAppLanguage.setText(getResources().getString(R.string.app_language));

                mQuestionsLanguageChoices[0] = s[0];
                mQuestionsLanguageChoices[1] = s[1];
                mQuestionsLanguageAdapter.notifyDataSetChanged();
                mTvQuestionsLanguage.setText(getResources().getString(R.string.questions_language));

                s = getResources().getStringArray(R.array.test_mode);
                mTestModeChoices[0] = s[0];
                mTestModeChoices[1] = s[1];
                mTestModeAdapter.notifyDataSetChanged();
                mTvTestMode.setText(getResources().getString(R.string.test_mode_spn));

                getSupportActionBar().setTitle(getResources().getString(R.string.action_settings));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
