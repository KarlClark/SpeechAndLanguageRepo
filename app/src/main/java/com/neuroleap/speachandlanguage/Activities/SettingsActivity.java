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
        if (mLanguage == Utilities.ENGLISH){
            mTvLanguage.setText(getResources().getString(R.string.language_eg));
            mChoices = getResources().getStringArray(R.array.language_eg);
        }else{
            mTvLanguage.setText(getResources().getString(R.string.language_sp));
            mChoices = getResources().getStringArray(R.array.language_sp);
        }

        mSpnLanguage= (Spinner)findViewById(R.id.spnLanguage);
        mLanguageAdapter = new ArrayAdapter<String>(this,R.layout.custom_spinner, mChoices);
        mLanguageAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        mSpnLanguage.setAdapter(mLanguageAdapter);
        mSpnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG ,  "onItemSelected called call count= " + mCallCount + "  Position= " + position);
                if (mCallCount == 0) {
                    if (mLanguage == Utilities.SPANISH){
                        mSpnLanguage.setSelection(1);
                    }
                    mCallCount++;
                    return;
                }
                Log.i(TAG, "Didn't return");
                String[] s;
                SharedPreferences.Editor prefsEditor = mPrefs.edit();

                if (position == 0) {
                    s = getResources().getStringArray(R.array.language_eg);
                    mTvLanguage.setText(getResources().getString(R.string.language_eg));
                    prefsEditor.putInt(Utilities.PREFS_LANGUAGE , Utilities.ENGLISH).commit();
                    Utilities.setLanguage(Utilities.ENGLISH);
                }else{
                    s = getResources().getStringArray(R.array.language_sp);
                    mTvLanguage.setText(getResources().getString(R.string.language_sp));
                    prefsEditor.putInt(Utilities.PREFS_LANGUAGE , Utilities.SPANISH).commit();
                    Utilities.setLanguage(Utilities.SPANISH);
                }
                mChoices[0] = s[0];
                mChoices[1] = s[1];
                mLanguageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
