package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clock.Adapters.NumberAdapter;
import com.example.clock.provider.AlarmContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewAlarmActivity extends AppCompatActivity {

    final String HOUR = "hour", MIN = "min";

    final int SCROLL = Integer.MAX_VALUE / 2;

    final String TAG = NewAlarmActivity.class.getName();
    private int id = 1;
    int hoursPosition, minutePosition;
    private TextView toolbarTitle, hover;
    private ArrayList<String> hoursList;
    private ArrayList<String> minutesList;
    private static RecyclerView hoursRecyclerView, minutesRecyclerView;
    private NumberAdapter hoursAdapter, minutesAdapter;
    private Button mButton, addAlarm;
    private AlarmManager alarmManager;
    private WorkManager workManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        workManager = WorkManager.getInstance(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        addAlarm = findViewById(R.id.add_new_alarm);
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
                    RecyclerView.ViewHolder minViewHolder = minutesRecyclerView.findViewHolderForAdapterPosition(hoursPosition + 2);
                    RecyclerView.ViewHolder viewHolder = hoursRecyclerView.findViewHolderForAdapterPosition(minutePosition + 2);

                    TextView h = viewHolder.itemView.findViewById(R.id.number_list);
                    TextView m = minViewHolder.itemView.findViewById(R.id.number_list);


                    int hour = Integer.parseInt(h.getText().toString());
                    int min = Integer.parseInt(m.getText().toString());

//                    PendingIntent pendingIntent;
//                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                    Intent intent = new Intent(NewAlarmActivity.this, SetAlarm.class);
//                    pendingIntent = PendingIntent.getBroadcast(NewAlarmActivity.this, 0, intent, 0);
//
//
//                    intent.putExtra("ac", NewAlarmActivity.class);
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTimeInMillis(System.currentTimeMillis());
//                    calendar.set(Calendar.HOUR_OF_DAY, hour);
//                    calendar.set(Calendar.MINUTE, min);
//                    calendar.set(Calendar.SECOND, 0);
//                    Log.v(TAG, "hour: " + hour + " min " + min);
//
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                    Data data = new Data.Builder()
                            .putInt(HOUR, hour)
                            .putInt(MIN, min)
                            .build();
                    WorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWork.class).setInputData(data).build();
//                    WorkRequest w = new PeriodicWorkRequest.Builder(MyWork.class).build();
                    workManager.enqueue(workRequest);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Toast.makeText(NewAlarmActivity.this, "Alarm set", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        });
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder minViewHolder = minutesRecyclerView.findViewHolderForAdapterPosition(hoursPosition + 2);
                RecyclerView.ViewHolder viewHolder = hoursRecyclerView.findViewHolderForAdapterPosition(minutePosition + 2);

                TextView h = viewHolder.itemView.findViewById(R.id.number_list);
                TextView m = minViewHolder.itemView.findViewById(R.id.number_list);

                String hour = h.getText().toString();
                String min = m.getText().toString();

                ContentValues contentValues = new ContentValues();

                contentValues.put("hour", hour);
                contentValues.put("min", min);

                Uri id = getContentResolver().insert(AlarmContract.AlarmEntry.CONTENT_URI, contentValues);
                if (id == null) {
                    Toast.makeText(NewAlarmActivity.this, "Alarm not added", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(NewAlarmActivity.this, "Alarm added", Toast.LENGTH_SHORT).show();
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
            if (listitem.equals(min)) {
                minutesRecyclerView.scrollToPosition(i);
                minutesRecyclerView.scrollToPosition(i - 2);
                break;
            }
            i++;
        }
    }


}