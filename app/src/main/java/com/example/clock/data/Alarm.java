package com.example.clock.data;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class Alarm {
    String hour, minute, second;
    Context context;
    Intent intent;
    PendingIntent pendingIntent;
    WorkManager manager;
    WorkRequest request;

    public Alarm(Context context, String hour, String minute, String second) {
        this.context = context;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }


}
