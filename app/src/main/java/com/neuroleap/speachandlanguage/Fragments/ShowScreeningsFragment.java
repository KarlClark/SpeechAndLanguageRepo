package com.neuroleap.speachandlanguage.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.neuroleap.speachandlanguage.Adapters.ScreeningsArrayAdapter;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ScreeningsEntry;
import com.neuroleap.speachandlanguage.Listeners.OnAlertDialogListener;
import com.neuroleap.speachandlanguage.Models.Screening;
import com.neuroleap.speachandlanguage.R;
import com.neuroleap.speachandlanguage.Utility.DbCRUD;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Karl on 4/2/2015.
 */
public class ShowScreeningsFragment extends BaseFragment implements OnAlertDialogListener {

    private ArrayList<Screening> mScreenings = new ArrayList<>();
    private ListView mLvScreenings;
    private ScreeningsArrayAdapter mScreeningsArrayAdapter;
    private TextView mTvNoScreenings;
    private int mPositionToDelete;
    private static final String TAG = "## My Info ##";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_show_screenings, container, false);
        mLvScreenings = (ListView)v.findViewById(R.id.lvScreenings);
        mTvNoScreenings = (TextView)v.findViewById((R.id.tvNoScreenings));
        fillList();
        mScreeningsArrayAdapter = new ScreeningsArrayAdapter(getActivity(), mScreenings);
        mLvScreenings.setAdapter(mScreeningsArrayAdapter);
        mLvScreenings.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Screening long click called");
                mPositionToDelete = position;
                AlertDialogFragment diaFrag = AlertDialogFragment.newInstance(R.string.delete_screening, 0, R.string.yes, R.string.no, 0);
                diaFrag.setTargetFragment(ShowScreeningsFragment.this, 0);
                diaFrag.show(getActivity().getSupportFragmentManager(), "dialog");
                return true;
            }
        });
        return v;
    }

    private void fillList(){
        Cursor c = DbCRUD.getShortScreens();
        mScreenings.clear();
        if (c.getCount() > 0 ){
            mTvNoScreenings.setVisibility(View.GONE);
            while (c.moveToNext()) {
                mScreenings.add(new Screening(c.getInt(c.getColumnIndex(ScreeningsEntry._ID)),
                                              c.getLong(c.getColumnIndex(ScreeningsEntry.STUDENT_ID)),
                                              c.getString(c.getColumnIndex(StudentsEntry.FIRST_NAME)),
                                              c.getString(c.getColumnIndex(StudentsEntry.LAST_NAME)),
                                              c.getInt(c.getColumnIndex(ScreeningsEntry.AGE)),
                                              c.getString(c.getColumnIndex(ScreeningsEntry.TEACHER)),
                                              c.getLong(c.getColumnIndex(ScreeningsEntry.TEST_DATE)),
                                              c.getInt(c.getColumnIndex(ScreeningsEntry.COMPLETION_STATE))));
            }
        }else{
            mTvNoScreenings.setVisibility(View.VISIBLE);
        }
        c.close();
    }

    @Override
    public void onAlertDialogPositiveClick(int tag){
        Log.i(TAG, "onAlertDialogPositiveClick, mPositionToDelete= " + mPositionToDelete);
        String underscoreName= mScreenings.get(mPositionToDelete).getFirstName() + "_" + mScreenings.get(mPositionToDelete).getLastName();
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), getString(R.string.neuro_underscore_leap) + "/" + underscoreName);
        File[] audioFiles = mediaStorageDir.listFiles();
        if (audioFiles != null) {
            for (File f : audioFiles) {
                f.delete();
            }
        }
        mediaStorageDir.delete();

        DbCRUD.deleteScreening(mScreenings.get(mPositionToDelete).getId(), mScreenings.get(mPositionToDelete).getStudentId());
        fillList();
        mScreeningsArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAlertDialogNegativeClick(int tag){
        Log.i(TAG, "onAlertDialogNegativeClick");
    }
}
