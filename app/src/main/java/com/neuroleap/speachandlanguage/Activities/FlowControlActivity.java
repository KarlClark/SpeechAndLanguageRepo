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
    private List<ArrayList<Question>> mDrawerQuestions = new ArrayList<ArrayList<Question>>();
    private ArrayList<Question> mViewPagerQuestions = new ArrayList<Question>();
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
    private int mAge;
    private int mCompletionState;
    public static final String SCREENING_ID_KEY = "screening_id_key";
    public static final String SCREENING_AGE_KEY = "screening_age_key";
    public static final String SCREENING_COMPLETION_STATE_KEY ="screening_completion_state_key";
    private static final String TAG = "## My Info ##";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "start onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_control);
        ScreeningDbHelper dbHelper = new ScreeningDbHelper(this);
        DbCRUD.setDatabase(dbHelper.getWritableDatabase());
        mScreeningId = getIntent().getIntExtra(SCREENING_ID_KEY, 0);
        mAge = getIntent().getIntExtra(SCREENING_AGE_KEY, 100);
        mCompletionState = getIntent().getIntExtra(SCREENING_COMPLETION_STATE_KEY, Utilities.SCREENING_NOT_STARTED);
        //Utilities.setTotalQuestions(DbCRUD.getQuestionCount());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        loadLists();
        mQuestionFragmentPagerAdapter = new QuestionFragmentPagerAdapter(getSupportFragmentManager(), mScreeningId, mViewPagerQuestions);
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mQuestionFragmentPagerAdapter);
        setUpDrawer();
        displayStartingQuestion();
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
        mDrawerQuestions.clear();
        Cursor categoryCursor = DbCRUD.getQuestionCategories();
        int categoryId;
        int categoryCount = 0;
        int questionCount = 0;
        int childCount = 0;
        while (categoryCursor.moveToNext()){
            //Log.i(TAG, "categoryCursor . movetoNext");
            if (mAge >= categoryCursor.getInt(2)) {
                categoryId = categoryCursor.getInt(0);
                QuestionCategory qc = new QuestionCategory(categoryId, categoryCursor.getString(1), Utilities.GROUP_DEFAULT_COLOR, categoryCount);
                mQuestionCategories.add(qc);
                Cursor questionCursor = DbCRUD.getQuestionsPrompts(categoryId);
                mDrawerQuestions.add(new ArrayList<Question>());
                while (questionCursor.moveToNext()) {
                    Question q = new Question(questionCursor.getInt(0), questionCursor.getInt(1), questionCursor.getString(2),
                                              Utilities.CHILD_DEFAULT_COLOR, categoryCursor.getString(3), questionCount,
                                              childCount, categoryCount);
                    mDrawerQuestions.get(categoryCount).add(q);
                    mViewPagerQuestions.add(q);
                    questionCount++;
                    childCount++;
                }
                childCount=0;
                questionCursor.close();
                categoryCount++;
            }
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

                Question q = mViewPagerQuestions.get(mViewPager.getCurrentItem());
                Log.i(TAG,"currentItem= " + mViewPager.getCurrentItem() );
                Log.i(TAG, " groupPosition= " + q.getGroupPosition() + "  Childposition= " + q.getChildPosition());
                setGroupColors(q.getGroupPosition());
                setChildColors(q.getGroupPosition(), q.getChildPosition());
                mDrawerList.expandGroup(q.getGroupPosition());
                mDrawerList.setSelection(q.getGroupPosition());
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
        mDrawerListAdapter = new DrawerListAdapter(this,mQuestionCategories, mDrawerQuestions);
        mDrawerList.setAdapter(mDrawerListAdapter);
        //mDrawerListAdapter.notifyDataSetChanged();

        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.i(TAG, "Group expanded = " + parent.isGroupExpanded(groupPosition));
                if ( ! parent.isGroupExpanded(groupPosition)) {
                    Log.i(TAG,"mOpenGroup= " + mOpenGroup);

                    setGroupColors(groupPosition);
                    Question q = mDrawerQuestions.get(groupPosition).get(0);
                    setChildColors(groupPosition,  q.getChildPosition());
                    mViewPager.setCurrentItem(q.getViewPagerPosition());
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
                        "  prompt=  " + mDrawerQuestions.get(groupPosition).get(childPosition).getText());
                setChildColors(groupPosition, childPosition);
                mViewPager.setCurrentItem(mDrawerQuestions.get(groupPosition).get(childPosition).getViewPagerPosition());
                return true;
            }
        });
    }

    @Override
    public void onFragmentInteraction(int id, Object ... args){
        if ((int)args[0]+1 < mViewPagerQuestions.size()){
            mViewPager.setCurrentItem((int)args[0]+1);
        }

    }

    private void setChildColors(int groupPosition, int childPosition){
        if (mPreviousHighLightedQuestiion != null) {
            Log.i(TAG,"Resetting color");
            mPreviousHighLightedQuestiion.setColor(mPreviousQuestionColor);
        }
        Question currentQuestion = mDrawerQuestions.get(groupPosition).get(childPosition);
        mPreviousHighLightedQuestiion = currentQuestion;
        mPreviousQuestionColor = currentQuestion.getColor();
        currentQuestion.setColor(Utilities.CHILD_HIGHLIGHT_COLOR);
        mDrawerListAdapter.notifyDataSetChanged();
    }

    private void setGroupColors(int position){
        if (mPreviousHighLightedCategory != null) {
            mPreviousHighLightedCategory.setColor(mPreviousCategoryColor);
        }
        mPreviousHighLightedCategory= mQuestionCategories.get(position);
        mPreviousCategoryColor = mQuestionCategories.get(position).getColor();
        mQuestionCategories.get(position).setColor(Utilities.GROUP_HIGHLIGHT_COLOR);
        mDrawerListAdapter.notifyDataSetChanged();
    }

    private void displayStartingQuestion(){

    }
}
