package com.example.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.clock.provider.AlarmContract;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MyWork extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        int id = intent.getIntExtra(AlarmContract.AlarmEntry._ID, -1);


        ContentValues contentValues = new ContentValues();
        contentValues.put(AlarmContract.AlarmEntry.SNOOZE, 0);
        contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmContract.AlarmEntry.ALARM_INACTIVE);
        Uri uri = ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, id);


        Intent i = new Intent(getApplicationContext(), AlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, i, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);


        getContentResolver().update(uri, contentValues, null, null);

        return START_NOT_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
