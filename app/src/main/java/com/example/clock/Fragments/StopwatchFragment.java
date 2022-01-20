package com.example.clock.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clock.Adapters.StopwatchAdapter;
import com.example.clock.R;
import com.example.clock.data.StopwatchCount;

import java.util.ArrayList;

public class StopwatchFragment extends Fragment {

    private final String TAG = StopwatchFragment.class.getName();

    private Button mStartButton, mResetButton;
    View view;
    private long startTime = 0, updateTime = 0, timeGap = 0, timeinMilli = 0;
    TextView screen;
    final Handler handler = new Handler();
    private boolean startOrPause, resetOrCount;

    RecyclerView recyclerView;
    StopwatchAdapter adapter;
    private int countNumber = 1;

    long presentTime = 0, timeDifference = 0;


    Runnable r = new Runnable() {
        long milliseconds = 0;

        @Override
        public void run() {
            timeinMilli = SystemClock.uptimeMillis() - startTime;
            updateTime = timeGap + timeinMilli;
            int seconds = (int) (updateTime / 1000);
            milliseconds = updateTime % 1000;
            int minutes = seconds / 60;
            int hours = (minutes / 60) % 24;
            int realSeconds = seconds % 60;
            int realMinutes = minutes % 60;
            screen.setText(String.format("%02d", hours) + ":" + String.format("%02d", realMinutes) + ":" + String.format("%02d", realSeconds) + ":" + String.format("%03d", milliseconds));


            handler.postDelayed(this, 0);

        }
    };

    ArrayList<StopwatchCount> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        screen = (TextView) view.findViewById(R.id.stopwatch_screen);

        list = new ArrayList<>();

        //initialize the recycler view and adapter
        recyclerView = view.findViewById(R.id.count_list);
        adapter = new StopwatchAdapter(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        //initialize start and reset buttons
        mStartButton = view.findViewById(R.id.start_stopwatch);
        mResetButton = view.findViewById(R.id.reset_stopwatch);

        //bool flags for start and reset buttons
        startOrPause = true;
        resetOrCount = true;

        //initialize text for start and reset buttons
        mStartButton.setText(R.string.start);
        mResetButton.setText(R.string.reset);

        //click listener for start/pause button
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * if startPause is true call startStopwatch()
                 * set text of start button to pause , set startOrpause and resetOrCount to false
                 *
                 */
                if (startOrPause) {
                    startStopwatch();
                    mStartButton.setText(R.string.pause);
                    startOrPause = false;
                    resetOrCount = false;

                    mResetButton.setText(R.string.count);
                }
                /**
                 * else call pauseStopwatch()
                 * and do all these stuff
                 */
                else {
                    pauseStopwatch();
                    mStartButton.setText(R.string.start);
                    startOrPause = true;
                    resetOrCount = true;
                    mResetButton.setText(R.string.reset);
                }
            }
        });

        //click listener for reset/count button
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //reset button is pressed
                if (resetOrCount) {
                    resetStopwatch();
                    countNumber = 1;
                    adapter.resetList();
                    screen.setText(R.string.stopwatch_time);
                }
                //count button is pressed
                else {
                    list.add(0, new StopwatchCount(screen.getText().toString(), setSecondTime(), countNumber));
                    countNumber += 1;
                    recyclerView.scrollToPosition(0);
                    adapter.updateList(list);


                }
            }
        });
        return view;
    }


    //function to get difference between present time and previous lap
    private String setSecondTime() {
        presentTime = updateTime - timeDifference;
        timeDifference = updateTime;
        int milliSec = (int) presentTime % 1000;
        int fakeSec = (int) presentTime / 1000;
        int fakeMin = fakeSec / 60;
        int hours = (fakeMin / 60) % 24;
        int realSec = fakeSec % 60;
        int realMin = fakeMin % 60;
        return "+" + String.format("%02d", hours) + ":" + String.format("%02d", realMin) + ":" + String.format("%02d", realSec) + ":" + String.format("%03d", milliSec);

    }

    //start the handler for stopwatch
    private void startStopwatch() {

        startTime = SystemClock.uptimeMillis();
        handler.post(r);
    }

    //stop the handler for stopwatch
    private void pauseStopwatch() {
        timeGap += timeinMilli;
        handler.removeCallbacks(r);

    }

    //reset the stopwatch
    private void resetStopwatch() {
        presentTime = 0;
        timeDifference = 0;
        timeGap = 0;
        handler.removeCallbacks(r);
    }
}