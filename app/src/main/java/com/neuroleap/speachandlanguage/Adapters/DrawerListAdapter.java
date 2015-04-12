package com.neuroleap.speachandlanguage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Models.Question;
import com.neuroleap.speachandlanguage.Models.QuestionCategory;
import com.neuroleap.speachandlanguage.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karl on 3/20/2015.
 */
public class DrawerListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<QuestionCategory> mCategories;
    private List <ArrayList<Question>> mQuestions;
    private static final String TAG = "## My Info ##";

    public DrawerListAdapter(Context context, List<QuestionCategory> categories, List<ArrayList<Question>> questions){
        mContext = context;
        mCategories = categories;
        mQuestions = questions;
    }

    @Override
    public int getGroupCount() {
        return mCategories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mQuestions.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCategories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mQuestions.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mCategories.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mQuestions.get(groupPosition).get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_group, parent, false);
        }
        TextView tvGroupItem = (TextView)convertView.findViewById(R.id.tvGroupItem);
        tvGroupItem.setText(mCategories.get(groupPosition).getText());
        ImageView ivSelector = (ImageView)convertView.findViewById(R.id.ivSelector);
        String drawableFileName;

            if(isExpanded){
                drawableFileName = "down_arrow";
            }else{
                drawableFileName = "right_arrow";
            }
        int resId = mContext.getResources().getIdentifier(drawableFileName, "drawable", mContext.getPackageName());
        ivSelector.setImageResource(resId);

        RelativeLayout rl = (RelativeLayout)convertView.findViewById(R.id.rlGroupItem);
        rl.setBackgroundResource(mCategories.get(groupPosition).getColor());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }
        TextView tvListItem = (TextView)convertView.findViewById(R.id.tvListItem);
        tvListItem.setText(mQuestions.get(groupPosition).get(childPosition).getText());
        int color = mQuestions.get(groupPosition).get(childPosition).getColor();
        tvListItem.setBackgroundResource(color);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
