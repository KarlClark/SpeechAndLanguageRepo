package com.neuroleap.speachandlanguage.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
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
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionCategoriesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentAnswersEntry;
import com.neuroleap.speachandlanguage.Fragments.AlertDialogFragment;
import com.neuroleap.speachandlanguage.Fragments.QuestionsBaseFragment;
import com.neuroleap.speachandlanguage.Listeners.OnAlertDialogListener;
import com.neuroleap.speachandlanguage.Listeners.OnFragmentInteractionListener;
import com.neuroleap.speachandlanguage.Models.Question;
import com.neuroleap.speachandlanguage.Models.QuestionCategory;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FlowControlActivity extends ActionBarActivity implements OnFragmentInteractionListener, OnAlertDialogListener {

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
    private Question mCurrentHighLightedQuestion, mPreviousHighLightedQuestion;
    private MediaRecorder mMediaRecorder;
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
    private static final int NO_SD_CARD_TAG = 0;
    private static final int CANT_CREATE_DIRECTORY_TAG = 1;
    private static final int CANT_PREPARE_MEDIA_RECORDER_TAG = 2;
    private static final int RECORD_AUDIO_WARNING_TAG = 3;
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
        checkSdCard();
        if (Utilities.getAudioRecordMode() == Utilities.ON &&
                Utilities.getTestMode() == Utilities.BOTH_SCORING_BUTTONS_AND_TEXT){
            startRecording();
        }

        //Log.i(TAG,"end onCreate");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Sync the ActionBar indicator with the state of the drawer.
        mDrawerToggle.syncState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
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

    @Override
    public void onBackPressed() {
        checkCompletionState();
        super.onBackPressed();
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
        //Changes in GlobalLayout may mean the virtual keyboard has be raised or lower.
        //Try to determine id keyboard is up or down.
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
        //Log.i(TAG,"mCompletionState= " + mCompletionState);
        //if (mCompletionState == Utilities.SCREENING_NOT_COMPLETE) {
            Cursor allCompletedCursor = DbCRUD.getAllCompletedQuestionsIds(mScreeningId);
            //Log.i(TAG, "allCompletedCursor size= " + allCompletedCursor.getCount());
            while (allCompletedCursor.moveToNext()) {
                mAllCompletedQuestionIds.add(allCompletedCursor.getInt(allCompletedCursor.getColumnIndex(StudentAnswersEntry.QUESTION_ID)));
                mAllCompletedQuestionIsCorrect.add(allCompletedCursor.getInt(allCompletedCursor.getColumnIndex(StudentAnswersEntry.CORRECT)) != 0);
                //Log.i(TAG, "add completed id " + allCompletedCursor.getInt(allCompletedCursor.getColumnIndex(StudentAnswersEntry.QUESTION_ID)));
            }
        //}

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
            //Log.i(TAG , "mAge = " + mAge + "  cursor age = " + categoryCursor.getInt(2));
            if (mAge >= categoryCursor.getInt(categoryCursor.getColumnIndex(QuestionCategoriesEntry.LOW_CUTOFF_AGE)) &&
                    mAge <= categoryCursor.getInt(categoryCursor.getColumnIndex(QuestionCategoriesEntry.HIGH_CUTOFF_AGE))) {
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
                    //Log.i(TAG, "questionDone= "+ questionDone);
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
                //When the drawer is opened we want to expand the list depending on which question category
                //the user is currently working on.  We want to set the colors of the drawer items
                //depending on which questions have been answered and which categories have been completed.

                lowerKeyboard(); // so drawer can use whole screen.
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Required to show hamburger icon

        //Create the draw list adapter and set it on the drawer list.
        mDrawerListAdapter = new DrawerListAdapter(this,mQuestionCategories, mDrawerQuestions);
        mDrawerList.setAdapter(mDrawerListAdapter);

        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if ( ! parent.isGroupExpanded(groupPosition)) {
                    //Set the color of the list items and display the fragment for the first
                    //unanswered question in the group.
                    setGroupColor(groupPosition);
                    Question q = getUnansweredChild(groupPosition);
                    setChildColors(groupPosition, q.getChildPosition());
                    mViewPager.setCurrentItem(q.getViewPagerPosition());
                }
                return false;
            }
        });


        mDrawerList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (mOpenGroup != groupPosition) {
                    //Collapse previously o[en group.
                    mDrawerList.collapseGroup(mOpenGroup);
                }

                //Make sure the list item for the current question is visible on the screen.
                //Do this in a delayed handler so that UI can complete collapseGroup from above.
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
                //Display fragment for selected question.
                setChildColors(groupPosition, childPosition);
                mViewPager.setCurrentItem(mDrawerQuestions.get(groupPosition).get(childPosition).getViewPagerPosition());
                return true;
            }
        });
    }

    @Override
    public void onFragmentInteraction(int id, Object ... args){
        //Call back from question fragments
        switch ((int)args[0]) {
            case QuestionsBaseFragment.SHOW_NEXT_FRAGMENT:
                //args[1] = ViewPager position, args[2] = group position
                //args[3] = answer correct true/false
                mViewPagerQuestions.get((int) args[1]).setDone(true);  //Question has been answered
                mViewPagerQuestions.get((int)args[1]).setCorrect((boolean)args[3]);
                if (groupIsCompleted((int) args[2])) {  //Check id all questions in group are answered
                    mQuestionCategories.get((int) args[2]).setDone(true);
                    setGroupColor((int)args[2]);
                }
                if ((int) args[0] + 1 < mViewPagerQuestions.size()) {
                    mViewPager.setCurrentItem((int) args[1] + 1); //Display fragment for next question
                }
                break;
            case QuestionsBaseFragment.OVERVIEW_BUTTON_CLICKED:
                // Return to StartUpActivity and display overview fragment.
                Intent i = new Intent();
                i.putExtra(REQUESTED_ACTION_KEY, SHOW_OVERVIEW);
                i.putExtra(SCREENING_ID_TAG, mScreeningId);
                String name = DbCRUD.getStudentNameStringFromScreeningId(mScreeningId);
                i.putExtra(SCREENING_STUDENT_NAME_TAG, name);
                setResult(RESULT_OK, i);
                lowerKeyboard();
                checkCompletionState();
                finish();
                break;
            case QuestionsBaseFragment.RESULTS_BUTTON_CLICKED:
                //Return to StartUpActivity and display results fragment.
                Intent intent = new Intent();
                intent.putExtra(REQUESTED_ACTION_KEY, SHOW_RESULTS);
                intent.putExtra(SCREENING_ID_TAG, mScreeningId);
                String studentName = DbCRUD.getStudentNameStringFromScreeningId(mScreeningId);
                intent.putExtra(SCREENING_STUDENT_NAME_TAG, studentName);
                setResult(RESULT_OK, intent);
                lowerKeyboard();
                checkCompletionState();
                finish();
                break;
            case QuestionsBaseFragment.SCREENINGS_BUTTON_CLICKED:
                //Return to StartUpActivity and display screenings fragment.
                callSetResult(SHOW_SCREENINGS);
        }

    }

    private void callSetResult(int nextAction){
        Intent i = new Intent();
        i.putExtra(REQUESTED_ACTION_KEY, nextAction);
        setResult(RESULT_OK, i);
        lowerKeyboard();
        checkCompletionState();
        finish();
    }

    private void setChildColors(int groupPosition, int childPosition){
        //The current question has its own highlight color.  Set the color of the previous
        //highlighted question back to its normal color. Then set the color of the current
        //question to the highlight color.
        if (mPreviousHighLightedQuestion != null) {
            if (mPreviousHighLightedQuestion.isDone()) {
                mPreviousHighLightedQuestion.setColor(Utilities.CHILD_COMPLETED_COLOR);
            }else{
                mPreviousHighLightedQuestion.setColor(Utilities.CHILD_DEFAULT_COLOR);
            }
        }
        mCurrentHighLightedQuestion = mDrawerQuestions.get(groupPosition).get(childPosition);
        mPreviousHighLightedQuestion = mCurrentHighLightedQuestion;
        mCurrentHighLightedQuestion.setColor(Utilities.CHILD_HIGHLIGHT_COLOR);
        mDrawerListAdapter.notifyDataSetChanged();
    }

    private void setGroupColor(int position){
        //Group color is set to one color if no questions have been answered.
        //If some questions in the group have been answered the color is set
        //depending on whether the student is passing or not.
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
        Log.i(TAG, "%%%%%%  position= " + position +"  doneCount= " + doneCount + "  correctCount= " + correctCount +"  total= " + total +"  correctCount/total= " + correctCount/total);
        if (doneCount == 0) {
            mQuestionCategories.get(position).setColor(Utilities.GROUP_UNTOUCHED_COLOR);
            return;
        }

        if (correctCount/total >= Utilities.PASSING_FRACTION) {
            mQuestionCategories.get(position).setColor(Utilities.GROUP_PASSING_COLOR);
        }else{
            mQuestionCategories.get(position).setColor(Utilities.GROUP_FAILING_COLOR);
        }

        if (mDrawerListAdapter != null) {
            mDrawerListAdapter.notifyDataSetChanged();
        }
    }

    private boolean groupIsCompleted(int groupPosition){
        //Check to see if all questions in the group have been answered.
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
        if (mScreeningCategoryRequest >=0 ){
            //We have a requested category from the Overview screen. Move to first
            //unanswered question in category. If all questions in category are
            //answered, the move to first question in category.
            int firstIndex = -1;
            for(int i = 0; i < mViewPagerQuestions.size(); i++){
                question = mViewPagerQuestions.get(i);
                if (question.getScreeningCategoryId() == mScreeningCategoryRequest){
                    if (firstIndex == -1){
                        firstIndex = i;
                    }
                    if ( ! question.isDone()) {
                        mViewPager.setCurrentItem(i);
                        return;
                    }
                }
            }
            Log.i(TAG,"FirstIndex = " + firstIndex);
            mViewPager.setCurrentItem(firstIndex);
            return;
        }

        //No category request, so move to first unanswered question, or first
        //question if all questions are answered.
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
        //Find first unanswered question n the group. If all
        //questions in group are answered, return the first question
        // in the group.
        ArrayList<Question> aq = mDrawerQuestions.get(groupPosition);
        for (int i = 0; i < aq.size(); i++){
            if( ! aq.get(i).isDone()){
                return aq.get(i);
            }
        }
        return(aq.get(0));
    }

    private void checkCompletionState(){
        for (Question question : mViewPagerQuestions) {
            if ( ! question.isDone()){
                Log.i(TAG, "Question '" + question.getText() +"'  at position " + question.getViewPagerPosition() +  " is not answered.");
                return;
            }
        }
        Log.i(TAG, "update DB with Screening completed");
        DbCRUD.updateScreeningCompletionState(mScreeningId, Utilities.SCREENING_COMPLETED);
    }

    @Override
    public void onAlertDialogPositiveClick(int tag){
        Log.i(TAG,"Alert dialog yes click");
    }

    @Override
    public void onAlertDialogNegativeClick(int tag){
        Log.i(TAG,"Alert dialog no click");
        callSetResult(SHOW_SCREENINGS);
    }

    private void checkSdCard(){
        if (Utilities.getAudioRecordMode() == Utilities.ON &&
            Utilities.getTestMode() == Utilities.BOTH_SCORING_BUTTONS_AND_TEXT &&
            ! Utilities.externalStorageIsWritable()){
            AlertDialogFragment diaFrag = AlertDialogFragment.newInstance(R.string.no_sdcard, R.string.want_to_continue,
                    R.string.yes, R.string.no, NO_SD_CARD_TAG);
            diaFrag.show(getSupportFragmentManager(), "dialog");
        }
    }

    private void startRecording(){
        if ( ! Utilities.externalStorageIsWritable()){
            return; // User was already given error msg in checkSdCard
        }
        String underscoreName = new String(mStudentName);
        underscoreName=underscoreName.replace(" " , "_");
        Log.i(TAG, "underscoreName= "+ underscoreName );
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), getString(R.string.neuro_underscore_leap) +"/" + underscoreName);
        Log.i(TAG, "storage directory = " + mediaStorageDir);
        if ( ! mediaStorageDir.exists()) {
            if ( ! mediaStorageDir.mkdirs()) {
                Log.i(TAG, "couldn't make directory");
                AlertDialogFragment diaFrag = AlertDialogFragment.newInstance(R.string.cant_make_dir, R.string.want_to_continue,
                        R.string.yes, R.string.no, CANT_CREATE_DIRECTORY_TAG);
                diaFrag.show(getSupportFragmentManager(), "dialog");
                return;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        long screeningDate = DbCRUD.getLongScreeningDate(mScreeningId);
        String screeningDateString = new SimpleDateFormat("MMM_dd_yyyy").format(screeningDate);
        Log.i(TAG, "time stamp= " + timeStamp +"  screening date= " + screeningDateString);
        String filename = mediaStorageDir.getPath() + File.separator + underscoreName +"_" + screeningDateString + "_" + timeStamp + ".3gp";
        Log.i(TAG, "filename= " + filename);
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setOutputFile(filename);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialogFragment diaFrag = AlertDialogFragment.newInstance(R.string.cant_prepare, R.string.want_to_continue,
                    R.string.yes, R.string.no, CANT_PREPARE_MEDIA_RECORDER_TAG);
            diaFrag.show(getSupportFragmentManager(), "dialog");
            return;
        }
        AlertDialogFragment diaFrag = AlertDialogFragment.newInstance(R.string.program_will_record,0, R.string.ok , 0, RECORD_AUDIO_WARNING_TAG );
        diaFrag.show(getSupportFragmentManager(), "dialog");
        mMediaRecorder.start();
    }
}
