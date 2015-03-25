package com.neuroleap.speachandlanguage.Fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.neuroleap.speachandlanguage.OnFragmentInteractionListener;

/**
 * Created by Karl on 3/24/2015.
 */
public class BaseFragment extends Fragment {
    protected Context mContext;
    protected OnFragmentInteractionListener mOnFragmentInteractionListener;
    protected int mId;
    private static final String TAG ="## My Info ##";

   public void setId(int id){
       mId = id;
   }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mOnFragmentInteractionListener = (OnFragmentInteractionListener)activity;
    }

}
