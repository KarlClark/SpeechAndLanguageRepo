package com.neuroleap.speachandlanguage.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.neuroleap.speachandlanguage.Data.ScreeningContract.PicturesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionCategoriesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningDbHelper;
import com.neuroleap.speachandlanguage.DrawerListAdapter;
import com.neuroleap.speachandlanguage.Fragments.NewOrContinuingFragment;
import com.neuroleap.speachandlanguage.Fragments.SplashFragment_1;
import com.neuroleap.speachandlanguage.Fragments.SplashFragment_2;
import com.neuroleap.speachandlanguage.Fragments.StudentInfoFragment;
import com.neuroleap.speachandlanguage.Fragments.WhichModeFragment;
import com.neuroleap.speachandlanguage.Models.Question;
import com.neuroleap.speachandlanguage.Models.QuestionCategory;
import com.neuroleap.speachandlanguage.Listeners.OnFragmentInteractionListener;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FlowControlActivity extends ActionBarActivity implements OnFragmentInteractionListener{

    private List<QuestionCategory> mQuestionCategories = new ArrayList<QuestionCategory>();
    private List<ArrayList<Question>> mQuestions = new ArrayList<ArrayList<Question>>();
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerListAdapter mDrawerListAdapter;
    private FragmentManager mFragmentManager=getSupportFragmentManager();
    private SharedPreferences mPrefs;
    private SplashFragment_1 mSplashFragment_1;
    private SplashFragment_2 mSplashFragment_2;
    private NewOrContinuingFragment mNewOrContinuingFragment;
    private StudentInfoFragment mStudentInfoFragment;
    private WhichModeFragment mWhichModeFragment;
    private boolean mShowNewScreeningMenuItem;
    private static final int SPLASH_FRAGMENT_1_ID = 1000;
    private static final int SPLASH_FRAGMENT_2_ID = 1001;
    private static final int NEW_OR_CONTINUING_FRAGMENT_ID = 1002;
    private static final int STUDENT_INFO_FRAGMENT_ID = 1003;
    private static final int WHICH_MODE_FRAGMENT_ID = 1004;

    private static final String TAG = "## My Info ##";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"start onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_control);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ScreeningDbHelper dbHelper = new ScreeningDbHelper(this);
        DbCRUD.setDatabase(dbHelper.getWritableDatabase());
        checkLanguagePreference();
        loadLists();

        //checkDB();
        if(savedInstanceState == null) {
            displayFirstSplashScreen();
        }
        Log.i(TAG,"end onCreate");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i(TAG,"onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.menu_flow_control, menu);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        MenuItem item = menu.findItem(R.id.new_screening);
        if (mShowNewScreeningMenuItem) {
            item.setVisible(true);
        }else{
            item.setVisible(false);
        }
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

        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "onActivityResult called");
        loadLists();
        mDrawerListAdapter.notifyDataSetChanged();
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        invalidateOptionsMenu();
    }

    private void displayFirstSplashScreen(){
        mSplashFragment_1= new SplashFragment_1();
        mSplashFragment_1.setId(SPLASH_FRAGMENT_1_ID);
        mFragmentManager.beginTransaction().add(R.id.fragmentContainer, mSplashFragment_1, "TAG").commit();
    }

    private void displaySecondSplashScreen(){
        mSplashFragment_2= new SplashFragment_2();
        mSplashFragment_2.setId(SPLASH_FRAGMENT_2_ID);
        mFragmentManager.beginTransaction().replace(R.id.fragmentContainer, mSplashFragment_2, "TAG").commit();
        mSplashFragment_1 = null;
    }

    private void displayWhichModeScreen(){
        mWhichModeFragment = new WhichModeFragment();
        mWhichModeFragment.setId(WHICH_MODE_FRAGMENT_ID);
        mFragmentManager.beginTransaction().replace(R.id.fragmentContainer, mWhichModeFragment, "TAG").commit();
        mSplashFragment_2 = null;
    }

    private void displayNewOrContinuingScreen(){
        mNewOrContinuingFragment = new NewOrContinuingFragment();
        mNewOrContinuingFragment.setId(NEW_OR_CONTINUING_FRAGMENT_ID);
        mFragmentManager.beginTransaction().replace(R.id.fragmentContainer, mNewOrContinuingFragment, "TAG").commit();
        mWhichModeFragment = null;
    }

    private void displayStudentInfoScreen() {
        mShowNewScreeningMenuItem = true;
        invalidateOptionsMenu();
        mStudentInfoFragment = new StudentInfoFragment();
        mStudentInfoFragment.setId(STUDENT_INFO_FRAGMENT_ID);
        mFragmentManager.beginTransaction().replace(R.id.fragmentContainer, mStudentInfoFragment, "TAG").commit();
        mNewOrContinuingFragment = null;
    }

    private void checkLanguagePreference() {
        mPrefs = getSharedPreferences(Utilities.PREFS_NAME, Activity.MODE_PRIVATE);
        int language = mPrefs.getInt(Utilities.PREFS_LANGUAGE, -1);
        if (language == -1) {
            Log.i (TAG, "locale language= " + Locale.getDefault().getLanguage());
            if(Locale.getDefault().getLanguage().equals("es")){
                language = Utilities.SPANISH;
            }else{
                language = Utilities.ENGLISH;
            }
        }
        Utilities.setLanguage(language);
        if (language == Utilities.ENGLISH){
            Utilities.setLocale(getBaseContext(), "en");
        }else{
            Utilities.setLocale(getBaseContext(), "es");
        }
    }

    private void loadLists(){
        mQuestionCategories.clear();
        mQuestions.clear();
        Cursor categoryCursor = DbCRUD.getQuestionCategories();
        int categoryId;
        int categoryCount = 0;
        while (categoryCursor.moveToNext()){
            //Log.i(TAG, "categoryCursor . movetoNext");
            categoryId = categoryCursor.getInt(0);
            QuestionCategory qc = new QuestionCategory(categoryId, categoryCursor.getString(1));
            mQuestionCategories.add(qc);
            Cursor questionCursor = DbCRUD.getQuestionsPrompts(categoryId);
            mQuestions.add(new ArrayList<Question>());
            while (questionCursor.moveToNext()) {
                Question q = new Question(questionCursor.getInt(0), questionCursor.getInt(1), questionCursor.getString(2));
                mQuestions.get(categoryCount).add(q);
            }
            categoryCount++;
        }
    }

    private void setUpDrawer(){

        mDrawerLayout.setStatusBarBackground(R.drawable.ic_launcher);
        mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.test_1,R.string.test_2){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the adapter for the list view
        mDrawerListAdapter = new DrawerListAdapter(this,mQuestionCategories, mQuestions);
        mDrawerList.setAdapter(mDrawerListAdapter);

        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Cursor categoryCursor = DbCRUD.getFacilitatorModeFragmentName(id);
                categoryCursor.moveToNext();
                Class myClass;
                Constructor constructor;
                Log.i(TAG, " package name=" + getPackageName());
                try {
                    myClass = Class.forName(getPackageName() + ".Fragments." +categoryCursor.getString(0));
                    constructor = myClass.getConstructor(null);
                    Fragment frag =(Fragment) constructor.newInstance();
                    mFragmentManager.beginTransaction().add(R.id.fragmentContainer, frag, "TAG").commit();
                } catch (ClassNotFoundException e) {
                    Log.i(TAG, "Class not found= "+ categoryCursor.getString(0));
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    Log.i(TAG, "bad constructor");
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.i(TAG, "Child clicked, category= " + mQuestionCategories.get(groupPosition).getText()+
                        "  prompt=  " + mQuestions.get(groupPosition).get(childPosition).getText());
                return true;
            }
        });
    }

    @Override
    public void onFragmentInteraction(int id, Object ... args){
        switch (id){
            case SPLASH_FRAGMENT_1_ID:
                displaySecondSplashScreen();
                break;
            case SPLASH_FRAGMENT_2_ID:
                displayWhichModeScreen();
                break;
            case WHICH_MODE_FRAGMENT_ID:
                displayNewOrContinuingScreen();
                break;
            case NEW_OR_CONTINUING_FRAGMENT_ID:
                Log.i(TAG, "NewOrContinuingFragment returned " + (Boolean)args[0]);
                if((Boolean)args[0]){

                }else{
                    displayStudentInfoScreen();
                }
                break;
            case STUDENT_INFO_FRAGMENT_ID:
                setUpDrawer();
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mDrawerToggle.syncState();
                break;
        }
    }

    private void checkDB(){
        String[] columns = new String[] {"_ID", QuestionCategoriesEntry.CATEGORY_NAME_EG, QuestionCategoriesEntry.CATEGORY_NAME_SP,
                QuestionCategoriesEntry.FACILITATOR_MODE_FRAGMENT,QuestionCategoriesEntry.STUDENT_MODE_FRAGMENT};
        String[] columns2 = new String[] {"_ID", QuestionsEntry.CATEGORY_ID, QuestionsEntry.TEXT_ENGLISH, QuestionsEntry.TEXT_SPANISH,
                QuestionsEntry.AUDIO_ENGLISH, QuestionsEntry.AUDIO_SPANISH, QuestionsEntry.PROMPT_ENGLISH, QuestionsEntry.PROMPT_SPANISH};
        String[] columns3 = new String[] {"_ID", PicturesEntry.QUESTION_ID, PicturesEntry.FILENAME};
        Cursor cursor = DbCRUD.getDatabase().query(QuestionCategoriesEntry.TABLE_NAME, columns, null, null, null, null, null);
        int categoryId, questionId;
        String category_eg;
        String category_sp;
        String fragName_fm;
        String fragname_sm;
        while (cursor.moveToNext()) {
            categoryId = cursor.getInt(0);
            category_eg = cursor.getString((1));
            category_sp = cursor.getString(2);
            fragName_fm = cursor.getString(3);
            fragname_sm = cursor.getString(4);
            Log.i(TAG, "id= " + categoryId + "  category EG = " + category_eg +"  category SP= " + category_sp + "  Facilitator Fragment name= " + fragName_fm +"  Student Fragment Name= " + fragname_sm);
            Log.i(TAG, "########################################################");
            Cursor cursor2 = DbCRUD.getDatabase().query(QuestionsEntry.TABLE_NAME, columns2, QuestionsEntry.CATEGORY_ID + "=" + categoryId, null, null, null, null);
            while (cursor2.moveToNext()){
                questionId = cursor2.getInt(0);
                int category_id = cursor2.getInt(1);
                String text_english = cursor2.getString(2);
                String text_spanish = cursor2.getString(3);
                String audio_english = cursor2.getString(4);
                String audio_spanish = cursor2.getString(5);
                String prompt_english = cursor2.getString(6);
                String prompt_spanish = cursor2.getString(7);
                Log.i(TAG, "questionId= " + questionId +" category id= " + category_id + " Text English= " + text_english +
                        " text Spanish= " + text_spanish + " audio english= " + audio_english +" audio spanish= " + audio_spanish
                        + " prompt english= " + prompt_english + " prompt spanish= " + prompt_spanish);
                Cursor cursor3 = DbCRUD.getDatabase().query(PicturesEntry.TABLE_NAME, columns3, PicturesEntry.QUESTION_ID + "=" + questionId, null, null, null, null);
                while (cursor3.moveToNext()) {
                    int answerId = cursor3.getInt(0);
                    int questionsId = cursor3.getInt(1);
                    String text = cursor3.getString(2);
                    Log.d(TAG, "picture id= " + answerId + "    question id= " + questionsId +"   filename: " + text);
                }
            }
        }
    }
}
