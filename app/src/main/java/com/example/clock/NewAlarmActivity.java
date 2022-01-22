package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.Adapters.NumberAdapter.NumberAdapterViewHolder;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clock.Adapters.NumberAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class NewAlarmActivity extends AppCompatActivity {

    int n;
    private TextView toolbarTitle, hover;
    private ArrayList<Integer> hoursList;
    private ArrayList<Integer> minutesList;
    private RecyclerView hoursRecyclerView, minutesRecyclerView;
    private NumberAdapter hoursAdapter, minutesAdapter;
    private Button mButton;

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

        mButton = findViewById(R.id.set_alarm);

        LinearLayoutManager hoursLayoutManager = new LinearLayoutManager(this);


        hoursRecyclerView = findViewById(R.id.hours_list);
        final LinearSnapHelper hoursSnap = new LinearSnapHelper();
        hoursSnap.attachToRecyclerView(hoursRecyclerView);
        hoursRecyclerView.setLayoutManager(hoursLayoutManager);
        hoursAdapter = new NumberAdapter(hoursList);
        hoursRecyclerView.setAdapter(hoursAdapter);
        hoursRecyclerView.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);


        LinearLayoutManager minLayoutManager = new LinearLayoutManager(this);
        minutesRecyclerView = findViewById(R.id.minutes_list);

        final LinearSnapHelper minutesSnap = new LinearSnapHelper();
        minutesSnap.attachToRecyclerView(minutesRecyclerView);

        minutesRecyclerView.setLayoutManager(minLayoutManager);
        minutesAdapter = new NumberAdapter(minutesList);
        minutesRecyclerView.setAdapter(minutesAdapter);
        minutesRecyclerView.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);


        minutesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //some code when initially scrollState changes
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Some code while the list is scrolling
                LinearLayoutManager lManager = (LinearLayoutManager) minutesRecyclerView.getLayoutManager();
                int firstElementPosition = lManager.findFirstVisibleItemPosition();
                n = firstElementPosition;
            }
        });
        RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder)
                minutesRecyclerView.findViewHolderForAdapterPosition(n);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    View v = minutesRecyclerView.getChildAt(n);

                    RecyclerView.ViewHolder viewHolder = minutesRecyclerView.findViewHolderForAdapterPosition(n + 2);
                    TextView t = viewHolder.itemView.findViewById(R.id.number_list);
                    Log.v("tag", "" + t.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


}