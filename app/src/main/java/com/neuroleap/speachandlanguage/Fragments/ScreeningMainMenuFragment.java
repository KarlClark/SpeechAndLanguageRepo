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
public class ScreeningMainMenuFragment extends BaseFragment implements View.OnClickListener{

    TableLayout tblMainMenu;
    int mScreeningId;
    private static final String ID_TAG = "id_tag";
    private static final String SCREENING_ID_TAG = "screening_id_tag";
    private static final String STUDENT_NAME_TAG = "student_name_tag";
    private static final String TAG = "## My Info ##";

    public static ScreeningMainMenuFragment newInstance(int id, int screeningId, String studentName){
        Bundle args = new Bundle();
        args.putInt(ID_TAG, id);
        args.putInt(SCREENING_ID_TAG, screeningId);
        args.putString(STUDENT_NAME_TAG, studentName);
        ScreeningMainMenuFragment fragment = new ScreeningMainMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_screening_main_menu, container, false);
        mId = getArguments().getInt(ID_TAG);
        mScreeningId = getArguments().getInt(SCREENING_ID_TAG);
        TextView tvStudentName = (TextView)v.findViewById(R.id.tvStudentName);
        tvStudentName.setText(getArguments().getString(STUDENT_NAME_TAG));
        setupButtons(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "main menu onresume called");
    }

    private void setupButtons(View v){
         Button b;
         tblMainMenu = (TableLayout)v.findViewById(R.id.tblMainMenu);

         b= (Button)((TableRow)tblMainMenu.getChildAt(1)).getChildAt(0);
         b.setText("Semantics");
         Cursor c = DbCRUD.getAnswersForCategoryType(mScreeningId, ScreeningContract.QuestionCategoriesEntry.SEMANTICS);
         if (c.getCount() > 0) {
             if (checkAnswer(c)) {
                 b.setBackgroundResource(R.drawable.button_green_shadowed);
             } else {
                 b.setBackgroundResource(R.drawable.button_red_shadowed);
             }
         }
         b.setTag(ScreeningContract.QuestionCategoriesEntry.SEMANTICS);
         b.setOnClickListener(this);

         b= (Button)((TableRow)tblMainMenu.getChildAt(2)).getChildAt(0);
         b.setText("Processing");
         b.setTag(ScreeningContract.QuestionCategoriesEntry.PROCESSING);
         b.setOnClickListener(this);

         b= (Button)((TableRow)tblMainMenu.getChildAt(3)).getChildAt(0);
         b.setText("Inferences");
         b.setTag(ScreeningContract.QuestionCategoriesEntry.INFERENCES);
         b.setOnClickListener(this);

         b= (Button)((TableRow)tblMainMenu.getChildAt(1)).getChildAt(1);
         b.setText("Idioms");
         b.setTag(ScreeningContract.QuestionCategoriesEntry.IDIOMS);
         b.setOnClickListener(this);

         b= (Button)((TableRow)tblMainMenu.getChildAt(2)).getChildAt(1);
         b.setText("Syntax");
         b.setTag(ScreeningContract.QuestionCategoriesEntry.SYNTAX);
         b.setOnClickListener(this);

         b= (Button)((TableRow)tblMainMenu.getChildAt(3)).getChildAt(1);
         b.setText("Auditory Processing");
         b.setTag(ScreeningContract.QuestionCategoriesEntry.AUDITORY_PROCESSING);
         b.setOnClickListener(this);

         b= (Button)((TableRow)tblMainMenu.getChildAt(1)).getChildAt(2);
         b.setText("Auditory Memory");
         b.setTag(ScreeningContract.QuestionCategoriesEntry.AUDITORY_MEMORY);
         b.setOnClickListener(this);

         b= (Button)((TableRow)tblMainMenu.getChildAt(2)).getChildAt(2);
         b.setText("Unknown");
         b.setTag(ScreeningContract.QuestionCategoriesEntry.UNKNOWN);
         b.setOnClickListener(this);

         b= (Button)((TableRow)tblMainMenu.getChildAt(3)).getChildAt(2);
         b.setText("Unknown");
         b.setTag(ScreeningContract.QuestionCategoriesEntry.UNKNOWN);
         b.setOnClickListener(this);

         b= (Button)v.findViewById(R.id.btnScreenings);
         b.setTag(Utilities.SCREENINGS);
         b.setOnClickListener(this);
     }

    public void onClick(View v){
        mOnFragmentInteractionListener.onFragmentInteraction(mId, mScreeningId, v.getTag());
    }

    private boolean checkAnswer(Cursor c){
        float rightAnswers=0;
        while(c.moveToNext()){
            if (c.getInt(1) == 1) {
                rightAnswers++;
            }
        }
        Log.i(TAG, "average = " + rightAnswers / c.getCount());
        return rightAnswers/c.getCount() >= 0.8;

    }
}
