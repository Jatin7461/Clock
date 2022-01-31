package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
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
import android.content.Intent;
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
    private Toolbar toolbar;
    private Intent intent;
    private int intentCode;
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
//        toolbarTitle = findViewById(R.id.toolbar_title);
//        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
        hover = findViewById(R.id.hover);
        intent = getIntent();
        intentCode = intent.getIntExtra(AlarmContract.AlarmEntry.EDIT_ALARM, -1);
        toolbar = findViewById(R.id.toolbar_add_alarm);


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

        if (intentCode == -1) {
            toolbar.setTitle(R.string.add_alarm);
        } else {
            toolbar.setTitle(R.string.edit_alarm);
            Bundle bundle = intent.getBundleExtra(AlarmContract.AlarmEntry.INTENT_BUNDLE);
            String time = bundle.getString(AlarmContract.AlarmEntry.TIME);
            String hour = time.substring(0, 2);
            String min = time.substring(3, 5);
            setToGivenTime(hour, min);

        }



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


                if (intentCode == -1) {

                    Uri uri = getContentResolver().insert(AlarmContract.AlarmEntry.CONTENT_URI, contentValues);
                    id = (int) ContentUris.parseId(uri);
                } else {
                    id = (int) intent.getLongExtra(AlarmContract.AlarmEntry._ID, -1);
                    Uri uri = ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, id);
                    getContentResolver().update(uri, contentValues, null, null);
                }
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
                            .setInputData(data).setInitialDelay(currentTime - calendarTime + AlarmUtils.DAY, TimeUnit.MILLISECONDS).build();

                } else {
                    oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
                            .setInputData(data).setInitialDelay(calendarTime - currentTime, TimeUnit.MILLISECONDS).build();

                }
//                OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class).setInputData(data).build();
                workManager.enqueueUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);

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
        if (intentCode == -1) {

            setToCurrentTime();
        }
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

    private void setToGivenTime(String hour, String min) {

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