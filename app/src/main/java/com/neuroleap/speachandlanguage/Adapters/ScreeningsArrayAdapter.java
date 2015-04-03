package com.neuroleap.speachandlanguage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Models.Screening;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.util.ArrayList;

/**
 * Created by Karl on 4/2/2015.
 */
public class ScreeningsArrayAdapter extends ArrayAdapter<Screening> {
    Context mContext;
    ArrayList<Screening> mScreenings;

    public ScreeningsArrayAdapter(Context context, ArrayList<Screening> screenings) {
        super(context, R.layout.list_screenings_item, screenings);
        mContext = context;
        mScreenings = screenings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_screenings_item, parent, false);
        }
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        TextView tvDate = (TextView)convertView.findViewById(R.id.tvDate);
        TextView tvTeacher = (TextView)convertView.findViewById(R.id.tvTeacher);
        TextView tvScreeningState = (TextView)convertView.findViewById(R.id.tvScreeningState);
        Screening screening = mScreenings.get(position);
        tvName.setText(screening.getFirstName() + " " + mScreenings.get(position).getLastName());
        tvDate.setText(screening.getDisplayDate());
        tvTeacher.setText(screening.getTeacher());
        switch (screening.getCompletionState()){
            case Utilities.SCREENING_COMPLETED:
                tvScreeningState.setText(mContext.getString(R.string.screening_completed));
                break;
            case Utilities.SCREENING_NOT_COMPLETE:
                tvScreeningState.setText(mContext.getString(R.string.screening_not_completed));
                break;
            case Utilities.SCREENING_NOT_STARTED:
                tvScreeningState.setText(mContext.getString(R.string.screening_not_started));
                break;
            default:
                tvScreeningState.setText("");
        }
        return convertView;
    }
}
