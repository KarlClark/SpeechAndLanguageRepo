package com.neuroleap.speachandlanguage.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.neuroleap.speachandlanguage.Listeners.OnIconButtonClickedListener;
import com.neuroleap.speachandlanguage.Models.AnswerIcon;
import com.neuroleap.speachandlanguage.R;

import java.util.ArrayList;

/**
 * Created by Karl on 4/21/2015.
 */
public class IconAnswersGridViewAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<AnswerIcon> mAnswerIcons;
    OnIconButtonClickedListener mOnIconButtonClickedListener;
    private static final String IdTag = "idtag";
    private static final String TAG = "## My Info ##";

    public IconAnswersGridViewAdapter(Context context, OnIconButtonClickedListener onIconButtonClickedListener, ArrayList<AnswerIcon> answerIcons){
        mContext = context;
        mAnswerIcons = answerIcons;
        mOnIconButtonClickedListener = onIconButtonClickedListener;
    }

    @Override
    public int getCount() {
       return  mAnswerIcons.size();
    }

    @Override
    public Object getItem(int position) {
        return mAnswerIcons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageButton ibIcon;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_icon_item, parent, false);
        }
        int resId = mContext.getResources().getIdentifier(mAnswerIcons.get(position).getFilename(), "drawable", mContext.getPackageName());
        Log.i(TAG,"IconAnswerGradViewAdapter filename= " + mAnswerIcons.get(position).getFilename()+ "  Position= " + position + "  resid = " + resId);
        ibIcon= (ImageButton)convertView.findViewById(R.id.ibIcon);
        ibIcon.setImageResource(resId);
        ibIcon.setTag(mAnswerIcons.get(position).getAnswerIconId());
        ibIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnIconButtonClickedListener.onIconButtonClicked((Long)v.getTag());
            }
        });
        return convertView;
    }
}
