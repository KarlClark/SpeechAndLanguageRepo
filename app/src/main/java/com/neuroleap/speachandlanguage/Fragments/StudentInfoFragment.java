package com.neuroleap.speachandlanguage.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Listeners.OnCustomDateDialogClickedListener;
import com.neuroleap.speachandlanguage.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Karl on 3/25/2015.
 */
public class StudentInfoFragment extends BaseFragment implements OnCustomDateDialogClickedListener{
    EditText mEtFirstName, mEtLastName, mEtDateOfBirth, mEtAge, mEtTeacher, mEtGrade, mEtRoom,
             mEtHearingDate, mEtVisionDate, mEtScreeningDate, mCurrentEditText;
    View nextView;
    TextView mTvDateOfBirth, mTvHearing, mTvVision, mTvSpeachAndLanguage, mCurrentTextView;
    Spinner mSpnHearing, mSpnVision;
    Button mBtnNext;
    ScrollView mSvStudentInfo;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat mDateFormatter;
    private CustomDatePickerDialogFragment mCustomDatePickerDialogFragment;
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
                mOnFragmentInteractionListener.onFragmentInteraction(mId);
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
                if ((TextView)parent.getChildAt(0) != null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_grey));
                }

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
                if ((TextView)parent.getChildAt(0) != null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_grey));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDatePickerDialog(String title, EditText et){
        mCustomDatePickerDialogFragment = CustomDatePickerDialogFragment.newInstance(title);
        mCustomDatePickerDialogFragment.setTargetFragment(this, 0);
        mCustomDatePickerDialogFragment.setCancelable(false);
        mCustomDatePickerDialogFragment.setField(et);
        mCustomDatePickerDialogFragment.show(getFragmentManager(), "tag");
    }

    public void onCustomDateDialogClicked() {
        Log.i(TAG, "onCustomDateDialogClickedListener called");
        nextView.requestFocus();
        if (nextView == mEtRoom){
            InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(mEtRoom.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            Log.i(TAG,"Scrolling");
            mSvStudentInfo.scrollTo(0,mSvStudentInfo.getBottom());
        }
    }

    private void setupDatePickers() {

        mDateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        mEtDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nextView = mEtAge;
                    showDatePickerDialog(getResources().getString(R.string.date_of_birth_title), mEtDateOfBirth);
                }
            }
        });


        mEtHearingDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nextView = mEtVisionDate;
                    showDatePickerDialog(getResources().getString(R.string.hearing_title),mEtHearingDate);
                }
            }
        });

        mEtScreeningDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nextView = mEtRoom;
                    showDatePickerDialog(getResources().getString(R.string.speach_and_language_title),mEtScreeningDate);
                    mSvStudentInfo.scrollTo(0, mSvStudentInfo.getBottom());
                }
            }
        });


        mEtVisionDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i(TAG,"etVision onFocusChange called hasFocus=  " +hasFocus);
                if (hasFocus) {
                    nextView = mEtScreeningDate;
                    showDatePickerDialog(getResources().getString(R.string.vision_title),mEtVisionDate);
                }
            }
        });



    }


}
