package com.example.clock.provider;

import android.net.Uri;
import android.os.Vibrator;
import android.provider.BaseColumns;

public class AlarmContract {

    public static final String CONTENT_AUTHORITY = "com.example.clock";

    public static final Uri BASE_CONTENT_AUTHORITY = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ALARMS = "alarm";

    public static class AlarmEntry implements BaseColumns {

        public static final String TABLE_NAME = "alarm";
        public static final String TIME = "time";
        public static final String HOUR = "hour";
        public static final String MIN = "min";
        public static final String ACTIVE = "active";
        public static final String SUNDAY = "sunday";
        public static final String MONDAY = "monday";
        public static final String TUESDAY = "tuesday";
        public static final String WEDNESDAY = "wednesday";
        public static final String THURSDAY = "thursday";
        public static final String FRIDAY = "friday";
        public static final String SATURDAY = "saturday";
        public static final String LABEL = "label";
        public static final String PENDING = "pending";
        public static final String _ID = BaseColumns._ID;
        public static final String RINGTONE = "ringtone";
        public static final String RINGTONE_URI = "ringtoneuri";
        public static final String VIBRATE = "vibrate";
        public static final String SNOOZE = "snooze";
        public static final String SNOOZE_TIME = "snoozetime";
        public static final String SNOOZE_HOUR = "snoozehour";
        public static final String SNOOZE_MIN = "snoozemin";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TIME + " INTEGER NOT NULL, " +
                HOUR + " TEXT NOT NULL, " + MIN + " TEXT NOT NULL, " + ACTIVE + " INTEGER NOT NULL DEFAULT 1, " + SUNDAY + " INTEGER NOT NULL DEFAULT 0, "
                + MONDAY + " INTEGER NOT NULL DEFAULT 0, " + TUESDAY + " INTEGER NOT NULL DEFAULT 0, " + WEDNESDAY
                + " INTEGER NOT NULL DEFAULT 0, " + THURSDAY + " INTEGER NOT NULL DEFAULT 0, " + FRIDAY + " INTEGER NOT NULL DEFAULT 0, "
                + SATURDAY + " INTEGER NOT NULL DEFAULT 0, " + RINGTONE + " TEXT, " + RINGTONE_URI + " TEXT, " + VIBRATE + " INTEGER, "
                + SNOOZE + " INTEGER NOT NULL DEFAULT 0, " + SNOOZE_TIME + " INTEGER NOT NULL DEFAULT 5, " + SNOOZE_HOUR + " INTEGER, " + SNOOZE_MIN
                + " INTEGER, " + LABEL + " TEXT )";

        public static final int SELECTED = 1;
        public static final int NOT_SELECTED = 0;

        public static final String RINGTONE_OFF = "ringtoneoff";
        public static final String REQUEST_CODE = "request_code";
        public static final int ALARM_ACTIVE = 1;
        public static final int ALARM_INACTIVE = 0;
        public static final int VIBRATE_ON = 1;
        public static final int VIBRATE_OFF = 0;
        public static final String INTENT_BUNDLE = "bundle";
        public static final String EDIT_ALARM = "edit";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_AUTHORITY, PATH_ALARMS);


        public static final int FIVE_MINUTES = 5;
        public static final int TEN_MINUTES = 10;
        public static final int FIFTEEN_MINUTES = 15;
        public static final int THIRTY_MINUTES = 30;

    }
}
