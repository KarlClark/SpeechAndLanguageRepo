package com.neuroleap.speachandlanguage.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Listeners.OnCustomDateDialogClickedListener;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Karl on 3/25/2015.
 */
public class StudentInfoFragment extends BaseFragment implements OnCustomDateDialogClickedListener{
    private EditText mEtFirstName, mEtLastName, mEtDateOfBirth, mEtAge, mEtTeacher, mEtGrade, mEtRoom,
                     mEtHearingDate, mEtVisionDate, mEtScreeningDate, mCurrentEditText;
    private View mNextView;
    private TextView mTvDateOfBirth, mTvHearing, mTvVision, mTvSpeachAndLanguage, mTvError;
    private Spinner mSpnHearing, mSpnVision;
    private Button mBtnNext;
    private ScrollView mSvStudentInfo;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat mDateFormatter;
    private CustomDatePickerDialogFragment mCustomDatePickerDialogFragment;
    private InputMethodManager mInputMethodManager;
    private boolean mKeyboardUp= false;
    private boolean mGotHereFrommEtRoom = false;
    long mStudentId;
    private static final int CUT_OFF_DATE = 3 * 365;
    public static final String DATE_FORMAT_STRING = "MMM dd, yyyy";
    private static final String TAG = "## My Info ##";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_info, container, false);
        getViews(v);
        mEtFirstName.requestFocus();
        setUpListeners();
        setUpSpinners();
        setupDatePickers();
        setUpNextButton();
        setUpRootViewListener();
        raiseKeyBoard();
        return v;
    }

    private void setUpListeners(){


        mEtRoom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGotHereFrommEtRoom = true;
                return false;
            }
        });
    }

    private void setUpNextButton(){
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG , "Next button clicked");
                if ( ! dataOk()){
                    return;
                }
                String[] sa = getResources().getStringArray(R.array.pass_fail);
                boolean hearingPass = mSpnHearing.getSelectedItem().toString().equals(sa[0]);
                boolean visionPass = mSpnVision.getSelectedItem().toString().equals(sa[0]);
                int age = Integer.parseInt(mEtAge.getText().toString());
                int grade = Integer.parseInt(mEtGrade.getText().toString());
                mStudentId= DbCRUD.insertStudent(mEtFirstName.getText().toString(),
                                                 mEtLastName.getText().toString(),
                                                 mEtDateOfBirth.getText().toString(),
                                                 mEtHearingDate.getText().toString(),
                                                 hearingPass,
                                                 mEtVisionDate.getText().toString(),
                                                 visionPass );
                Log.i(TAG ,"Student id= " + mStudentId);

                mOnFragmentInteractionListener.onFragmentInteraction(mId);
            }
        });

        mEtAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    raiseKeyBoard();
                }
            }
        });
    }

    private void raiseKeyBoard(){
        mInputMethodManager =(InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
        if ( ! mKeyboardUp) {
            mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void getViews(View v) {
        mSvStudentInfo = (ScrollView)v.findViewById(R.id.svStudentInfo);
        mTvDateOfBirth = (TextView)v.findViewById(R.id.tvDateOfBirth);
        mTvHearing = (TextView)v.findViewById(R.id.tvHearing);
        mTvSpeachAndLanguage = (TextView)v.findViewById(R.id.tvSpeachAndLanguage);
        mTvError = (TextView)v.findViewById(R.id.tvError);
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

    private void showDatePickerDialog(String title, int titleColor, EditText et){
        mCustomDatePickerDialogFragment = CustomDatePickerDialogFragment.newInstance(title);
        mCustomDatePickerDialogFragment.setTargetFragment(this, 0);
        mCustomDatePickerDialogFragment.setCancelable(false);
        mCustomDatePickerDialogFragment.setField(et);
        if (titleColor != 0) {
            mCustomDatePickerDialogFragment.setTitleColor(titleColor);
        }
        mCustomDatePickerDialogFragment.show(getFragmentManager(), "tag");
    }

    public void onCustomDateDialogClicked(EditText et) {
        //Log.i(TAG, "onCustomDateDialogClickedListener called Next view = " + mNextView);
        Log.d(TAG,"date of birth= " + mEtDateOfBirth.getText());
        if (et == mEtDateOfBirth){
            mEtAge.setText("" + yearsAgo(mEtDateOfBirth));
        }
        mNextView.requestFocus();
        if (mNextView == mEtFirstName){

            //Log.i(TAG,"next view was next button----------------");
            //Log.i(TAG,"mNextView = " + mNextView);
            //Log.i(TAG,"mEtFirstName = " + mEtFirstName);
            //Log.i(TAG, "focused child is " + ((LinearLayout)((LinearLayout)mSvStudentInfo.getFocusedChild()).getFocusedChild()).getFocusedChild()         );
            View v =  ((LinearLayout)((LinearLayout)mSvStudentInfo.getFocusedChild()).getFocusedChild()).getFocusedChild();
            //mSvStudentInfo.fullScroll(ScrollView.FOCUS_DOWN);
            //InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            //mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            if (mKeyboardUp) {
                mInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
            }
            //Log.i(TAG,"Scrolling");
            //mSvStudentInfo.fullScroll(ScrollView.FOCUS_DOWN);
            mSvStudentInfo.post(new Runnable() {
                public void run(){
                    View v =  ((LinearLayout)((LinearLayout)mSvStudentInfo.getFocusedChild()).getFocusedChild()).getFocusedChild();
                    //mSvStudentInfo.fullScroll(ScrollView.FOCUS_DOWN);
                    //InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    //mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    mSvStudentInfo.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
            //mSvStudentInfo.scrollTo(0,mSvStudentInfo.getBottom());
        }
    }

    private void setUpRootViewListener(){
        mSvStudentInfo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Log.i(TAG,"OnGlobalLayout called#################3##");

                int heightDiff = mSvStudentInfo.getRootView().getHeight() - mSvStudentInfo.getHeight();
                mKeyboardUp = (heightDiff > 400);
                //Log.i(TAG,"height diff= " +heightDiff);
            }
        });
    }

    private void setupDatePickers() {

        mDateFormatter = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.US);

        mEtDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mEtDateOfBirth.getText().toString().equals("")) {
                        mNextView = mEtAge;
                    }else{
                        mNextView = mEtFirstName;
                    }
                    showDatePickerDialog(getResources().getString(R.string.date_of_birth_title), 0, mEtDateOfBirth);
                }
            }
        });


        mEtHearingDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (( ! mEtHearingDate.getText().toString().equals("")) && mGotHereFrommEtRoom){
                        mGotHereFrommEtRoom = false;
                        mNextView = mEtFirstName;
                        onCustomDateDialogClicked(mEtHearingDate);
                        return;
                    }
                    if (mEtHearingDate.getText().toString().equals("")) {
                        mNextView = mEtVisionDate;
                    }else{
                        mNextView = mEtFirstName;
                    }
                    showDatePickerDialog(getResources().getString(R.string.hearing_title) , 0 , mEtHearingDate);
                }
            }
        });

        mEtScreeningDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mNextView = mEtFirstName;
                    showDatePickerDialog(getResources().getString(R.string.speach_and_language_title) , 0 , mEtScreeningDate);
                    //mSvStudentInfo.scrollTo(0, mSvStudentInfo.getBottom());
                }
            }
        });


        mEtVisionDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mEtVisionDate.getText().toString().equals("")) {
                        mNextView = mEtScreeningDate;
                    }else{
                        mNextView = mEtFirstName;
                    }
                    showDatePickerDialog(getResources().getString(R.string.vision_title) , 0 , mEtVisionDate);
                }
            }
        });


    }


    private boolean dataOk() {


        if (mEtFirstName.getText().toString().equals("")){
            processError(mContext.getString(R.string.error_first_name), mEtFirstName);
            return false;
        }

        if (mEtLastName.getText().toString().equals("")) {
            processError(mContext.getString(R.string.error_last_name), mEtLastName);
            return false;
        }

        if (mEtDateOfBirth.getText().toString().equals("")){
            //processError(mContext.getString(R.string.error_date_of_birth), mEtDateOfBirth);
            mNextView = mEtFirstName;
            Log.i(TAG,"Calling show datepicker ");
            showDatePickerDialog(mContext.getString(R.string.error_date_of_birth), getResources().getColor(R.color.red), mEtDateOfBirth);
            return false;
        }

        if (mEtAge.getText().toString().equals("")){
            processError(mContext.getString(R.string.error_age), mEtAge);
            return false;
        }


        if(mEtHearingDate.getText().toString().equals("")){
            //processError("Error: Require date of hearing screen.", mEtHearingDate);
            mNextView = mEtFirstName;
            showDatePickerDialog(mContext.getString(R.string.error_hearing_required), getResources().getColor(R.color.red), mEtDateOfBirth);
            return false;
        }

        if (mEtVisionDate.getText().toString().equals("")){
            showDatePickerDialog(mContext.getString(R.string.error_vision_required), getResources().getColor(R.color.red),  mEtVisionDate);
            return false;
        }

        if (mEtScreeningDate.getText().toString().equals("")){
            showDatePickerDialog(mContext.getString(R.string.error_speach_required), getResources().getColor(R.color.red),  mEtScreeningDate);
            return false;
        }
        if (daysAgo(mEtHearingDate) > CUT_OFF_DATE){
            mTvError.setText(mContext.getString(R.string.error_hearing_to_old));
            mTvError.setVisibility(View.VISIBLE);
            return false;
        }
        if(daysAgo(mEtVisionDate) > CUT_OFF_DATE){
            mTvError.setText((mContext.getString(R.string.error_vision_to_old)));
            mTvError.setVisibility(View.VISIBLE);
            return false;
        }

        mTvError.setVisibility(View.GONE);
        return true;
    }

    private void processError(String errorMsg, EditText et){
        et.setHint(errorMsg);
        et.requestFocus();
    }

    private long daysAgo (EditText et) {
        return milliSecsAgo(et)/(24 * 60 * 60 * 1000);
    }

    private long yearsAgo(EditText et){
        return daysAgo(et)/365;
    }

    private long milliSecsAgo (EditText et){
        Date d = null;
        try {
            d = mDateFormatter.parse(et.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar testDate = Calendar.getInstance();
        testDate.setTime(d);
        Calendar today = Calendar.getInstance();
        long diff = today.getTimeInMillis() - testDate.getTimeInMillis();
        Log.i(TAG, "days ago = " +(diff/(24*60*60*1000)));
        return diff;
    }

}
