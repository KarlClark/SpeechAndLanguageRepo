package com.neuroleap.speachandlanguage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Listeners.OnScreeningsListButtonsListener;
import com.neuroleap.speachandlanguage.Models.Screening;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.Utilities;

import java.util.ArrayList;

/**
 * Created by Karl on 4/2/2015.
 * Adapter use for the list of screenings.
 */
public class ScreeningsArrayAdapter extends ArrayAdapter<Screening> {
    Context mContext;
    ArrayList<Screening> mScreenings;
    OnScreeningsListButtonsListener mOnScreeningsListButtonsListener;

    public ScreeningsArrayAdapter(Context context, ArrayList<Screening> screenings) {
        super(context, R.layout.list_screenings_item, screenings);
        mContext = context;
        mOnScreeningsListButtonsListener = (OnScreeningsListButtonsListener)context;
        mScreenings = screenings;
    }

    @Override
    public long getItemId(int position) {
        return mScreenings.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_screenings_item, parent, false);
        }

        //Get the views and fill them in with data from the Screening model.
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        TextView tvDate = (TextView)convertView.findViewById(R.id.tvDate);
        TextView tvTeacher = (TextView)convertView.findViewById(R.id.tvTeacher);
        TextView tvScreeningState = (TextView)convertView.findViewById(R.id.tvScreeningState);
        Button btnResults =(Button)convertView.findViewById(R.id.btnResults);
        Button btnOverview =(Button)convertView.findViewById(R.id.btnOverview);
        Button btnQuestions = (Button)convertView.findViewById(R.id.btnQuestions);
        Button btnProfile = (Button)convertView.findViewById(R.id.btnProfile);

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

        //Each list item has four buttons. For each button store the Screening object in its
        //tag.  Then set an onClick listener on it. In the onClick listener call the appropriate
        //method from the mOnScreeningsListButtonsListener that was passed in in the constructor.
        //In this case the listener will actually be the Activity hosting the fragment that is
        //using this adapter.
        btnResults.setTag(screening);
        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             mOnScreeningsListButtonsListener.onScreeningResultsButtonClicked((Screening)v.getTag());
            }
        });

        btnQuestions.setTag(screening);
        btnQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnScreeningsListButtonsListener.onScreeningQuestionsButtonClicked((Screening)v.getTag());
            }
        });

        btnOverview.setTag(screening);
        btnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnScreeningsListButtonsListener.onScreeningOverviewButtonClicked((Screening)v.getTag());
            }
        });

        btnProfile.setTag(screening);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnScreeningsListButtonsListener.onScreeningProfileButtonClicked((Screening)v.getTag());
            }
        });

        return convertView;
    }
}
