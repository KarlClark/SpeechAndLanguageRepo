package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
        mScreeningsArrayAdapter = new ScreeningsArrayAdapter(getActivity(), mScreenings);
        mLvScreenings.setAdapter(mScreeningsArrayAdapter);
        /*mLvScreenings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnFragmentInteractionListener.onFragmentInteraction(mId, (int)id, mScreenings.get(position).getFirstName(),mScreenings.get(position).getLastName());
            }
        });*/
        return v;
    }

    private void fillList(){
        Cursor c = DbCRUD.getShortScreens();
        if (c.getCount() > 0 ){
            mTvNoScreenings.setVisibility(View.GONE);
            while (c.moveToNext()) {
                mScreenings.add(new Screening(c.getInt(0), c.getLong(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5),c.getLong(6), c.getInt(7)));
            }
            c.close();
        }else{
            mTvNoScreenings.setVisibility(View.VISIBLE);
        }
    }
}
