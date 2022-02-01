package com.example.clock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
//import androidx.loader.app.LoaderManager.LoaderCallbacks;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clock.Adapters.AlarmAdapter;
import com.example.clock.Adapters.AlarmCursorAdapter;
import com.example.clock.Fragments.AlarmFragment;
import com.example.clock.Fragments.StopwatchFragment;
import com.example.clock.provider.AlarmContract.AlarmEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Button AlarmButton, StopwatchButton, TimerButton;
    private FloatingActionButton newAlarm;
    private Fragment mAlarmFragment, mStopwatchFragment;
    private ListView listView;
    private AlarmCursorAdapter adapter;
    private RecyclerView recyclerView;
    private AlarmAdapter recyclerAdapter;
    private Toolbar toolbar;
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O_MR1){
//
//        }
        getSupportActionBar().setTitle(R.string.alarm);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, AlarmFragment.class, null)
                .commit();
        mAlarmFragment = new AlarmFragment();
        mStopwatchFragment = new StopwatchFragment();


        newAlarm = findViewById(R.id.new_alarm);
        AlarmButton = findViewById(R.id.alarm_button);
        StopwatchButton = findViewById(R.id.stopwatch_button);
        TimerButton = findViewById(R.id.timer_button);

//        recyclerView = findViewById(R.id.alarm_list);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerAdapter = new AlarmAdapter(this);
////        recyclerView.setAdapter(recyclerAdapter);

        listView = findViewById(R.id.list_of_alarms);
        adapter = new AlarmCursorAdapter(this, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);


        AlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mAlarmFragment).commit();
                getSupportActionBar().setTitle(R.string.alarm);
                newAlarm.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);

            }
        });
        StopwatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mStopwatchFragment).commit();
                getSupportActionBar().setTitle(R.string.stopwatch);
                listView.setVisibility(View.GONE);
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.bottom_up);
                LinearLayout layout = findViewById(R.id.linearLayout);
                layout.startAnimation(slide_up);
                layout.setVisibility(View.VISIBLE);

                Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                LinearLayout transparent = findViewById(R.id.transparent_screen);


                Button button = findViewById(R.id.delete_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Uri uri = ContentUris.withAppendedId(AlarmEntry.CONTENT_URI, l);
                        int id = (int) l;
                        Data data = new Data.Builder()
                                .putInt(AlarmEntry._ID, id).build();
                        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(CancelAlarm.class).setInputData(data).build();

                        WorkManager.getInstance(MainActivity.this).enqueue(oneTimeWorkRequest);
                        getContentResolver().delete(uri, null, null);
                        transparent.setVisibility(View.VISIBLE);

                    }
                });

                transparent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        layout.startAnimation(slide_down);
                        transparent.setVisibility(View.GONE);
                        layout.setVisibility(View.INVISIBLE);
                    }
                });
                transparent.setVisibility(View.VISIBLE);
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, NewAlarmActivity.class);
                Bundle bundle = new Bundle();
                TextView t = view.findViewById(R.id.alarm_time);
                String time = t.getText().toString();

                bundle.putString(AlarmEntry.TIME, time);
                intent.putExtra(AlarmEntry._ID, l);
                intent.putExtra(AlarmEntry.INTENT_BUNDLE, bundle);
                intent.putExtra(AlarmEntry.EDIT_ALARM, 100);

                startActivity(intent);
            }
        });


        LoaderManager.getInstance(this).initLoader(1, null, this);


    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String projection[] = {AlarmEntry._ID, AlarmEntry.TIME, AlarmEntry.HOUR, AlarmEntry.MIN, AlarmEntry.ACTIVE, AlarmEntry.SUNDAY,
                AlarmEntry.MONDAY, AlarmEntry.TUESDAY, AlarmEntry.WEDNESDAY, AlarmEntry.THURSDAY, AlarmEntry.FRIDAY, AlarmEntry.SATURDAY};
        CursorLoader cursorLoader = new CursorLoader(this, AlarmEntry.CONTENT_URI, projection, null, null, AlarmEntry.TIME);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) {
            return;
        }
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        recyclerAdapter.swapCursor(null);
    }


}