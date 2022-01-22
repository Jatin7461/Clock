package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clock.Adapters.NumberAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class NewAlarmActivity extends AppCompatActivity {

    final int SCROLL = Integer.MAX_VALUE / 2;

    final String TAG = NewAlarmActivity.class.getName();

    int hoursPosition, minutePosition;
    private TextView toolbarTitle, hover;
    private ArrayList<String> hoursList;
    private ArrayList<String> minutesList;
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

        hoursList = new ArrayList<>();
        minutesList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hoursList.add(String.format("%02d", i));
        }

        for (int i = 0; i < 60; i++) {
            minutesList.add(String.format("%02d", i));
        }

        mButton = findViewById(R.id.set_alarm);


        LinearLayoutManager hoursLayoutManager = new LinearLayoutManager(this);
        hoursRecyclerView = findViewById(R.id.hours_list);
        final LinearSnapHelper hoursSnap = new LinearSnapHelper();
        hoursSnap.attachToRecyclerView(hoursRecyclerView);
        hoursRecyclerView.setLayoutManager(hoursLayoutManager);
        hoursAdapter = new NumberAdapter(hoursList);
        hoursRecyclerView.setAdapter(hoursAdapter);

        //scroll to half of max integer to scroll both upwards and downwards
        hoursRecyclerView.getLayoutManager().scrollToPosition(SCROLL);

        final LinearSnapHelper minutesSnap = new LinearSnapHelper();

        LinearLayoutManager minLayoutManager = new LinearLayoutManager(this);
        minutesRecyclerView = findViewById(R.id.minutes_list);
        minutesRecyclerView.setLayoutManager(minLayoutManager);
        minutesAdapter = new NumberAdapter(minutesList);
        minutesRecyclerView.setAdapter(minutesAdapter);

        //scroll to half of max integer to scroll both upwards and downwards
        minutesRecyclerView.getLayoutManager().scrollToPosition(SCROLL);


        //add scroll listeners to find the view in the center
        minutesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                minutesSnap.attachToRecyclerView(minutesRecyclerView);
                super.onScrolled(recyclerView, dx, dy);
                //Some code while the list is scrolling
                LinearLayoutManager lManager = (LinearLayoutManager) minutesRecyclerView.getLayoutManager();
                int firstElementPosition = lManager.findFirstVisibleItemPosition();
                hoursPosition = firstElementPosition;
            }
        });

        hoursRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) hoursRecyclerView.getLayoutManager();
                minutePosition = linearLayoutManager.findFirstVisibleItemPosition();
            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

//                    View hoursv = minutesRecyclerView.getChildAt(hoursPosition);
//                    View minv = hoursRecyclerView.getChildAt(minutePosition);
                    RecyclerView.ViewHolder minViewHolder = minutesRecyclerView.findViewHolderForAdapterPosition(hoursPosition + 2);
                    RecyclerView.ViewHolder viewHolder = hoursRecyclerView.findViewHolderForAdapterPosition(minutePosition + 2);

                    TextView h = viewHolder.itemView.findViewById(R.id.number_list);
                    TextView m = minViewHolder.itemView.findViewById(R.id.number_list);

                    String s = h.getText().toString() + ":" + m.getText().toString();

                    Toast.makeText(NewAlarmActivity.this, s, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        setToCurrentTime();
    }

    private void setToCurrentTime() {
//        long milliSec = System.currentTimeMillis();
//        long sec = milliSec / 1000;
//        long min = (sec / 60) % 60;
//        long hour = min / 60;
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        String t = dateFormat.format(date);

        String hour = t.substring(0, 2);
        String min = t.substring(3, 5);
        Log.v(TAG, hour + ":" + min);

//        boolean overflow=false;
        int k = 0;
        int i = SCROLL;
        while (true) {

            String listitem = hoursList.get(i % hoursList.size());
            Log.v(TAG, listitem);
            if (listitem.equals(hour)) {
                hoursRecyclerView.scrollToPosition(i);
                hoursRecyclerView.scrollToPosition(i - 2);
                break;
            }
            i++;
        }
        k = 0;
        i = SCROLL;
        while (true) {

            String listitem = minutesList.get(i % minutesList.size());
            Log.v(TAG, listitem);
            if (listitem.equals(min)) {
                minutesRecyclerView.scrollToPosition(i);
                minutesRecyclerView.scrollToPosition(i - 2);
                break;
            }
            i++;
        }
    }


}