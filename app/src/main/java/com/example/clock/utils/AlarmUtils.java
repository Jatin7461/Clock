package com.example.clock.utils;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import androidx.work.Data;

import java.util.Calendar;

public class AlarmUtils {

    public static final long DAY = 24 * 60 * 60 * 1000;

    public static void showMessage(Context context, Calendar calendar) {

        long currentTime = System.currentTimeMillis();
        long calendarTime = calendar.getTimeInMillis();
        String time;
        long remainingTime;
        if (currentTime > calendarTime) {
            remainingTime = calendarTime + DAY - currentTime;
        } else {
            remainingTime = calendarTime - currentTime;
        }
        time = calculateTime(remainingTime);
        Toast.makeText(context, "Alarm will remind in " + time, Toast.LENGTH_SHORT).show();


    }

    private static String calculateTime(long remainingTime) {
        int seconds = (int) remainingTime / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        int days = hours / 24;

        minutes %= 60;
        hours %= 24;

        String time = "";
        if (days != 0) {
            time += days + " days ";
        }

        if (hours != 0) {
            if (hours == 1) {
                time += hours + " hour";
            } else {
                time += hours + " hours ";
            }
        }
        if (minutes != 0) {
            if (minutes == 1) {
                time += minutes + "minute";
            } else {
                time += minutes + " minutes";
            }
        } else if (days == 0 && hours == 0 && minutes == 0) {
            time += "less than a minute";
        }

        return time;
    }




}
