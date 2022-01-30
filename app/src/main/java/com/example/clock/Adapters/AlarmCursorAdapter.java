package com.example.clock.Adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cursoradapter.widget.CursorAdapter;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.clock.MyWork;
import com.example.clock.R;
import com.example.clock.provider.AlarmContract;
import com.example.clock.utils.AlarmUtils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AlarmCursorAdapter extends CursorAdapter {

    WorkManager workManager;

    public AlarmCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        workManager = WorkManager.getInstance(context);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.alarms, viewGroup, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView time = view.findViewById(R.id.alarm_time);
        int hourId = cursor.getColumnIndex(AlarmContract.AlarmEntry.HOUR);
        String hour = cursor.getString(hourId);
        int minId = cursor.getColumnIndex(AlarmContract.AlarmEntry.MIN);
        String min = cursor.getString(minId);
        time.setText(hour + ":" + min);

        Switch s = view.findViewById(R.id.alarm_switch);
        int boolId = cursor.getColumnIndex(AlarmContract.AlarmEntry.ACTIVE);
        int str = cursor.getInt(boolId);
        boolean b;
        if (str == AlarmContract.AlarmEntry.ALARM_ACTIVE) {
            b = true;
        } else {
            b = false;
        }
        int colId = cursor.getColumnIndex(AlarmContract.AlarmEntry._ID);

        int id = cursor.getInt(colId);
        s.setChecked(b);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                Uri uri = ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, id);
                if (b) {
//                    Toast.makeText(context, "yes", Toast.LENGTH_SHORT).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmContract.AlarmEntry.ALARM_ACTIVE);
                    context.getContentResolver().update(uri, contentValues, null, null);

                    Data data = new Data.Builder()
                            .putInt(AlarmContract.AlarmEntry.HOUR, Integer.parseInt(hour))
                            .putInt(AlarmContract.AlarmEntry.MIN, Integer.parseInt(min))
                            .putInt(AlarmContract.AlarmEntry.PENDING, id).build();


                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(min));
                    calendar.set(Calendar.SECOND, 0);

                    long currentTime = System.currentTimeMillis();
                    long calendarTime = calendar.getTimeInMillis();
                    OneTimeWorkRequest oneTimeWorkRequest;
                    if (calendarTime < currentTime) {
                        oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
                                .setInputData(data).setInitialDelay(currentTime - calendarTime + AlarmUtils.DAY, TimeUnit.MILLISECONDS).build();

                    } else {
                        oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
                                .setInputData(data).setInitialDelay(calendarTime - currentTime, TimeUnit.MILLISECONDS).build();

                    }
//                    OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
//                            .setInitialDelay(calendar.getTimeInMillis(), TimeUnit.MILLISECONDS).setInputData(data).build();
                    workManager.enqueueUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id, ExistingWorkPolicy.KEEP, oneTimeWorkRequest);
//                    workManager.enqueueUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id, ExistingWorkPolicy.KEEP, oneTimeWorkRequest);

                } else {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmContract.AlarmEntry.ALARM_INACTIVE);
                    context.getContentResolver().update(uri, contentValues, null, null);
                    workManager.cancelUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id);

//                    workManager.cancelUniqueWork(AlarmContract.AlarmEntry.TABLE_NAME + id);

                }
            }
        });

    }
}
