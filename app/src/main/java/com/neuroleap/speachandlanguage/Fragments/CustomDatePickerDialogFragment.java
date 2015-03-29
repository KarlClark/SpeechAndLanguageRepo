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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
// ...

public class CustomDatePickerDialogFragment extends DialogFragment {

    private TextView tvDateText;
    private DatePicker mDatePicker;
    private String mTitle = "";
    private EditText mField;
    private SimpleDateFormat mDateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    OnCustomDateDialogClickedListener mOnCustomDateDialogClickedListener;
    public static final String TITLE_TAG ="title=";
    public static final String FIELD_TAG ="field";
    private static final String TAG = "## My Info ##";

    public CustomDatePickerDialogFragment() {
        // Empty constructor required for DialogFragment
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
        mDatePicker=(DatePicker)v.findViewById(R.id.datePicker);
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
                mOnCustomDateDialogClickedListener.onCustomDateDialogClicked();
            }
        });
        return alertDialogBuilder.create();
    }

}