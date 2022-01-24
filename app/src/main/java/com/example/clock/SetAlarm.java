package com.example.clock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import android.view.Window;
import android.view.WindowManager.LayoutParams;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class SetAlarm extends BroadcastReceiver {

    final Handler handler = new Handler();
    String TAG = SetAlarm.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {


        String time = intent.getStringExtra("time");
        Log.v(TAG, "alarm called for " + time);
        Toast.makeText(context, "wake up", Toast.LENGTH_LONG).show();

        try {

            Intent inten = new Intent(context, AlarmActivity.class);
            inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(inten);
        } catch (Exception e) {
            e.printStackTrace();

        }


    }


}
