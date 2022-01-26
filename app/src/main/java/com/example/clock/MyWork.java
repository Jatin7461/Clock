package com.example.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;

public class MyWork extends Worker {

    AlarmManager alarmManager;
    public static final String HOUR = "hour", MIN = "min";


    public MyWork(Context context, WorkerParameters parameters) {
        super(context, parameters);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @NonNull
    @Override
    public Result doWork() {


        try {

            int hour = getInputData().getInt("hour", -1);
            int min = getInputData().getInt("min", -1);
            if (hour == -1 || min == -1) {
                Toast.makeText(getApplicationContext(), "invalid time", Toast.LENGTH_SHORT).show();
                Result.failure();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);
            Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }


}
