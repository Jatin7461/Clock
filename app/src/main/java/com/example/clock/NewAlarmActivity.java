package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.clock.Adapters.NumberAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class NewAlarmActivity extends AppCompatActivity {

    private TextView toolbarTitle;
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


        LinearLayoutManager minLayoutManager = new LinearLayoutManager(this);
        minutesRecyclerView = findViewById(R.id.minutes_list);
        minutesRecyclerView.setLayoutManager(minLayoutManager);
        minutesAdapter = new NumberAdapter(minutesList);
        minutesRecyclerView.setAdapter(minutesAdapter);
        minutesRecyclerView.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);

    }
}