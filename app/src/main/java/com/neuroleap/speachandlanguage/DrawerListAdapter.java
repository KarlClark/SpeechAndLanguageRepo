package com.neuroleap.speachandlanguage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Models.Question;
import com.neuroleap.speachandlanguage.Models.QuestionCategory;

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
        Log.i(TAG,"categories size= " + categories.size());
    }

    @Override
    public int getGroupCount() {
        Log.i(TAG, "group count= " + mCategories.size());
        return mCategories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.i(TAG, "Child count = " + mQuestions.get(groupPosition).size());
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_group, parent, false);
        }
        Log.i(TAG,"here 1");
        TextView tvGroupItem = (TextView)convertView.findViewById(R.id.tvGroupItem);
        tvGroupItem.setText(mCategories.get(groupPosition).getText());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }
        Log.i(TAG, "here 2");
        TextView tvListItem = (TextView)convertView.findViewById(R.id.tvListItem);
        Log.i(TAG ,"text = " +mQuestions.get(groupPosition).get(childPosition).getText() );
        tvListItem.setText(mQuestions.get(groupPosition).get(childPosition).getText());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
