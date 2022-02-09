package com.example.clock;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clock.Fragments.SnoozeFragment;
import com.example.clock.provider.AlarmContract.AlarmEntry;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;

import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
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

import android.widget.LinearLayout;
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
import java.util.Map;


public class NewAlarmActivity extends AppCompatActivity implements SnoozeFragment.SnoozeTimeHelper {


    boolean alarmChanged = false;
    private Map<String, String> map;
    final int SCROLL = Integer.MAX_VALUE / 2;

    public static final int SUNDAY_CODE = 10000;
    public static final int MONDAY_CODE = 20000;
    public static final int TUESDAY_CODE = 30000;
    public static final int WEDNESDAY_CODE = 40000;
    public static final int THURSDAY_CODE = 50000;
    public static final int FRIDAY_CODE = 60000;
    public static final int SATURDAY_CODE = 70000;
    RadioButton toggleVibrate;
    private final int DAY = 24 * 60 * 60 * 1000;
    final String TAG = NewAlarmActivity.class.getName();

    LinearLayout snoozeLayout;

    int hoursPosition, minutePosition;

    private ArrayList<String> hoursList;
    private ArrayList<String> minutesList;
    private static RecyclerView hoursRecyclerView, minutesRecyclerView;
    //    private Button mButton, addAlarm, addNewAlarm;
    private AlarmManager alarmManager;
    //    private ConstraintLayout vibration;
//    private Toolbar toolbar;
    private Intent intent;
    private int intentCode;
    private FloatingActionButton sunday, monday, tuesday, wednesday, thursday, friday, saturday;
    private boolean sun, mon, tue, wed, thu, fri, sat, vib;
    //    private final int LOADER_ID = 1;
    private String alarmRingtone;
    private String ringtoneUri;
    LinearLayout ringtoneActivity;

    private int snoozeTime = -1;
    TextView ringtone, vibrate, snooze;


    @Override
    public void getSnoozeInterval(int time) {
        snoozeTime = time;
        setSnoozeTime(snoozeTime);
        Log.v("", "time interval b/w snooze is " + time);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            NumberAdapter hoursAdapter, minutesAdapter;
            setContentView(R.layout.activity_new_alarm);


            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            ringtone = findViewById(R.id.name_of_ringtone);

            snoozeLayout = findViewById(R.id.snooze);

            ringtoneActivity = findViewById(R.id.ringtone);

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

            snooze = findViewById(R.id.snooze_time);

            RingtoneManager manager = new RingtoneManager(this);
            manager.setType(RingtoneManager.TYPE_ALARM);
            Cursor cursor = manager.getCursor();
            Uri uri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
            while (cursor.moveToNext()) {
                String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
                Uri ringtoneuri = Uri.parse(notificationUri);
                if (ringtoneuri.toString().equals(uri.toString())) {
                    ringtone.setText(notificationTitle);
                    alarmRingtone = notificationTitle;
                }
            }
//


            sun = mon = tue = wed = thu = fri = sat = false;
            vib = true;

            hoursList = new ArrayList<>();
            minutesList = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                String s = String.format("%02d", i);
                hoursList.add(s);
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


            //when intent is for new alarm just change title of activity/
            if (intentCode == -1) {
                getSupportActionBar().setTitle(R.string.add_alarm);
                ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM).toString();
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
                alarmRingtone = intent.getStringExtra(AlarmEntry.RINGTONE);
                ringtone.setText(alarmRingtone);
                ringtoneUri = intent.getStringExtra(AlarmEntry.RINGTONE_URI);
                int vibrate = intent.getIntExtra(AlarmEntry.VIBRATE, -1);
                if (vibrate == 1) vib = true;
                else vib = false;

                snoozeTime = intent.getIntExtra(AlarmEntry.SNOOZE_TIME, -1);
                setSnoozeTime(snoozeTime);

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

            ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                // There are no request codes


                                Intent data = result.getData();
                                alarmRingtone = data.getStringExtra(AlarmEntry.RINGTONE);
                                ringtoneUri = data.getStringExtra(AlarmEntry.RINGTONE_URI);
                                ringtone.setText(alarmRingtone);


                            }
                        }
                    });
            ringtoneActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ringIntent = new Intent(NewAlarmActivity.this, RingtoneActivity.class);
                    ringIntent.putExtra(AlarmEntry.RINGTONE, alarmRingtone);
                    ringIntent.putExtra(AlarmEntry.EDIT_ALARM, intentCode);
                    someActivityResultLauncher.launch(ringIntent);


                }
            });

            toggleVibrate = findViewById(R.id.radioButton);
            vibrate = findViewById(R.id.vibrate);
            toggleVibrate.setChecked(vib);

            toggleVibrate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setVibration();
                }
            });
            vibrate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setVibration();
                }
            });


            snoozeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment snoozeFragment = new SnoozeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(AlarmEntry.EDIT_ALARM, intentCode);
                    if (intentCode != -1)
                        snoozeTime = intent.getIntExtra(AlarmEntry.SNOOZE_TIME, -1);
                    bundle.putInt(AlarmEntry.SNOOZE_TIME, snoozeTime);

                    snoozeFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                            android.R.animator.fade_in, android.R.animator.fade_out).add(R.id.snooze_fragment, snoozeFragment).addToBackStack("snooze").commit();

                    Log.v("", "fragment");

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setVibration() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id;
        int menuId = item.getItemId();
        if (menuId == R.id.done_button) {

            try {


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
                contentValues.put(AlarmEntry.RINGTONE, alarmRingtone);
                contentValues.put(AlarmEntry.RINGTONE_URI, ringtoneUri);
                contentValues.put(AlarmEntry.SNOOZE_TIME, snoozeTime);


                if (vib) contentValues.put(AlarmEntry.VIBRATE, AlarmEntry.VIBRATE_ON);
                else contentValues.put(AlarmEntry.VIBRATE, AlarmEntry.VIBRATE_OFF);


                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                calendar.set(Calendar.MINUTE, Integer.parseInt(min));
                calendar.set(Calendar.SECOND, 0);

                //if the option for repeat is selected then change values for specific days in db
                // and set that day in the calendar
                if (sun) {
//                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    contentValues.put(AlarmContract.AlarmEntry.SUNDAY, 1);
                }
                if (mon) {
//                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    contentValues.put(AlarmContract.AlarmEntry.MONDAY, 1);
                }
                if (tue) {
//                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    contentValues.put(AlarmContract.AlarmEntry.TUESDAY, 1);
                }
                if (wed) {
//                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    contentValues.put(AlarmContract.AlarmEntry.WEDNESDAY, 1);
                }
                if (thu) {
//                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    contentValues.put(AlarmContract.AlarmEntry.THURSDAY, 1);
                }
                if (fri) {
//                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    contentValues.put(AlarmContract.AlarmEntry.FRIDAY, 1);
                }
                if (sat) {
//                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
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


                //if any day is selected that means alarm should repeat
                boolean repeat = false;
                if (sun || mon || tue || wed || thu || fri || sat) repeat = true;

                int Hour = Integer.parseInt(hour);
                int Min = Integer.parseInt(min);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                // code when alarm is set for repeat

                if (repeat) {
                    if (sun) {
                        setRepeatingAlarm(SUNDAY_CODE, Hour, Min, id);
                    }
                    if (mon) {
                        setRepeatingAlarm(MONDAY_CODE, Hour, Min, id);
                    }
                    if (tue) {
                        setRepeatingAlarm(TUESDAY_CODE, Hour, Min, id);
                    }
                    if (wed) {
                        setRepeatingAlarm(WEDNESDAY_CODE, Hour, Min, id);
                    }
                    if (thu) {
                        setRepeatingAlarm(THURSDAY_CODE, Hour, Min, id);
                    }
                    if (fri) {
                        setRepeatingAlarm(FRIDAY_CODE, Hour, Min, id);
                    }
                    if (sat) {
                        setRepeatingAlarm(SATURDAY_CODE, Hour, Min, id);
                    }
                }

                // when alarm is set for one time only
                else {
                    //data for work request
                    Intent i = new Intent(NewAlarmActivity.this, AlarmActivity.class);

                    i.putExtra(AlarmEntry.HOUR, Integer.parseInt(hour));
                    i.putExtra(AlarmEntry.MIN, Integer.parseInt(min));
                    i.putExtra(AlarmEntry._ID, id);
                    i.putExtra(AlarmEntry.RINGTONE_URI, ringtoneUri);


                    PendingIntent pendingIntent = PendingIntent.getActivity(this, id, i, 0);
                    long calendarTime = calendar.getTimeInMillis();
                    if (System.currentTimeMillis() > calendarTime) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarTime + DAY, pendingIntent);
                        } else
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarTime + DAY, pendingIntent);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        else
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarTime, pendingIntent);
                    }


                    AlarmUtils.showMessage(NewAlarmActivity.this, calendar);

                }
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
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


    private void setRepeatingAlarm(int requestCode, int hour, int min, int id) {
        Intent i = new Intent(NewAlarmActivity.this, AlarmActivity.class);
        i.putExtra(AlarmEntry.HOUR, hour);
        i.putExtra(AlarmEntry.MIN, min);
        i.putExtra(AlarmEntry._ID, id);
        i.putExtra("multiple", true);
        i.putExtra(AlarmEntry.REQUEST_CODE, requestCode);
        i.putExtra(AlarmEntry.RINGTONE, ringtoneUri.toString());


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, requestCode / 10000);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id + requestCode, i, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + DAY * 7, pendingIntent);
            else
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + DAY * 7, pendingIntent);
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            else
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public void onBackPressed() {
        Log.v("", "back pressed");
        super.onBackPressed();
    }

    private void setSnoozeTime(int snoozeTime) {
//        int snoozeTime = intent.getIntExtra(AlarmEntry.SNOOZE_TIME, -1);
        switch (snoozeTime) {
            case AlarmEntry.FIVE_MINUTES:
                snooze.setText(getResources().getString(R.string.minutes_five));
                break;
            case AlarmEntry.TEN_MINUTES:
                snooze.setText(getResources().getString(R.string.minutes_ten));
                break;
            case AlarmEntry.FIFTEEN_MINUTES:
                snooze.setText(getResources().getString(R.string.minutes_fifteen));
                break;
            case AlarmEntry.THIRTY_MINUTES:
                snooze.setText(getResources().getString(R.string.minutes_thirty));
                break;
            default:
                break;


        }
    }


}