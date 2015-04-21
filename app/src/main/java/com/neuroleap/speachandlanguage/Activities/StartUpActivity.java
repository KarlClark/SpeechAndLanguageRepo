package com.neuroleap.speachandlanguage.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.neuroleap.speachandlanguage.Data.ScreeningDbHelper;
import com.neuroleap.speachandlanguage.Fragments.ScreeningMainMenuFragment;
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
    private ScreeningMainMenuFragment mScreeningMainMenuFragment;
    private boolean mShowSettingOption = false;
    private boolean mShowNewOption = false;
    private static final int SPLASH_FRAGMENT_1_ID = 1000;
    private static final int SHOW_SCREENINGS_FRAGMENT_ID = 1002;
    private static final int STUDENT_INFO_FRAGMENT_ID = 1003;
    private static final int SCREENING_MAIN_MENU_FRAGMENT_ID = 1004;
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
            startActivityForResult(i,0);
            return true;
        }

        if (id == R.id.new_screening){
            displayStudentInfoFragment();
        }

        return super.onOptionsItemSelected(item);
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
            case SCREENING_MAIN_MENU_FRAGMENT_ID:
                if((int)args[1] == Utilities.SCREENINGS){
                    displayShowScreeningsFragment();
                }else {
                    Log.i(TAG, "screening id = " + (int) args[0] + "  category= " + (int) args[1]);
                    startFlowControlActivity((int)args[0], (int)args[1]);
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
        mScreeningMainMenuFragment = null;
    }

    private void displayStudentInfoFragment() {
        mShowNewOption = false;
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getString(R.string.student_info_title));
        invalidateOptionsMenu();
        mStudentInfoFragment = new StudentInfoFragment();
        mStudentInfoFragment.setId(STUDENT_INFO_FRAGMENT_ID);
        mFragmentManager.beginTransaction().replace(frContainerId,mStudentInfoFragment, "TAG").commit();
        mShowScreeningsFragment = null;
    }

    private void displayScreeningMainMenuFragment(int screeningId, String studentName) {
        mShowNewOption = false;
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getString(R.string.main_menu));
        invalidateOptionsMenu();
        mScreeningMainMenuFragment = ScreeningMainMenuFragment.newInstance(SCREENING_MAIN_MENU_FRAGMENT_ID, screeningId, studentName);
        mScreeningMainMenuFragment.setId(SCREENING_MAIN_MENU_FRAGMENT_ID);
        mFragmentManager.beginTransaction().replace(frContainerId,mScreeningMainMenuFragment, "TAG").commit();
        mShowScreeningsFragment = null;
    }

    private void startFlowControlActivity(int screeningId, int categoryRequest){
        Intent i = new Intent(this, FlowControlActivity.class);
        i.putExtra(FlowControlActivity.SCREENING_ID_KEY, screeningId);
        i.putExtra(FlowControlActivity.CATEGORY_REQUEST_KEY, categoryRequest);
        startActivity(i);
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
        displayScreeningMainMenuFragment(screening.getId(), screening.getFirstName() + " " +  screening.getLastName());
    }

    @Override
    public void onScreeningQuestionsButtonClicked(Screening screening) {
        Log.i(TAG, "onScreeningQuestionsButtonClicked called. Screening id = " + screening.getId());
        startFlowControlActivity(screening.getId(), -1);
    }
}
