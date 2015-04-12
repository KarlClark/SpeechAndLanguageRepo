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

/**
 * Created by Karl on 4/6/2015.
 */
public class SemanticsBodyPartsFragment extends QuestionsBaseFragment {


    private ImageView mIvPicture;



    public static SemanticsBodyPartsFragment newInstance(Integer questionId, Integer screeningId, Integer position){

        SemanticsBodyPartsFragment fragment = new SemanticsBodyPartsFragment();
        fragment.setArguments(createBundle(questionId, screeningId, position));
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_semantics_body_parts, container, false);
        setupBaseViews(v);
        setupWindow();

        mIvPicture = (ImageView)v.findViewById(R.id.ivPicture);
        Cursor questionCursor = DbCRUD.getQuestionData(mQuestionId);
        questionCursor.moveToNext();
        mTvQuestion.setText(questionCursor.getString(1));
        questionCursor.close();
        Cursor filenameCursor = DbCRUD.getPictureFilenames(mQuestionId);
        filenameCursor.moveToNext();
        String drawableFileName = filenameCursor.getString(0);
        filenameCursor.close();
        int resId = getResources().getIdentifier(drawableFileName, "drawable", mContext.getPackageName());
        mIvPicture.setImageResource(resId);

        return v;

    }
}
