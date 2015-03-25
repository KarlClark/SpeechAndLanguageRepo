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

import java.util.ArrayList;
import java.util.Locale;

public class SettingsActivity extends ActionBarActivity {

    private Spinner mSpnLanguage;
    private TextView mTvLanguage;
    private SharedPreferences mPrefs;
    private ArrayList<String> mLanguageChoices;
    private ArrayAdapter<String> mLanguageAdapter;
    private String[] mChoices;
    private int mCallCount = 0;
    private int mLanguage;
    private static final String TAG = "## My Info ##";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPrefs = getSharedPreferences(Utilities.PREFS_NAME, Activity.MODE_PRIVATE);
        mLanguage = mPrefs.getInt(Utilities.PREFS_LANGUAGE, Utilities.ENGLISH);

        mTvLanguage = (TextView)findViewById(R.id.tvLanguage);
        mSpnLanguage= (Spinner)findViewById(R.id.spnLanguage);
        mTvLanguage.setText(getResources().getString(R.string.language));
        mChoices = getResources().getStringArray(R.array.languages);
        mLanguageAdapter = new ArrayAdapter<String>(this,R.layout.custom_spinner, mChoices);
        mLanguageAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        mSpnLanguage.setAdapter(mLanguageAdapter);
        mSpnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG ,  "onItemSelected called call count= " + mCallCount + "  Position= " + position);
                Locale locale;
                if (mCallCount == 0) {
                    if (mLanguage == Utilities.SPANISH){
                        mSpnLanguage.setSelection(1);
                    }
                    mCallCount++;
                    return;
                }
                Log.i(TAG, "Didn't return");
                SharedPreferences.Editor prefsEditor = mPrefs.edit();

                if (position == 0) {
                    prefsEditor.putInt(Utilities.PREFS_LANGUAGE , Utilities.ENGLISH).commit();
                    Utilities.setLanguage(Utilities.ENGLISH);
                    Utilities.setLocale(getBaseContext(),"en");
                }else{
                    prefsEditor.putInt(Utilities.PREFS_LANGUAGE , Utilities.SPANISH).commit();
                    Utilities.setLanguage(Utilities.SPANISH);
                    Utilities.setLocale(getBaseContext(),"es");
                }

                String[] s = getResources().getStringArray(R.array.languages);
                mChoices[0] = s[0];
                mChoices[1] = s[1];
                mLanguageAdapter.notifyDataSetChanged();
                mTvLanguage.setText(getResources().getString(R.string.language));
                getSupportActionBar().setTitle(getResources().getString(R.string.action_settings));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
