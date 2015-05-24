package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;
import com.neuroleap.speachandlanguage.Utility.Utilities;

/**
 * Created by Karl on 4/6/2015.
 */
public class SemanticsBodyPartsFragment extends QuestionsBaseFragment {


    private ImageView mIvPicture;
    //private ArrayList<String> mIconFilenames = new ArrayList<String>();

    public static SemanticsBodyPartsFragment newInstance(Integer questionId, Integer screeningId, Long screeningCategoryId, Integer pageViewerPosition, Integer groupPosition){

        SemanticsBodyPartsFragment fragment = new SemanticsBodyPartsFragment();
        fragment.setArguments(createBundle(questionId, screeningId, screeningCategoryId, pageViewerPosition, groupPosition));
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.question_one_picture, container, false);

        setupBaseViews(v, 1);
        setupWindow();

        mIvPicture = (ImageView)v.findViewById(R.id.ivPicture);

        Cursor filenameCursor = DbCRUD.getPictureFilenames(mQuestionId);
        filenameCursor.moveToNext();
        String drawableFileName = filenameCursor.getString(0);
        filenameCursor.close();
        int resId = mResources.getIdentifier(drawableFileName, "drawable", mContext.getPackageName());
        mIvPicture.setImageBitmap(Utilities.decodeSampledBitmapFromResource(mResources, resId, 200, 200));

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BitmapDrawable)mIvPicture.getDrawable()).getBitmap().recycle();
    }

    @Override
    protected boolean answerCorrect(){
        String answer = Utilities.toLowerCaseAndTrim(mEtAnswers.get(0).getText().toString());
        Cursor c = DbCRUD.getValidAnswersInCorrectLanguage(mQuestionId);
        c.moveToNext();
        boolean b = (c.getString(0).equals(answer));
        Log.i(TAG, "answerCorrect= " + b + "  valid answer= " + c.getString(0) + "  student answer= " + mEtAnswers.get(0).getText());
        c.close();
        return b;
    }
}
