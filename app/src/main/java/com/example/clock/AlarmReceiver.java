package com.example.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.clock.provider.AlarmContract;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AlarmActivity.class);
        Intent alarm;
        Calendar calendar = Calendar.getInstance();
        int hour = intent.getIntExtra("hour", -1);
        int min = intent.getIntExtra("min", -1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        if (hour == -1 || min == -1)
            Log.v("", "no alarm");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 999, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
    }
}
