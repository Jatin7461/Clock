package com.example.clock;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class AutoStartService extends Service {
//
//    public AutoStartService(){
//
//    }

//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//
//    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
