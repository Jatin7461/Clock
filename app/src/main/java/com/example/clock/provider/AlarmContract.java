package com.example.clock.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class AlarmContract {

    public static final String CONTENT_AUTHORITY = "com.example.clock";

    public static final Uri BASE_CONTENT_AUTHORITY = Uri.parse("content:// " + CONTENT_AUTHORITY);

    public static final String PATH_ALARMS = "alarm";

    public static class AlarmEntry implements BaseColumns {

        public static final String TABLE_NAME = "alarm";
        public static final String HOUR = "hour";
        public static final String MIN = "min";
        public static final String SUNDAY = "sunday";
        public static final String MONDAY = "monday";
        public static final String TUESDAY = "tuesday";
        public static final String WEDNESDAY = "wednesday";
        public static final String THURSDAY = "thursday";
        public static final String FRIDAY = "friday";
        public static final String SATURDAY = "saturday";
        public static final String LABEL = "label";
        public static final String _ID = BaseColumns._ID;

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID
                + " INTEGER PRIMARY KEY AUTO INCREMENT," + HOUR + " TEXT NOT NULL, " + MIN + " TEXT NOT NULL," + SUNDAY + " INTEGER NOT NULL DEFAULT 0, "
                + MONDAY + " INTEGER NOT NULL DEFAULT 0, " + TUESDAY + "INTEGER NOT NULL DEFAULT 0, " + WEDNESDAY
                + " INTEGER NOT NULL DEFAULT 0, " + THURSDAY + "INTEGER NOT NULL DEFAULT 0, " + FRIDAY + " INTEGER NOT NULL DEFAULT 0, "
                + SATURDAY + " INTEGER NOT NULL DEFAULT 0, " + LABEL + " TEXT )";

        public static final int SELECTED = 1;
        public static final int NOT_SELECTED = 0;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_AUTHORITY, PATH_ALARMS);
    }
}
