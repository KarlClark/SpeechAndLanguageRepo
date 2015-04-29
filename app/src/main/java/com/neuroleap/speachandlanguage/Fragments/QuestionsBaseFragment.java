package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Adapters.IconAnswersGridViewAdapter;
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
public class QuestionsBaseFragment extends BaseFragment implements OnIconButtonClickedListener {

    protected int mQuestionId;
    protected int mScreeningId;
    protected int mViewPagerPosition;
    protected int mGroupPosition;
    protected int mCategoryType;
    protected int mAnswerNumber = 1;
    protected TextView mTvQuestion;
    protected Button mBtnZero, mBtnOne, mBtnNext;
    protected ArrayList<EditText> mEtAnswers = new ArrayList<EditText>();
    protected ArrayList<TextView> mTvAnswerPrompts = new ArrayList<TextView>();
    protected ArrayList<AnswerIcon> mAnswerIcons = new ArrayList<AnswerIcon>();
    protected ArrayList<PressedIcon> mPressedIcons = new ArrayList<PressedIcon>();
    protected GridView mGvIconAnswers;
    protected IconAnswersGridViewAdapter mIconAnswersGridViewAdapter;
    protected static final String QUESTION_ID_KEY = "question_id_key";
    protected static final String SCREENING_ID_KEY = "screening_id_key";
    protected static final String VIEW_PAGER_POSITION_KEY = "view_pager_position_key";
    protected static final String GROUP_POSITION_KEY = "group_position_key";
    protected static final String ICON_BUTTON_TAG_KEY = "icon_button_tag_key";
    protected static final String TAG = "## My Info ##";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestionId = getArguments().getInt(QUESTION_ID_KEY);
        mScreeningId = getArguments().getInt(SCREENING_ID_KEY);
        mViewPagerPosition = getArguments().getInt(VIEW_PAGER_POSITION_KEY);
        mGroupPosition = getArguments().getInt(GROUP_POSITION_KEY);
    }

    protected static Bundle createBundle(Integer questionId, Integer screeningId, Integer pageViewerPosition, Integer groupPosition){
        Bundle args = new Bundle();
        args.putInt(QUESTION_ID_KEY, questionId);
        args.putInt (SCREENING_ID_KEY , screeningId);
        Log.i(TAG, "QuestionBaseFragment screeningid= " + screeningId);
        args.putInt(VIEW_PAGER_POSITION_KEY, pageViewerPosition);
        args.putInt(GROUP_POSITION_KEY, groupPosition);
        return args;
    }

    protected void setupBaseViews(View v, int numberOfAnswers){
        mTvQuestion = (TextView)v.findViewById(R.id.tvQuestion);
        mBtnZero = (Button)v.findViewById(R.id.btnZero);
        mBtnOne = (Button)v.findViewById(R.id.btnOne);
        mBtnNext = (Button)v.findViewById(R.id.btnNext);
        mEtAnswers.add((EditText)v.findViewById(R.id.etAnswer1));
        mTvAnswerPrompts.add((TextView)v.findViewById(R.id.tvOther1));

        Cursor questionCursor = DbCRUD.getQuestionData(mQuestionId);
        questionCursor.moveToNext();
        mTvQuestion.setText(questionCursor.getString(1));
        questionCursor.close();

        if (numberOfAnswers > 1) {
            mEtAnswers.add((EditText)v.findViewById(R.id.etAnswer2));
            mTvAnswerPrompts.add((TextView)v.findViewById(R.id.tvOther2));
        }
        if(numberOfAnswers > 2){
            mEtAnswers.add((EditText)v.findViewById(R.id.etAnswer3));
            mTvAnswerPrompts.add((TextView)v.findViewById(R.id.tvOther3));
        }

        Cursor c_answer = DbCRUD.getStudentAnswer(mQuestionId, mScreeningId);
        if (c_answer.getCount() > 0) {
            c_answer.moveToNext();
            Cursor c_text = DbCRUD.getStudentAnswersText(c_answer.getLong(0));
            int i=0;
            while (c_text.moveToNext()){
                mEtAnswers.get(i).setText(c_text.getString(1));
                i++;
            }
        }
        c_answer.close();

        mGvIconAnswers =(GridView)v.findViewById(R.id.gvIconAnswers);
        Cursor ic_Cursor = DbCRUD.getIconFilenames(mQuestionId);
        while(ic_Cursor.moveToNext()){
            mAnswerIcons.add(new AnswerIcon(ic_Cursor.getLong(0), ic_Cursor.getString(1)));
        }
        ic_Cursor.close();
        mIconAnswersGridViewAdapter = new IconAnswersGridViewAdapter(mContext,this,mAnswerIcons);
        mGvIconAnswers.setAdapter(mIconAnswersGridViewAdapter);

        mBtnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long answerId = DbCRUD.enterAnswer(mQuestionId, mScreeningId, false, mCategoryType);
                DbCRUD.insertAnswerIconsPressed(answerId, mPressedIcons);
                enterTextAnswers(answerId);
                mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId, mViewPagerPosition, mGroupPosition);
            }
        });

        mBtnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long answerId = DbCRUD.enterAnswer(mQuestionId, mScreeningId, true, mCategoryType);
                DbCRUD.insertAnswerIconsPressed(answerId, mPressedIcons);
                enterTextAnswers(answerId);
                mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId, mViewPagerPosition, mGroupPosition);
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId, mViewPagerPosition, mGroupPosition);
            }
        });
    }

    public void enterTextAnswers(long studentAnswerId){
        ArrayList<StudentAnswerText> sat = new ArrayList<>();
        for (EditText et : mEtAnswers){
            String s = et.getText().toString();
            if ( ! s.equals("")){
                sat.add(new StudentAnswerText(++mAnswerNumber, s));
            }
        }
        if (sat.size() > 0) {
            DbCRUD.insertAnswerText(studentAnswerId,sat);
        }
    }

    protected void setupWindow() {

        for (EditText et : mEtAnswers){
            et.setVisibility(View.VISIBLE);
        }

        switch (Utilities.getTestMode()) {

            case Utilities.TEXT_INPUT_ONLY:
                mBtnNext.setVisibility(View.VISIBLE);
                mBtnOne.setVisibility(View.GONE);
                mBtnZero.setVisibility(View.GONE);
                for(int i = 0; i < mTvAnswerPrompts.size(); i++){
                    mTvAnswerPrompts.get(i).setText("Answer " + (i+1));
                    mTvAnswerPrompts.get(i).setVisibility(View.VISIBLE);
                }
                break;

            case Utilities.BOTH_SCORING_BUTTONS_AND_TEXT:
                mBtnNext.setVisibility(View.GONE);
                mBtnOne.setVisibility(View.VISIBLE);
                mBtnZero.setVisibility(View.VISIBLE);
                for(TextView tv : mTvAnswerPrompts){
                    tv.setVisibility(View.VISIBLE);
                }
        }
    }


    @Override
    public void onIconButtonClicked(long answerIconId){
        mPressedIcons.add(new PressedIcon(answerIconId, mAnswerNumber));
        mAnswerNumber++;
    }
}
