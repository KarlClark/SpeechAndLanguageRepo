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
public class SemanticsAssociationsFragment extends QuestionsBaseFragment {

    private ImageView[] mImageViews = new ImageView[4];
    public static SemanticsAssociationsFragment newInstance(Integer questionId, Integer screeningId, Long screeningCategoryId, Integer pageViewerPosition, Integer groupPosition){

        SemanticsAssociationsFragment fragment = new SemanticsAssociationsFragment();
        fragment.setArguments(createBundle(questionId, screeningId, screeningCategoryId,pageViewerPosition, groupPosition));
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.question_four_pictures, container, false);

        setupBaseViews(v, 1);
        setupWindow();

        mImageViews[0] = (ImageView)v.findViewById(R.id.ivPicture1);
        mImageViews[1] = (ImageView)v.findViewById(R.id.ivPicture2);
        mImageViews[2] = (ImageView)v.findViewById(R.id.ivPicture3);
        mImageViews[3] = (ImageView)v.findViewById(R.id.ivPicture4);

        int i;
        for (i = 0; i < mImageViews.length; i++){
            mImageViews[i].setVisibility(View.GONE);
        }

        Cursor filenameCursor = DbCRUD.getPictureFilenames(mQuestionId);
        i=0;
        while (filenameCursor.moveToNext()) {
            String drawableFileName = filenameCursor.getString(0);
            int resId = mResources.getIdentifier(drawableFileName, "drawable", mContext.getPackageName());
            mImageViews[i].setImageBitmap(Utilities.decodeSampledBitmapFromResource(mResources, resId, 100, 100));
            mImageViews[i].setVisibility(View.VISIBLE);
            i++;
        }
        filenameCursor.close();
        return v;

    }

    @Override
    protected boolean answerCorrect(){
        return false;
    }
}
