package com.neuroleap.speachandlanguage.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 3/30/2015.
 */
public class WhichModeFragment extends BaseFragment {

    Button mBtnScoringButtons, mBtnTextOnly, mBtnBoth;
    private static final String TAG = "## My Info ##";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_which_mode, container, false);
        mBtnScoringButtons = (Button)v.findViewById(R.id.btnScoringButtons);
        mBtnScoringButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mId, Utilities.SCORING_BUTTONS_ONLY);
            }
        });

        mBtnTextOnly = (Button)v.findViewById(R.id.btnTexrtOnly);
        mBtnTextOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Text only button pressed");
                mOnFragmentInteractionListener.onFragmentInteraction(mId, Utilities.TEXT_INPUT_ONLY);
            }
        });

        mBtnBoth = (Button)v.findViewById(R.id.btnBoth);
        mBtnBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mId, Utilities.BOTH_SCORING_BUTTONS_AND_TEXT);
            }
        });

        return v;
    }
}
