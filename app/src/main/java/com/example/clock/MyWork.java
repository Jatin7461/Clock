package com.example.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.clock.provider.AlarmContract;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MyWork extends Worker {

    AlarmManager alarmManager;
    public static final String HOUR = "hour", MIN = "min";
    private final int DAY = 24 * 60 * 60 * 1000;

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
            int code = getInputData().getInt(AlarmContract.AlarmEntry.PENDING, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);
            Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
            intent.putExtra(AlarmContract.AlarmEntry._ID, code);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), code, intent, 0);


//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

//            ContentValues contentValues = new ContentValues();
//            contentValues.put(AlarmContract.AlarmEntry.ACTIVE, 0);
//
//            Uri uri = ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, code);
//            getApplicationContext().getContentResolver().update(uri, contentValues, null, null);

//            Toast.makeText(getApplicationContext(), "alarm set", Toast.LENGTH_SHORT).show();

            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }


}
