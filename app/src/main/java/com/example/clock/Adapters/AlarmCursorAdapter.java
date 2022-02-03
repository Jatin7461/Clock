package com.example.clock.Adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
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

import androidx.cursoradapter.widget.CursorAdapter;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.clock.CancelAlarm;
import com.example.clock.MultipleAlarm;
import com.example.clock.MyWork;
import com.example.clock.R;
import com.example.clock.provider.AlarmContract;
import com.example.clock.utils.AlarmUtils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AlarmCursorAdapter extends CursorAdapter {

    WorkManager workManager;

    public AlarmCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        workManager = WorkManager.getInstance(context);
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
        String hour = cursor.getString(hourId);
        int minId = cursor.getColumnIndex(AlarmContract.AlarmEntry.MIN);
        String min = cursor.getString(minId);
        time.setText(hour + ":" + min);

        TextView days = view.findViewById(R.id.alarm_days);
        String daysText = getDaysText(cursor, context);
        days.setText(daysText);


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
                    if (b) {
//                    Toast.makeText(context, "yes", Toast.LENGTH_SHORT).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmContract.AlarmEntry.ALARM_ACTIVE);
                        context.getContentResolver().update(uri, contentValues, null, null);
                        boolean sunday = false, monday = false, tuesday = false, wednesday = false, thursday = false, friday = false, saturday = false;
                        if (sun == 1) sunday = true;
                        if (mon == 1) monday = true;
                        if (tue == 1) tuesday = true;
                        if (wed == 1) wednesday = true;
                        if (thu == 1) thursday = true;
                        if (fri == 1) friday = true;
                        if (sat == 1) saturday = true;

                        Data data = new Data.Builder()
                                .putBoolean(AlarmContract.AlarmEntry.SUNDAY, sunday)
                                .putBoolean(AlarmContract.AlarmEntry.MONDAY, monday)
                                .putBoolean(AlarmContract.AlarmEntry.TUESDAY, tuesday)
                                .putBoolean(AlarmContract.AlarmEntry.WEDNESDAY, wednesday)
                                .putBoolean(AlarmContract.AlarmEntry.THURSDAY, thursday)
                                .putBoolean(AlarmContract.AlarmEntry.FRIDAY, friday)
                                .putBoolean(AlarmContract.AlarmEntry.SATURDAY, saturday)
                                .putInt(AlarmContract.AlarmEntry.HOUR, Integer.parseInt(hour))
                                .putInt(AlarmContract.AlarmEntry.MIN, Integer.parseInt(min))
                                .putInt(AlarmContract.AlarmEntry.PENDING, id).build();


                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(min));
                        calendar.set(Calendar.SECOND, 0);

                        long currentTime = System.currentTimeMillis();
                        long calendarTime = calendar.getTimeInMillis();

                        OneTimeWorkRequest oneTimeWorkRequest;
                        if (multiple) {
                            oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MultipleAlarm.class).setInputData(data).build();
                            workManager.enqueueUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);
                        } else {

                            if (calendarTime < currentTime) {
                                oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
                                        .setInputData(data).setInitialDelay(currentTime - calendarTime + AlarmUtils.DAY, TimeUnit.MILLISECONDS).build();

                            } else {
                                oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
                                        .setInputData(data).setInitialDelay(calendarTime - currentTime, TimeUnit.MILLISECONDS).build();

                            }
                            workManager.enqueueUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id, ExistingWorkPolicy.KEEP, oneTimeWorkRequest);
                        }


                        AlarmUtils.showMessage(context, calendar);

                    } else {
                        Log.v("", "toggled off");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmContract.AlarmEntry.ALARM_INACTIVE);
                        context.getContentResolver().update(uri, contentValues, null, null);
//                        workManager.cancelUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id);
                        Data data = new Data.Builder()
                                .putInt(AlarmContract.AlarmEntry._ID, id)
                                .putBoolean("multiple", multiple)
                                .build();
                        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(CancelAlarm.class).setInputData(data).build();
                        workManager.enqueue(oneTimeWorkRequest);
//                    workManager.cancelUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id);

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

}
