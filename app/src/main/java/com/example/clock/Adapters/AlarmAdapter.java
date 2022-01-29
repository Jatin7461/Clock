package com.example.clock.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;
import com.example.clock.provider.AlarmContract;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    //    public List<Alarm> alarmList;
    final String ACTIVE = "1";

    public Cursor cursor;
    private OnAlarmClickListener onAlarmClickListener;

    public AlarmAdapter(OnAlarmClickListener onAlarmClickListener) {

        this.onAlarmClickListener = onAlarmClickListener;
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

//
//        Alarm obj = alarmList.get(position);
//        String hour = obj.hour;
//        String min = obj.min;

        cursor.moveToPosition(position);
        int hourid = cursor.getColumnIndex(AlarmContract.AlarmEntry.HOUR);
        String hour = cursor.getString(hourid);
        int minid = cursor.getColumnIndex(AlarmContract.AlarmEntry.MIN);
        String min = cursor.getString(minid);
        holder.alarmTime.setText(hour + ":" + min);

        int columnId = cursor.getColumnIndex(AlarmContract.AlarmEntry._ID);
        int id = cursor.getInt(columnId);

        int activeId = cursor.getColumnIndex(AlarmContract.AlarmEntry.ACTIVE);
        int active = cursor.getInt(activeId);
        Log.v("", " active " + active);
        boolean b;
        if (active == AlarmContract.AlarmEntry.ALARM_ACTIVE) {
            Log.v("", "true");
            b = true;
        } else {
            b = false;
        }
        holder.alarmSwitch.setChecked(b);

        holder.id = id;
//
//        try {
//            holder.itemView.setTag(Integer.toString(id));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    @Override
    public int getItemCount() {
        if (cursor == null)
            return 0;
        return cursor.getCount();
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView alarmTime;
        public Switch alarmSwitch;
        public int id;

        public AlarmViewHolder(View view) {
            super(view);
            alarmTime = view.findViewById(R.id.alarm_time);
            alarmSwitch = view.findViewById(R.id.alarm_switch);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
//            alarmSwitch.setChecked(true);
//            alarmSwitch.setOnClickListener(this);


            alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    onAlarmClickListener.switchClick(id, b);
//                    Log.v("", "switch on for " + id);

                }
            });

        }

        @Override
        public void onClick(View view) {
//            Integer id = (Integer) view.getTag();
            onAlarmClickListener.onAlarmClick(id);
            alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    onAlarmClickListener.switchClick(id, b);


                }
            });
//            final GestureDetector gestureDetector = new GestureDetector(view.getContext(), new GestureDetector.SimpleOnGestureListener() {
//                public void onLongPress(MotionEvent e) {
//                    Log.e("", "Longpress detected");
//                }
//            });
        }

        @Override
        public boolean onLongClick(View view) {
//            Integer id = (Integer) view.getTag();
            onAlarmClickListener.onLongPress(view, id);
            return true;
        }
    }


    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public interface OnAlarmClickListener {
        void onAlarmClick(int id);

        void onLongPress(View view, int id);

        void switchClick(int id, boolean b);
    }

//    public void setList(List<Alarm> list) {
//        this.alarmList = list;
////        notifyItemChanged(alarmList.size() - 1);
//        notifyDataSetChanged();
//    }


}
