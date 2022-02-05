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
        int hour = intent.getIntExtra(AlarmContract.AlarmEntry.HOUR, -1);
        int min = intent.getIntExtra(AlarmContract.AlarmEntry.MIN, -1);
        int id = intent.getIntExtra(AlarmContract.AlarmEntry._ID, -1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        if (hour == -1 || min == -1)
            Log.v("", "no alarm");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
