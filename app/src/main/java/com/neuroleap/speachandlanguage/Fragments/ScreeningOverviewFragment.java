package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Data.ScreeningContract.ScreeningsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ScreeningCategoriesEntry;
import com.neuroleap.speachandlanguage.Models.ScreeningCategory;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.util.ArrayList;

/**
 * Created by Karl on 4/15/2015.
 */
public class ScreeningOverviewFragment extends BaseFragment implements View.OnClickListener{

    private TableLayout mTblMainMenu;
    private int mScreeningId, mAge;
    private String mStudentName;
    private Button mBtnScreenings, mBtnResults;
    private ArrayList<ScreeningCategory> mScreeningCategories = new ArrayList<>();
    private static final String ID_TAG = "id_tag";
    private static final String SCREENING_ID_TAG = "screening_id_tag";
    private static final String STUDENT_NAME_TAG = "student_name_tag";
    private static final String TAG = "## My Info ##";

    public static ScreeningOverviewFragment newInstance(int id, int screeningId, String studentName){
        Bundle args = new Bundle();
        args.putInt(ID_TAG, id);
        args.putInt(SCREENING_ID_TAG, screeningId);
        args.putString(STUDENT_NAME_TAG, studentName);
        ScreeningOverviewFragment fragment = new ScreeningOverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScreeningCategories();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_screening_overview, container, false);
        mId = getArguments().getInt(ID_TAG);
        mScreeningId = getArguments().getInt(SCREENING_ID_TAG);
        Cursor c = DbCRUD.getTestModeAndAge(mScreeningId);
        c.moveToNext();
        mAge=c.getInt(c.getColumnIndex(ScreeningsEntry.AGE));
        c.close();
        TextView tvStudentName = (TextView)v.findViewById(R.id.tvStudentName);
        mStudentName = getArguments().getString(STUDENT_NAME_TAG);
        tvStudentName.setText(mStudentName);
        mTblMainMenu = (TableLayout)v.findViewById(R.id.tblMainMenu);
        mBtnScreenings= (Button)v.findViewById(R.id.btnScreenings);
        mBtnScreenings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mId, mScreeningId, Utilities.SCREENINGS);
            }
        });

        mBtnResults = (Button)v.findViewById(R.id.btnResults);
        mBtnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mId, mScreeningId, Utilities.SUMMARY_RESULTS, mStudentName);
            }
        });
        //setupButtons();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "main menu onresume called");
        setupButtons();
    }

    private void getScreeningCategories(){
        Cursor c = DbCRUD.getScreeningCategories();
        while(c.moveToNext()){
            mScreeningCategories.add(new ScreeningCategory(c.getLong(c.getColumnIndex(ScreeningCategoriesEntry._ID)),
                                                           c.getString(c.getColumnIndex(ScreeningCategoriesEntry.NAME_EG)),
                                                           c.getString(c.getColumnIndex(ScreeningCategoriesEntry.NAME_SP)),
                                                           c.getInt(c.getColumnIndex(ScreeningCategoriesEntry.LOW_CUT_OFF_AGE)),
                                                           c.getInt(c.getColumnIndex(ScreeningCategoriesEntry.HIGH_CUT_OFF_AGE))));
        }
        c.close();
    }

    private void setupButtons(){
        Button b;

        int buttonCnt=0;
        //int index;
        for (int row = 1; row <=3; row++){
            for (int column = 0; column <=2; column++){
                b= (Button)((TableRow)mTblMainMenu.getChildAt(row)).getChildAt(column);
                if (buttonCnt < mScreeningCategories.size()) {
                    if (Utilities.getAppLanguage() == Utilities.ENGLISH) {
                        b.setText(mScreeningCategories.get(buttonCnt).getName_Eg());
                    }else{
                        b.setText(mScreeningCategories.get(buttonCnt).getName_Sp());
                    }
                    if(mAge >= mScreeningCategories.get(buttonCnt).getLowCutoffAge() &&
                            mAge <= mScreeningCategories.get(buttonCnt).getHighCutoffAge()) {
                        long screeningCategoryId = mScreeningCategories.get(buttonCnt).getScreeningCategoryId();
                        Cursor c = DbCRUD.getStudentAnswersForScreeningCategoryId(mScreeningId, screeningCategoryId);
                        if (c.getCount() > 0) {
                            if (checkAnswer(c, screeningCategoryId)) {
                                b.setBackgroundResource(R.drawable.button_green_shadowed);
                            } else {
                                b.setBackgroundResource(R.drawable.button_red_shadowed);
                            }
                        }
                        c.close();
                        b.setTag(screeningCategoryId);
                        b.setOnClickListener(this);
                    }else{
                        b.setBackgroundResource(R.drawable.button_grey_shadowed);
                    }
                }else{
                    //b.setTag(-1);
                    b.setText("Unknown");
                    b.setBackgroundResource(R.drawable.button_grey_shadowed);
                }
                buttonCnt++;
            }
        }
     }

    public void onClick(View v){
        mOnFragmentInteractionListener.onFragmentInteraction(mId, mScreeningId, v.getTag());
    }

    private boolean checkAnswer(Cursor c, long screeningCategoryId){
        float rightAnswers=0;
        while(c.moveToNext()){
            if (c.getInt(0) == 1) {
                rightAnswers++;
            }
        }
        int totalQuestions = DbCRUD.getNumberOfQuestionsForScreeningCategoryId(screeningCategoryId);
        Log.i(TAG, "totalQuestions= " + totalQuestions +"  %%%%%%%%%%%%%%%%%%%");
        return rightAnswers/totalQuestions >= Utilities.PASSING_FRACTION;
    }
}
