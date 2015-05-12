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
    private boolean mShowSettingOption = false;
    private boolean mShowNewOption = false;
    private boolean mReturningWithResult = false;
    private Intent mResultIntent= null;
    private static final int SPLASH_FRAGMENT_1_ID = 1000;
    private static final int SHOW_SCREENINGS_FRAGMENT_ID = 1002;
    private static final int STUDENT_INFO_FRAGMENT_ID = 1003;
    private static final int SCREENING_OVERVIEW_FRAGMENT_ID = 1004;
    private static final int FLOW_CONTROL_ACTIVITY_TAG = 0;
    private static final int SETTINGS_ACTIVITY_TAG = 1;
    private static final String TAG = "## My Info ##";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        frContainerId = R.id.frContainer;
        ScreeningDbHelper dbHelper = new ScreeningDbHelper(this);
        DbCRUD.setDatabase(dbHelper.getWritableDatabase());
        mPrefs = getSharedPreferences(Utilities.PREFS_NAME, Activity.MODE_PRIVATE);
        checkLanguagePreference();
        checkTestModePreference();
        Utilities.setPackageName(getPackageName());
        if(savedInstanceState == null) {
            displayFirstSplashScreen();
        }
    }

    @Override
    protected void onPostResume(){
        super.onPostResume();
        if (mReturningWithResult) {
            showRequestedFragment(mResultIntent);
            mReturningWithResult = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_up, menu);
        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        MenuItem newItem = menu.findItem(R.id.new_screening);
        settingsItem.setVisible(mShowSettingOption);
        newItem.setVisible(mShowNewOption);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        Fragment fragment = mFragmentManager.findFragmentById(R.id.frContainer);
        if (fragment instanceof ShowScreeningsFragment) {
            super.onBackPressed();
        }else{
            displayShowScreeningsFragment();
        }
    }

    public void onFragmentInteraction(int id, Object ... args){
        switch (id){
            case SPLASH_FRAGMENT_1_ID:
                displayShowScreeningsFragment();
                break;
            case STUDENT_INFO_FRAGMENT_ID:
                displayShowScreeningsFragment();
                break;
            /*case SHOW_SCREENINGS_FRAGMENT_ID:
                displayScreeningMainMenuFragment((int)args[0], (String)args[1] + " " +  (String)args[2]);
                break;*/
            case SCREENING_OVERVIEW_FRAGMENT_ID:
                if((long)args[1] == Utilities.SCREENINGS){
                    displayShowScreeningsFragment();
                }else {
                    Log.i(TAG, "screening id = " + (int) args[0] + "  category= " + (long) args[1]);
                    startFlowControlActivity((int)args[0], (long)args[1]);
                }
        }
    }

    private void displayFirstSplashScreen(){
        mSplashFragment_1= new SplashFragment_1();
        mSplashFragment_1.setId(SPLASH_FRAGMENT_1_ID);
        mFragmentManager.beginTransaction().add(frContainerId, mSplashFragment_1, "TAG").commit();
    }



    private void displayShowScreeningsFragment(){
        mShowNewOption = true;
        mShowSettingOption = true;
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getString(R.string.screenings_title));
        invalidateOptionsMenu();
        mShowScreeningsFragment = new ShowScreeningsFragment();
        mShowScreeningsFragment.setId(SHOW_SCREENINGS_FRAGMENT_ID);
        mFragmentManager.beginTransaction().replace(frContainerId,mShowScreeningsFragment, "TAG").commit();
        mSplashFragment_1 = null;
        mStudentInfoFragment = null;
        mScreeningOverviewFragment = null;
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
    }

    private void startFlowControlActivity(int screeningId, long screeningCategoryRequest){
        Intent i = new Intent(this, FlowControlActivity.class);
        i.putExtra(FlowControlActivity.SCREENING_ID_KEY, screeningId);
        i.putExtra(FlowControlActivity.SCREENING_CATEGORY_REQUEST_KEY, screeningCategoryRequest);
        startActivityForResult(i, FLOW_CONTROL_ACTIVITY_TAG);
    }

    private void checkLanguagePreference() {

        int appLanguage = mPrefs.getInt(Utilities.PREFS_APP_LANGUAGE, -1);
        if (appLanguage == -1) {
            Log.i (TAG, "locale language= " + Locale.getDefault().getLanguage());
            if(Locale.getDefault().getLanguage().equals("es")){
                appLanguage = Utilities.SPANISH;
            }else{
                appLanguage = Utilities.ENGLISH;
            }
        }
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


    @Override
    public void onScreeningResultsButtonClicked(Screening screening) {
        Log.i(TAG, "onScreeningResultsButtonClicked called. Screening id = " + screening.getId());
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
                    break;
                case FlowControlActivity.SHOW_SCREENINGS:
                    displayShowScreeningsFragment();
            }
        }
    }
}
