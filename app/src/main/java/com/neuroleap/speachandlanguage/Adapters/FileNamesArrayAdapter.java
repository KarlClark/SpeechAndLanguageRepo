package com.neuroleap.speachandlanguage.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.R;

import java.io.File;

/**
 * Created by Karl on 6/9/2015.
 */
public class FileNamesArrayAdapter extends ArrayAdapter {

    private Context mContext;
    private File[] mFiles;
    private static final String TAG = "## My Info ##";

    public FileNamesArrayAdapter (Context context, File[] files){
        super(context, R.layout.list_item_audio_files, files);
        mContext = context;
        mFiles = files;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item_audio_files, parent, false);
        }

        TextView tvFileName = (TextView)convertView.findViewById(R.id.tvFileName);
        TextView tvDuration = (TextView)convertView.findViewById((R.id.tvDuration));

        tvFileName.setText(mFiles[position].getName());
        MediaPlayer mp = MediaPlayer.create(mContext, Uri.parse(mFiles[position].getAbsolutePath()));
        int iDuration = mp.getDuration()/1000;  //seconds
        int hours = iDuration/3600;
        int mins = (iDuration/60) % 60;
        int secs = iDuration % 60;
        //Log.i(TAG , "iDuration= " + iDuration + "  hours= " + hours + "  minutes= " + mins + "  seconds= " +secs);
        String sDuration = "";
        if (hours > 0){
            sDuration = "" + hours + ":";
        }

        if (mins > 0 || hours > 0) {
            sDuration = sDuration + mins + ":";
        }
        sDuration = sDuration + secs;
        tvDuration.setText(sDuration);
        mp.reset();
        mp.release();
        mp=null;

        return convertView;
    }
}
