package com.example.clock.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.clock.R;
import com.example.clock.provider.AlarmContract;

public class AlarmCursorAdapter extends android.widget.CursorAdapter {


    public AlarmCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.alarms, viewGroup, false);
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView time = view.findViewById(R.id.alarm_time);
        int hourId = cursor.getColumnIndex(AlarmContract.AlarmEntry.HOUR);
        String hour = cursor.getString(hourId);
        int minId = cursor.getColumnIndex(AlarmContract.AlarmEntry.MIN);
        String min = cursor.getString(minId);
        time.setText(hour + ":" + min);


    }
}
