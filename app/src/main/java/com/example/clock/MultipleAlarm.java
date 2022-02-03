package com.example.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.clock.provider.AlarmContract;

import java.util.Calendar;

public class MultipleAlarm extends Worker {

    private static final long DAY = 24 * 60 * 60 * 1000;
    public static final int SUNDAY_CODE = 10000;
    public static final int MONDAY_CODE = 20000;
    public static final int TUESDAY_CODE = 30000;
    public static final int WEDNESDAY_CODE = 40000;
    public static final int THURSDAY_CODE = 50000;
    public static final int FRIDAY_CODE = 60000;
    public static final int SATURDAY_CODE = 70000;
    AlarmManager alarmManager;
    Context context;

    public MultipleAlarm(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {

            boolean sun = getInputData().getBoolean(AlarmContract.AlarmEntry.SUNDAY, false);
            boolean mon = getInputData().getBoolean(AlarmContract.AlarmEntry.MONDAY, false);
            boolean tue = getInputData().getBoolean(AlarmContract.AlarmEntry.TUESDAY, false);
            boolean wed = getInputData().getBoolean(AlarmContract.AlarmEntry.WEDNESDAY, false);
            boolean thu = getInputData().getBoolean(AlarmContract.AlarmEntry.THURSDAY, false);
            boolean fri = getInputData().getBoolean(AlarmContract.AlarmEntry.FRIDAY, false);
            boolean sat = getInputData().getBoolean(AlarmContract.AlarmEntry.SATURDAY, false);
            int code = getInputData().getInt(AlarmContract.AlarmEntry.PENDING, -1);
            int hour = getInputData().getInt(AlarmContract.AlarmEntry.HOUR, -1);
            int min = getInputData().getInt(AlarmContract.AlarmEntry.MIN, -1);
            Log.v("", "do work");


            if (sun) {
                setAlarm(hour, min, code + SUNDAY_CODE);
            }
            if (mon) {
                setAlarm(hour, min, code + MONDAY_CODE);
            }
            if (tue) {
                setAlarm(hour, min, code + TUESDAY_CODE);
            }
            if (wed) {
                setAlarm(hour, min, code + WEDNESDAY_CODE);
            }
            if (thu) {
                setAlarm(hour, min, code + THURSDAY_CODE);
            }
            if (fri) {
                setAlarm(hour, min, code + FRIDAY_CODE);
            }
            if (sat) {
                setAlarm(hour, min, code + SATURDAY_CODE);
            }
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }


    private void setAlarm(int hour, int min, int requestCode) {
        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
        intent.putExtra(AlarmContract.AlarmEntry.HOUR, hour);
        intent.putExtra(AlarmContract.AlarmEntry.MIN, min);
        intent.putExtra("code", requestCode);
        intent.putExtra("multiple", true);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, requestCode / 10000);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, intent, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + (DAY * 7), pendingIntent);
        } else {

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        }
    }
}
