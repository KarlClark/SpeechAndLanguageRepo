package com.neuroleap.speachandlanguage.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Adapters.IconAnswersGridViewAdapter;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.*;
import com.neuroleap.speachandlanguage.Listeners.OnIconButtonClickedListener;
import com.neuroleap.speachandlanguage.Models.AnswerIcon;
import com.neuroleap.speachandlanguage.Models.PressedIcon;
import com.neuroleap.speachandlanguage.Models.StudentAnswerText;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.util.ArrayList;

/**
 * Created by Karl on 4/10/2015.
 */
public abstract class QuestionsBaseFragment extends BaseFragment implements OnIconButtonClickedListener {

    protected int mQuestionId;
    protected int mScreeningId;
    protected int mViewPagerPosition;
    protected int mGroupPosition;
    //protected int mCategoryType;
    protected long mScreeningCategoryId;
    protected int mAnswerNumber = 1;
    protected boolean mCommitted=false;
    private boolean mNeedAnswerText= false;
    protected TextView mTvQuestion;
    protected Button mBtnZero, mBtnOne, mBtnNext, mBtnScreenings, mBtnResults, mBtnOverview;
    protected ArrayList<EditText> mEtAnswers = new ArrayList<EditText>();
    protected ArrayList<TextView> mTvAnswerPrompts = new ArrayList<TextView>();
    protected ArrayList<AnswerIcon> mAnswerIcons = new ArrayList<AnswerIcon>();
    //protected ArrayList<Long> mPressedIconIds = new ArrayList<>();
    protected ArrayList<PressedIcon> mPressedIcons = new ArrayList<PressedIcon>();
    protected ArrayList<PressedIcon> mOriginalPressedIcons = new ArrayList<PressedIcon>();
    protected GridView mGvIconAnswers;
    protected boolean[] mOriginalClicked = new boolean[20];
    protected String[] mOriginalTextAnswers = new String[] {"", "", ""};
    protected int[] mOriginalTextAnswerNumbers = new int[3];
    //protected long[] mOriginalPressedIconIds = new long [20];
    //protected int mOriginalPressedIconIdsCount = 0;
    protected IconAnswersGridViewAdapter mIconAnswersGridViewAdapter;
    protected Resources mResources;
    protected static final String QUESTION_ID_KEY = "question_id_key";
    protected static final String SCREENING_ID_KEY = "screening_id_key";
    protected static final String SCREENING_CATEGORY_ID_KEY = "screening_category_id_key";
    protected static final String VIEW_PAGER_POSITION_KEY = "view_pager_position_key";
    protected static final String GROUP_POSITION_KEY = "group_position_key";
    protected static final String ICON_BUTTON_TAG_KEY = "icon_button_tag_key";
    public static final int SHOW_NEXT_FRAGMENT = 0;
    public static final int OVERVIEW_BUTTON_CLICKED  = 1;
    public static final int RESULTS_BUTTON_CLICKED = 2;
    public static final int SCREENINGS_BUTTON_CLICKED = 3;
    protected static final String TAG = "## My Info ##";

    protected abstract boolean answerCorrect();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuestionId = getArguments().getInt(QUESTION_ID_KEY);
        //Log.i(TAG,"fragment for question " + mQuestionId + "  onCreate called");
        mScreeningId = getArguments().getInt(SCREENING_ID_KEY);
        mScreeningCategoryId = getArguments().getLong(SCREENING_CATEGORY_ID_KEY);
        mViewPagerPosition = getArguments().getInt(VIEW_PAGER_POSITION_KEY);
        mGroupPosition = getArguments().getInt(GROUP_POSITION_KEY);
        mResources = getResources();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Fragment for question " + mQuestionId+" onResume Called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "Fragment for question " + mQuestionId+" onPause Called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Fragment for question " + mQuestionId+" onDestroy Called");
        if (mIconAnswersGridViewAdapter != null) {
            mIconAnswersGridViewAdapter.nullOutListener();
            mIconAnswersGridViewAdapter.notifyDataSetInvalidated();
            mIconAnswersGridViewAdapter = null;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //Log.i(TAG, "Fragment for question " + mQuestionId +" is visible= " + isVisibleToUser +"  mCommitted= " + mCommitted + "   mIconGridViewAdapter= " + mIconAnswersGridViewAdapter
         //        + "  mEtAnswers size= " + mEtAnswers.size());
        if ( ! isVisibleToUser) {
            if ( ! mCommitted)  {// The user swiped the screen without committing an answer so
                                 // set things back to the way they were before user interacted with UI
                for(int i =0; i < mEtAnswers.size(); i++){
                    mEtAnswers.get(i).setText(mOriginalTextAnswers[i]);
                    mEtAnswers.get(i).setTag(mOriginalTextAnswerNumbers[i]);
                }

                for (int i = 0; i < mAnswerIcons.size(); i++) {
                    //Log.i(TAG,"i= " + i + "  mOriginal click= " + mOriginalClicked[i]);
                    mAnswerIcons.get(i).setClicked(mOriginalClicked[i]);
                }

                mPressedIcons.clear();
                for (PressedIcon opi : mOriginalPressedIcons){
                    mPressedIcons.add(opi);
                }

                if (mIconAnswersGridViewAdapter != null) {
                    //Log.i(TAG, "Calling notifyDataSetChanged for question " + mQuestionId);
                    mIconAnswersGridViewAdapter.notifyDataSetChanged();
                }
            }
            mCommitted = false;
        }
    }

    protected static Bundle createBundle(Integer questionId, Integer screeningId, Long screeningCategoryId, Integer pageViewerPosition, Integer groupPosition){
        // Subclass will call this from the newInstance method to create the bundle to pass data to fragment instance.
        Bundle args = new Bundle();
        args.putInt(QUESTION_ID_KEY, questionId);
        args.putInt (SCREENING_ID_KEY , screeningId);
        args.putLong(SCREENING_CATEGORY_ID_KEY, screeningCategoryId);
        //Log.i(TAG, "QuestionBaseFragment screeningid= " + screeningId);
        args.putInt(VIEW_PAGER_POSITION_KEY, pageViewerPosition);
        args.putInt(GROUP_POSITION_KEY, groupPosition);
        return args;
    }

    protected void setupBaseViews(View v, int numberOfAnswers){
        mTvQuestion = (TextView)v.findViewById(R.id.tvQuestion);
        mBtnZero = (Button)v.findViewById(R.id.btnZero);
        mBtnOne = (Button)v.findViewById(R.id.btnOne);
        mBtnNext = (Button)v.findViewById(R.id.btnNext);
        mBtnOverview = (Button)v.findViewById(R.id.btnOverview);
        mBtnResults = (Button)v.findViewById(R.id.btnResults);
        mBtnScreenings = (Button)v.findViewById(R.id.btnScreenings);

        //Get question from database and put it in text view.
        Cursor questionCursor = DbCRUD.getQuestionData(mQuestionId);
        questionCursor.moveToNext();
        if (Utilities.getAppLanguage() == Utilities.SPANISH || Utilities.getQuestionsLanguage() == Utilities.SPANISH){
            mTvQuestion.setText(questionCursor.getString(questionCursor.getColumnIndex(QuestionsEntry.TEXT_SPANISH)));
        }else {
            mTvQuestion.setText(questionCursor.getString(questionCursor.getColumnIndex(QuestionsEntry.TEXT_ENGLISH)));
        }
        questionCursor.close();

        //All questions have at least one view for a text answer.
        mEtAnswers.add((EditText) v.findViewById(R.id.etAnswer1));
        mTvAnswerPrompts.add((TextView) v.findViewById(R.id.tvOther1));
        mEtAnswers.get(0).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0){
                    mEtAnswers.get(0).setTag(null);
                }else{
                    if(s.length() == 1){
                        mEtAnswers.get(0).setTag(mAnswerNumber++);
                    }
                }
            }
        });

        //Add extra text answer fields if question has more than one answer
        if (numberOfAnswers > 1) {
            mEtAnswers.add((EditText)v.findViewById(R.id.etAnswer2));
            mTvAnswerPrompts.add((TextView)v.findViewById(R.id.tvOther2));
            mEtAnswers.get(1).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0){
                        mEtAnswers.get(1).setTag(null);
                    }else{
                        if(s.length() == 1){
                            mEtAnswers.get(1).setTag(mAnswerNumber++);
                        }
                    }
                }
            });
        }
        if(numberOfAnswers > 2){
            mEtAnswers.add((EditText)v.findViewById(R.id.etAnswer3));
            mTvAnswerPrompts.add((TextView)v.findViewById(R.id.tvOther3));
            mEtAnswers.get(2).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0){
                        mEtAnswers.get(2).setTag(null);
                    }else{
                        if(s.length() == 1){
                            mEtAnswers.get(2).setTag(mAnswerNumber++);
                        }
                    }
                }
            });
        }

        //If user has gone back to a previously answered question fill in the
        //text he previously entered and make a list of the icon answer buttons
        //he pressed.
        Cursor c_answer = DbCRUD.getStudentAnswer(mQuestionId, mScreeningId);
        if (c_answer.getCount() > 0) {

            c_answer.moveToNext();
            long studentAnswersId = c_answer.getLong(c_answer.getColumnIndex(StudentAnswersEntry._ID));

            Cursor c_text = DbCRUD.getStudentAnswersText(studentAnswersId);
            int i=0;
            while (c_text.moveToNext()){
                String answerText = c_text.getString(c_text.getColumnIndex(StudentAnswersTextEntry.TEXT));
                int answerNumber = c_text.getInt(c_text.getColumnIndex(StudentAnswersTextEntry.ANSWER_NUMBER));
                mEtAnswers.get(i).setText(answerText);
                mEtAnswers.get(i).setTag(answerNumber);
                mOriginalTextAnswers[i]  = answerText;
                mOriginalTextAnswerNumbers[i] = answerNumber;
                if (mAnswerNumber <= answerNumber){
                    mAnswerNumber = answerNumber + 1;
                }
                i++;
            }
            c_text.close();

            Cursor c_answerIcons = DbCRUD.getStudentAnswersIcons(studentAnswersId);
            //Log.i(TAG,"c_answersIcons size= " + c_answerIcons.getCount());
            while (c_answerIcons.moveToNext()){
                Log.i(TAG, "answerIconsId= " + c_answerIcons.getLong(1));
                long answerIconId = c_answerIcons.getLong(c_answerIcons.getColumnIndex(AnswerButtonsPressedEntry.ANSWER_ICONS_ID));
                int answerNumber = c_answerIcons.getInt(c_answerIcons.getColumnIndex(AnswerButtonsPressedEntry.ANSWER_NUMBER));
                mPressedIcons.add(new PressedIcon(answerIconId , answerNumber));
                mOriginalPressedIcons.add(new PressedIcon(answerIconId , answerNumber));
                if (mAnswerNumber <= answerNumber){
                    mAnswerNumber = answerNumber + 1;
                }
            }
            c_answerIcons.close();
        }
        c_answer.close();

        mGvIconAnswers =(GridView)v.findViewById(R.id.gvIconAnswers);
        Cursor ic_Cursor = DbCRUD.getIconFilenames(mQuestionId);
        AnswerIcon ai;
        if (ic_Cursor.getCount() > 0 && Utilities.getTestMode() == Utilities.BOTH_SCORING_BUTTONS_AND_TEXT) {
            // Need answer icon buttons.  Make a list of AnswerIcon models to use with the grid view adapter.
            while (ic_Cursor.moveToNext()) {
                ai = new AnswerIcon(ic_Cursor.getLong(ic_Cursor.getColumnIndex(AnswerIconEntry._ID)),
                        ic_Cursor.getString(ic_Cursor.getColumnIndex(AnswerIconEntry.FILENAME)));
                if (idIsaPressedIcon(ai.getAnswerIconId())) {
                    //Log.i(TAG, "setting clicked true for id " + ai.getAnswerIconId());
                    ai.setClicked(true);
                }
                mAnswerIcons.add(ai);
            }
            ic_Cursor.close();
            for (int i = 0; i < mAnswerIcons.size(); i++) {
                mOriginalClicked[i] = mAnswerIcons.get(i).isClicked();
            }

            mIconAnswersGridViewAdapter = new IconAnswersGridViewAdapter(mContext, this, mAnswerIcons);
            mGvIconAnswers.setAdapter(mIconAnswersGridViewAdapter);
            mEtAnswers.get(0).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Log.i(TAG, "Hide keyboard");
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mEtAnswers.get(0).getWindowToken(), 0);
                    }
                }
            });
        }else{
            //In this mode we don't display icon answer button, so hide the gridview frame. Answers
            // have to be entered manually, so raise the virtual keyboard.
            mGvIconAnswers.setBackgroundResource(0);
            mNeedAnswerText = true;
            mEtAnswers.get(0).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(mEtAnswers.get(0), InputMethodManager.SHOW_FORCED);
                    }
                }
            });
        }

        //If the user presses any of these buttons, then enter his answer/answers into the database.
        mBtnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long answerId = DbCRUD.enterAnswer(mQuestionId, mScreeningId, false, mScreeningCategoryId);
                enterIconAnswers(answerId);
                enterTextAnswers(answerId);
                mCommitted = true;
                mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId, SHOW_NEXT_FRAGMENT, mViewPagerPosition, mGroupPosition, false);
            }
        });

        mBtnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long answerId = DbCRUD.enterAnswer(mQuestionId, mScreeningId, true, mScreeningCategoryId);
                enterIconAnswers(answerId);
                enterTextAnswers(answerId);
                mCommitted=true;
                mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId, SHOW_NEXT_FRAGMENT, mViewPagerPosition, mGroupPosition, true);
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrect = answerCorrect();
                long answerId = DbCRUD.enterAnswer(mQuestionId, mScreeningId, isCorrect, mScreeningCategoryId);
                enterTextAnswers(answerId);
                mCommitted=true;
                mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId, SHOW_NEXT_FRAGMENT, mViewPagerPosition, mGroupPosition, isCorrect);
            }
        });

        mBtnScreenings.setOnClickListener(new View.OnClickListener() { //Go back to screenings page
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId, SCREENINGS_BUTTON_CLICKED);
            }
        });

        mBtnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Go to results page
                mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId, RESULTS_BUTTON_CLICKED);
            }
        });

        mBtnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Go to Overview page
                mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId, OVERVIEW_BUTTON_CLICKED);
            }
        });
    }

    private boolean idIsaPressedIcon(long iconId){
        //Search the list of pressed icons to see if this icon is in it.
        for (PressedIcon pi : mPressedIcons){
            if (pi.getAnswerIconId() == iconId){
                return true;
            }
        }
        return false;
    }

    public void enterIconAnswers(long studentAnswerId){
        DbCRUD.insertAnswerIconsPressed(studentAnswerId, mPressedIcons);
    }

    public void enterTextAnswers(long studentAnswerId){
        //Enter any text answers into the data base.
        ArrayList<StudentAnswerText> sat = new ArrayList<>();
        for (EditText et : mEtAnswers){
            String s = et.getText().toString();
            if ( ! s.equals("")){
                sat.add(new StudentAnswerText((Integer)et.getTag(), s));
            }
        }
        if (sat.size() > 0) {
            DbCRUD.insertAnswerText(studentAnswerId,sat);
        }
    }

    protected void setupWindow() {
        //Different views are displayed depending on answer mode and number of answers
        for (EditText et : mEtAnswers){
            et.setVisibility(View.VISIBLE);
        }

        switch (Utilities.getTestMode()) {

            case Utilities.TEXT_INPUT_ONLY:
                mBtnNext.setVisibility(View.VISIBLE);
                mBtnOne.setVisibility(View.GONE);
                mBtnZero.setVisibility(View.GONE);
                mGvIconAnswers.setVisibility(View.GONE);
                // If one answer field label it Answer otherwise label them Answer 1, Answer 2 etc.
                if (mTvAnswerPrompts.size() == 1){
                    mTvAnswerPrompts.get(0).setText(mContext.getString(R.string.answer));
                } else {
                    for (int i = 0; i < mTvAnswerPrompts.size(); i++) {
                        mTvAnswerPrompts.get(i).setText(mContext.getString(R.string.answer) + (i + 1));
                        mTvAnswerPrompts.get(i).setVisibility(View.VISIBLE);
                    }
                }

                break;

            case Utilities.BOTH_SCORING_BUTTONS_AND_TEXT:
                mBtnNext.setVisibility(View.GONE);
                mBtnOne.setVisibility(View.VISIBLE);
                mBtnZero.setVisibility(View.VISIBLE);
                mGvIconAnswers.setVisibility(View.VISIBLE);
                if(mNeedAnswerText) {
                    if (mTvAnswerPrompts.size() == 1){
                        mTvAnswerPrompts.get(0).setText(mContext.getString(R.string.answer));
                    } else {
                        for (int i = 0; i < mTvAnswerPrompts.size(); i++) {
                            mTvAnswerPrompts.get(i).setText(mContext.getString(R.string.answer) + (i + 1));
                            mTvAnswerPrompts.get(i).setVisibility(View.VISIBLE);
                        }
                    }
                }else {
                    for (TextView tv : mTvAnswerPrompts) {
                        tv.setVisibility(View.VISIBLE);
                    }
                }
        }
    }


    @Override
    public void onIconButtonClicked(AnswerIcon answerIcon){
        // If an icon has been pressed then add it to the list. If an
        //already pressed icons has been re-pressed then delete it from the list.
        if (answerIcon.isClicked()){
            mPressedIcons.add(new PressedIcon(answerIcon.getAnswerIconId() , mAnswerNumber++));
        }else {
            deleteIdFromList(answerIcon.getAnswerIconId());
        }
    }

    private void deleteIdFromList(long answerIconId){
        for (int i = 0; i < mPressedIcons.size(); i++){
            if (mPressedIcons.get(i).getAnswerIconId() == answerIconId){
                mPressedIcons.remove(i);
                return;
            }
        }
    }
}
