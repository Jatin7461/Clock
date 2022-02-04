package com.example.clock.Adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clock.provider.AlarmContract.AlarmEntry;

import androidx.cursoradapter.widget.CursorAdapter;


import com.example.clock.AlarmActivity;

import com.example.clock.NewAlarmActivity;
import com.example.clock.R;
import com.example.clock.provider.AlarmContract;
import com.example.clock.utils.AlarmUtils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AlarmCursorAdapter extends CursorAdapter {


    AlarmManager alarmManager;
    private final int SUNDAY_CODE = 10000;
    private final int MONDAY_CODE = 20000;
    private final int TUESDAY_CODE = 30000;
    private final int WEDNESDAY_CODE = 40000;
    private final int THURSDAY_CODE = 50000;
    private final int FRIDAY_CODE = 60000;
    private final int SATURDAY_CODE = 70000;

    public AlarmCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.alarms, viewGroup, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView time = view.findViewById(R.id.alarm_time);
        int hourId = cursor.getColumnIndex(AlarmContract.AlarmEntry.HOUR);
        String h = cursor.getString(hourId);
        int minId = cursor.getColumnIndex(AlarmContract.AlarmEntry.MIN);
        String m = cursor.getString(minId);


        int hour = Integer.parseInt(h);
        int min = Integer.parseInt(m);
        h = String.format("%02d", hour);
        m = String.format("%02d", min);
        time.setText(h + ":" + m);

        String daysText = getDaysText(cursor, context);
        TextView days = view.findViewById(R.id.alarm_days);
        if (daysText.length() > 0) {
            days.setText(daysText);
            days.setVisibility(View.VISIBLE);
        }
//        TextView days = view.findViewById(R.id.alarm_days);
//        days.setText(daysText);

        Switch s = view.findViewById(R.id.alarm_switch);
        int boolId = cursor.getColumnIndex(AlarmContract.AlarmEntry.ACTIVE);
        int str = cursor.getInt(boolId);
        boolean b;
        if (str == AlarmContract.AlarmEntry.ALARM_ACTIVE) {
            b = true;
        } else {
            b = false;
        }
        int colId = cursor.getColumnIndex(AlarmContract.AlarmEntry._ID);
        int id = cursor.getInt(colId);
        s.setChecked(b);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isShown()) {

                    int sunId = cursor.getColumnIndex(AlarmContract.AlarmEntry.SUNDAY);
                    int sun = cursor.getInt(sunId);

                    int monId = cursor.getColumnIndex(AlarmContract.AlarmEntry.MONDAY);
                    int mon = cursor.getInt(monId);

                    int tueId = cursor.getColumnIndex(AlarmContract.AlarmEntry.TUESDAY);
                    int tue = cursor.getInt(tueId);

                    int wedId = cursor.getColumnIndex(AlarmContract.AlarmEntry.WEDNESDAY);
                    int wed = cursor.getInt(wedId);

                    int thuId = cursor.getColumnIndex(AlarmContract.AlarmEntry.THURSDAY);
                    int thu = cursor.getInt(thuId);

                    int friId = cursor.getColumnIndex(AlarmContract.AlarmEntry.FRIDAY);
                    int fri = cursor.getInt(friId);

                    int satId = cursor.getColumnIndex(AlarmContract.AlarmEntry.SATURDAY);
                    int sat = cursor.getInt(satId);


                    boolean multiple = false;
                    if (daysText.length() > 0) {

                        multiple = true;
                    }

                    Uri uri = ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, id);
                    boolean sunday = false, monday = false, tuesday = false, wednesday = false, thursday = false, friday = false, saturday = false;
                    if (sun == 1) sunday = true;
                    if (mon == 1) monday = true;
                    if (tue == 1) tuesday = true;
                    if (wed == 1) wednesday = true;
                    if (thu == 1) thursday = true;
                    if (fri == 1) friday = true;
                    if (sat == 1) saturday = true;
                    if (b) {
//                    Toast.makeText(context, "yes", Toast.LENGTH_SHORT).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmContract.AlarmEntry.ALARM_ACTIVE);
                        context.getContentResolver().update(uri, contentValues, null, null);

//                        Data data = new Data.Builder()
//                                .putBoolean(AlarmContract.AlarmEntry.SUNDAY, sunday)
//                                .putBoolean(AlarmContract.AlarmEntry.MONDAY, monday)
//                                .putBoolean(AlarmContract.AlarmEntry.TUESDAY, tuesday)
//                                .putBoolean(AlarmContract.AlarmEntry.WEDNESDAY, wednesday)
//                                .putBoolean(AlarmContract.AlarmEntry.THURSDAY, thursday)
//                                .putBoolean(AlarmContract.AlarmEntry.FRIDAY, friday)
//                                .putBoolean(AlarmContract.AlarmEntry.SATURDAY, saturday)
//                                .putInt(AlarmContract.AlarmEntry.HOUR, (hour))
//                                .putInt(AlarmContract.AlarmEntry.MIN, (min))
//                                .putInt(AlarmContract.AlarmEntry.PENDING, id).build();


                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, (hour));
                        calendar.set(Calendar.MINUTE, (min));
                        calendar.set(Calendar.SECOND, 0);

                        long currentTime = System.currentTimeMillis();
                        long calendarTime = calendar.getTimeInMillis();


                        if (multiple) {
                            if (sunday) setRepeatingAlarm(SUNDAY_CODE, hour, min, context, id);
                            if (monday) setRepeatingAlarm(MONDAY_CODE, hour, min, context, id);
                            if (tuesday) setRepeatingAlarm(TUESDAY_CODE, hour, min, context, id);
                            if (wednesday)
                                setRepeatingAlarm(WEDNESDAY_CODE, hour, min, context, id);
                            if (thursday) setRepeatingAlarm(THURSDAY_CODE, hour, min, context, id);
                            if (friday) setRepeatingAlarm(FRIDAY_CODE, hour, min, context, id);
                            if (saturday) setRepeatingAlarm(SATURDAY_CODE, hour, min, context, id);
                        } else {
                            Intent i = new Intent(context, AlarmActivity.class);
                            i.putExtra(AlarmEntry.HOUR, hour);
                            i.putExtra(AlarmEntry.MIN, min);
                            i.putExtra(AlarmEntry._ID, id);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, id, i, 0);
                            if (calendarTime < currentTime) {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);
                            } else {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            }
                        }


                        AlarmUtils.showMessage(context, calendar);

                    } else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmContract.AlarmEntry.ALARM_INACTIVE);
                        context.getContentResolver().update(uri, contentValues, null, null);

                        if (multiple) {

                            if (sunday) {
                                cancelRepeatingAlarm(SUNDAY_CODE, id, context, hour, min);
                            }
                            if (monday) cancelRepeatingAlarm(MONDAY_CODE, id, context, hour, min);
                            if (tuesday) cancelRepeatingAlarm(TUESDAY_CODE, id, context, hour, min);
                            if (wednesday)
                                cancelRepeatingAlarm(WEDNESDAY_CODE, id, context, hour, min);
                            if (thursday)
                                cancelRepeatingAlarm(THURSDAY_CODE, id, context, hour, min);
                            if (friday) cancelRepeatingAlarm(FRIDAY_CODE, id, context, hour, min);
                            if (saturday)
                                cancelRepeatingAlarm(SATURDAY_CODE, id, context, hour, min);

                        } else {
                            Intent i = new Intent(context, AlarmActivity.class);
                            i.putExtra(AlarmEntry.HOUR, hour);
                            i.putExtra(AlarmEntry.MIN, min);
                            i.putExtra(AlarmEntry._ID, id);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, id, i, 0);
                            alarmManager.cancel(pendingIntent);
                        }
                    }
                }
            }
        });

    }

    private String getDaysText(Cursor cursor, Context context) {
        int sunId = cursor.getColumnIndex(AlarmContract.AlarmEntry.SUNDAY);
        int sun = cursor.getInt(sunId);

        int monId = cursor.getColumnIndex(AlarmContract.AlarmEntry.MONDAY);
        int mon = cursor.getInt(monId);

        int tueId = cursor.getColumnIndex(AlarmContract.AlarmEntry.TUESDAY);
        int tue = cursor.getInt(tueId);

        int wedId = cursor.getColumnIndex(AlarmContract.AlarmEntry.WEDNESDAY);
        int wed = cursor.getInt(wedId);

        int thuId = cursor.getColumnIndex(AlarmContract.AlarmEntry.THURSDAY);
        int thu = cursor.getInt(thuId);

        int friId = cursor.getColumnIndex(AlarmContract.AlarmEntry.FRIDAY);
        int fri = cursor.getInt(friId);

        int satId = cursor.getColumnIndex(AlarmContract.AlarmEntry.SATURDAY);
        int sat = cursor.getInt(satId);

        String showDays = new String();

        if (sun == 1 && mon == 1 && tue == 1 && wed == 1 && thu == 1 && fri == 1 && sat == 1) {
            showDays = context.getResources().getString(R.string.everyday);
            return showDays;
        }

        if (mon == 1) {
            showDays += context.getResources().getString(R.string.monday);
        }
        if (tue == 1) {
            if (showDays.length() != 0) showDays += ", ";
            showDays += context.getResources().getString(R.string.tuesday);
        }
        if (wed == 1) {
            if (showDays.length() != 0) showDays += ", ";
            showDays += context.getResources().getString(R.string.wednesday);
        }
        if (thu == 1) {
            if (showDays.length() != 0) showDays += ", ";
            showDays += context.getResources().getString(R.string.thursday);
        }
        if (fri == 1) {
            if (showDays.length() != 0) showDays += ", ";
            showDays += context.getResources().getString(R.string.friday);
        }
        if (sat == 1) {
            if (showDays.length() != 0) showDays += ", ";
            showDays += context.getResources().getString(R.string.saturday);
        }
        if (sun == 1) {
            if (showDays.length() != 0) showDays += ", ";
            showDays += context.getResources().getString(R.string.sunday);
        }
        return showDays;
    }

    private void setRepeatingAlarm(int requestCode, int hour, int min, Context context, int id) {
        long DAY = 24 * 60 * 60 * 1000;
        Intent i = new Intent(context, AlarmActivity.class);
        i.putExtra(AlarmEntry.HOUR, hour);
        i.putExtra(AlarmEntry.MIN, min);
        i.putExtra(AlarmEntry._ID, id);
        i.putExtra("multiple", true);
        i.putExtra(AlarmEntry.REQUEST_CODE, requestCode);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, requestCode / 10000);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id + requestCode, i, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + DAY * 7, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void cancelRepeatingAlarm(int requestCode, int id, Context context, int hour, int min) {
        Intent i = new Intent(context, AlarmActivity.class);
        i.putExtra(AlarmEntry.HOUR, hour);
        i.putExtra(AlarmEntry.MIN, min);
        i.putExtra(AlarmEntry._ID, id);
        i.putExtra("multiple", true);
        i.putExtra(AlarmEntry.REQUEST_CODE, requestCode);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id + requestCode, i, 0);
        alarmManager.cancel(pendingIntent);
    }
}
