package com.example.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.clock.provider.AlarmContract;

public class CancelAlarm extends Worker {

    AlarmManager alarmManager;

    public CancelAlarm(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    }

    @NonNull
    @Override
    public Result doWork() {

        int code = getInputData().getInt(AlarmContract.AlarmEntry._ID, -1);
        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), code, intent, 0);

        if (code != -1) {
            alarmManager.cancel(pendingIntent);
            return Result.success();
        }
        return Result.failure();

    }
}
