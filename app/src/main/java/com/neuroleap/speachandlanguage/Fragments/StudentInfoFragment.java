package com.neuroleap.speachandlanguage.Fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Karl on 3/25/2015.
 */
public class StudentInfoFragment extends BaseFragment {
    EditText mEtFirstName, mEtLastName, mEtDateOfBirth, mEtAge, mEtTeacher, mEtGrade, mEtRoom,
             mEtHearingDate, mEtVisionDate, mEtScreeningDate, mCurrentEditText;
    View nextView;
    TextView mTvDateOfBirth, mTvHearing, mTvVision, mTvSpeachAndLanguage, mCurrentTextView;
    Spinner mSpnHearing, mSpnVision;
    Button mBtnNext;
    ScrollView mSvStudentInfo;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat mDateFormatter;
    private int mSaveColor;
    private static final String TAG = "## My Info ##";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_info, container, false);
        getViews(v);
        mEtFirstName.requestFocus();
        setUpSpinners();
        setupDatePickers();
        setUpNextButton();
        raiseKeyBoard();
        return v;
    }

    private void setUpNextButton(){
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG , "Next button clicked");
            }
        });
    }

    private void raiseKeyBoard(){
        InputMethodManager imm =(InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    private void getViews(View v) {
        mSvStudentInfo = (ScrollView)v.findViewById(R.id.svStudentInfo);
        mTvDateOfBirth = (TextView)v.findViewById(R.id.tvDateOfBirth);
        mTvHearing = (TextView)v.findViewById(R.id.tvHearing);
        mTvSpeachAndLanguage = (TextView)v.findViewById(R.id.tvSpeachAndLanguage);
        mTvVision = (TextView)v.findViewById(R.id.tvVision);
        mEtFirstName = (EditText)v.findViewById(R.id.etFirstName);
        mEtLastName = (EditText)v.findViewById(R.id.etLastName);
        mEtDateOfBirth = (EditText)v.findViewById(R.id.etDateOfBirth);
        mEtAge = (EditText)v.findViewById(R.id.etAge);
        mEtTeacher = (EditText)v.findViewById(R.id.etTeacher);
        mEtGrade = (EditText)v.findViewById(R.id.etGrade);
        mEtRoom = (EditText)v.findViewById(R.id.etRoom);
        mEtHearingDate = (EditText)v.findViewById(R.id.etHearing);
        mEtVisionDate = (EditText)v.findViewById(R.id.etVision);
        mEtScreeningDate = (EditText)v.findViewById(R.id.etSpeachLanguage);
        mSpnHearing = (Spinner)v.findViewById(R.id.spnHearing);
        mSpnVision = (Spinner)v.findViewById(R.id.spnVision);
        mBtnNext =(Button)v.findViewById(R.id.btnNext);
    }

    private void setUpSpinners(){
        ArrayAdapter<CharSequence> hearingAdapter = ArrayAdapter.createFromResource(mContext, R.array.pass_fail, R.layout.custom_spinner_layout);
        hearingAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        mSpnHearing.setAdapter(hearingAdapter);
        mSpnHearing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_grey));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> visionAdapter = ArrayAdapter.createFromResource(mContext, R.array.pass_fail, R.layout.custom_spinner_layout);
        visionAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        mSpnVision.setAdapter(visionAdapter);
        mSpnVision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_grey));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupDatePickers() {

        mDateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        Calendar calendar = Calendar.getInstance();

        mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day){
                Calendar pickedDate = Calendar.getInstance();
                pickedDate.set(year, month, day);
                mCurrentEditText.setText(mDateFormatter.format(pickedDate.getTime()));

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        mDatePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.i(TAG,"onDismiss called");
                mCurrentTextView.setTextColor(mSaveColor);
                nextView.requestFocus();

            }
        });

        mEtDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mCurrentEditText = (EditText) v;
                    nextView = mEtAge;
                    mCurrentTextView = mTvDateOfBirth;
                    mSaveColor = mCurrentTextView.getCurrentTextColor();
                    mCurrentTextView.setTextColor(getResources().getColor(R.color.red));
                    mDatePickerDialog.show();
                }
            }
        });


        mEtHearingDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mCurrentEditText = (EditText) v;
                    nextView = mEtVisionDate;
                    mCurrentTextView = mTvHearing;
                    mSaveColor = mCurrentTextView.getCurrentTextColor();
                    mCurrentTextView.setTextColor(getResources().getColor(R.color.red));
                    mDatePickerDialog.show();
                }
            }
        });

        mEtScreeningDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mCurrentEditText = (EditText) v;
                    nextView = mEtRoom;
                    mCurrentTextView = mTvSpeachAndLanguage;
                    mSaveColor = mCurrentTextView.getCurrentTextColor();
                    mCurrentTextView.setTextColor(getResources().getColor(R.color.red));
                    mDatePickerDialog.show();
                    mSvStudentInfo.scrollTo(0,mSvStudentInfo.getBottom());
                }
            }
        });


        mEtVisionDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i(TAG,"etVision onFocusChange called hasFocus=  " +hasFocus);
                if (hasFocus) {
                    mCurrentEditText = (EditText) v;
                    nextView = mEtScreeningDate;
                    mCurrentTextView = mTvVision;
                    mSaveColor = mCurrentTextView.getCurrentTextColor();
                    mCurrentTextView.setTextColor(getResources().getColor(R.color.red));
                    Log.i(TAG, "Showing datepicker");
                    mDatePickerDialog.show();
                }
            }
        });



    }


}
