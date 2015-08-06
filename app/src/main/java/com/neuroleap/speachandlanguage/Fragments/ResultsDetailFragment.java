package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Data.ScreeningContract.AnswerButtonsPressedEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionCategoriesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentAnswersEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentAnswersTextEntry;
import com.neuroleap.speachandlanguage.Models.StudentDisplayAnswer;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Karl on 7/10/2015.
 */
public class ResultsDetailFragment extends BaseFragment {

    private int mScreeningId, mScreeningCategoryId;
    private TextView mTvStudentName, mTvResultsDetail;
    private Button mBtnProfile, mBtnScreenings, mBtnOverview, mBtnQuestions;
    private LinearLayout mLlCategoryLevel;
    private String mStudentName;
    private ArrayList<StudentDisplayAnswer> mStudentDisplayAnswers = new ArrayList<>();
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
        setScreenTitle();
        setUpButtonListeners();
        fillOutScreen(inflater);

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
        mTvResultsDetail = (TextView)v.findViewById(R.id.tvResultsDetail);
        mBtnOverview = (Button)v.findViewById(R.id.btnOverview);
        mBtnProfile = (Button)v.findViewById(R.id.btnProfile);
        mBtnQuestions =(Button)v.findViewById(R.id.btnQuestions);
        mBtnScreenings=(Button)v.findViewById(R.id.btnScreenings);
        mLlCategoryLevel = (LinearLayout)v.findViewById(R.id.llCategoryLevel);
    }

    private void setScreenTitle(){
        Cursor c = DbCRUD.getScreeningCategory(mScreeningCategoryId);
        c.moveToNext();
        String categoryName = c.getString(3);  // 3 is category name in either English or Spanish
        c.close();
        mTvResultsDetail.setText(getString(R.string.results_detail) +": " + categoryName);
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

    private void fillOutScreen(LayoutInflater inflater){
        Cursor c_questionCategories = DbCRUD.getQuestionCategories(mScreeningCategoryId);
        while(c_questionCategories.moveToNext()){
            View categoryView = inflater.inflate(R.layout.fragment_results_detail_category_view, null);
            TextView tvCategory = (TextView)categoryView.findViewById(R.id.tvCategory);
            LinearLayout llQuestion = (LinearLayout)categoryView.findViewById(R.id.llQuestion);
            tvCategory.setText(c_questionCategories.getString(1)); // 1 will be category in either English or Spanish
            Cursor c_questions = DbCRUD.getQuestionsForQuestionCategory(c_questionCategories.getLong(c_questionCategories.getColumnIndex(QuestionCategoriesEntry._ID)));
            int i = 0;
            while (c_questions.moveToNext()){
                View questionView = inflater.inflate(R.layout.fragment_results_detail_questions, null);
                TextView tvQuestion = (TextView)questionView.findViewById(R.id.tvQuestion);
                TextView tvScore = (TextView)questionView.findViewById(R.id.tvScore);
                String q = c_questions.getString(1);  // 1 will be question in either English or Spanish
                if (  c_questions.getString(2) != null){  // 2 will be unique text in either English or Spanish
                    q = q + "  " + c_questions.getString(2);
                }
                tvQuestion.setText(q);
                mStudentDisplayAnswers.clear();
                Cursor c_studentAnswer = DbCRUD.getStudentAnswer(c_questions.getLong(c_questions.getColumnIndex(QuestionsEntry._ID)), mScreeningId);
                if (c_studentAnswer.getCount() == 0) {
                    tvScore.setText("");
                    mStudentDisplayAnswers.add(new StudentDisplayAnswer());
                }else {
                    c_studentAnswer.moveToNext();
                    tvScore.setText(c_studentAnswer.getString(c_studentAnswer.getColumnIndex(StudentAnswersEntry.CORRECT)));


                    Cursor c_answerIcons = DbCRUD.getStudentAnswersIcons(c_studentAnswer.getLong(c_studentAnswer.getColumnIndex(StudentAnswersEntry._ID)));
                    while (c_answerIcons.moveToNext()) {
                        mStudentDisplayAnswers.add(new StudentDisplayAnswer(c_answerIcons.getLong(c_answerIcons.getColumnIndex(AnswerButtonsPressedEntry.ANSWER_ICONS_ID)),
                                c_answerIcons.getInt(c_answerIcons.getColumnIndex(AnswerButtonsPressedEntry.ANSWER_NUMBER))));
                    }
                    c_answerIcons.close();

                    Cursor c_answersText = DbCRUD.getStudentAnswersText(c_studentAnswer.getLong(c_studentAnswer.getColumnIndex(StudentAnswersEntry._ID)));
                    while (c_answersText.moveToNext()) {
                        mStudentDisplayAnswers.add(new StudentDisplayAnswer(c_answersText.getString(c_answersText.getColumnIndex(StudentAnswersTextEntry.TEXT)),
                                c_answersText.getInt(c_answersText.getColumnIndex(StudentAnswersTextEntry.ANSWER_NUMBER))));
                    }
                    c_answersText.close();

                    Collections.sort(mStudentDisplayAnswers);
                }

                LinearLayout llAnswer =(LinearLayout)questionView.findViewById(R.id.llAnswer);
                for (StudentDisplayAnswer sda : mStudentDisplayAnswers) {
                    View answerView = inflater.inflate(R.layout.fragment_results_detail_answers, null);
                    TextView tvAnswerType = (TextView)answerView.findViewById(R.id.tvAnswerType);
                    TextView tvAnswerText = (TextView)answerView.findViewById(R.id.tvAnswerText);
                    if (sda.getAnswerType() == StudentDisplayAnswer.ICON) {
                        tvAnswerType.setText(mContext.getString(R.string.icon));
                    }else{
                        if (sda.getAnswerType() == StudentDisplayAnswer.TEXT){
                            tvAnswerType.setText(mContext.getString(R.string.text));
                        }else{
                            tvAnswerType.setText("");
                        }
                    }
                    tvAnswerText.setText(sda.getAnswerText());
                    llAnswer.addView(answerView);
                }
                if ( i % 2 == 1) {
                    questionView.setBackgroundColor(getResources().getColor(R.color.light_grey));
                }
                i++;
                llQuestion.addView(questionView);
                c_studentAnswer.close();
            }
            c_questions.close();
            mLlCategoryLevel.addView(categoryView);
        }
        c_questionCategories.close();
    }
}
