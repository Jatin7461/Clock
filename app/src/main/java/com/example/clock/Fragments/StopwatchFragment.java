package com.example.clock.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clock.R;

public class StopwatchFragment extends Fragment {

    private Button mStartButton, mResetButton;
    View view;
    private long startTime = 0, updateTime = 0, timeGap = 0, timeinMilli = 0;
    TextView screen;
    final Handler handler = new Handler();
    private boolean startOrPause,resetOrCount;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        screen = (TextView) view.findViewById(R.id.stopwatch_screen);

        mStartButton = view.findViewById(R.id.start_stopwatch);
        mResetButton = view.findViewById(R.id.reset_stopwatch);
        startOrPause = true;
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
                    mResetButton.setText(R.string.count);
                } else {
                    Toast.makeText(getContext(), "Stopwatch paused", Toast.LENGTH_SHORT).show();
                    pauseStopwatch();
                    mStartButton.setText(R.string.start);
                    startOrPause = true;
                    mResetButton.setText(R.string.reset);
                }
            }
        });
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "Stopwatch Stopped", Toast.LENGTH_SHORT).show();
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
}