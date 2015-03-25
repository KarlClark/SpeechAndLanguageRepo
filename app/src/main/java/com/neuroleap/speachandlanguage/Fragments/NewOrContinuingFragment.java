package com.neuroleap.speachandlanguage.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.neuroleap.speachandlanguage.R;

/**
 * Created by Karl on 3/25/2015.
 */
public class NewOrContinuingFragment extends BaseFragment {

    Button mBtnContinuing, mBtnNew;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_or_continuing, container, false);

        mBtnContinuing = (Button)v.findViewById(R.id.btnContinuing);
        mBtnContinuing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mId, true);
            }
        });

        mBtnNew = (Button)v.findViewById(R.id.btnNew);
        mBtnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.onFragmentInteraction(mId, false);
            }
        });

        return v;
    }
}
