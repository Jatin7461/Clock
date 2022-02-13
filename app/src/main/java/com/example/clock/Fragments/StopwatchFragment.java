package com.example.clock.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.TextView;

import com.example.clock.Adapters.StopwatchAdapter;
import com.example.clock.R;
import com.example.clock.data.StopwatchCount;

import java.util.ArrayList;

public class StopwatchFragment extends Fragment {

    private final String TAG = StopwatchFragment.class.getName();

    private Button mStartButton, mResetButton;
    View view;
    private long startTime = 0, updateTime = 0, timeGap = 0, timeinMilli = 0;
    TextView screen, millisec;
    final Handler handler = new Handler();
    private boolean startOrPause, resetOrCount;
    private final String COUNT = "count", PRESENT = "present", PREVIOUS = "previous";
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

            milliseconds /= 10;
            String displayHour = String.format("%02d", hours);
            String displayMin = String.format("%02d", realMinutes);
            String displaySeconds = String.format("%02d", realSeconds);
            String displayMilli = String.format("%02d", milliseconds);

            if (hours > 0) {
                screen.setTextSize(70);
                screen.setText(displayHour + ":" + displayMin + ":" + displaySeconds);
            } else
                screen.setText(displayMin + ":" + displaySeconds);
            millisec.setText(displayMilli);


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
        millisec = view.findViewById(R.id.millisec);
        list = new ArrayList<>();

//        if (savedInstanceState != null) {
//            ArrayList<String> count = savedInstanceState.getStringArrayList(COUNT);
//            ArrayList<String> previous = savedInstanceState.getStringArrayList(PREVIOUS);
//            ArrayList<String> present = savedInstanceState.getStringArrayList(PRESENT);
//            int n = count.size();
//            for (int i = 0; i < n; i++) {
//                String getCount = count.get(i);
//                String getPrevious = previous.get(i);
//                String getPresent = present.get(i);
//
//                list.add(new StopwatchCount(getPresent, getPrevious, Integer.parseInt(getCount)));
//            }
//
//        }

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
                    millisec.setText(R.string.stopwatch_milli);
                }
                //count button is pressed
                else {
                    list.add(0, new StopwatchCount(screen.getText().toString() + ":" + millisec.getText().toString(), setSecondTime(), countNumber));
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
        String displayHour = String.format("%02d", hours);
        String displayMin = String.format("%02d", realMin);
        String displaySeconds = String.format("%02d", realSec);
        String displayMilli = String.format("%02d", milliSec / 10);
        return "+" + displayMin + ":" + displaySeconds + ":" + displayMilli;

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
        screen.setTextSize(80);
        handler.removeCallbacks(r);
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        ArrayList<String> count = new ArrayList<>();
//        ArrayList<String> presentTime = new ArrayList<>();
//        ArrayList<String> previousTime = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            String getCount = list.get(i).getCount();
//            String getPresentTime = list.get(i).getPresentTime();
//            String getPreviousTime = list.get(i).getPreviousTime();
//
//            count.add(getCount);
//            presentTime.add(getPresentTime);
//            previousTime.add(getPreviousTime);
//
//        }
//
//        outState.putStringArrayList(COUNT, count);
//        outState.putStringArrayList(PRESENT, presentTime);
//        outState.putStringArrayList(PREVIOUS, previousTime);
//        outState.putString("str", "str");
//    }
}