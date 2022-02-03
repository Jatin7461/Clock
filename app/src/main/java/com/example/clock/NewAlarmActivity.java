package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clock.provider.AlarmContract.AlarmEntry;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.Data;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


import com.example.clock.Adapters.NumberAdapter;
import com.example.clock.provider.AlarmContract;
import com.example.clock.utils.AlarmUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;


public class NewAlarmActivity extends AppCompatActivity {

    final String HOUR = "hour", MIN = "min";

    final int SCROLL = Integer.MAX_VALUE / 2;

    private TextView ringtone, vibrate;
    RadioButton toggleVibrate;
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
    private boolean sun, mon, tue, wed, thu, fri, sat, vib;
    private final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        workManager = WorkManager.getInstance(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        ringtone = findViewById(R.id.ringtone);
        hover = findViewById(R.id.hover);
        intent = getIntent();
        intentCode = intent.getIntExtra(AlarmContract.AlarmEntry.EDIT_ALARM, -1);
        int daysun = intent.getIntExtra(AlarmEntry.SUNDAY, 0);
        int daymon = intent.getIntExtra(AlarmEntry.MONDAY, 0);
        int daytue = intent.getIntExtra(AlarmEntry.TUESDAY, 0);
        int daywed = intent.getIntExtra(AlarmEntry.WEDNESDAY, 0);
        int daythu = intent.getIntExtra(AlarmEntry.THURSDAY, 0);
        int dayfri = intent.getIntExtra(AlarmEntry.FRIDAY, 0);
        int daysat = intent.getIntExtra(AlarmEntry.SATURDAY, 0);
        sunday = findViewById(R.id.sunday);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);

        sun = mon = tue = wed = thu = fri = sat = false;
        vib = true;

        hoursList = new ArrayList<>();
        minutesList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hoursList.add(String.format("%02d", i));
        }

        for (int i = 0; i < 60; i++) {
            minutesList.add(String.format("%02d", i));
        }


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


        //when intent is for new alarm just change title of activity
        if (intentCode == -1) {
            getSupportActionBar().setTitle(R.string.add_alarm);
        }
        //else set the time to the alarm time and change color of fabs accordingly
        else {
            getSupportActionBar().setTitle(R.string.edit_alarm);
            Bundle bundle = intent.getBundleExtra(AlarmContract.AlarmEntry.INTENT_BUNDLE);
            String time = bundle.getString(AlarmContract.AlarmEntry.TIME);
            String hour = time.substring(0, 2);
            String min = time.substring(3, 5);
            setToGivenTime(hour, min);
            if (daysun == 1) {
                sun = true;
                sunday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
            }
            if (daymon == 1) {
                mon = true;
                monday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
            }
            if (daytue == 1) {
                tue = true;
                tuesday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
            }
            if (daywed == 1) {
                wed = true;
                wednesday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
            }
            if (daythu == 1) {
                thu = true;
                thursday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
            }
            if (dayfri == 1) {
                fri = true;
                friday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
            }
            if (daysat == 1) {
                sat = true;
                saturday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
            }


        }


        //click listeners on fabs to select days for repeating alarms
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sun == false) {
                    sunday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
                    sun = true;
                } else {
                    sunday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
                    sun = false;
                }

            }
        });

        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mon) {
                    monday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
                    mon = false;
                } else {
                    monday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
                    mon = true;
                }
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tue) {
                    tuesday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
                    tue = false;
                } else {
                    tuesday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
                    tue = true;
                }
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wed) {
                    wednesday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
                    wed = false;
                } else {
                    wednesday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
                    wed = true;
                }
            }
        });
        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thu) {
                    thursday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
                    thu = false;
                } else {
                    thursday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
                    thu = true;
                }
            }
        });


        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fri) {
                    friday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
                    fri = false;
                } else {
                    friday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
                    fri = true;
                }
            }
        });

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sat) {
                    saturday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
                    sat = false;
                } else {
                    saturday.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
                    sat = true;
                }
            }
        });

//
//        ringtone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent ringIntent = new Intent(NewAlarmActivity.this, RingtoneActivity.class);
//                startActivity(ringIntent);
//
//            }
//        });

        toggleVibrate = findViewById(R.id.radioButton);
        vibrate = findViewById(R.id.vibrate);
        toggleVibrate.setChecked(vib);
        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vib) {
                    vib = false;
                    toggleVibrate.setChecked(false);
                } else {


                    vib = true;
                    toggleVibrate.setChecked(true);
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


                    try {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createWaveform(new long[]{0, 200}, 1));

                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(new long[]{0, 200}, 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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


            //get the position of middle number in hours list and minutes list to select the time for alarm
            RecyclerView.ViewHolder minViewHolder = minutesRecyclerView.findViewHolderForAdapterPosition(hoursPosition + 2);
            RecyclerView.ViewHolder viewHolder = hoursRecyclerView.findViewHolderForAdapterPosition(minutePosition + 2);

            TextView h = viewHolder.itemView.findViewById(R.id.number_list);
            TextView m = minViewHolder.itemView.findViewById(R.id.number_list);

            String hour = h.getText().toString();
            String min = m.getText().toString();


            //values to be inserted or updated in db
            ContentValues contentValues = new ContentValues();
            contentValues.put(AlarmContract.AlarmEntry.HOUR, hour);
            contentValues.put(AlarmContract.AlarmEntry.MIN, min);
            contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmContract.AlarmEntry.ALARM_ACTIVE);
            contentValues.put(AlarmContract.AlarmEntry.TIME, Integer.parseInt(hour + min));
            contentValues.put(AlarmContract.AlarmEntry.SUNDAY, 0);
            contentValues.put(AlarmEntry.MONDAY, 0);
            contentValues.put(AlarmEntry.TUESDAY, 0);
            contentValues.put(AlarmEntry.WEDNESDAY, 0);
            contentValues.put(AlarmEntry.THURSDAY, 0);
            contentValues.put(AlarmEntry.FRIDAY, 0);
            contentValues.put(AlarmEntry.SATURDAY, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            calendar.set(Calendar.MINUTE, Integer.parseInt(min));
            calendar.set(Calendar.SECOND, 0);

            //if the option for repeat is selected then change values for specific days in db
            // and set that day in the calendar
            if (sun) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                contentValues.put(AlarmContract.AlarmEntry.SUNDAY, 1);
            }
            if (mon) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                contentValues.put(AlarmContract.AlarmEntry.MONDAY, 1);
            }
            if (tue) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                contentValues.put(AlarmContract.AlarmEntry.TUESDAY, 1);
            }
            if (wed) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                contentValues.put(AlarmContract.AlarmEntry.WEDNESDAY, 1);
            }
            if (thu) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                contentValues.put(AlarmContract.AlarmEntry.THURSDAY, 1);
            }
            if (fri) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                contentValues.put(AlarmContract.AlarmEntry.FRIDAY, 1);
            }
            if (sat) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                contentValues.put(AlarmContract.AlarmEntry.SATURDAY, 1);
            }


            //for adding alarm insert in db
            //else update the alarm
            if (intentCode == -1) {
                Uri uri = getContentResolver().insert(AlarmContract.AlarmEntry.CONTENT_URI, contentValues);
                id = (int) ContentUris.parseId(uri);
            } else {
                id = (int) intent.getLongExtra(AlarmContract.AlarmEntry._ID, -1);
                Uri uri = ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, id);
                getContentResolver().update(uri, contentValues, null, null);
            }


            //data for work request
            Data data = new Data.Builder()
                    .putInt(AlarmContract.AlarmEntry.HOUR, Integer.parseInt(hour))
                    .putInt(AlarmContract.AlarmEntry.MIN, Integer.parseInt(min))
                    .putInt(AlarmContract.AlarmEntry.PENDING, id)
                    .putBoolean(AlarmContract.AlarmEntry.SUNDAY, sun)
                    .putBoolean(AlarmContract.AlarmEntry.MONDAY, mon)
                    .putBoolean(AlarmContract.AlarmEntry.TUESDAY, tue)
                    .putBoolean(AlarmContract.AlarmEntry.WEDNESDAY, wed)
                    .putBoolean(AlarmContract.AlarmEntry.THURSDAY, thu)
                    .putBoolean(AlarmContract.AlarmEntry.FRIDAY, fri)
                    .putBoolean(AlarmContract.AlarmEntry.SATURDAY, sat)
                    .build();

            //if any day is selected that means alarm should repeat
            boolean repeat = false;
            if (sun || mon || tue || wed || thu || fri || sat) repeat = true;


            // code when alarm is set for repeat
            OneTimeWorkRequest oneTimeWorkRequest;
            if (repeat) {
                Log.v("", "making a work request");
                oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MultipleAlarm.class).setInputData(data).build();
                workManager.enqueueUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);
                AlarmUtils.repeatingAlarmMessage(NewAlarmActivity.this, calendar);
            }
            // when alarm is set for one time only
            else {
                oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class).setInputData(data).build();
                workManager.enqueueUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);
                AlarmUtils.showMessage(NewAlarmActivity.this, calendar);
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //set to current time only when new alarm is added
        //not when editing alarm
        if (intentCode == -1) {
            setToCurrentTime();
        }
    }


    //method to set the hours and minutes list to present time when the activity is created
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