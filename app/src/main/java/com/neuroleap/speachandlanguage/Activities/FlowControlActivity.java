package com.neuroleap.speachandlanguage.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;

import com.neuroleap.speachandlanguage.Adapters.DrawerListAdapter;
import com.neuroleap.speachandlanguage.Adapters.QuestionFragmentPagerAdapter;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.*;
import com.neuroleap.speachandlanguage.Fragments.QuestionsBaseFragment;
import com.neuroleap.speachandlanguage.Listeners.OnFragmentInteractionListener;
import com.neuroleap.speachandlanguage.Models.Question;
import com.neuroleap.speachandlanguage.Models.QuestionCategory;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.util.ArrayList;
import java.util.List;


public class FlowControlActivity extends ActionBarActivity implements OnFragmentInteractionListener{

    private List<QuestionCategory> mQuestionCategories = new ArrayList<QuestionCategory>(); //Used with DrawListAdapter
    private List<ArrayList<Question>> mDrawerQuestions = new ArrayList<ArrayList<Question>>(); //Used with DrawListAdapter
    private ArrayList<Question> mViewPagerQuestions = new ArrayList<Question>();  // Used with QuestionFragmentPagerAdapter
    private ArrayList<Integer> mAllCompletedQuestionIds = new ArrayList<Integer>(); //List of completed(answered) questions
    private ArrayList<Boolean> mAllCompletedQuestionIsCorrect = new ArrayList<Boolean>(); //List of whether completed questions was corrected
    private int[] mIndex;
    private DrawerLayout mDrawerLayout;
    private InputMethodManager mInputMethodManager;
    private ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerListAdapter mDrawerListAdapter;
    private ViewPager mViewPager;
    private QuestionFragmentPagerAdapter mQuestionFragmentPagerAdapter;
    private int mOpenGroup = 0;
    private QuestionCategory mPreviousHighLightedCategory;
    private Question mCurrentHighLightedQuestion, mPreviousHighLightedQuestiion;
    private int mScreeningId;
    private int mAge;
    private String mStudentName;
    private int mCompletionState= Utilities.SCREENING_NOT_STARTED;
    private long mScreeningCategoryRequest;
    private boolean mKeyboardUp = false;
    public static final String SCREENING_ID_KEY = "screening_id_key";
    public static final String SCREENING_CATEGORY_REQUEST_KEY = "category_request_key";
    public static final String REQUESTED_ACTION_KEY ="requested_fragment_key";
    public static final String STUDENT_NAME_KEY = "student_name_key";
    public static final String SCREENING_ID_TAG = "screening-id_tag";
    public static final String SCREENING_STUDENT_NAME_TAG = "screening_student_name_tag";
    public static final int SHOW_SCREENINGS = 0;
    public static final int SHOW_OVERVIEW = 1;
    public static final int SHOW_RESULTS = 2;
    private static final String TAG = "## My Info ##";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.i(TAG, "start onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_control);
        if (Utilities.getTestMode() == Utilities.TEXT_INPUT_ONLY) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE |
                                         WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );
        }else{
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                                         WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );
        }
        mInputMethodManager =(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        mScreeningId = getIntent().getIntExtra(SCREENING_ID_KEY, 0);
        mAge = DbCRUD.getAge(mScreeningId);
        mStudentName = DbCRUD.getStudentNameStringFromScreeningId(mScreeningId);
        mCompletionState = DbCRUD.getScreeningCompletionState(mScreeningId);
        mIndex = new int[ DbCRUD.getQuestionCount() + 1 ];
        Log.i(TAG, "Screening id= " + mScreeningId);
        mScreeningCategoryRequest= getIntent().getLongExtra(SCREENING_CATEGORY_REQUEST_KEY, -1);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        loadLists();
        setupViewPager();
        setUpDrawer();
        setUpRootViewListener();
        displayFirstQuestion();

        //Log.i(TAG,"end onCreate");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Sync the ActionBar indicator with the state of the drawer.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flow_control, menu);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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

        //Return from SettingsActivity
        loadLists();  //Since language may have changed
        mDrawerListAdapter.notifyDataSetChanged();
        mQuestionFragmentPagerAdapter.notifyDataSetChanged();
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        invalidateOptionsMenu();
        if (Utilities.getTestMode() == Utilities.TEXT_INPUT_ONLY ){
            raiseKeyBoard();
        }else {
            lowerKeyboard();
        }
    }

    private void setupViewPager(){
        mQuestionFragmentPagerAdapter = new QuestionFragmentPagerAdapter(getSupportFragmentManager(), mScreeningId, mViewPagerQuestions);
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mQuestionFragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (Utilities.getTestMode() == Utilities.TEXT_INPUT_ONLY ){
                    raiseKeyBoard();
                }else {
                    lowerKeyboard();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpRootViewListener(){
        mDrawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = mDrawerLayout.getRootView().getHeight() - mDrawerLayout.getHeight();
                mKeyboardUp = (heightDiff > 400);
            }
        });
    }

    private void lowerKeyboard() {
        if (mKeyboardUp) {
            mInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        }
    }

    private void raiseKeyBoard(){
        if ( ! mKeyboardUp) {
            mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void loadLists(){
        mQuestionCategories.clear();
        mDrawerQuestions.clear();
        mAllCompletedQuestionIds.clear();
        mAllCompletedQuestionIsCorrect.clear();
        mViewPagerQuestions.clear();

        //Build a list of completed questions, and a matching list of whether the answer was correct.
        Log.i(TAG,"mCompletionState= " + mCompletionState);
        if (mCompletionState == Utilities.SCREENING_NOT_COMPLETE) {
            Cursor allCompletedCursor = DbCRUD.getAllCompletedQuestionsIds(mScreeningId);
            Log.i(TAG, "allCompletedCursor size= " + allCompletedCursor.getCount());
            while (allCompletedCursor.moveToNext()) {
                mAllCompletedQuestionIds.add(allCompletedCursor.getInt(allCompletedCursor.getColumnIndex(StudentAnswersEntry.QUESTION_ID)));
                mAllCompletedQuestionIsCorrect.add(allCompletedCursor.getInt(allCompletedCursor.getColumnIndex(StudentAnswersEntry.CORRECT)) != 0);
                Log.i(TAG, "add completed id " + allCompletedCursor.getInt(allCompletedCursor.getColumnIndex(StudentAnswersEntry.QUESTION_ID)));
            }
        }

        // build the mQuestionCategories, mDrawQuestions, and mViewPagerQuestions lists.
        int categoryId;
        int categoryCount = 0;
        int questionCount = 0;
        int childCount = 0;
        boolean categoryDone;
        boolean isCorrect;
        int index;
        Cursor categoryCursor = DbCRUD.getQuestionCategories();
        while (categoryCursor.moveToNext()){
            //Log.i(TAG, "categoryCursor . movetoNext");
            Log.i(TAG , "mAge = " + mAge + "  cursor age = " + categoryCursor.getInt(2));
            if (mAge >= categoryCursor.getInt(categoryCursor.getColumnIndex(QuestionCategoriesEntry.CUTOFF_AGE))) {
                categoryDone = true;
                categoryId = categoryCursor.getInt(categoryCursor.getColumnIndex(QuestionCategoriesEntry._ID));
                QuestionCategory qc = new QuestionCategory(categoryId,
                        categoryCursor.getInt(categoryCursor.getColumnIndex(QuestionCategoriesEntry.SCREENING_CATEGORY_ID)),
                        categoryCursor.getString(1), Utilities.GROUP_UNTOUCHED_COLOR, categoryCount);
                mQuestionCategories.add(qc);
                Cursor questionCursor = DbCRUD.getQuestionsPrompts(categoryId);
                mDrawerQuestions.add(new ArrayList<Question>());
                while (questionCursor.moveToNext()) {
                    //Check if question is in the completed questions list. If it is set
                    //the isCorrect variable appropriately. If all questions in a category
                    //are complete (answered) the the entire category is complete.
                    boolean questionDone = ((index = mAllCompletedQuestionIds.indexOf
                            (questionCursor.getInt(questionCursor.getColumnIndex(QuestionsEntry._ID)))) >= 0);
                    Log.i(TAG, "questionDone= "+ questionDone);
                    if (  questionDone){
                        isCorrect = mAllCompletedQuestionIsCorrect.get(index);
                    }else{
                        categoryDone = false;
                        isCorrect = false;
                    }
                    Question q = new Question(questionCursor.getInt(questionCursor.getColumnIndex(QuestionsEntry._ID)),
                                              questionCursor.getInt(questionCursor.getColumnIndex(QuestionsEntry.CATEGORY_ID)),
                                              categoryCursor.getInt(categoryCursor.getColumnIndex(QuestionCategoriesEntry.SCREENING_CATEGORY_ID)),
                                              questionCursor.getString(2), Utilities.CHILD_DEFAULT_COLOR,
                                              categoryCursor.getString(categoryCursor.getColumnIndex(QuestionCategoriesEntry.FRAGMENT_NAME)),
                                              questionCount, childCount, categoryCount, questionDone, isCorrect);
                    mDrawerQuestions.get(categoryCount).add(q);
                    mViewPagerQuestions.add(q);
                    mIndex[questionCursor.getInt(questionCursor.getColumnIndex(QuestionsEntry._ID))] = questionCount;
                    questionCount++;
                    childCount++;
                }
                qc.setDone(categoryDone);
                qc.setNumberOfChildren(childCount);
                setGroupColor(categoryCount);
                childCount=0;
                questionCursor.close();
                categoryCount++;
            }
        }
        categoryCursor.close();
    }

    private void setUpDrawer(){

        //mDrawerLayout.setStatusBarBackground(R.drawable.ic_launcher);
        mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.test_1,R.string.test_2){
            @Override
            public void onDrawerOpened(View drawerView) {
                lowerKeyboard();
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                Question q = mViewPagerQuestions.get(mViewPager.getCurrentItem());
                setGroupColor(q.getGroupPosition());
                setChildColors(q.getGroupPosition(), q.getChildPosition());
                mDrawerList.expandGroup(q.getGroupPosition());
                mDrawerList.setSelection(q.getGroupPosition()); //to first un-answered question in the group
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                if (Utilities.getTestMode() == Utilities.TEXT_INPUT_ONLY){
                    raiseKeyBoard();
                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerListAdapter = new DrawerListAdapter(this,mQuestionCategories, mDrawerQuestions);
        mDrawerList.setAdapter(mDrawerListAdapter);

        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //Log.i(TAG, "Group expanded = " + parent.isGroupExpanded(groupPosition));
                if ( ! parent.isGroupExpanded(groupPosition)) {
                    //Log.i(TAG,"mOpenGroup= " + mOpenGroup);

                    setGroupColor(groupPosition);
                    Question q = getUnansweredChild(groupPosition);
                    setChildColors(groupPosition, q.getChildPosition());
                    mViewPager.setCurrentItem(q.getViewPagerPosition());
                    //Log.i(TAG, "here 1");
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
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerList.setSelectedGroup(mOpenGroup);
                        int lastPosition =mDrawerList.getLastVisiblePosition();
                        if (mCurrentHighLightedQuestion.getChildPosition() > lastPosition-2){
                            mDrawerList.setSelection(mCurrentHighLightedQuestion.getChildPosition() - lastPosition/2);
                        }
                    }
                }, 250);
            }
        });



        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Log.i(TAG, "Child clicked, category= " + mQuestionCategories.get(groupPosition).getText()+
                 //       "  prompt=  " + mDrawerQuestions.get(groupPosition).get(childPosition).getText());
                setChildColors(groupPosition, childPosition);
                mViewPager.setCurrentItem(mDrawerQuestions.get(groupPosition).get(childPosition).getViewPagerPosition());
                return true;
            }
        });
    }

    @Override
    public void onFragmentInteraction(int id, Object ... args){
        switch ((int)args[0]) {
            case QuestionsBaseFragment.SHOW_NEXT_FRAGMENT:
                mViewPagerQuestions.get((int) args[1]).setDone(true);
                if (groupIsCompleted((int) args[2])) {
                    mQuestionCategories.get((int) args[2]).setDone(true);
                }
                if ((int) args[0] + 1 < mViewPagerQuestions.size()) {
                    mViewPager.setCurrentItem((int) args[1] + 1);
                }
                break;
            case QuestionsBaseFragment.OVERVIEW_BUTTON_CLICKED:
                Log.i(TAG,"Overview Button pressed");

                Intent i = new Intent();
                i.putExtra(REQUESTED_ACTION_KEY, SHOW_OVERVIEW);
                i.putExtra(SCREENING_ID_TAG, mScreeningId);
                String name = DbCRUD.getStudentNameStringFromScreeningId(mScreeningId);
                i.putExtra(SCREENING_STUDENT_NAME_TAG, name);
                setResult(RESULT_OK, i);
                lowerKeyboard();
                finish();
                break;
            case QuestionsBaseFragment.RESULTS_BUTTON_CLICKED:
                Log.i(TAG, "Results Button pressed");
                Intent intent = new Intent();
                intent.putExtra(REQUESTED_ACTION_KEY, SHOW_RESULTS);
                intent.putExtra(SCREENING_ID_TAG, mScreeningId);
                String studentName = DbCRUD.getStudentNameStringFromScreeningId(mScreeningId);
                intent.putExtra(SCREENING_STUDENT_NAME_TAG, studentName);
                setResult(RESULT_OK, intent);
                lowerKeyboard();
                finish();
                break;
            case QuestionsBaseFragment.SCREENINGS_BUTTON_CLICKED:
                Log.i(TAG, " Screenings button pressed");
                callSetResult(SHOW_SCREENINGS);
        }

    }

    private void callSetResult(int nextAction){
        Intent i = new Intent();
        i.putExtra(REQUESTED_ACTION_KEY, nextAction);
        setResult(RESULT_OK, i);
        lowerKeyboard();
        finish();
    }

    private void setChildColors(int groupPosition, int childPosition){
        if (mPreviousHighLightedQuestiion != null) {
            if (mPreviousHighLightedQuestiion.isDone()) {
                mPreviousHighLightedQuestiion.setColor(Utilities.CHILD_COMPLETED_COLOR);
            }else{
                mPreviousHighLightedQuestiion.setColor(Utilities.CHILD_DEFAULT_COLOR);
            }
        }
        mCurrentHighLightedQuestion = mDrawerQuestions.get(groupPosition).get(childPosition);
        mPreviousHighLightedQuestiion = mCurrentHighLightedQuestion;
        mCurrentHighLightedQuestion.setColor(Utilities.CHILD_HIGHLIGHT_COLOR);
        mDrawerListAdapter.notifyDataSetChanged();
    }

    private void setGroupColor(int position){
        ArrayList<Question> al = mDrawerQuestions.get(position);
        int doneCount = 0;
        float correctCount = 0;
        int total = al.size();
        for(int i = 0; i < total; i++){
            Question question = al.get(i);
            if (question.isDone()){
                doneCount++;
                if(question.isCorrect()){
                    correctCount++;
                }
            }
        }
        if (doneCount == 0) {
            mQuestionCategories.get(position).setColor(Utilities.GROUP_UNTOUCHED_COLOR);
            return;
        }

        if (correctCount/total >= 0.8) {
            mQuestionCategories.get(position).setColor(Utilities.GROUP_PASSING_COLOR);
        }else{
            mQuestionCategories.get(position).setColor(Utilities.GROUP_FAILING_COLOR);
        }

        if (mDrawerListAdapter != null) {
            mDrawerListAdapter.notifyDataSetChanged();
        }
    }

    private boolean groupIsCompleted(int groupPosition){
        ArrayList<Question> al = mDrawerQuestions.get(groupPosition);
        for (int i = 0; i < al.size(); i++){
            if ( ! al.get(i).isDone()){
                return false;
            }
        }
        return true;
    }

    private void displayFirstQuestion() {
        Question question;
        Log.i(TAG,"mCategoryRequest= " + mScreeningCategoryRequest + "  ############################");
        if (mScreeningCategoryRequest >=0 ){
            int firstIndex = -1;
            for(int i = 0; i < mViewPagerQuestions.size(); i++){
                question = mViewPagerQuestions.get(i);
                if (question.getScreeningCategoryId() == mScreeningCategoryRequest){
                    if (firstIndex == -1){
                        Log.i(TAG ,"here1");
                        firstIndex = i;
                    }
                    if ( ! question.isDone()) {
                        mViewPager.setCurrentItem(i);
                        Log.i(TAG,"here2");
                        return;
                    }
                }
            }
            Log.i(TAG,"FirstIndwx = " + firstIndex);
            mViewPager.setCurrentItem(firstIndex);
            return;
        }

        for (int i = 0; i < mViewPagerQuestions.size(); i++){
            question = mViewPagerQuestions.get(i);
            if ( ! question.isDone()){
                mViewPager.setCurrentItem(i);
                return;
            }
            mViewPager.setCurrentItem(0);
        }
    }

    private Question getUnansweredChild(int groupPosition){
        ArrayList<Question> aq = mDrawerQuestions.get(groupPosition);
        for (int i = 0; i < aq.size(); i++){
            if( ! aq.get(i).isDone()){
                return aq.get(i);
            }
        }
        return(aq.get(0));
    }

}
