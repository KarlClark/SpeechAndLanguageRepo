package com.neuroleap.speachandlanguage.Activities;

import android.content.Intent;
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

import com.neuroleap.speachandlanguage.Adapters.DrawerListAdapter;
import com.neuroleap.speachandlanguage.Data.ScreeningDbHelper;
import com.neuroleap.speachandlanguage.Listeners.OnFragmentInteractionListener;
import com.neuroleap.speachandlanguage.Models.Question;
import com.neuroleap.speachandlanguage.Models.QuestionCategory;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        loadLists();

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
    }

}
