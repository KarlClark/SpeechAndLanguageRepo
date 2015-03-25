package com.neuroleap.speachandlanguage.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neuroleap.speachandlanguage.R;

/**
 * Created by Karl on 3/20/2015.
 */
public class SplashFragment_1 extends BaseFragment {

    private static final int DISPLAY_TIME = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash_1, container, false);
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
            mOnFragmentInteractionListener.onFragmentInteraction(mId);
            }
        }, DISPLAY_TIME*1000);
    }
}
