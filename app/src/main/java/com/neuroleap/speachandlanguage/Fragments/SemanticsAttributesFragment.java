package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.neuroleap.speachandlanguage.Data.ScreeningContract;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 4/7/2015.
 */
public class SemanticsAttributesFragment extends  QuestionsBaseFragment {

    private ImageView mIvPicture;
    private boolean mFirstTime= true;

    public static SemanticsAttributesFragment newInstance(Integer questionId, Integer screeningId, Integer pageViewerPosition, Integer groupPosition){

        SemanticsAttributesFragment fragment = new SemanticsAttributesFragment();
        fragment.setArguments(createBundle(questionId, screeningId, pageViewerPosition, groupPosition));
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.question_one_picture, container, false);

        mCategoryType = ScreeningContract.QuestionCategoriesEntry.SEMANTICS;

        setupBaseViews(v, 3);
        setupWindow();

        mIvPicture = (ImageView)v.findViewById(R.id.ivPicture);
        mGvIconAnswers.setVisibility(View.GONE);
        for (int i = 0; i < mTvAnswerPrompts.size(); i++) {
            mTvAnswerPrompts.get(i).setText(mContext.getString(R.string.answer) + (i + 1));
        }


        if (Utilities.getTestMode() == Utilities.TEXT_INPUT_ONLY){
            mIvPicture.setVisibility(View.GONE);
        }else {
            Cursor filenameCursor = DbCRUD.getPictureFilenames(mQuestionId);
            filenameCursor.moveToNext();
            String drawableFileName = filenameCursor.getString(0);
            filenameCursor.close();
            int resId = getResources().getIdentifier(drawableFileName, "drawable", mContext.getPackageName());
            mIvPicture.setImageResource(resId);
        }

        /*mEtAnswers.get(0).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mEtAnswers.get(0), InputMethodManager.SHOW_FORCED);
                }
            }
        });*/
        return v;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG,"mEtAnswers size= " + mEtAnswers.size() +"  $$$$$$$$$$$$$$$$$$$$$$$");
        if (isVisibleToUser){
            //mEtAnswers.get(0).performClick();
        }
    }

    @Override
    protected boolean answerCorrect(){
        return false;
    }
}
