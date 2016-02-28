package com.neuroleap.speachandlanguage.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.neuroleap.speachandlanguage.Models.Question;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * Created by Karl on 4/7/2015.
 * Adapter used by the fragment PageViewer.
 */
public class QuestionFragmentPagerAdapter extends FragmentNoSavedStatePagerAdapter {

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
            //Use fragment filename to get the fragment class.
            myFragmentClass = Class.forName(Utilities.getPackageName() +".Fragments." + mQuestions.get(position).getFragmentName());
            //Use the class to get the newInstance method.
            Method m = myFragmentClass.getMethod("newInstance", Integer.class, Integer.class, Long.class, Integer.class, Integer.class);
            //Invoke newInstance method to create fragment object.
            frag = m.invoke(null, mQuestions.get(position).getId(), mScreeningId, mQuestions.get(position).getScreeningCategoryId(), position, mQuestions.get(position).getGroupPosition());
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
