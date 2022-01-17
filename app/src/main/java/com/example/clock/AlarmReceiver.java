package com.example.clock;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ((Intent.ACTION_BOOT_COMPLETED).equals(intent.getAction())) {
            // reset all alarms
        } else {
            // perform your scheduled task here (eg. send alarm notification)
            Toast.makeText(context, "Alarm went off", Toast.LENGTH_SHORT).show();

            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "0")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText("Wake up")
                    .setContentTitle("Alarm")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL);

            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            manager.notify(0, notification.build());


            Uri notify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            Ringtone r = RingtoneManager.getRingtone(context, notify);
            r.play();
        }
    }
}
