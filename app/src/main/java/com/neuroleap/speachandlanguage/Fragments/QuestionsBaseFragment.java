package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 4/10/2015.
 */
public class QuestionsBaseFragment extends BaseFragment {

    protected int mQuestionId;
    protected int mScreeningId;
    protected int mPosition;
    protected TextView mTvQuestion;
    protected Button mBtnZero, mBtnOne, mBtnNext;
    protected EditText mEtAnswer;
    protected static final String QUESTION_ID_KEY = "question_id_key";
    protected static final String SCREENING_ID_KEY = "screening_id_key";
    protected static final String POSITION_KEY = "position_key";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestionId = getArguments().getInt(QUESTION_ID_KEY);
        mScreeningId = getArguments().getInt(SCREENING_ID_KEY);
        mPosition = getArguments().getInt(POSITION_KEY);
    }

    protected static Bundle createBundle(Integer questionId, Integer screeningId, Integer position){
        Bundle args = new Bundle();
        args.putInt(QUESTION_ID_KEY, questionId);
        args.putInt (SCREENING_ID_KEY , screeningId);
        args.putInt(POSITION_KEY, position);
        return args;
    }

    protected void setupBaseViews(View v){
        mTvQuestion = (TextView)v.findViewById(R.id.tvQuestion);
        mBtnZero = (Button)v.findViewById(R.id.btnZero);
        mBtnOne = (Button)v.findViewById(R.id.btnOne);
        mBtnNext = (Button)v.findViewById(R.id.btnNext);
        mEtAnswer = (EditText)v.findViewById(R.id.etAnswer);

        Cursor c = DbCRUD.getAnswer(mQuestionId, mScreeningId);
        if (c.getCount() > 0) {
            c.moveToNext();
            mTvQuestion.setText(c.getString(0));
        }
        c.close();

        mBtnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerPresent()) {
                    DbCRUD.enterAnswer(mQuestionId, mScreeningId, mEtAnswer.getText().toString(), false);
                    mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId, mPosition);
                }
            }
        });

        mBtnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerPresent()) {
                    DbCRUD.enterAnswer(mQuestionId, mScreeningId, mEtAnswer.getText().toString(), true);
                    mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId, mPosition);
                }
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId, mPosition);
            }
        });
    }

    protected void setupWindow() {

        switch (Utilities.getTestMode()) {

            case Utilities.SCORING_BUTTONS_ONLY:
                mBtnNext.setVisibility(View.GONE);
                mBtnOne.setVisibility(View.VISIBLE);
                mBtnZero.setVisibility(View.VISIBLE);
                mEtAnswer.setVisibility(View.GONE);
                break;

            case Utilities.TEXT_INPUT_ONLY:
                mBtnNext.setVisibility(View.VISIBLE);
                mBtnOne.setVisibility(View.GONE);
                mBtnZero.setVisibility(View.GONE);
                mEtAnswer.setVisibility(View.VISIBLE);
                break;

            case Utilities.BOTH_SCORING_BUTTONS_AND_TEXT:
                mBtnNext.setVisibility(View.GONE);
                mBtnOne.setVisibility(View.VISIBLE);
                mBtnZero.setVisibility(View.VISIBLE);
                mEtAnswer.setVisibility(View.VISIBLE);
        }
    }

    private boolean answerPresent(){
        if (mEtAnswer.getVisibility() == View.GONE){
            return true;
        }else{
            if (mEtAnswer.getText().toString().equals("")){
                mEtAnswer.setHint("Require answer");
                return false;
            }else {
                return true;
            }
        }
    }
}