package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 4/7/2015.
 */
public class SemanticsPrepositionsFragment extends QuestionsBaseFragment {
    private ImageView mIvPicture;

    public static SemanticsPrepositionsFragment newInstance(Integer questionId, Integer screeningId, Long screeningCategoryId, Integer pageViewerPosition, Integer groupPosition){

        SemanticsPrepositionsFragment fragment = new SemanticsPrepositionsFragment();
        fragment.setArguments(createBundle(questionId, screeningId, screeningCategoryId, pageViewerPosition, groupPosition));
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.question_one_picture, container, false);

        setupBaseViews(v, 1);
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
