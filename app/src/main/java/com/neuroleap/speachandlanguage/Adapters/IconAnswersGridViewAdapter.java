package com.neuroleap.speachandlanguage.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.neuroleap.speachandlanguage.Listeners.OnIconButtonClickedListener;
import com.neuroleap.speachandlanguage.Models.AnswerIcon;
import com.neuroleap.speachandlanguage.R;

import java.util.ArrayList;

/**
 * Created by Karl on 4/21/2015.
 */
public class IconAnswersGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<AnswerIcon> mAnswerIcons;
    private OnIconButtonClickedListener mOnIconButtonClickedListener;
    private boolean mFirstTime = true;
    private Drawable mDefaultBackground;
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
            if (mFirstTime){
                mDefaultBackground = convertView.findViewById(R.id.rlIconContainer).getBackground();
                mFirstTime = false;
            }
        }
        int resId = mContext.getResources().getIdentifier(mAnswerIcons.get(position).getFilename(), "drawable", mContext.getPackageName());
        Log.i(TAG, "IconAnswerGridViewAdapter filename= " + mAnswerIcons.get(position).getFilename() + "  Position= " + position + "  resid = " + resId);
        ibIcon= (ImageButton)convertView.findViewById(R.id.ibIcon);
        ibIcon.setImageResource(resId);
        RelativeLayout rlIconContainer = (RelativeLayout)convertView.findViewById(R.id.rlIconContainer);
        if (mAnswerIcons.get(position).isClicked()){
            Log.i(TAG, "position " + position + " is clicked");
            rlIconContainer.setBackgroundColor(mContext.getResources().getColor(R.color.green));
        } else {
            rlIconContainer.setBackgroundDrawable(mDefaultBackground);
        }
        ibIcon.setTag(mAnswerIcons.get(position));

        ibIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.setBackgroundResource(android.R.drawable.button_onoff_indicator_on);
                //v.setPressed(true);
                if (((AnswerIcon)v.getTag()).isClicked()){
                    ((RelativeLayout)v.getParent()).setBackgroundDrawable(mDefaultBackground);
                    ((AnswerIcon)v.getTag()).setClicked(false);
                }else {
                    ((RelativeLayout)v.getParent()).setBackgroundColor(mContext.getResources().getColor(R.color.green));
                    ((AnswerIcon)v.getTag()).setClicked(true);
                }

                mOnIconButtonClickedListener.onIconButtonClicked((AnswerIcon) v.getTag());
            }
        });

        /*ibIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //v.setPressed(true);
                //mOnIconButtonClickedListener.onIconButtonClicked((Long) v.getTag());
                //v.performClick();
                Log.i(TAG, "is Pressed = " + v.isPressed());
                return true;
            }
        });*/
        return convertView;
    }
}
