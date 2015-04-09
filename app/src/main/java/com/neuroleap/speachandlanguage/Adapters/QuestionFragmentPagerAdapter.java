package com.neuroleap.speachandlanguage.Adapters;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionCategoriesEntry;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by Karl on 4/7/2015.
 */
public class QuestionFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private int mScreeningId;
    private static final String TAG = "## My Info ##";

    public QuestionFragmentPagerAdapter(FragmentManager fm, int screeningId) {
        super(fm);
        mScreeningId = screeningId;
    }

    @Override
    public Fragment getItem(int position) {
        int questionId = position+1;
        int categoryId = DbCRUD.getQuestionCategory(questionId);
        Cursor c = DbCRUD.getFragmentNames(categoryId);
        c.moveToNext();
        String fragmentName;
        if (Utilities.getTestMode() == Utilities.SCORING_BUTTONS_ONLY){
            fragmentName=c.getString(c.getColumnIndex(QuestionCategoriesEntry.STUDENT_MODE_FRAGMENT));
        }else{
            fragmentName=c.getString(c.getColumnIndex(QuestionCategoriesEntry.FACILITATOR_MODE_FRAGMENT));
        }
        c.close();
        Object frag = null;
        Class myFragmentClass;
        try {
            myFragmentClass = Class.forName(Utilities.getPackageName() +".Fragments." + fragmentName);
            Method m = myFragmentClass.getMethod("newInstance", Integer.class, Integer.class);
            frag = m.invoke(null, questionId, mScreeningId);
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
        return Utilities.getTotalQuestions();
    }
}
