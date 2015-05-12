package com.neuroleap.speachandlanguage.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.neuroleap.speachandlanguage.R;

/**
 * Created by Karl on 4/7/2015.
 */
public class SentenceRepetitionFragment extends  QuestionsBaseFragment {

    private ImageView mIvPicture;

    public static SentenceRepetitionFragment newInstance(Integer questionId, Integer screeningId, Long screeningCategoryId, Integer pageViewerPosition, Integer groupPosition){

        SentenceRepetitionFragment fragment = new SentenceRepetitionFragment();
        fragment.setArguments(createBundle(questionId, screeningId, screeningCategoryId, pageViewerPosition, groupPosition));
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.question_one_picture, container, false);

        setupBaseViews(v, 1);
        setupWindow();

        mIvPicture = (ImageView)v.findViewById(R.id.ivPicture);
        mIvPicture.setVisibility(View.GONE);

        return v;

    }

    @Override
    protected boolean answerCorrect(){
        return false;
    }
}
