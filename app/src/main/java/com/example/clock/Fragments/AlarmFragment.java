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
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clock.Adapters.AlarmAdapter;
import com.example.clock.Adapters.AlarmCursorAdapter;
import com.example.clock.NewAlarmActivity;
import com.example.clock.R;
import com.example.clock.provider.AlarmContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class AlarmFragment extends Fragment implements AlarmAdapter.OnAlarmClickListener {

    private FloatingActionButton addAlarm;
    private RecyclerView recyclerView;
    private AlarmAdapter adapter;
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
        addAlarm = view.findViewById(R.id.add_alarm_fab);

        cursorAdapter = new AlarmCursorAdapter(getContext(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);


        hiddenPanel = (TextView) view.findViewById(R.id.slide_up);
//        database = AlarmDatabase.getInstance(getContext());
        //initialize recycler view and adapter
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.alarm_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AlarmAdapter(this);
        recyclerView.setAdapter(adapter);

//        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        //onclick listener for FAB
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewAlarmActivity.class);
                startActivity(intent);
            }
        });


        updateData();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }


    //method to retrieve data from database
    void updateData() {
        Cursor cursor = getContext().getContentResolver().query(AlarmContract.AlarmEntry.CONTENT_URI, null, null, null, AlarmContract.AlarmEntry.TIME);
        adapter.swapCursor(cursor);
    }

    @Override
    public void onAlarmClick(int id) {
        Log.v(TAG, "" + id);
    }

    @Override
    public void onLongPress(View view, int id) {
        Log.v(TAG, "long press " + id);
        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                R.anim.bottom_up);
//        ViewGroup hiddenPanel = (ViewGroup) findViewById(R.id.slide_up);
        hiddenPanel.startAnimation(bottomUp);
        hiddenPanel.setVisibility(View.VISIBLE);

    }

    @Override
    public void switchClick(int id, boolean b) {

        Uri uri = ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, id);
        if (b) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmEntry.ALARM_ACTIVE);
            getContext().getContentResolver().update(uri, contentValues, null, null);

            Toast.makeText(getContext(), "switch on for " + id, Toast.LENGTH_SHORT).show();
        } else {

            ContentValues contentValues = new ContentValues();
            contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmEntry.ALARM_INACTIVE);
            getContext().getContentResolver().update(uri, contentValues, null, null);


            Toast.makeText(getContext(), "switch off for " + id, Toast.LENGTH_SHORT).show();

        }
    }
}