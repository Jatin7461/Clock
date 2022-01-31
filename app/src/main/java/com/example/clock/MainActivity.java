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
//import androidx.loader.app.LoaderManager.LoaderCallbacks;

import android.content.Intent;
import android.database.Cursor;
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
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, AlarmFragment.class, null)
                .commit();
        mAlarmFragment = new AlarmFragment();
        mStopwatchFragment = new StopwatchFragment();

        toolbar = findViewById(R.id.toolbar_add_alarm);
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
                newAlarm.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                toolbar.setTitle(R.string.alarm);
            }
        });
        StopwatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mStopwatchFragment).commit();
                listView.setVisibility(View.GONE);
                newAlarm.setVisibility(View.GONE);
                toolbar.setTitle(R.string.stopwatch);
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
        String projection[] = {AlarmEntry._ID, AlarmEntry.TIME, AlarmEntry.HOUR, AlarmEntry.MIN, AlarmEntry.ACTIVE};
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