package com.neuroleap.speachandlanguage.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.neuroleap.speachandlanguage.Adapters.DrawerListAdapter;
import com.neuroleap.speachandlanguage.Adapters.QuestionFragmentPagerAdapter;
import com.neuroleap.speachandlanguage.Data.ScreeningDbHelper;
import com.neuroleap.speachandlanguage.Listeners.OnFragmentInteractionListener;
import com.neuroleap.speachandlanguage.Models.Question;
import com.neuroleap.speachandlanguage.Models.QuestionCategory;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.util.ArrayList;
import java.util.List;


public class FlowControlActivity extends ActionBarActivity implements OnFragmentInteractionListener{

    private List<QuestionCategory> mQuestionCategories = new ArrayList<QuestionCategory>();
    private List<ArrayList<Question>> mQuestions = new ArrayList<ArrayList<Question>>();
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerListAdapter mDrawerListAdapter;
    private FragmentManager mFragmentManager=getSupportFragmentManager();
    private ViewPager mViewPager;
    private QuestionFragmentPagerAdapter mQuestionFragmentPagerAdapter;
    private int mOpenGroup = 0;
    private QuestionCategory mPreviousHighLightedCategory;
    private int mPreviousCategoryColor;
    private Question mPreviousHighLightedQuestiion;
    private int mPreviousQuestionColor;
    private int mScreeningId;
    public static final String SCREENING_ID_KEY = "screening_id_key";
    private static final String TAG = "## My Info ##";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "start onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_control);
        ScreeningDbHelper dbHelper = new ScreeningDbHelper(this);
        DbCRUD.setDatabase(dbHelper.getWritableDatabase());
        mScreeningId = getIntent().getIntExtra(SCREENING_ID_KEY, 0);
        Utilities.setTotalQuestions(DbCRUD.getQuestionCount());
        mQuestionFragmentPagerAdapter = new QuestionFragmentPagerAdapter(getSupportFragmentManager(), mScreeningId);
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mQuestionFragmentPagerAdapter);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        loadLists();
        setUpDrawer();
        //displayQuestion(1);
        Log.i(TAG,"end onCreate");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i(TAG,"onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.menu_flow_control, menu);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
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



    private void loadLists(){
        mQuestionCategories.clear();
        mQuestions.clear();
        Cursor categoryCursor = DbCRUD.getQuestionCategories();
        int categoryId;
        int categoryCount = 0;
        while (categoryCursor.moveToNext()){
            //Log.i(TAG, "categoryCursor . movetoNext");
            categoryId = categoryCursor.getInt(0);
            QuestionCategory qc = new QuestionCategory(categoryId, categoryCursor.getString(1), Utilities.GROUP_DEFAULT_COLOR);
            mQuestionCategories.add(qc);
            Cursor questionCursor = DbCRUD.getQuestionsPrompts(categoryId);
            mQuestions.add(new ArrayList<Question>());
            while (questionCursor.moveToNext()) {
                Question q = new Question(questionCursor.getInt(0), questionCursor.getInt(1), questionCursor.getString(2), Utilities.CHILD_DEFAULT_COLOR);
                mQuestions.get(categoryCount).add(q);
            }
            questionCursor.close();
            categoryCount++;
        }
        categoryCursor.close();
    }

    private void setUpDrawer(){

        mDrawerLayout.setStatusBarBackground(R.drawable.ic_launcher);
        mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.test_1,R.string.test_2){
            @Override
            public void onDrawerOpened(View drawerView) {
                Log.i(TAG, " Drawer open");
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                int questionId = mViewPager.getCurrentItem()+1;
                int categoryId = DbCRUD.getQuestionCategory(questionId);
                Log.i(TAG,"here 2");
                setGroupColors(categoryId);
                setChildColors(categoryId,questionId);
                Log.i(TAG,"categoryId= " + categoryId);
                mDrawerList.expandGroup(categoryId-1);
                mDrawerList.setSelection(categoryId-1);
                //mOpenGroup = categoryId-1;
                Log.i(TAG,"here 3");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.i(TAG, "Drawer closed");
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                //mDrawerList.collapseGroup(mOpenGroup);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the adapter for the list view
        mDrawerListAdapter = new DrawerListAdapter(this,mQuestionCategories, mQuestions);
        mDrawerList.setAdapter(mDrawerListAdapter);
        //mDrawerListAdapter.notifyDataSetChanged();

        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.i(TAG, "Group expanded = " + parent.isGroupExpanded(groupPosition));
                if ( ! parent.isGroupExpanded(groupPosition)) {
                    Log.i(TAG,"mOpenGroup= " + mOpenGroup);

                    int questionId = DbCRUD.getFirstQuestion(id);
                    setGroupColors((int)id);
                    setChildColors((int)id, questionId);
                    mViewPager.setCurrentItem(questionId - 1);
                    Log.i(TAG, "here 1");
                }
                return false;
            }
        });

        mDrawerList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (mOpenGroup != groupPosition) {
                    mDrawerList.collapseGroup(mOpenGroup);
                }
                mOpenGroup = groupPosition;
            }
        });

        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.i(TAG, "Child clicked, category= " + mQuestionCategories.get(groupPosition).getText()+
                        "  prompt=  " + mQuestions.get(groupPosition).get(childPosition).getText());
                setChildColors(groupPosition + 1 , (int)id);
                mViewPager.setCurrentItem((int)id -1);
                return true;
            }
        });
    }

    @Override
    public void onFragmentInteraction(int id, Object ... args){
        if (id <= Utilities.getTotalQuestions()){
            mViewPager.setCurrentItem(id);
        }

    }

    private Question getCurrentQuestion(int categoryId, int questionId){
        for (int i=0; i < mQuestions.get(categoryId-1).size(); i++){
            if (mQuestions.get(categoryId-1).get(i).getId() == questionId){
                return mQuestions.get(categoryId-1).get(i);
            }
        }
        return null;
    }

    private void setChildColors(int categoryId, int questionId){
        if (mPreviousHighLightedQuestiion != null) {
            Log.i(TAG,"Resetting color");
            mPreviousHighLightedQuestiion.setColor(mPreviousQuestionColor);
        }
        Question currentQuestion = getCurrentQuestion(categoryId, questionId);
        mPreviousHighLightedQuestiion = currentQuestion;
        mPreviousQuestionColor = currentQuestion.getColor();
        currentQuestion.setColor(Utilities.CHILD_HIGHLIGHT_COLOR);
        mDrawerListAdapter.notifyDataSetChanged();
    }

    private void setGroupColors(int categoryId){
        if (mPreviousHighLightedCategory != null) {
            mPreviousHighLightedCategory.setColor(mPreviousCategoryColor);
        }
        mPreviousHighLightedCategory= mQuestionCategories.get(categoryId-1);
        mPreviousCategoryColor = mQuestionCategories.get(categoryId -1).getColor();
        mQuestionCategories.get(categoryId -1).setColor(Utilities.GROUP_HIGHLIGHT_COLOR);
        mDrawerListAdapter.notifyDataSetChanged();
    }
}
