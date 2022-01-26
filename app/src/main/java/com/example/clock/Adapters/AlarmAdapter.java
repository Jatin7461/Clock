package com.example.clock.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;
import com.example.clock.provider.AlarmContract;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    public ArrayList<String> alarmList;

    public Cursor cursor;

    public AlarmAdapter(ArrayList<String> list) {
        this.alarmList = list;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alarms, parent, false);


        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {

        cursor.moveToPosition(position);

        int hourId = cursor.getColumnIndex(AlarmContract.AlarmEntry.HOUR);

        String hour = cursor.getString(hourId);
        int minId = cursor.getColumnIndex(AlarmContract.AlarmEntry.MIN);
        String min = cursor.getString(minId);
//        String s = alarmList.get(position);
        holder.alarmTime.setText(hour + ":" + min);


    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        public TextView alarmTime;
        public Switch alarmSwitch;

        public AlarmViewHolder(View view) {
            super(view);
            alarmTime = view.findViewById(R.id.alarm_time);
            alarmSwitch = view.findViewById(R.id.alarm_switch);
        }


    }

    public void addNewAlarm(ArrayList<String> list) {
        this.alarmList = list;
//        notifyItemChanged(alarmList.size() - 1);
        notifyDataSetChanged();
    }


}
