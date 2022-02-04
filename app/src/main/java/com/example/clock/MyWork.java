//package com.example.clock;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.work.WorkManager;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//import com.example.clock.provider.AlarmContract;
//
//import java.util.Calendar;
//import java.util.concurrent.TimeUnit;
//
//public class MyWork extends Worker {
//
//    AlarmManager alarmManager;
//
//    public MyWork(Context context, WorkerParameters parameters) {
//        super(context, parameters);
//
//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//
//
//        try {
//
//            //get input data
//            int hour = getInputData().getInt(AlarmContract.AlarmEntry.HOUR, -1);
//            int min = getInputData().getInt(AlarmContract.AlarmEntry.MIN, -1);
//            if (hour == -1 || min == -1) {
//                Toast.makeText(getApplicationContext(), "invalid time", Toast.LENGTH_SHORT).show();
//                Result.failure();
//            }
//            int code = getInputData().getInt(AlarmContract.AlarmEntry.PENDING, 0);
//            int requestCode = getInputData().getInt(AlarmContract.AlarmEntry.REQUEST_CODE, -1);
//            boolean multiple = getInputData().getBoolean("multiple", false);
//
//
//            //set intent and put data
//            Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
//            intent.putExtra(AlarmContract.AlarmEntry._ID, code);
//            intent.putExtra(AlarmContract.AlarmEntry.HOUR, hour);
//            intent.putExtra(AlarmContract.AlarmEntry.MIN, min);
//            intent.putExtra(AlarmContract.AlarmEntry.REQUEST_CODE, requestCode);
//            intent.putExtra("multiple", multiple);
//
//            //fire the alarm
//            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), code, intent, 0);
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1, pendingIntent);
//
//
//            return Result.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.failure();
//        }
//    }
//
//
//}
