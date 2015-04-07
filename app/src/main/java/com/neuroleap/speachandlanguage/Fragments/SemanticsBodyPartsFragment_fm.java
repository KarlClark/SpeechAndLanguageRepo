package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;

/**
 * Created by Karl on 4/6/2015.
 */
public class SemanticsBodyPartsFragment_fm extends BaseFragment {

    TextView mTvQuestion;
    Button mBtnNext;

    public static SemanticsBodyPartsFragment_fm newInstance(Integer questionId){
        Bundle args = new Bundle();
        //args.putInt(ID_KEY, id);
        args.putInt(QUESTION_ID_KEY, questionId);

        SemanticsBodyPartsFragment_fm fragment = new SemanticsBodyPartsFragment_fm();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getArguments().getInt(ID_KEY);
        mQuestionId = getArguments().getInt(QUESTION_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_fragment, container, false);

        mTvQuestion = (TextView)v.findViewById(R.id.tvTestQuestion);
        mBtnNext = (Button)v.findViewById(R.id.btnTestNext);
        Cursor c = DbCRUD.getQuestionData(mQuestionId);
        c.moveToNext();
        mTvQuestion.setText(c.getString(1));
        c.close();
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mQuestionId);
            }
        });
        return v;

    }
}
