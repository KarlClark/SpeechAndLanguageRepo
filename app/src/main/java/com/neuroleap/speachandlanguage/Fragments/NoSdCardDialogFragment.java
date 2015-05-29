package com.neuroleap.speachandlanguage.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.neuroleap.speachandlanguage.Activities.FlowControlActivity;
import com.neuroleap.speachandlanguage.R;

/**
 * Created by Karl on 5/28/2015.
 */
public class NoSdCardDialogFragment extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_no_sdcard,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(v);

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((FlowControlActivity)getActivity()).onNoSdCardDialogYesClick();
            }
        });

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((FlowControlActivity)getActivity()).onNoSdCardDialogNoClick();
            }
        });

        return alertDialogBuilder.create();
    }
}
