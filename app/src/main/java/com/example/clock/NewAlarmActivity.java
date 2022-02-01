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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clock.Adapters.NumberAdapter;
import com.example.clock.provider.AlarmContract;
import com.example.clock.utils.AlarmUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private Button mButton, addAlarm, addNewAlarm;
    private AlarmManager alarmManager;
    private WorkManager workManager;
    private Toolbar toolbar;
    private Intent intent;
    private int intentCode;
    private FloatingActionButton sunday, monday, tuesday, wednesday, thursday, friday, saturday;
    private final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        workManager = WorkManager.getInstance(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        hover = findViewById(R.id.hover);
        intent = getIntent();
        intentCode = intent.getIntExtra(AlarmContract.AlarmEntry.EDIT_ALARM, -1);

        sunday = findViewById(R.id.sunday);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);

        hoursList = new ArrayList<>();
        minutesList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hoursList.add(String.format("%02d", i));
        }

        for (int i = 0; i < 60; i++) {
            minutesList.add(String.format("%02d", i));
        }

//        addNewAlarm = findViewById(R.id.add_new_alarm);


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
            getSupportActionBar().setTitle(R.string.add_alarm);
        } else {
            getSupportActionBar().setTitle(R.string.edit_alarm);
            Bundle bundle = intent.getBundleExtra(AlarmContract.AlarmEntry.INTENT_BUNDLE);
            String time = bundle.getString(AlarmContract.AlarmEntry.TIME);
            String hour = time.substring(0, 2);
            String min = time.substring(3, 5);
            setToGivenTime(hour, min);

        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.done_button) {
            RecyclerView.ViewHolder minViewHolder = minutesRecyclerView.findViewHolderForAdapterPosition(hoursPosition + 2);
            RecyclerView.ViewHolder viewHolder = hoursRecyclerView.findViewHolderForAdapterPosition(minutePosition + 2);

            TextView h = viewHolder.itemView.findViewById(R.id.number_list);
            TextView m = minViewHolder.itemView.findViewById(R.id.number_list);

            String hour = h.getText().toString();
            String min = m.getText().toString();

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
//            if (calendarTime < currentTime) {
//                oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
//                        .setInputData(data).setInitialDelay(currentTime - calendarTime + AlarmUtils.DAY, TimeUnit.MILLISECONDS).build();
//
//            } else {
//                oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
//                        .setInputData(data).setInitialDelay(calendarTime - currentTime, TimeUnit.MILLISECONDS).build();
//            }
            oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class).setInputData(data).build();
            workManager.enqueueUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);
            AlarmUtils.showMessage(NewAlarmActivity.this, calendar);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (intentCode == -1) {
            setToCurrentTime();
        }
    }

    private void setToCurrentTime() {

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        String t = dateFormat.format(date);

        String hour = t.substring(0, 2);
        String min = t.substring(3, 5);
        Log.v(TAG, hour + ":" + min);

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