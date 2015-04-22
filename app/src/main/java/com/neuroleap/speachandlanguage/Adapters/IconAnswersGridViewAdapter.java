package com.neuroleap.speachandlanguage.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.neuroleap.speachandlanguage.R;

import java.util.ArrayList;

/**
 * Created by Karl on 4/21/2015.
 */
public class IconAnswersGridViewAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> mIconFilenames;
    private static final String TAG = "## My Info ##";

    public IconAnswersGridViewAdapter(Context context, ArrayList<String> iconFilenames){
        mContext = context;
        mIconFilenames = iconFilenames;
    }

    @Override
    public int getCount() {
       return  mIconFilenames.size();
    }

    @Override
    public Object getItem(int position) {
        return mIconFilenames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageButton ibIcon;
        if (convertView == null) {
            /*imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);*/
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_icon_item, parent, false);
        }/*else{
            imageView = (ImageView)convertView;
        }*/
        int resId = mContext.getResources().getIdentifier(mIconFilenames.get(position), "drawable", mContext.getPackageName());
        Log.i(TAG,"IconAnswerGradViewAdapter filename= " + mIconFilenames.get(position)+ "  Position= " + position + "  resid = " + resId);
        ibIcon= (ImageButton)convertView.findViewById(R.id.ibIcon);
        ibIcon.setImageResource(resId);
        return convertView;
    }
}
