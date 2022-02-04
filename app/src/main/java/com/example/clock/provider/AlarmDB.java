package com.example.clock.provider;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class AlarmDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "Alarm.db";
    public static final int DB_VERSION = 2;

    public AlarmDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v("", "creating db");
        sqLiteDatabase.execSQL(AlarmContract.AlarmEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
