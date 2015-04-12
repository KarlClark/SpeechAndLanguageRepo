package com.neuroleap.speachandlanguage.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Activities.FlowControlActivity;
import com.neuroleap.speachandlanguage.Adapters.ScreeningsArrayAdapter;
import com.neuroleap.speachandlanguage.Models.Screening;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;

import java.util.ArrayList;

/**
 * Created by Karl on 4/2/2015.
 */
public class ShowScreeningsFragment extends BaseFragment {

    private ArrayList<Screening> mScreenings = new ArrayList<>();
    private ListView mLvScreenings;
    private ScreeningsArrayAdapter mScreeningsArrayAdapter;
    private TextView mTvNoScreenings;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_show_screenings, container, false);
        mLvScreenings = (ListView)v.findViewById(R.id.lvScreenings);
        mTvNoScreenings = (TextView)v.findViewById((R.id.tvNoScreenings));
        fillList();
        mScreeningsArrayAdapter = new ScreeningsArrayAdapter(mContext, mScreenings);
        mLvScreenings.setAdapter(mScreeningsArrayAdapter);
        mLvScreenings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(mContext,FlowControlActivity.class);
                i.putExtra(FlowControlActivity.SCREENING_ID_KEY, (int)id);
                i.putExtra(FlowControlActivity.SCREENING_AGE_KEY, mScreenings.get(position).getAge());
                i.putExtra(FlowControlActivity.SCREENING_COMPLETION_STATE_KEY, mScreenings.get(position).getCompletionState());
                startActivity(i);
            }
        });
        return v;
    }

    private void fillList(){
        Cursor c = DbCRUD.getShortScreens();
        if (c.getCount() > 0 ){
            mTvNoScreenings.setVisibility(View.GONE);
            while (c.moveToNext()) {
                mScreenings.add(new Screening(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4),c.getLong(5), c.getInt(6)));
            }
            c.close();
        }else{
            mTvNoScreenings.setVisibility(View.VISIBLE);
        }
    }
}
