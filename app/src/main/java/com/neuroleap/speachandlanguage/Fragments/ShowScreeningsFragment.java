package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    ListView mLvScreenings;
    ScreeningsArrayAdapter mScreeningsArrayAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_show_screenings, container, false);
        mLvScreenings = (ListView)v.findViewById(R.id.lvScreenings);
        fillList();
        mScreeningsArrayAdapter = new ScreeningsArrayAdapter(mContext, mScreenings);
        mLvScreenings.setAdapter(mScreeningsArrayAdapter);
        return v;
    }

    private void fillList(){
        Cursor c = DbCRUD.getShortScreens();
        if (c.getCount() > 0 ){
            while (c.moveToNext()) {
                mScreenings.add(new Screening(c.getString(0), c.getString(1), c.getString(2), c.getLong(3),c.getInt(4)));
            }
        }else{
            mScreenings.add(new Screening ("No screens", "", "", 0L, -1));
        }
    }
}
