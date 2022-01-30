package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.content.ContentUris;
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
import com.example.clock.utils.AlarmUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NewAlarmActivity extends AppCompatActivity {

    final String HOUR = "hour", MIN = "min";

    final int SCROLL = Integer.MAX_VALUE / 2;


    private final int DAY = 24 * 60 * 60 * 1000;
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
//    private Alarm alarm;
//
//    private AlarmDatabase database;

    private final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        workManager = WorkManager.getInstance(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        addAlarm = findViewById(R.id.adding_alarm);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
        hover = findViewById(R.id.hover);

//        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);

//        database = AlarmDatabase.getInstance(this);

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


                    Data data = new Data.Builder()
                            .putInt(HOUR, hour)
                            .putInt(MIN, min)
                            .putInt(AlarmContract.AlarmEntry.PENDING, 1)
                            .build();
//                    uniqueWork++;
                    Data data2 = new Data.Builder()
                            .putInt(HOUR, hour)
                            .putInt(MIN, min + 1)
                            .putInt(AlarmContract.AlarmEntry.PENDING, 2)
                            .build();
                    Data data3 = new Data.Builder()
                            .putInt(HOUR, hour)
                            .putInt(MIN, min + 2)
                            .putInt(AlarmContract.AlarmEntry.PENDING, 3)
                            .build();
//

//                    WorkRequest workRequest = new PeriodicWorkRequest.Builder(MyWork.class).setInputData(data).build();
//                    WorkRequest w = new PeriodicWorkRequest.Builder(MyWork.class).build();
//                    String uni = Integer.toString(uniqueWork);
//                    workManager.enqueueUniquePeriodicWork(uni, ExistingPeriodicWorkPolicy.KEEP, workRequest);

//                    PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(MyWork.class, 10, TimeUnit.SECONDS).setInputData(data).build();
//                    workManager.enqueueUniquePeriodicWork("work", ExistingPeriodicWorkPolicy.KEEP, workRequest);

                    OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWork.class).setInputData(data).build();
                    OneTimeWorkRequest workRequest2 = new OneTimeWorkRequest.Builder(MyWork.class).setInputData(data2).build();
                    OneTimeWorkRequest workRequest3 = new OneTimeWorkRequest.Builder(MyWork.class).setInputData(data3).build();
//                    workManager.enqueueUniqueWork("work" + uniqueWork, ExistingWorkPolicy.APPEND, workRequest2);
                    workManager.beginWith(Arrays.asList(workRequest, workRequest2, workRequest3)).enqueue();


//                    workManager.enqueue(workRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Toast.makeText(NewAlarmActivity.this, "Alarm set", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        });


        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder minViewHolder = minutesRecyclerView.findViewHolderForAdapterPosition(hoursPosition + 2);
                RecyclerView.ViewHolder viewHolder = hoursRecyclerView.findViewHolderForAdapterPosition(minutePosition + 2);

                TextView h = viewHolder.itemView.findViewById(R.id.number_list);
                TextView m = minViewHolder.itemView.findViewById(R.id.number_list);

                String hour = h.getText().toString();
                String min = m.getText().toString();


//                alarm = new Alarm(hour, min);
//                LoaderManager.getInstance(NewAlarmActivity.this).restartLoader(LOADER_ID, null, NewAlarmActivity.this);

                ContentValues contentValues = new ContentValues();
                contentValues.put(AlarmContract.AlarmEntry.HOUR, hour);
                contentValues.put(AlarmContract.AlarmEntry.MIN, min);
                contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmContract.AlarmEntry.ALARM_ACTIVE);
                contentValues.put(AlarmContract.AlarmEntry.TIME, Integer.parseInt(hour + min));


                Uri uri = getContentResolver().insert(AlarmContract.AlarmEntry.CONTENT_URI, contentValues);
                int id = (int) ContentUris.parseId(uri);
                Data data = new Data.Builder()
                        .putInt(HOUR, Integer.parseInt(hour))
                        .putInt(MIN, Integer.parseInt(min))
                        .putInt(AlarmContract.AlarmEntry.PENDING, id)
                        .build();


                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                calendar.set(Calendar.MINUTE, Integer.parseInt(min));
                calendar.set(Calendar.SECOND, 0);

                long currentTime = System.currentTimeMillis();
                long calendarTime = calendar.getTimeInMillis();
                OneTimeWorkRequest oneTimeWorkRequest;
                if (calendarTime < currentTime) {
                    oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
                            .setInputData(data).setInitialDelay(currentTime - calendarTime + AlarmUtils.DAY , TimeUnit.MILLISECONDS).build();

                } else {
                    oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
                            .setInputData(data).setInitialDelay(calendarTime - currentTime , TimeUnit.MILLISECONDS).build();

                }
//                OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class).setInputData(data).build();
                workManager.enqueueUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id, ExistingWorkPolicy.KEEP, oneTimeWorkRequest);

//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(System.currentTimeMillis());
//                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
//                calendar.set(Calendar.MINUTE, Integer.parseInt(min));
                AlarmUtils.showMessage(NewAlarmActivity.this, calendar);
                finish();
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