package com.example.clock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
//import androidx.loader.app.LoaderManager.LoaderCallbacks;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.clock.Adapters.AlarmCursorAdapter;
import com.example.clock.Fragments.AlarmFragment;
import com.example.clock.Fragments.StopwatchFragment;
import com.example.clock.provider.AlarmContract.AlarmEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Button AlarmButton, StopwatchButton, TimerButton;
    private FloatingActionButton newAlarm;
    private Fragment mAlarmFragment, mStopwatchFragment;
    private ListView listView;
    private AlarmCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, AlarmFragment.class, null)
                .commit();
        mAlarmFragment = new AlarmFragment();
        mStopwatchFragment = new StopwatchFragment();

        newAlarm = findViewById(R.id.new_alarm);
        AlarmButton = findViewById(R.id.alarm_button);
        StopwatchButton = findViewById(R.id.stopwatch_button);
        TimerButton = findViewById(R.id.timer_button);

        listView = findViewById(R.id.list_of_alarms);
        adapter = new AlarmCursorAdapter(this, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);


        AlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mAlarmFragment).commit();
                newAlarm.setVisibility(View.VISIBLE);
            }
        });
        StopwatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mStopwatchFragment).commit();

                newAlarm.setVisibility(View.GONE);
            }
        });

        newAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewAlarmActivity.class);
                startActivity(intent);
            }
        });


        LoaderManager.getInstance(this).initLoader(1, null, this);
//        getSupportLoaderManager().initLoader(1,null,this);

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String projection[] = {AlarmEntry._ID, AlarmEntry.TIME, AlarmEntry.HOUR, AlarmEntry.MIN, AlarmEntry.ACTIVE};
        CursorLoader cursorLoader = new CursorLoader(this, AlarmEntry.CONTENT_URI, projection, null, null, AlarmEntry.TIME);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
//        Toast.makeText(MainActivity.this, "data changed", Toast.LENGTH_SHORT).show();
        if (data.getCount() == 0) {
            return;
        }

//        data.moveToFirst();
//        ArrayList<OneTimeWorkRequest> alarm = new ArrayList<>();
//        do {
//            int hourId = data.getColumnIndex(AlarmEntry.HOUR);
//            int hour = Integer.parseInt(data.getString(hourId));
//
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
//        WorkManager workManager = WorkManager.getInstance(this);
//        workManager.beginWith(alarm).enqueue();
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}