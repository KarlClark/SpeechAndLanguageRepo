package com.neuroleap.speachandlanguage.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.neuroleap.speachandlanguage.Models.Question;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * Created by Karl on 4/7/2015.
 */
public class QuestionFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private int mScreeningId;
    private ArrayList<Question> mQuestions;
    private static final String TAG = "## My Info ##";

    public QuestionFragmentPagerAdapter(FragmentManager fm, int screeningId, ArrayList<Question> questions) {
        super(fm);
        mScreeningId = screeningId;
        mQuestions = questions;
    }

    @Override
    public Fragment getItem(int position) {

        Object frag = null;
        Class myFragmentClass;
        try {
            myFragmentClass = Class.forName(Utilities.getPackageName() +".Fragments." + mQuestions.get(position).getFragmentName());
            Method m = myFragmentClass.getMethod("newInstance", Integer.class, Integer.class, Integer.class);
            frag = m.invoke(null, mQuestions.get(position).getId(), mScreeningId, position);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (Fragment)frag;
    }

    @Override
    public int getCount() {
        //Log.i(TAG, "Question count= " + Utilities.getTotalQuestions());
        return mQuestions.size();
    }
}
