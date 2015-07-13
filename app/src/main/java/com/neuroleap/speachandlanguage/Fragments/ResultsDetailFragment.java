package com.neuroleap.speachandlanguage.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 7/10/2015.
 */
public class ResultsDetailFragment extends BaseFragment {

    private int mScreeningId, mScreeningCategoryId;
    private TextView mTvStudentName;
    private Button mBtnProfile, mBtnScreenings, mBtnOverview, mBtnQuestions;
    private String mStudentName;
    private static final String ID_TAG = "id_tag";
    private static final String SCREENING_CATEGORY_ID_TAG = "screening_category_id_tag";
    private static final String SCREENING_ID_TAG = "screening_id_tag";
    private static final String STUDENT_NAME_TAG = "student_name_tag";

    private static final String TAG = "## My Info ##";

    public static ResultsDetailFragment newInstance(int id, int screeningCategoryId, int screeningId, String studentName){
        Bundle args = new Bundle();
        args.putInt(ID_TAG, id);
        args.putInt(SCREENING_CATEGORY_ID_TAG, screeningCategoryId);
        args.putInt(SCREENING_ID_TAG, screeningId);
        args.putString(STUDENT_NAME_TAG, studentName);
        ResultsDetailFragment fragment = new ResultsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View topView = inflater.inflate(R.layout.fragment_results_detail_top_view, container, false);
        getTheArguments();
        getViews(topView);
        mTvStudentName.setText(mStudentName);
        setUpButtonListeners();

        return topView;
    }

    private void getTheArguments(){
        mId = getArguments().getInt(ID_TAG);
        mScreeningCategoryId = getArguments().getInt(SCREENING_CATEGORY_ID_TAG);
        mScreeningId = getArguments().getInt(SCREENING_ID_TAG);
        mStudentName = getArguments().getString(STUDENT_NAME_TAG);
    }

    private void getViews(View v){
        mTvStudentName = (TextView)v.findViewById(R.id.tvStudentName);
        mBtnOverview = (Button)v.findViewById(R.id.btnOverview);
        mBtnProfile = (Button)v.findViewById(R.id.btnProfile);
        mBtnQuestions =(Button)v.findViewById(R.id.btnQuestions);
        mBtnScreenings=(Button)v.findViewById(R.id.btnScreenings);
    }

    private void setUpButtonListeners(){

        mBtnScreenings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mId, Utilities.SCREENINGS);
            }
        });

        mBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int studentId = DbCRUD.getStudentId(mScreeningId);
                mOnFragmentInteractionListener.onFragmentInteraction(mId, Utilities.PROFILE, studentId);
            }
        });

        mBtnQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mId, Utilities.QUESTIONS, mScreeningId);
            }
        });

        mBtnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mId, Utilities.OVERVIEW, mScreeningId, mStudentName);
            }
        });
    }
}
