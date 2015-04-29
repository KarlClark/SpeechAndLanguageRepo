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

import com.neuroleap.speachandlanguage.Data.ScreeningContract;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 4/15/2015.
 */
public class ScreeningOverviewFragment extends BaseFragment implements View.OnClickListener{

    TableLayout mTblMainMenu;
    int mScreeningId;
    private static final String ID_TAG = "id_tag";
    private static final String SCREENING_ID_TAG = "screening_id_tag";
    private static final String STUDENT_NAME_TAG = "student_name_tag";
    private int[] mCategoryTypes = new int[] {ScreeningContract.QuestionCategoriesEntry.SEMANTICS,
                                              ScreeningContract.QuestionCategoriesEntry.PROCESSING,
                                              ScreeningContract.QuestionCategoriesEntry.INFERENCES,
                                              ScreeningContract.QuestionCategoriesEntry.IDIOMS,
                                              ScreeningContract.QuestionCategoriesEntry.SYNTAX,
                                              ScreeningContract.QuestionCategoriesEntry.AUDITORY_PROCESSING,
                                              ScreeningContract.QuestionCategoriesEntry.AUDITORY_MEMORY,
                                              ScreeningContract.QuestionCategoriesEntry.UNKNOWN};
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_screening_overview, container, false);
        mId = getArguments().getInt(ID_TAG);
        mScreeningId = getArguments().getInt(SCREENING_ID_TAG);
        TextView tvStudentName = (TextView)v.findViewById(R.id.tvStudentName);
        tvStudentName.setText(getArguments().getString(STUDENT_NAME_TAG));
        mTblMainMenu = (TableLayout)v.findViewById(R.id.tblMainMenu);
        Button b= (Button)v.findViewById(R.id.btnScreenings);
        b.setTag(Utilities.SCREENINGS);
        b.setOnClickListener(this);

        b = (Button)v.findViewById(R.id.btnResults);
        b.setTag(Utilities.RESULTS);
        b.setOnClickListener(this);
        //setupButtons();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "main menu onresume called");
        setupButtons();
    }

    private void setupButtons(){
        Button b;



        int buttonCnt=0;
        int index;
        for (int row = 1; row <=3; row++){
            for (int column = 0; column <=2; column++){
                b= (Button)((TableRow)mTblMainMenu.getChildAt(row)).getChildAt(column);
                index = Math.min(buttonCnt, mCategoryTypes.length-1);
                Cursor c = DbCRUD.getStudentAnswersForCategoryType(mScreeningId , mCategoryTypes[index]);
                if (c.getCount() > 0) {
                    if (checkAnswer(c,mCategoryTypes[index])){
                        b.setBackgroundResource(R.drawable.button_green_shadowed);
                    } else {
                        b.setBackgroundResource(R.drawable.button_red_shadowed);
                    }
                }
                c.close();
                b.setTag(mCategoryTypes[index]);
                b.setOnClickListener(this);
                switch (buttonCnt) {
                    case 0:
                        b.setText("Semantics");
                        break;
                    case 1:
                        b.setText("Processing");
                        break;
                    case 2:
                        b.setText("Inferences");
                        break;
                    case 3:
                        b.setText("Idioms");
                        break;
                    case 4:
                        b.setText("Syntax");
                        break;
                    case 5:
                        b.setText("Auditory Processing");
                        break;
                    case 6:
                        b.setText("Auditory Memory");
                        break;
                    case 7:
                        b.setText("Unknown");
                        break;
                    case 8:
                        b.setText("Unknown");
                        break;
                }
                buttonCnt++;
            }
        }
     }

    public void onClick(View v){
        mOnFragmentInteractionListener.onFragmentInteraction(mId, mScreeningId, v.getTag());
    }

    private boolean checkAnswer(Cursor c, int categoryType){
        float rightAnswers=0;
        while(c.moveToNext()){
            if (c.getInt(0) == 1) {
                rightAnswers++;
            }
        }
        int totalQuestions = DbCRUD.getNumberOfQuestionsForCategoryType(categoryType);
        Log.i(TAG, "totalQuestions= " + totalQuestions +"  %%%%%%%%%%%%%%%%%%%");
        return rightAnswers/totalQuestions >= 0.8;

    }
}
