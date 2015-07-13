package com.neuroleap.speachandlanguage.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.neuroleap.speachandlanguage.Data.ScreeningDbHelper;
import com.neuroleap.speachandlanguage.Fragments.ResultsDetailFragment;
import com.neuroleap.speachandlanguage.Fragments.ResultsSummaryFragment;
import com.neuroleap.speachandlanguage.Fragments.ScreeningOverviewFragment;
import com.neuroleap.speachandlanguage.Fragments.ShowScreeningsFragment;
import com.neuroleap.speachandlanguage.Fragments.SplashFragment_1;
import com.neuroleap.speachandlanguage.Fragments.SplashFragment_2;
import com.neuroleap.speachandlanguage.Fragments.StudentInfoFragment;
import com.neuroleap.speachandlanguage.Listeners.OnFragmentInteractionListener;
import com.neuroleap.speachandlanguage.Listeners.OnScreeningsListButtonsListener;
import com.neuroleap.speachandlanguage.Models.Screening;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.util.Locale;

public class StartUpActivity extends ActionBarActivity implements OnFragmentInteractionListener, OnScreeningsListButtonsListener{

    private int frContainerId;
    private SharedPreferences mPrefs;
    private FragmentManager mFragmentManager=getSupportFragmentManager();
    private SplashFragment_1 mSplashFragment_1;
    private SplashFragment_2 mSplashFragment_2;
    private ShowScreeningsFragment mShowScreeningsFragment;
    private StudentInfoFragment mStudentInfoFragment;
    private ScreeningOverviewFragment mScreeningOverviewFragment;
    private ResultsSummaryFragment mResultsSummaryFragment;
    private ResultsDetailFragment mResultsDetailFragment;
    private boolean mShowSettingOption = false;
    private boolean mShowNewOption = false;
    private boolean mReturningWithResult = false;
    private boolean mNeedScreeningsFragment= false;
    private Intent mResultIntent= null;
    private static final int SPLASH_FRAGMENT_1_ID = 1000;
    private static final int SHOW_SCREENINGS_FRAGMENT_ID = 1002;
    private static final int STUDENT_INFO_FRAGMENT_ID = 1003;
    private static final int SCREENING_OVERVIEW_FRAGMENT_ID = 1004;
    private static final int RESULTS_SUMMARY_FRAGMENT_ID = 1005;
    private static final int RESULTS_DETAIL_FRAGMENT_ID = 1006;
    private static final int FLOW_CONTROL_ACTIVITY_TAG = 0;
    private static final int SETTINGS_ACTIVITY_TAG = 1;
    private static final String TAG = "## My Info ##";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        frContainerId = R.id.frContainer;  //Used to hold fragments.
        ScreeningDbHelper dbHelper = new ScreeningDbHelper(this);
        DbCRUD.setDatabase(dbHelper.getWritableDatabase());  //Pass the database to the DbCRUD class.
        mPrefs = getSharedPreferences(Utilities.PREFS_NAME, Activity.MODE_PRIVATE);
        checkLanguagePreference();
        checkTestModePreference();
        checkAudioRecordPreference();
        Utilities.setPackageName(getPackageName());
        if(savedInstanceState == null) {
            displayFirstSplashScreen();
        }
    }

    @Override
    protected void onPostResume(){
        super.onPostResume();
        Log.i(TAG, "Startup activity onPostResume called");
        if (mReturningWithResult) {
            //Can't start a fragment from onActivityResult so we saved necessary
            //data to start next fragment here.
            showRequestedFragment(mResultIntent);
            mReturningWithResult = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Show Settings and NEW on action bar depending on values
        //of mShowSettingsOption and mShowNewOption which are
        //changed through out the activity depending on what
        //fragment is being displayed.
        getMenuInflater().inflate(R.menu.menu_start_up, menu);
        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        MenuItem newItem = menu.findItem(R.id.new_screening);
        settingsItem.setVisible(mShowSettingOption);
        newItem.setVisible(mShowNewOption);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i =new Intent(this,SettingsActivity.class);
            startActivityForResult(i,SETTINGS_ACTIVITY_TAG);
            return true;
        }

        if (id == R.id.new_screening){
            displayStudentInfoFragment(-1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //If we are on the screenings fragment, end the program. If we are on
        //any other fragment then display the screenings fragment
        Fragment fragment = mFragmentManager.findFragmentById(R.id.frContainer);
        if (fragment instanceof ShowScreeningsFragment  || fragment instanceof ResultsDetailFragment) {
            super.onBackPressed();
        }else{
            displayShowScreeningsFragment();
        }
    }

    public void onFragmentInteraction(int id, Object ... args){
        //Fragment use this method to call back to this activity.
        switch (id){
            case SPLASH_FRAGMENT_1_ID:  //Splash screen is done. Show Screenings fragment
                displayShowScreeningsFragment();
                break;
            case STUDENT_INFO_FRAGMENT_ID:  //User finished student input. Show Screenings fragment.
                displayShowScreeningsFragment();
                break;
            case SCREENING_OVERVIEW_FRAGMENT_ID:
                if(args[1] == Utilities.SCREENINGS){ //User hit Screenings button on Overview screen.
                    displayShowScreeningsFragment();
                }else {
                    if ( args[1] == Utilities.SUMMARY_RESULTS) { //User hit Results button on Overview screen.
                        displayResultsSummaryFragment((int)args[0], (String)args[2]);
                    } else {
                        Log.i(TAG, "screening id = " + (int) args[0] + "  category= " + (long) args[1]);
                        //User selected a category from the overview screen. args[0] = screening id.
                        //args[1] = requested category id.
                        startFlowControlActivity((int) args[0], (long) args[1]);
                    }
                }
                break;
            case RESULTS_SUMMARY_FRAGMENT_ID:
            case RESULTS_DETAIL_FRAGMENT_ID:
                switch ((int)args[0]){
                    case Utilities.SCREENINGS:
                        displayShowScreeningsFragment();
                        break;
                    case Utilities.PROFILE:
                        displayStudentInfoFragment((int)args[1]); //args[1] = student id
                        break;
                    case Utilities.QUESTIONS:
                        startFlowControlActivity((int)args[1], -1); //args[1] = screening id
                        break;
                    case Utilities.OVERVIEW:
                        //args[1] = screening id, args[2] = student name.
                        displayScreeningOverviewFragment((int)args[1], (String)args[2]);
                        break;
                    case Utilities.DETAIL_RESULTS:
                        //args[1] = seceening category id, args[2] = screening id, args[3] = student name;
                        displayResultsDetailFragment((int)args[1], (int)args[2], (String)args[3]);
                }
        }
    }

    private void displayFirstSplashScreen(){
        mSplashFragment_1= new SplashFragment_1();
        mSplashFragment_1.setId(SPLASH_FRAGMENT_1_ID);
        mFragmentManager.beginTransaction().add(frContainerId, mSplashFragment_1, "TAG").commit();
    }



    private void displayShowScreeningsFragment(){

        //setup action bar
        mShowNewOption = true;
        mShowSettingOption = true;
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getString(R.string.screenings_title));
        invalidateOptionsMenu();

        mShowScreeningsFragment = new ShowScreeningsFragment();
        mShowScreeningsFragment.setId(SHOW_SCREENINGS_FRAGMENT_ID);
        try {
            mFragmentManager.beginTransaction().replace(frContainerId, mShowScreeningsFragment, "TAG").commit();
        }catch(IllegalStateException e){
            e.printStackTrace();
        }
        mSplashFragment_1 = null;
        mStudentInfoFragment = null;
        mScreeningOverviewFragment = null;
        mResultsSummaryFragment = null;
        mResultsDetailFragment = null;
    }

    private void displayStudentInfoFragment(long studentId) {
        mShowNewOption = false;
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getString(R.string.student_info_title));
        invalidateOptionsMenu();
        mStudentInfoFragment = StudentInfoFragment.newInstance(studentId);
        mStudentInfoFragment.setId(STUDENT_INFO_FRAGMENT_ID);
        mFragmentManager.beginTransaction().replace(frContainerId,mStudentInfoFragment, "TAG").commit();
        mShowScreeningsFragment = null;
        mResultsSummaryFragment = null;
        mResultsDetailFragment = null;
    }

    private void displayScreeningOverviewFragment(int screeningId, String studentName) {
        mShowNewOption = false;
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getString(R.string.main_menu));
        invalidateOptionsMenu();
        mScreeningOverviewFragment = ScreeningOverviewFragment.newInstance(SCREENING_OVERVIEW_FRAGMENT_ID, screeningId, studentName);
        //mScreeningOverviewFragment.setId(SCREENING_OVERVIEW_FRAGMENT_ID);
        mFragmentManager.beginTransaction().replace(frContainerId, mScreeningOverviewFragment, "TAG").commit();
        mShowScreeningsFragment = null;
        mResultsSummaryFragment = null;
        mResultsDetailFragment = null;
    }

    private void displayResultsSummaryFragment(int screeningId, String studentName){
        mShowNewOption = false;
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getString(R.string.results_summary));
        invalidateOptionsMenu();
        mResultsSummaryFragment = ResultsSummaryFragment.newInstance(RESULTS_SUMMARY_FRAGMENT_ID, screeningId, studentName);
        mFragmentManager.beginTransaction().replace(frContainerId, mResultsSummaryFragment, "TAG").commit();
        mShowScreeningsFragment = null;
        mScreeningOverviewFragment = null;
        mResultsDetailFragment = null;
    }

    private void displayResultsDetailFragment(int screeningCategoryId, int screeningId, String studentName){
        mShowNewOption = false;
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getString(R.string.results_detail));
        invalidateOptionsMenu();
        mResultsDetailFragment = ResultsDetailFragment.newInstance(RESULTS_DETAIL_FRAGMENT_ID, screeningCategoryId, screeningId, studentName);
        mFragmentManager.beginTransaction().replace(frContainerId, mResultsDetailFragment, "TAG").addToBackStack("").commit();
    }



    private void startFlowControlActivity(int screeningId, long screeningCategoryRequest){
        Intent i = new Intent(this, FlowControlActivity.class);
        i.putExtra(FlowControlActivity.SCREENING_ID_KEY, screeningId);
        i.putExtra(FlowControlActivity.SCREENING_CATEGORY_REQUEST_KEY, screeningCategoryRequest);
        startActivityForResult(i, FLOW_CONTROL_ACTIVITY_TAG);
    }

    private void checkLanguagePreference() {

        //Get app language from shared preferences.
        int appLanguage = mPrefs.getInt(Utilities.PREFS_APP_LANGUAGE, -1);
        if (appLanguage == -1) {
            //no language preference, so set it to English unless the Locale language is actually Spanish.
            Log.i (TAG, "locale language= " + Locale.getDefault().getLanguage());
            if(Locale.getDefault().getLanguage().equals("es")){
                appLanguage = Utilities.SPANISH;
            }else{
                appLanguage = Utilities.ENGLISH;
            }
        }

        //set the Locale based on app language.
        Utilities.setAppLanguage(appLanguage);
        if (appLanguage == Utilities.ENGLISH){
            Utilities.setLocale(getBaseContext(), "en");
        }else{
            Utilities.setLocale(getBaseContext(), "es");
        }
    }

    private void checkTestModePreference(){
        int testMode = mPrefs.getInt(Utilities.PREFS_TEST_MODE , -1);
        if (testMode != -1){
            Utilities.setTestMode(testMode);
        }
    }

    private void checkAudioRecordPreference() {
        int audioRecordMode = mPrefs.getInt(Utilities.PREFS_RECORD_AUDIO_MODE , -1);
        if (audioRecordMode != -1) {
            Utilities.setAudioRecordMode(audioRecordMode);
        }
    }


    @Override
    public void onScreeningResultsButtonClicked(Screening screening) {
        Log.i(TAG, "onScreeningResultsButtonClicked called. Screening id = " + screening.getId());
        displayResultsSummaryFragment(screening.getId(), screening.getFirstName() + " " + screening.getLastName());
    }

    @Override
    public void onScreeningOverviewButtonClicked(Screening screening) {
        Log.i(TAG, "onScreeningOverviewsButtonClicked called. Screening id = " + screening.getId());
        displayScreeningOverviewFragment(screening.getId(), screening.getFirstName() + " " + screening.getLastName());
    }

    @Override
    public void onScreeningQuestionsButtonClicked(Screening screening) {
        Log.i(TAG, "onScreeningQuestionsButtonClicked called. Screening id = " + screening.getId());
        startFlowControlActivity(screening.getId(), -1);
    }

    @Override
    public void onScreeningProfileButtonClicked(Screening screening){
        Log.i(TAG, "onScreeningProfileButtonClicked called. Screening id = " + screening.getId());
        displayStudentInfoFragment(screening.getStudentId());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SETTINGS_ACTIVITY_TAG:
                break;
            case FLOW_CONTROL_ACTIVITY_TAG:
                //Can't start a fragment from here because of state loss. So
                //save data and start fragment in onPostResume.
                mReturningWithResult = true;
                mResultIntent = data;
        }
    }

    private void showRequestedFragment(Intent data){
        if (data == null){
            displayShowScreeningsFragment();
        }else {
            switch (data.getIntExtra(FlowControlActivity.REQUESTED_ACTION_KEY, 0)) {
                case FlowControlActivity.SHOW_OVERVIEW:
                    int screeningId = data.getIntExtra(FlowControlActivity.SCREENING_ID_TAG, -1);
                    String studentName = data.getStringExtra(FlowControlActivity.SCREENING_STUDENT_NAME_TAG);
                    Log.i(TAG, "screeningId = " + screeningId + "  student name= " + studentName);
                    displayScreeningOverviewFragment(screeningId, studentName);
                    break;
                case FlowControlActivity.SHOW_RESULTS:
                    Log.i(TAG, "Show Results");
                    screeningId = data.getIntExtra(FlowControlActivity.SCREENING_ID_TAG, -1);
                    studentName = data.getStringExtra(FlowControlActivity.SCREENING_STUDENT_NAME_TAG);
                    displayResultsSummaryFragment(screeningId, studentName);
                    break;
                case FlowControlActivity.SHOW_SCREENINGS:
                    displayShowScreeningsFragment();
            }
        }
    }
}
