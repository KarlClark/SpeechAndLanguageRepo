package com.neuroleap.speachandlanguage.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Adapters.FileNamesArrayAdapter;
import com.neuroleap.speachandlanguage.Adapters.ResultsSummaryArrayAdapter;
import com.neuroleap.speachandlanguage.CustomViews.NonScrollListView;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.*;
import com.neuroleap.speachandlanguage.Listeners.OnAlertDialogListener;
import com.neuroleap.speachandlanguage.Models.ScreeningCategoryResult;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Karl on 5/12/2015.
 */
public class ResultsSummaryFragment extends BaseFragment implements OnAlertDialogListener{
    private int mScreeningId;
    private TextView mTvStudentName, mTvAudioFiles;
    private Button mBtnProfile, mBtnScreenings, mBtnOverview, mBtnQuestions;
    private NonScrollListView mLvResultsSummary, mLvFileNames;
    private String mStudentName;
    private File[] mAudioFiles;
    int mTestMode, mAge;
    private float mTotalCorrectAnswers, mTotalAnswers, mTotalQuestions;
    private ResultsSummaryArrayAdapter mResultsSummaryArrayAdapter;
    private FileNamesArrayAdapter mFileNamesArrayAdapter;
    private ArrayList<ScreeningCategoryResult> mScreeningCategoriesResults = new ArrayList<>();
    private static final String ID_TAG = "id_tag";
    private static final String SCREENING_ID_TAG = "screening_id_tag";
    private static final String STUDENT_NAME_TAG = "student_name_tag";
    private static final int NO_SD_CARD_TAG = 0;

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
        getTestModeAndAge();
        getViews(v);
        mTvStudentName.setText(mStudentName);
        loadLists();
        setAdapters();
        setUpListViewListeners();
        setUpButtonListeners();
        return v;
    }

    private void getTestModeAndAge(){
        Cursor c = DbCRUD.getTestModeAndAge(mScreeningId);
        mTestMode = c.getInt(c.getColumnIndex(ScreeningsEntry.TEST_MODE));
        mAge = c.getInt(c.getColumnIndex(ScreeningsEntry.AGE));
        c.close();
    }

    private void setUpListViewListeners(){
        mLvFileNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(mAudioFiles[position]), "audio/*");
                //i.setData(Uri.fromFile(mAudioFiles[position]));
                startActivity(i);
            }
        });
    }

    private void setAdapters(){
        mResultsSummaryArrayAdapter = new ResultsSummaryArrayAdapter(mContext , mScreeningCategoriesResults);
        mLvResultsSummary.setAdapter(mResultsSummaryArrayAdapter);
        if(mAudioFiles != null){
            mFileNamesArrayAdapter = new FileNamesArrayAdapter(mContext, mAudioFiles);
            mLvFileNames.setAdapter(mFileNamesArrayAdapter);
        }else{
            mTvAudioFiles.setText(mContext.getString(R.string.no_audio_files));
        }
    }

    private void loadLists(){
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

        if (mTestMode == Utilities.BOTH_SCORING_BUTTONS_AND_TEXT &&
               ! Utilities.externalStorageIsReadable()) {
            AlertDialogFragment diaFrag = AlertDialogFragment.newInstance(R.string.audio_unavailable, 0, R.string.ok, 0, NO_SD_CARD_TAG);
        }else {
            String underscoreName = new String(mStudentName);
            underscoreName = underscoreName.replace(" ", "_");
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), getString(R.string.neuro_underscore_leap) + "/" + underscoreName);
            mAudioFiles = mediaStorageDir.listFiles();
        }
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



    private void getTheArguments(){
        mId = getArguments().getInt(ID_TAG);
        mScreeningId = getArguments().getInt(SCREENING_ID_TAG);
        mStudentName = getArguments().getString(STUDENT_NAME_TAG);
    }

    private void getViews(View v){
        mTvStudentName = (TextView)v.findViewById(R.id.tvStudentName);
        mTvAudioFiles = (TextView)v.findViewById(R.id.tvAudioFiles);
        mBtnOverview = (Button)v.findViewById(R.id.btnOverview);
        mBtnProfile = (Button)v.findViewById(R.id.btnProfile);
        mBtnQuestions =(Button)v.findViewById(R.id.btnQuestions);
        mBtnScreenings=(Button)v.findViewById(R.id.btnScreenings);
        mLvResultsSummary = (NonScrollListView)v.findViewById(R.id.lvResultsSummary);
        mLvFileNames = (NonScrollListView)v.findViewById(R.id.lvFileNames);
    }

    @Override
    public void onAlertDialogPositiveClick(int tag){}

    @Override
    public void onAlertDialogNegativeClick(int tag) {}
}
