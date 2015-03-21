package com.neuroleap.speachandlanguage.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.neuroleap.speachandlanguage.R;

/**
 * Created by Karl on 3/21/2015.
 */
public class PrepositionsFragment_fm extends Fragment {
    private static final String TAG ="## My Info ##";
    Button mBtnOne, mBtnZero;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_prepositions_fm, container,false);
        mBtnOne=(Button)v.findViewById(R.id.btnOne);
        mBtnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "One button clicked");
            }
        });

        mBtnZero=(Button)v.findViewById(R.id.btnZero);
        mBtnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Zero button clicked");
            }
        });

        return v;
    }

}
