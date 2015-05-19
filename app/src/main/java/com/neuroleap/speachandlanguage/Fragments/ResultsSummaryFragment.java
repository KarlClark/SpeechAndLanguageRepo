package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Adapters.ResultsSummaryArrayAdapter;
import com.neuroleap.speachandlanguage.Models.ScreeningCategoryResult;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.util.ArrayList;

/**
 * Created by Karl on 5/12/2015.
 */
public class ResultsSummaryFragment extends BaseFragment {
    private int mScreeningId;
    private TextView mTvStudentName;
    private Button mBtnProfile, mBtnScreenings, mBtnOverview, mBtnQuestions;
    private ListView mLvResultsSummary;
    private String mStudentName;
    private float mTotalCorrectAnswers, mTotalAnswers, mTotalQuestions;
    private ResultsSummaryArrayAdapter mResultsSummaryArrayAdapter;
    private ArrayList<ScreeningCategoryResult> mScreeningCategoriesResults = new ArrayList<>();
    private static final String ID_TAG = "id_tag";
    private static final String SCREENING_ID_TAG = "screening_id_tag";
    private static final String STUDENT_NAME_TAG = "student_name_tag";

    private static final String TAG = "## My Info ##";

    public static ResultsSummaryFragment newInstance(int id, int screeningId, String studentName){
        Bundle args = new Bundle();
        args.putInt(ID_TAG, id);
        args.putInt(SCREENING_ID_TAG, screeningId);
        args.putString(STUDENT_NAME_TAG, studentName);
        ResultsSummaryFragment fragment = new ResultsSummaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_results_summary, container, false);
        getTheArguments();
        getViews(v);
        mTvStudentName.setText(mStudentName);
        loadList();
        mResultsSummaryArrayAdapter = new ResultsSummaryArrayAdapter(mContext , mScreeningCategoriesResults);
        mLvResultsSummary.setAdapter(mResultsSummaryArrayAdapter);
        setUpButtons();
        return v;
    }

    private void loadList(){
        Cursor screeningCategoriesCursor = DbCRUD.getScreeningCategories();
        while (screeningCategoriesCursor.moveToNext()){
            int screeningCategoryId = screeningCategoriesCursor.getInt(0);
            Cursor studentAnswerCursor = DbCRUD.getStudentAnswersForScreeningCategoryId(mScreeningId, screeningCategoryId);
            float rightAnswers = 0;
            int totalAnswers= 0;
            boolean isCompleted= false;
            while (studentAnswerCursor.moveToNext()){
                if (studentAnswerCursor.getInt(0) == 1){
                    rightAnswers++;
                    mTotalCorrectAnswers++;
                }
                totalAnswers++;
                mTotalAnswers++;
            }
            studentAnswerCursor.close();
            int totalQuestions = DbCRUD.getNumberOfQuestionsForScreeningCategoryId(screeningCategoryId);
            mTotalQuestions += totalQuestions;
            isCompleted = totalAnswers == totalQuestions;
            ScreeningCategoryResult scr = new ScreeningCategoryResult(screeningCategoriesCursor.getString(1),
                    screeningCategoriesCursor.getString(2), isCompleted, rightAnswers, totalQuestions);
            mScreeningCategoriesResults.add(scr);
        }
        screeningCategoriesCursor.close();
        boolean totallyCompleted = mTotalAnswers == mTotalQuestions;
        ScreeningCategoryResult scr = new ScreeningCategoryResult(getString(R.string.total), getString(R.string.total),
                totallyCompleted, mTotalAnswers, mTotalQuestions);
        mScreeningCategoriesResults.add(scr);
    }

    private void setUpButtons(){

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



    private void getTheArguments(){
        mId = getArguments().getInt(ID_TAG);
        mScreeningId = getArguments().getInt(SCREENING_ID_TAG);
        mStudentName = getArguments().getString(STUDENT_NAME_TAG);
    }

    private void getViews(View v){
        mTvStudentName = (TextView)v.findViewById(R.id.tvStudentName);
        mBtnOverview = (Button)v.findViewById(R.id.btnOverview);
        mBtnProfile = (Button)v.findViewById(R.id.btnProfile);
        mBtnQuestions =(Button)v.findViewById(R.id.btnQuestions);
        mBtnScreenings=(Button)v.findViewById(R.id.btnScreenings);
        mLvResultsSummary = (ListView)v.findViewById(R.id.lvResultsSummary);
    }
}
