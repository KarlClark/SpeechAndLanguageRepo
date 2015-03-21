package com.neuroleap.speachandlanguage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.neuroleap.speachandlanguage.Fragments.PrepositionsFragment_fm;
import com.neuroleap.speachandlanguage.Models.Question;
import com.neuroleap.speachandlanguage.Models.QuestionCategory;

import java.util.ArrayList;
import java.util.List;


public class FlowControlActivity extends ActionBarActivity {

    private ScreeningDbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private List<QuestionCategory> mQuestionCategories = new ArrayList<QuestionCategory>();
    private List<ArrayList<Question>> mQuestions = new ArrayList<ArrayList<Question>>();
    private int mLangage = ENGLISH;
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    DrawerListAdapter mDrawerListAdapter;
    private FragmentManager mFragmentManager=getSupportFragmentManager();

    private static final int ENGLISH = 0;
    private static final int SPANISH = 1;
    private static final String TAG = "## My Info ##";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_control);
        mDbHelper = new ScreeningDbHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        loadLists();
        setUpDrawer();
        //checkDB();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flow_control, menu);
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
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadLists(){
        String[] categoryColumns;
        String[] questionColumns;
        if (mLangage == ENGLISH) {
            categoryColumns = new String[]{"_ID", QuestionCategoriesEntry.CATEGORY_NAME_EG};
            questionColumns = new String[] {"_ID", QuestionsEntry.CATEGORY_ID, QuestionsEntry.PROMPT_ENGLISH};
        }else{
            categoryColumns = new String[]{"_ID", QuestionCategoriesEntry.CATEGORY_NAME_SP};
            questionColumns = new String[] {"_ID", QuestionsEntry.CATEGORY_ID, QuestionsEntry.PROMPT_SPANISH};
        }

        Cursor categoryCursor = mDb.query(QuestionCategoriesEntry.TABLE_NAME, categoryColumns, null, null, null, null, null);
        int categoryId;
        int categoryCount = 0;
        while (categoryCursor.moveToNext()){
            Log.i(TAG, "categoryCursor . movetoNext");
            categoryId = categoryCursor.getInt(0);
            QuestionCategory qc = new QuestionCategory(categoryId, categoryCursor.getString(1));
            mQuestionCategories.add(qc);
            Cursor questionCursor = mDb.query(QuestionsEntry.TABLE_NAME, questionColumns, QuestionsEntry.CATEGORY_ID + "=" + categoryId, null, null, null, null);
            mQuestions.add(new ArrayList<Question>());
            while (questionCursor.moveToNext()) {
                Question q = new Question(questionCursor.getInt(0), questionCursor.getInt(1), questionCursor.getString(2));
                mQuestions.get(categoryCount).add(q);
            }
            categoryCount++;
        }
    }

    private void setUpDrawer(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                Log.i (TAG ,"Group click category= " + mQuestionCategories.get(groupPosition).getText());
                String[] columns = new String[] {QuestionCategoriesEntry.FACILITATOR_MODE_FRAGMENT, QuestionCategoriesEntry.STUDENT_MODE_FRAGMENT};
                Cursor categoryCursor = mDb.query(QuestionCategoriesEntry.TABLE_NAME, columns, "_ID="+id, null, null, null, null);
                categoryCursor.moveToNext();
                Log.i(TAG, "Facilitator fragment= " + categoryCursor.getString(0) + "   Student Fragment= " + categoryCursor.getString(1));
                if (mFragmentManager.findFragmentById(R.id.fragmentContainer) == null ) {
                    PrepositionsFragment_fm frag = new PrepositionsFragment_fm();
                    mFragmentManager.beginTransaction().add(R.id.fragmentContainer, frag, "TAG").commit();
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

    private void checkDB(){
        String[] columns = new String[] {"_ID", QuestionCategoriesEntry.CATEGORY_NAME_EG, QuestionCategoriesEntry.CATEGORY_NAME_SP,
                QuestionCategoriesEntry.FACILITATOR_MODE_FRAGMENT,QuestionCategoriesEntry.STUDENT_MODE_FRAGMENT};
        String[] columns2 = new String[] {"_ID", QuestionsEntry.CATEGORY_ID, QuestionsEntry.TEXT_ENGLISH, QuestionsEntry.TEXT_SPANISH,
                QuestionsEntry.AUDIO_ENGLISH, QuestionsEntry.AUDIO_SPANISH, QuestionsEntry.PROMPT_ENGLISH, QuestionsEntry.PROMPT_SPANISH};
        String[] columns3 = new String[] {"_ID", PicturesEntry.QUESTION_ID, PicturesEntry.FILENAME};
        Cursor cursor = mDb.query(QuestionCategoriesEntry.TABLE_NAME, columns, null,null,null,null,null);
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
            Cursor cursor2 = mDb.query(QuestionsEntry.TABLE_NAME, columns2, QuestionsEntry.CATEGORY_ID + "=" + categoryId, null, null, null, null);
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
                Cursor cursor3 = mDb.query(PicturesEntry.TABLE_NAME, columns3, PicturesEntry.QUESTION_ID  + "=" + questionId, null, null, null,null);
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
