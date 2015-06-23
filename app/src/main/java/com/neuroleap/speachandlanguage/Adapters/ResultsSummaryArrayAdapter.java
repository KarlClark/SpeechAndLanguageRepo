package com.neuroleap.speachandlanguage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Models.ScreeningCategoryResult;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Karl on 5/12/2015.
 */
public class ResultsSummaryArrayAdapter extends ArrayAdapter<ScreeningCategoryResult>{

    Context mContext;
    ArrayList<ScreeningCategoryResult> mScreeningCategoryResults;
    DecimalFormat mRound0 = new DecimalFormat("#");
    private static final String TAG = "## My Info ##";

    public ResultsSummaryArrayAdapter(Context context, ArrayList<ScreeningCategoryResult> screeningCategoryResults){
        super(context, R.layout.fragment_results_summary, screeningCategoryResults);
        mContext = context;
        mScreeningCategoryResults = screeningCategoryResults;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item_results_summary, parent, false);
        }
        TextView tvCategory = (TextView)convertView.findViewById(R.id.tvCategory);
        TextView tvCompleted = (TextView)convertView.findViewById(R.id.tvCompleted);
        TextView tvResults = (TextView)convertView.findViewById(R.id.tvResults);
        TextView tvPercentage = (TextView)convertView.findViewById(R.id.tvPercentage);
        TextView tvPassFail = (TextView)convertView.findViewById(R.id.tvPassFail);

        ScreeningCategoryResult scr = mScreeningCategoryResults.get(position);
        if (Utilities.getAppLanguage() == Utilities.ENGLISH) {
            //Log.i(TAG, "ResultSummaryArrayAdapter position = " + position);
            tvCategory.setText(scr.getScreeningCategoryNameEg());
        }else{
            tvCategory.setText(scr.getScreeningCategoryNameSp());
        }
        if (scr.isCompleted()) {
            tvCompleted.setText(mContext.getString(R.string.completed));
        }else{
            tvCompleted.setText(mContext.getString(R.string.not_completed));
        }
        tvResults.setText("" + mRound0.format(scr.getNumberCorrectAnswers()) + "/" + mRound0.format(scr.getNumberOfQuestions()));
        tvPercentage.setText("" + mRound0.format(scr.getPercentCorrect()) + "%");
        if (scr.isPassed()){
            tvPassFail.setText(mContext.getString(R.string.passed));
        }else{
            tvPassFail.setText(mContext.getString(R.string.failed));
        }
        return convertView;
    }
}
