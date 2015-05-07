package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.neuroleap.speachandlanguage.Data.ScreeningContract;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 4/6/2015.
 */
public class SemanticsCategoriesFragment extends QuestionsBaseFragment {

    private ImageView mIvPicture;

    public static SemanticsCategoriesFragment newInstance(Integer questionId, Integer screeningId, Integer pageViewerPosition, Integer groupPosition){

        SemanticsCategoriesFragment fragment = new SemanticsCategoriesFragment();
        fragment.setArguments(createBundle(questionId, screeningId, pageViewerPosition, groupPosition));
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.question_one_picture, container, false);

        mCategoryType = ScreeningContract.QuestionCategoriesEntry.SEMANTICS;

        setupBaseViews(v, 2);
        setupWindow();

        mIvPicture = (ImageView)v.findViewById(R.id.ivPicture);

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

        return v;

    }

    @Override
    protected boolean answerCorrect(){
        return false;
    }
}
