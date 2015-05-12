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
public class SemanticsAnalogiesFragment extends QuestionsBaseFragment {
    private ImageView mIvPicture;

    public static SemanticsAnalogiesFragment newInstance(Integer questionId, Integer screeningId, Long screeningCategoryId, Integer pageViewerPosition, Integer groupPosition){

        SemanticsAnalogiesFragment fragment = new SemanticsAnalogiesFragment();
        fragment.setArguments(createBundle(questionId, screeningId, screeningCategoryId, pageViewerPosition, groupPosition));
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.question_one_picture, container, false);

        setupBaseViews(v, 1);
        setupWindow();

        mIvPicture = (ImageView)v.findViewById(R.id.ivPicture);

        Cursor filenameCursor = DbCRUD.getPictureFilenames(mQuestionId);
        filenameCursor.moveToNext();
        String drawableFileName = filenameCursor.getString(0);
        filenameCursor.close();
        int resId = mResources.getIdentifier(drawableFileName, "drawable", mContext.getPackageName());
        mIvPicture.setImageBitmap(Utilities.decodeSampledBitmapFromResource(mResources, resId, 200, 200));        return v;

    }

    @Override
    protected boolean answerCorrect(){
        return false;
    }
}
