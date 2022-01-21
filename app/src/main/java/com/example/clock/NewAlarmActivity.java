package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.clock.Adapters.NumberAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class NewAlarmActivity extends AppCompatActivity {

    private TextView toolbarTitle, hover;
    private ArrayList<Integer> hoursList;
    private ArrayList<Integer> minutesList;
    private RecyclerView hoursRecyclerView, minutesRecyclerView;
    private NumberAdapter hoursAdapter, minutesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
        hover = findViewById(R.id.hover);
        hoursList = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
        minutesList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minutesList.add(i);
        }

        LinearLayoutManager hoursLayoutManager = new LinearLayoutManager(this);

        hoursRecyclerView = findViewById(R.id.hours_list);
        hoursRecyclerView.setLayoutManager(hoursLayoutManager);
        hoursAdapter = new NumberAdapter(hoursList);
        hoursRecyclerView.setAdapter(hoursAdapter);
        hoursRecyclerView.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);


        RecyclerView.OnFlingListener onFlingListener = new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                Log.v("fling", "velocity: " + velocityY);
                velocityY = 1000;
                return false;
            }
        };

        hoursRecyclerView.setOnFlingListener(onFlingListener);


        LinearLayoutManager minLayoutManager = new LinearLayoutManager(this);
        minutesRecyclerView = findViewById(R.id.minutes_list);

        final LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(minutesRecyclerView);

        minutesRecyclerView.setLayoutManager(minLayoutManager);
        minutesAdapter = new NumberAdapter(minutesList);
        minutesRecyclerView.setAdapter(minutesAdapter);
        minutesRecyclerView.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);


    }


}