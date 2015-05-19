package com.neuroleap.speachandlanguage.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neuroleap.speachandlanguage.R;

/**
 * Created by Karl on 3/20/2015.
 */
public class SplashFragment_1 extends BaseFragment {

    private static final int DISPLAY_TIME = 2;
    private static final String ID_KEY = " id_key";
    private static final String TAG = "## My Info ##";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            mId=savedInstanceState.getInt(ID_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash_1, container, false);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ID_KEY,mId);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "SplashFragment_1 onResume called");

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Log.i(TAG,"SpoashFragment_1 calling listener");
                mOnFragmentInteractionListener.onFragmentInteraction(mId);
            }
        }, DISPLAY_TIME*1000);
    }

}
