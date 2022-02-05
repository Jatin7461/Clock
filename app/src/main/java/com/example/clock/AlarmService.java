package com.example.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.example.clock.provider.AlarmContract;

import java.util.Calendar;

import com.example.clock.provider.AlarmContract.AlarmEntry;

public class AlarmService extends Service {

    Ringtone r;
    RadioButton radioButton;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Uri uri = Uri.parse(intent.getStringExtra("ringtone"));
        this.r = RingtoneManager.getRingtone(this, uri);
        r.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.FLAG_AUDIBILITY_ENFORCED).build());
        r.play();

        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        r.stop();
    }
}
