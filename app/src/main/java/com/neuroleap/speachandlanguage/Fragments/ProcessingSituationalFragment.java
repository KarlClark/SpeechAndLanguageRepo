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
 * Created by Karl on 4/7/2015.
 */
public class ProcessingSituationalFragment extends  QuestionsBaseFragment{

    private ImageView mIvPicture;

    public static ProcessingSituationalFragment newInstance(Integer questionId, Integer screeningId, Integer pageViewerPosition, Integer groupPosition){

        ProcessingSituationalFragment fragment = new ProcessingSituationalFragment();
        fragment.setArguments(createBundle(questionId, screeningId, pageViewerPosition, groupPosition));
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.question_one_picture, container, false);
        mCategoryType = ScreeningContract.QuestionCategoriesEntry.PROCESSING;

        setupBaseViews(v, 1);
        setupWindow();

        mIvPicture = (ImageView)v.findViewById(R.id.ivPicture);

        Cursor filenameCursor = DbCRUD.getPictureFilenames(mQuestionId);
        if (filenameCursor.getCount() == 0){
            mIvPicture.setVisibility(View.GONE);
        }else {
            filenameCursor.moveToNext();
            String drawableFileName = filenameCursor.getString(0);
            int resId = mResources.getIdentifier(drawableFileName, "drawable", mContext.getPackageName());
            mIvPicture.setImageBitmap(Utilities.decodeSampledBitmapFromResource(mResources, resId, 200, 200));
        }
        filenameCursor.close();

        return v;

    }
    @Override
    protected boolean answerCorrect(){
        return false;
    }
}
