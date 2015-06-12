package com.neuroleap.speachandlanguage.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.neuroleap.speachandlanguage.Listeners.OnIconButtonClickedListener;
import com.neuroleap.speachandlanguage.Models.AnswerIcon;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.util.ArrayList;

/**
 * Created by Karl on 4/21/2015.
 * Adapter for the GridView that holds the answer icon buttons.
 */
public class IconAnswersGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<AnswerIcon> mAnswerIcons;
    private OnIconButtonClickedListener mOnIconButtonClickedListener;
    private boolean mFirstTime = true;
    private Drawable mDefaultBackground;
    private Resources mResources;
    private static final String IdTag = "idtag";
    private static final String TAG = "## My Info ##";

    public IconAnswersGridViewAdapter(Context context, OnIconButtonClickedListener onIconButtonClickedListener, ArrayList<AnswerIcon> answerIcons){
        mContext = context;
        mAnswerIcons = answerIcons;
        //The following listener is implemented by QuestionBaseFragment so it can tell
        //which icon buttons have be pressed by the user.
        mOnIconButtonClickedListener = onIconButtonClickedListener;
        mResources = mContext.getResources();
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
                //Save the background of a normal container so we can reset the background if
                //the user double clicks a button.
                mDefaultBackground = convertView.findViewById(R.id.rlIconContainer).getBackground();
                mFirstTime = false;
            }
        }
        //get resource id of file that has picture for this ImageButton.
        int resId = mResources.getIdentifier(mAnswerIcons.get(position).getFilename(), "drawable", mContext.getPackageName());
        //Log.i(TAG, "IconAnswerGridViewAdapter filename= " + mAnswerIcons.get(position).getFilename() + "  Position= " + position + "  resid = " + resId);
        ibIcon= (ImageButton)convertView.findViewById(R.id.ibIcon);
        ibIcon.setImageBitmap(Utilities.decodeSampledBitmapFromResource(mResources, resId, 50, 50)); //Load a bitmap probably smaller than the file.

        //Changing the color of the button's container creates a highlighted frame around it.
        RelativeLayout rlIconContainer = (RelativeLayout)convertView.findViewById(R.id.rlIconContainer);
        if (mAnswerIcons.get(position).isClicked()){
            rlIconContainer.setBackgroundColor(mContext.getResources().getColor(R.color.green));
        } else {
            rlIconContainer.setBackgroundDrawable(mDefaultBackground);
        }

        //Store the AnswerIcon model object for this button in the button's tag so it can be retrieved later.
        ibIcon.setTag(mAnswerIcons.get(position));

        ibIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((AnswerIcon)v.getTag()).isClicked()){
                    //Button was clicked before do un-click it
                    ((RelativeLayout)v.getParent()).setBackgroundDrawable(mDefaultBackground);
                    ((AnswerIcon)v.getTag()).setClicked(false);
                }else {
                    ((RelativeLayout)v.getParent()).setBackgroundColor(mContext.getResources().getColor(R.color.green));
                    ((AnswerIcon)v.getTag()).setClicked(true);
                }

                //Tell listener (QuestionBaseFragment) which AnswerIcon was selected by
                //passing back the tag that was set above.
                mOnIconButtonClickedListener.onIconButtonClicked((AnswerIcon) v.getTag());
            }
        });
        return convertView;
    }

    public void nullOutListener(){
        mOnIconButtonClickedListener = null;
    }

}
