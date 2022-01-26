package com.example.clock;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkRequest;

import java.util.Set;

public class AlarmRebootReceiver extends BroadcastReceiver {

    String TAG = AlarmRebootReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {

//        Log.v(TAG, intent.getAction().toString());
//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")) {
////            Toast.makeText(context, "Device Rebooted rescheduling alarms", Toast.LENGTH_SHORT).show();
//            NotificationCompat.Builder build = new NotificationCompat.Builder(context, "hello")
//                    .setSmallIcon(R.drawable.ic_launcher_background)
//                    .setContentTitle("alarm")
//                    .setContentText("alarm")
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setVibrate(new long[]{1000, 1000, 1000, 1000})
//                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//
//            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
//            manager.notify(1, build.build());
//            Log.v(TAG, "alarm rescheduled");

        
        Log.v(TAG, intent.getAction());
        Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();
    }

}

