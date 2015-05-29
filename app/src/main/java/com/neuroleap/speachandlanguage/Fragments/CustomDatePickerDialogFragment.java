package com.neuroleap.speachandlanguage.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Listeners.OnCustomDateDialogClickedListener;
import com.neuroleap.speachandlanguage.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/*
* This date picker can display a title describing which date is being picked.  This is
* important for this app because the user profile screen has four date fields on it.
* Since the date picker obscures the fields the user can't tell for sure which
* date he is entering.
 */

public class CustomDatePickerDialogFragment extends DialogFragment {

    private TextView tvDateText;
    private DatePicker mDatePicker;
    private String mTitle = "";
    private EditText mField;
    private SimpleDateFormat mDateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    private OnCustomDateDialogClickedListener mOnCustomDateDialogClickedListener;
    private int mTitleColor;
    private boolean mChangeColor = false;



    public static final String TITLE_TAG ="title=";
    public static final String FIELD_TAG ="field";
    private static final String TAG = "## My Info ##";


    public CustomDatePickerDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public void setTitleColor(int titleColor) {
        mTitleColor = titleColor;
        mChangeColor=true;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setField(EditText field) {
        mField = field;
    }

    public static CustomDatePickerDialogFragment newInstance(String title) {
        CustomDatePickerDialogFragment frag = new CustomDatePickerDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_TAG, title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        mTitle = b.getString(TITLE_TAG);
        mOnCustomDateDialogClickedListener=(OnCustomDateDialogClickedListener) getTargetFragment();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_custom_date_picker_dialog, null);
        View titleView = getActivity().getLayoutInflater().inflate(R.layout.custom_title, null);
        TextView tvTitle = (TextView)titleView.findViewById(R.id.tvCustomTitle);
        tvTitle.setText(mTitle);
        if(mChangeColor){
            Log.i(TAG, "date picker change color");
            tvTitle.setTextColor(mTitleColor);
        }
        mDatePicker=(DatePicker)v.findViewById(R.id.datePicker);
        if ( ! mField.getText().toString().equals("")){ //set the date picker to the date already in the field.
            SimpleDateFormat dateFormat = new SimpleDateFormat(StudentInfoFragment.DATE_FORMAT_STRING);
            try {
                Date date = dateFormat.parse(mField.getText().toString());
                Calendar fieldDate = Calendar.getInstance();
                fieldDate.setTime(date);
                Log.d(TAG, "date= " +date);
                Log.i(TAG, "Year= " +fieldDate.YEAR + "  month= " + fieldDate.MONTH + "  day= " + fieldDate.DAY_OF_MONTH);
                mDatePicker.updateDate(fieldDate.get(Calendar.YEAR), fieldDate.get(Calendar.MONTH), fieldDate.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(v);
        alertDialogBuilder.setCustomTitle(titleView);
        String buttonTitle=getResources().getString(R.string.done);
        alertDialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "CustomDatePickerFragment onClick called");
                Calendar pickedDate = Calendar.getInstance();
                pickedDate.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                mField.setText(mDateFormatter.format(pickedDate.getTime()));
                Log.i(TAG,"Date of birth from picker =" + mField.getText());
                mOnCustomDateDialogClickedListener.onCustomDateDialogClicked(mField);
            }
        });
        return alertDialogBuilder.create();
    }

}