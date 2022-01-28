package com.example.clock.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AlarmProvider extends ContentProvider {


    private static final int ALARM = 100;
    private static final int ALARM_ID = 101;


    private AlarmDB mDbHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AlarmContract.CONTENT_AUTHORITY, AlarmContract.PATH_ALARMS, ALARM);
        uriMatcher.addURI(AlarmContract.CONTENT_AUTHORITY, AlarmContract.PATH_ALARMS + "/#", ALARM_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new AlarmDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        Cursor cursor;
        int match = uriMatcher.match(uri);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        switch (match) {
            case ALARM:
                cursor = db.query(AlarmContract.AlarmEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
            default:
                throw new IllegalArgumentException("wrong uri");

        }
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(AlarmContract.AlarmEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Toast.makeText(getContext(), "alarm was not added", Toast.LENGTH_SHORT).show();
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsdeleted;
        switch (match) {
            case ALARM_ID:
                String selection = AlarmContract.AlarmEntry._ID + "=?";
                String selectionargs[] = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsdeleted = db.delete(AlarmContract.AlarmEntry.TABLE_NAME, selection, selectionargs);

                break;
            default:
                throw new IllegalArgumentException("wrong uri");
        }
        if (rowsdeleted != 0) {
            getContext().getContentResolver().notifyChange(AlarmContract.AlarmEntry.CONTENT_URI, null);
        }
        return rowsdeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsupdated = 0;
        switch (match) {
            case ALARM_ID:
                String selection = AlarmContract.AlarmEntry._ID + "=?";
                String selectionargs[] = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsupdated = db.update(AlarmContract.AlarmEntry.TABLE_NAME, contentValues, selection, selectionargs);

                break;
            default:
                throw new IllegalArgumentException("failed to update");
        }

        if (rowsupdated != 0) {
            Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
            getContext().getContentResolver().notifyChange(AlarmContract.AlarmEntry.CONTENT_URI, null);
        }
        if (rowsupdated == 0) {

            Toast.makeText(getContext(), "not updated", Toast.LENGTH_SHORT).show();
        }
        return rowsupdated;
    }
}
