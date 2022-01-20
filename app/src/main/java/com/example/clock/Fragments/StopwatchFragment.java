package com.example.clock.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
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

    private Button mStartButton, mResetButton;
    View view;
    private long startTime = 0, updateTime = 0, timeGap = 0, timeinMilli = 0;
    TextView screen;
    final Handler handler = new Handler();
    private boolean startOrPause, resetOrCount;

    ListView listView;
    StopwatchAdapter adapter;

    private String presentTime, timeDifference, previousTime;


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

        listView = view.findViewById(R.id.count_list);
        adapter = new StopwatchAdapter(list);



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
                 * if startPause is true
                 */
                if (startOrPause) {

                    Toast.makeText(getContext(), "Stopwatch Started", Toast.LENGTH_SHORT).show();
                    startStopwatch();
                    mStartButton.setText(R.string.pause);
                    startOrPause = false;
                    resetOrCount = false;

                    mResetButton.setText(R.string.count);
                } else {
                    Toast.makeText(getContext(), "Stopwatch paused", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(getContext(), "Stopwatch Reset", Toast.LENGTH_SHORT).show();
                    resetStopwatch();
                    screen.setText(R.string.stopwatch_time);
                }
                //count button is pressed
                else {
                    list.add(new StopwatchCount(100, 20));
                    Toast.makeText(getContext(), "Stopwatch Count", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void startStopwatch() {

        startTime = SystemClock.uptimeMillis();
        handler.post(r);
    }

    private void pauseStopwatch() {
        timeGap += timeinMilli;
        handler.removeCallbacks(r);

    }

    private void resetStopwatch() {
        timeGap = 0;
        handler.removeCallbacks(r);
    }
}