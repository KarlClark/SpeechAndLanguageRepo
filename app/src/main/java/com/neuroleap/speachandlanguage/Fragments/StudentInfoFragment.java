package com.neuroleap.speachandlanguage.Fragments;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Data.ScreeningContract.ScreeningsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentsEntry;
import com.neuroleap.speachandlanguage.Listeners.OnCustomDateDialogClickedListener;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Karl on 3/25/2015.
 */
public class StudentInfoFragment extends BaseFragment implements OnCustomDateDialogClickedListener{
    private EditText mEtFirstName, mEtLastName, mEtDateOfBirth, mEtAgeYears, mEtAgeMonths, mEtTeacher, mEtGrade, mEtRoom,
                     mEtHearingDate, mEtVisionDate, mEtScreeningDate, mCurrentEditText;
    private View mNextView;
    private TextView mTvDateOfBirth, mTvHearing, mTvVision, mTvSpeachAndLanguage, mTvError;
    private Spinner mSpnHearing, mSpnVision;
    private Button mBtnDone, mBtnCancel;
    private ScrollView mSvStudentInfo;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat mDateFormatter;
    private CustomDatePickerDialogFragment mCustomDatePickerDialogFragment;
    private InputMethodManager mInputMethodManager;
    private boolean mKeyboardUp= false;
    private boolean mGotHereFrommEtRoom = false;
    long mStudentId=-1;
    private static final int CUT_OFF_DATE = 3 * 365;
    public static final String DATE_FORMAT_STRING = "MMM dd, yyyy";
    private static final String STUDENT_ID_KEY = "student_id_key";
    private static final String ID_KEY = "id_key";
    private static final String TAG = "## My Info ##";

    public static StudentInfoFragment newInstance(int id, long studentId){
        Bundle args = new Bundle();
        args.putLong(STUDENT_ID_KEY , studentId);
        args.putInt(ID_KEY, id);
        StudentInfoFragment fragment = new StudentInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getArguments().getInt(ID_KEY);
        mStudentId = getArguments().getLong(STUDENT_ID_KEY);
        mDateFormatter = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.US);
        mInputMethodManager = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_info, container, false);
        getViews(v);
        if (mStudentId != -1){
            fillInFields();
        }
        mEtFirstName.requestFocus();
        setUpListeners();
        setUpSpinners();
        setupDatePickers();
        setupDoneButton();
        setupCancelButton();
        setUpRootViewListener();
        if (savedInstanceState == null) {
            raiseKeyBoard();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "StudentInfoFragment onResume called");
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

    private void setupDoneButton(){
        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG , "Next button clicked");
                if ( ! dataOk()){
                    return;
                }
                String[] sa = getResources().getStringArray(R.array.pass_fail);
                boolean hearingPass = mSpnHearing.getSelectedItem().toString().equals(sa[0]);
                boolean visionPass = mSpnVision.getSelectedItem().toString().equals(sa[0]);
                int ageYears = Integer.parseInt(mEtAgeYears.getText().toString());
                int ageMonths = Integer.parseInt(mEtAgeMonths.getText().toString());
                int totalMonths = (ageYears *12) + ageMonths;
                Log.i(TAG,"total months=" + totalMonths);
                int grade = Integer.parseInt(mEtGrade.getText().toString());
                mStudentId= DbCRUD.insertStudent(mStudentId, mEtFirstName.getText().toString(),
                                                 mEtLastName.getText().toString(),
                                                 mEtDateOfBirth.getText().toString(),
                                                 mEtHearingDate.getText().toString(),
                                                 hearingPass,
                                                 mEtVisionDate.getText().toString(),
                                                 visionPass );
                Log.i(TAG ,"Student id= " + mStudentId);

                DbCRUD.insertScreening(mStudentId,
                                       mEtScreeningDate.getText().toString(),
                                       Utilities.getTestMode(),
                                       Utilities.getQuestionsLanguage(),
                                       totalMonths,
                                       mEtRoom.getText().toString(),
                                       grade,
                                       mEtTeacher.getText().toString());

                lowerKeyboard();
                mOnFragmentInteractionListener.onFragmentInteraction(mId);
            }
        });

        mEtAgeYears.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    raiseKeyBoard();
                }
            }
        });
    }

    private void setupCancelButton() {
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowerKeyboard();
                mOnFragmentInteractionListener.onFragmentInteraction(mId);
            }
        });
    }

    private void lowerKeyboard() {
        Log.i(TAG,"lowerKeyboard, mKeyboardUp= " + mKeyboardUp);
        //mInputMethodManager =(InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
        if (mKeyboardUp) {
            mInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        }
    }

    private void raiseKeyBoard(){
        Log.i(TAG,"raiseKeyboard, mKeyboardUp= " + mKeyboardUp);
        //mInputMethodManager =(InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
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
        mEtAgeYears = (EditText)v.findViewById(R.id.etAgeYears);
        mEtAgeMonths = (EditText)v.findViewById(R.id.etAgeMonths);
        mEtTeacher = (EditText)v.findViewById(R.id.etTeacher);
        mEtGrade = (EditText)v.findViewById(R.id.etGrade);
        mEtRoom = (EditText)v.findViewById(R.id.etRoom);
        mEtHearingDate = (EditText)v.findViewById(R.id.etHearing);
        mEtVisionDate = (EditText)v.findViewById(R.id.etVision);
        mEtScreeningDate = (EditText)v.findViewById(R.id.etSpeachLanguage);
        mSpnHearing = (Spinner)v.findViewById(R.id.spnHearing);
        mSpnVision = (Spinner)v.findViewById(R.id.spnVision);
        mBtnDone =(Button)v.findViewById(R.id.btnDone);
        mBtnCancel = (Button)v.findViewById(R.id.btnCancel);
    }

    private void fillInFields(){
        Cursor c = DbCRUD.getStudentInfo(mStudentId);
        c.moveToNext();
        mEtFirstName.setText(c.getString(c.getColumnIndex(StudentsEntry.FIRST_NAME)));
        mEtLastName.setText(c.getString(c.getColumnIndex(StudentsEntry.LAST_NAME)));
        //mField.setText(mDateFormatter.format(pickedDate.getTime()));
        mEtDateOfBirth.setText(mDateFormatter.format(c.getLong(c.getColumnIndex(StudentsEntry.BIRTHDAY))));
        mEtHearingDate.setText(mDateFormatter.format(c.getLong(c.getColumnIndex(StudentsEntry.HEARING_TEST_DATE))));
        if (c.getLong(c.getColumnIndex(StudentsEntry.HEARING_PASS)) == 1){
            mSpnHearing.setSelection(0);
        }else{
            mSpnHearing.setSelection(1);
        }
        mEtVisionDate.setText(mDateFormatter.format(c.getLong(c.getColumnIndex(StudentsEntry.VISION_TEST_DATE))));
        if (c.getLong(c.getColumnIndex(StudentsEntry.VISION_PASS)) == 1){
            mSpnVision.setSelection(0);
        }else{
            mSpnVision.setSelection(1);
        }
        c.close();
        c = DbCRUD.getScreeningStudentInfo(mStudentId);
        c.moveToNext();
        mEtScreeningDate.setText(mDateFormatter.format(c.getLong(c.getColumnIndex(ScreeningsEntry.TEST_DATE))));
        mEtAgeYears.setText("" + c.getLong(c.getColumnIndex(ScreeningsEntry.AGE))/12);
        mEtAgeMonths.setText(("" + c.getLong(c.getColumnIndex(ScreeningsEntry.AGE))%12));
        mEtRoom.setText(c.getString(c.getColumnIndex(ScreeningsEntry.ROOM)));
        mEtTeacher.setText(c.getString(c.getColumnIndex(ScreeningsEntry.TEACHER)));
        mEtGrade.setText(c.getString(c.getColumnIndex(ScreeningsEntry.GRADE)));
        c.close();
    }

    private void setUpSpinners(){
        ArrayAdapter<CharSequence> hearingAdapter = ArrayAdapter.createFromResource(mContext, R.array.pass_fail, R.layout.custom_spinner_layout);
        hearingAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        mSpnHearing.setAdapter(hearingAdapter);

        ArrayAdapter<CharSequence> visionAdapter = ArrayAdapter.createFromResource(mContext, R.array.pass_fail, R.layout.custom_spinner_layout);
        visionAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        mSpnVision.setAdapter(visionAdapter);
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
            mEtAgeYears.setText("" + yearsAgo(mEtDateOfBirth));
            mEtAgeMonths.setText("" + monthsOfAge(mEtDateOfBirth));
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
                Log.i(TAG,"OnGlobalLayout called#################3##");

                int heightDiff = mSvStudentInfo.getRootView().getHeight() - mSvStudentInfo.getHeight();
                mKeyboardUp = (heightDiff > 400);
                Log.i(TAG,"height diff= " +heightDiff);
            }
        });
    }

    private void setupDatePickers() {

        mEtDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mEtDateOfBirth.getText().toString().equals("")) {
                        mNextView = mEtTeacher;
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

        if (mEtAgeYears.getText().toString().equals("")){
            processError(mContext.getString(R.string.error_age), mEtAgeYears);
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

    private long monthsOfAge (EditText et){
        Log.i(TAG,"daysAgo= " + daysAgo(et) + "days % 365=" + (daysAgo(et) % 365));
        return (daysAgo(et) % 365L)/30L;
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
