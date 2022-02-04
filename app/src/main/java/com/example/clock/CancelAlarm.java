//package com.example.clock;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//
//import androidx.annotation.NonNull;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//import com.example.clock.provider.AlarmContract;
//
//public class CancelAlarm extends Worker {
//
//    AlarmManager alarmManager;
//
//    public CancelAlarm(Context context, WorkerParameters workerParameters) {
//        super(context, workerParameters);
//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//
//        int code = getInputData().getInt(AlarmContract.AlarmEntry._ID, -1);
//        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), code, intent, 0);
//        boolean multiple = getInputData().getBoolean("multiple", false);
//        if (multiple == false) {
//            alarmManager.cancel(pendingIntent);
//            return Result.success();
//        } else {
//            PendingIntent pendingIntent1 = PendingIntent.getActivity(getApplicationContext(), code + MultipleAlarm.SUNDAY_CODE, intent, 0);
//            PendingIntent pendingIntent2 = PendingIntent.getActivity(getApplicationContext(), code + MultipleAlarm.MONDAY_CODE, intent, 0);
//            PendingIntent pendingIntent3 = PendingIntent.getActivity(getApplicationContext(), code + MultipleAlarm.TUESDAY_CODE, intent, 0);
//            PendingIntent pendingIntent4 = PendingIntent.getActivity(getApplicationContext(), code + MultipleAlarm.WEDNESDAY_CODE, intent, 0);
//            PendingIntent pendingIntent5 = PendingIntent.getActivity(getApplicationContext(), code + MultipleAlarm.THURSDAY_CODE, intent, 0);
//            PendingIntent pendingIntent6 = PendingIntent.getActivity(getApplicationContext(), code + MultipleAlarm.FRIDAY_CODE, intent, 0);
//            PendingIntent pendingIntent7 = PendingIntent.getActivity(getApplicationContext(), code + MultipleAlarm.SATURDAY_CODE, intent, 0);
//            alarmManager.cancel(pendingIntent1);
//            alarmManager.cancel(pendingIntent2);
//            alarmManager.cancel(pendingIntent3);
//            alarmManager.cancel(pendingIntent4);
//            alarmManager.cancel(pendingIntent5);
//            alarmManager.cancel(pendingIntent6);
//            alarmManager.cancel(pendingIntent7);
//            return Result.success();
//        }
////        return Result.failure();
//
//    }
//}
