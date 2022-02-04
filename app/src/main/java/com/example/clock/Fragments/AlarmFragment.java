package com.example.clock.Fragments;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.clock.provider.AlarmContract.AlarmEntry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clock.Adapters.AlarmCursorAdapter;
import com.example.clock.NewAlarmActivity;
import com.example.clock.R;
import com.example.clock.provider.AlarmContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class AlarmFragment extends Fragment{
//        implements AlarmAdapter.OnAlarmClickListener, LoaderManager.LoaderCallbacks<Cursor>

    private FloatingActionButton addAlarm;
    //    private RecyclerView recyclerView;
    private ListView listView;
//    private AlarmAdapter adapter;
    private ArrayList<String> list;
    private AlarmCursorAdapter cursorAdapter;
    private TextView hiddenPanel;
//    private AlarmAdapter.OnAlarmClickListener onAlarmClickListener;

    private final int LOADER_ID = 1;
    final String TAG = AlarmFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
//        addAlarm = view.findViewById(R.id.add_alarm_fab);

//        cursorAdapter = new AlarmCursorAdapter(getContext(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
//
//
//        hiddenPanel = (TextView) view.findViewById(R.id.slide_up);
////        database = AlarmDatabase.getInstance(getContext());
//        //initialize recycler view and adapter
//        list = new ArrayList<>();
////        recyclerView = view.findViewById(R.id.alarm_list);
//        listView = view.findViewById(R.id.alarm_list);
////        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
////        recyclerView.setLayoutManager(linearLayoutManager);
////        adapter = new AlarmAdapter(this);
////        recyclerView.setAdapter(cursorAdapter);
//
//        listView.setAdapter(cursorAdapter);
////        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), "" + l, Toast.LENGTH_SHORT).show();
//                Log.v(TAG, "clicked");
//                Switch s = view.findViewById(R.id.alarm_switch);
////                s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////                    @Override
////                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                        if (b) {
////
////                            Toast.makeText(getContext(), "true " + l, Toast.LENGTH_SHORT).show();
////                        } else {
////
////                            Toast.makeText(getContext(), "false " + l, Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                });
//
//            }
//        });
//
//
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//        });
//
//        //onclick listener for FAB
//        addAlarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), NewAlarmActivity.class);
//                startActivity(intent);
//            }
//        });
//
////        LoaderManager.getInstance(getContext()).initLoader(LOADER_ID, null, getContext());
//        LoaderManager.getInstance(this).initLoader(1, null, this);
////        updateData();
        return view;
    }

//
//    @Override
//    public void onResume() {
//        super.onResume();
////        updateData();
////        LoaderManager.getInstance(this).restartLoader(1, null, this);
//    }
//
//
//    //method to retrieve data from database
//    void updateData() {
//        Cursor cursor = getContext().getContentResolver().query(AlarmContract.AlarmEntry.CONTENT_URI, null, null, null, AlarmContract.AlarmEntry.TIME);
//        adapter.swapCursor(cursor);
//    }
//
//    @Override
//    public void onAlarmClick(int id) {
//        Log.v(TAG, "" + id);
//    }
//
//    @Override
//    public void onLongPress(View view, int id) {
//        Log.v(TAG, "long press " + id);
//        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
//                R.anim.bottom_up);
////        ViewGroup hiddenPanel = (ViewGroup) findViewById(R.id.slide_up);
//        hiddenPanel.startAnimation(bottomUp);
//        hiddenPanel.setVisibility(View.VISIBLE);
//
//    }
//
//    @Override
//    public void switchClick(int id, boolean b) {
//
//        if (id != 0) {
//
//            Uri uri = ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, id);
//            if (b) {
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmEntry.ALARM_ACTIVE);
//                getContext().getContentResolver().update(uri, contentValues, null, null);
//
//                Toast.makeText(getContext(), "switch on for " + id, Toast.LENGTH_SHORT).show();
//            } else {
//
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmEntry.ALARM_INACTIVE);
//                getContext().getContentResolver().update(uri, contentValues, null, null);
//
//
//                Toast.makeText(getContext(), "switch off for " + id, Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    }
//
//    @NonNull
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
//
//        String projection[] = {AlarmEntry._ID, AlarmEntry.TIME, AlarmEntry.HOUR, AlarmEntry.MIN, AlarmEntry.ACTIVE, AlarmEntry.LABEL};
//        CursorLoader cursorLoader = new CursorLoader(getContext(), AlarmEntry.CONTENT_URI, projection, null, null, AlarmEntry.TIME);
//
//        return cursorLoader;
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
//
//        if (data.getCount() == 0) {
//            return;
//        }
////        adapter.swapCursor(data);
//        data.moveToFirst();
//        ArrayList<OneTimeWorkRequest> alarm = new ArrayList<>();
//        do {
//            int hourId = data.getColumnIndex(AlarmEntry.HOUR);
////            int hour = Integer.parseInt(data.getString(hourId));
//            String h = data.getString(hourId);
//            int hour = Integer.parseInt(h);
//
//            int minId = data.getColumnIndex(AlarmEntry.MIN);
//            int min = Integer.parseInt(data.getString(minId));
//
//            int activeId = data.getColumnIndex(AlarmEntry.ACTIVE);
//            int active = data.getInt(activeId);
//
//            int alarmId = data.getColumnIndex(AlarmEntry._ID);
//            int id = data.getInt(alarmId);
//
//
//            Data alarmData = new Data.Builder()
//                    .putInt(AlarmEntry.HOUR, hour)
//                    .putInt(AlarmEntry.MIN, min)
//                    .putInt(AlarmEntry.PENDING, id).build();
//
//            OneTimeWorkRequest workRequest;
//            if (active == AlarmEntry.ALARM_ACTIVE) {
//                workRequest = new OneTimeWorkRequest.Builder(MyWork.class).setInputData(alarmData).build();
//                alarm.add(workRequest);
//            }
//
//        }
//        while (data.moveToNext());
//
//        WorkManager workManager = WorkManager.getInstance(getContext());
//        workManager.beginWith(alarm).enqueue();
//        cursorAdapter.swapCursor(data);
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
//
//    }
}