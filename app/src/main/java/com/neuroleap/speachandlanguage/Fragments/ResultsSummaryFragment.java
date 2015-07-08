package com.neuroleap.speachandlanguage.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Adapters.FileNamesArrayAdapter;
import com.neuroleap.speachandlanguage.Adapters.ResultsSummaryArrayAdapter;
import com.neuroleap.speachandlanguage.CustomViews.BarChart;
import com.neuroleap.speachandlanguage.CustomViews.NonScrollListView;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ScreeningCategoriesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ScreeningsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentAnswersEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentsEntry;
import com.neuroleap.speachandlanguage.Listeners.OnAlertDialogListener;
import com.neuroleap.speachandlanguage.Models.ScreeningCategoryResult;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Karl on 5/12/2015.
 */
public class ResultsSummaryFragment extends BaseFragment implements OnAlertDialogListener{
    private int mScreeningId;
    private TextView mTvStudentName, mTvAudioFiles;
    private Button mBtnProfile, mBtnScreenings, mBtnOverview, mBtnQuestions, mBtnEmailReport;
    private NonScrollListView mLvResultsSummary, mLvFileNames;
    private BarChart mBcQuestionResults;
    private String mStudentName;
    private File[] mAudioFiles;
    private float[] mBarValues;
    private String[] mBarLabels;
    int mTestMode, mAge;
    DecimalFormat mRound0 = new DecimalFormat("#");
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
        mBcQuestionResults.setBarLables(mBarLabels);
        mBcQuestionResults.setBarValues(mBarValues);
        setAdapters();
        setUpListViewListeners();
        setUpButtonListeners();
        return v;
    }

    private void getTestModeAndAge(){
        Cursor c = DbCRUD.getTestModeAndAge(mScreeningId);
        c.moveToNext();
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
        while (screeningCategoriesCursor.moveToNext()) {
            if (mAge >= screeningCategoriesCursor.getInt(screeningCategoriesCursor.getColumnIndex(ScreeningCategoriesEntry.CUT_OFF_AGE))) {
                int screeningCategoryId = screeningCategoriesCursor.getInt(screeningCategoriesCursor.getColumnIndex(ScreeningCategoriesEntry._ID));
                Cursor studentAnswerCursor = DbCRUD.getStudentAnswersForScreeningCategoryId(mScreeningId, screeningCategoryId);
                float rightAnswers = 0;
                int totalAnswers = 0;
                boolean isCompleted = false;
                while (studentAnswerCursor.moveToNext()) {
                    if (studentAnswerCursor.getInt(studentAnswerCursor.getColumnIndex(StudentAnswersEntry.CORRECT)) == 1) {
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
                ScreeningCategoryResult scr = new ScreeningCategoryResult(
                        screeningCategoriesCursor.getString(screeningCategoriesCursor.getColumnIndex(ScreeningCategoriesEntry.NAME_EG)),
                        screeningCategoriesCursor.getString(screeningCategoriesCursor.getColumnIndex(ScreeningCategoriesEntry.NAME_SP)),
                        isCompleted, rightAnswers, totalQuestions);
                mScreeningCategoriesResults.add(scr);
            }
        }
        mBarLabels = new String[mScreeningCategoriesResults.size()];
        mBarValues = new float[mScreeningCategoriesResults.size()];
        for (int i = 0; i < mBarValues.length; i++){
            mBarValues[i] = mScreeningCategoriesResults.get(i).getPercentCorrect();
            mBarLabels[i] = mScreeningCategoriesResults.get(i).getScreeningCategoryNameEg();
        }
        screeningCategoriesCursor.close();
        boolean totallyCompleted = mTotalAnswers == mTotalQuestions;
        ScreeningCategoryResult scr = new ScreeningCategoryResult(getString(R.string.total), getString(R.string.total),
                totallyCompleted, mTotalCorrectAnswers, mTotalQuestions);
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

        mBtnEmailReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailReport();
            }
        });
    }

    private void emailReport(){
        Log.i(TAG, "emailReport called");
        if ( ! Utilities.externalStorageIsWritable()){
            AlertDialogFragment diaFrag = AlertDialogFragment.newInstance(R.string.can_not_generate_report, 0, R.string.ok, 0, NO_SD_CARD_TAG);
            return;
        }
        Cursor screeningCursor = DbCRUD.getScreeningInfo(mScreeningId);
        screeningCursor.moveToNext();
        Cursor studentCursor = DbCRUD.getStudentInfo(screeningCursor.getLong(screeningCursor.getColumnIndex(ScreeningsEntry.STUDENT_ID)));
        studentCursor.moveToNext();

        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n");
        builder.append("<html lang=\"en\">\n");
        builder.append("<head>\n");
        builder.append("<style>\n");
        builder.append("table, tr {\nborder: 1px solid black;\nborder-collapse: collapse;\n}\n");
        builder.append("th, td {padding: 10px;\ntext-align: left;\n}\n");
        builder.append("caption {font-size: 160%;\nfont-weight: bold;}\n");
        builder.append("</style>\n");
        builder.append("</head>\n");
        builder.append("<body>\n<h1>");
        builder.append( mContext.getString(R.string.Screening_report));
        builder.append("</h1>\n");
        builder.append("<h3>");
        builder.append( studentCursor.getString(studentCursor.getColumnIndex(StudentsEntry.FIRST_NAME)));
        builder.append(" ");
        builder.append( studentCursor.getString(studentCursor.getColumnIndex(StudentsEntry.LAST_NAME)));
        builder.append( "</h3>\n");
        builder.append("<pre>\n");
        builder.append(mContext.getString(R.string.test_date));
        builder.append( Utilities.getDisplayDate(screeningCursor.getLong(screeningCursor.getColumnIndex(ScreeningsEntry.TEST_DATE))));
        builder.append( "\n");
        int ageMonths = screeningCursor.getInt(screeningCursor.getColumnIndex(ScreeningsEntry.AGE));
        builder.append(mContext.getString(R.string.age_at_time));
        builder.append( ageMonths/12);
        builder.append( " ");
        builder.append( mContext.getString(R.string.years));
        builder.append( " ");
        builder.append( ageMonths%12);
        builder.append( " ");
        builder.append( mContext.getString(R.string.months));
        builder.append( ".\n");
        int grade = screeningCursor.getInt(screeningCursor.getColumnIndex(ScreeningsEntry.GRADE));
        builder.append(mContext.getString(R.string.grade_at_time));
        builder.append( grade );
        switch (grade) {
            case 0: builder.append("\n");
                    break;
            case 1: builder.append("st\n");
                    break;
            case 2: builder.append("nd\n");
                    break;
            default: builder.append("rd\n");
        }
        builder.append(mContext.getString(R.string.Date_of_bitth_colon));
        builder.append( Utilities.getDisplayDate(studentCursor.getLong(studentCursor.getColumnIndex(StudentsEntry.BIRTHDAY))));
        builder.append( "\n");
        builder.append(mContext.getString(R.string.date_of_hearing));
        builder.append( Utilities.getDisplayDate(studentCursor.getLong(studentCursor.getColumnIndex(StudentsEntry.HEARING_TEST_DATE))));
        boolean passed = studentCursor.getInt(studentCursor.getColumnIndex(StudentsEntry.HEARING_PASS)) == 1;
        if (passed){
            builder.append("  ");
            builder.append( mContext.getString(R.string.passed));
            builder.append( "\n");
        }else{
            builder.append("<b>  ");
            builder.append( mContext.getString(R.string.failed));
            builder.append( "</b>\n");
        }
        builder.append(mContext.getString(R.string.date_of_vison));
        builder.append( Utilities.getDisplayDate(studentCursor.getLong(studentCursor.getColumnIndex(StudentsEntry.VISION_TEST_DATE))));
        passed = studentCursor.getInt(studentCursor.getColumnIndex(StudentsEntry.VISION_PASS)) == 1;
        if (passed){
            builder.append( "  ");
            builder.append( mContext.getString(R.string.passed));
            builder.append( "\n");
        }else{
            builder.append("<b>  ");
            builder.append( mContext.getString(R.string.failed));
            builder.append( "</b>\n");
        }
        builder.append(mContext.getString(R.string.teacher));
        builder.append( ": ");
        builder.append( screeningCursor.getString(screeningCursor.getColumnIndex(ScreeningsEntry.TEACHER)));
        builder.append("\n");
        builder.append(mContext.getString(R.string.room));
        builder.append( ": ");
        builder.append( screeningCursor.getString(screeningCursor.getColumnIndex(ScreeningsEntry.ROOM)));
        builder.append("\n");
        switch (screeningCursor.getInt(screeningCursor.getColumnIndex(ScreeningsEntry.COMPLETION_STATE))){
            case Utilities.SCREENING_NOT_STARTED:
                builder.append(mContext.getString(R.string.screening_not_satrted));
                builder.append("\n");
                break;
            case Utilities.SCREENING_NOT_COMPLETE:
                builder.append(mContext.getString(R.string.screening_not_complete));
                builder.append( "\n");
                break;
            case Utilities.SCREENING_COMPLETED:
                builder.append(mContext.getString(R.string.screening_complete));
                builder.append( "\n");
        }

        builder.append("</pre>\n");
        builder.append("<table style=\"width:100%\">\n");
        builder.append("<caption>");
        builder.append( mContext.getString(R.string.results_summary));
        builder.append("</caption>\n");
        builder.append("<tr>\n<th>");
        builder.append(mContext.getString(R.string.category));
        builder.append("</th>\n<th>");
        builder.append(mContext.getString(R.string.completion_state));
        builder.append("</th>\n<th>");
        builder.append(mContext.getString(R.string.number_correct));
        builder.append("</th>\n<th>");
        builder.append(mContext.getString(R.string.percent_correct));
        builder.append("</th>\n<th>");
        builder.append(mContext.getString(R.string.pass_fail));
        builder.append("</th>\n</tr>\n");
        for (ScreeningCategoryResult scr : mScreeningCategoriesResults){
            builder.append("<tr>\n");
            String categoryName;
            if (Utilities.getQuestionsLanguage() == Utilities.SPANISH || Utilities.getAppLanguage() == Utilities.SPANISH){
                categoryName = scr.getScreeningCategoryNameSp();
            }else{
                categoryName = scr.getScreeningCategoryNameEg();
            }
            builder.append("<td>");
            builder.append( categoryName);
            builder.append( "</td>\n" );
            if (scr.isCompleted()){
                builder.append("<td>");
                builder.append( mContext.getString(R.string.completed));
                builder.append( "</td>\n");
            }else{
                builder.append("<td>");
                builder.append( mContext.getString(R.string.not_completed));
                builder.append( "</td>\n");
            }
            builder.append("<td>");
            builder.append( mRound0.format(scr.getNumberCorrectAnswers()));
            builder.append("/");
            builder.append( mRound0.format(scr.getNumberOfQuestions()));
            builder.append("</td>\n");
            builder.append("<td>");
            builder.append( mRound0.format(scr.getPercentCorrect()));
            builder.append("%");
            builder.append( "</td>\n");
            if (scr.isPassed()) {
                builder.append("<td>");
                builder.append( mContext.getString(R.string.passed));
                builder.append("</td>\n");
            }else{
                builder.append("<td>");
                builder.append( mContext.getString(R.string.failed));
                builder.append( "</td>\n");
            }
            builder.append("</tr>\n");
        }
        builder.append("</table>\n");

        builder.append("</body>\n");
        builder.append("</html>");
        String report = builder.toString();

        screeningCursor.close();
        studentCursor.close();

        File reportFile = new File(Environment.getExternalStorageDirectory(), getString(R.string.neuro_underscore_leap) + "/" + "tmp_report.htm");
        FileWriter fw;
        try {
            fw = new FileWriter(reportFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(report);
            bw.flush();
            bw.close();
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        //i.setType("*/*");
        i.putExtra(Intent.EXTRA_SUBJECT, "test");
        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(reportFile));
        startActivity(i);
        Log.i(TAG, "activity started");
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
        mBtnEmailReport = (Button)v.findViewById(R.id.btnEmailReport);
        mLvResultsSummary = (NonScrollListView)v.findViewById(R.id.lvResultsSummary);
        mLvFileNames = (NonScrollListView)v.findViewById(R.id.lvFileNames);
        mBcQuestionResults = (BarChart)v.findViewById(R.id.bcQuestionResults);
    }

    @Override
    public void onAlertDialogPositiveClick(int tag){}

    @Override
    public void onAlertDialogNegativeClick(int tag) {}
}
