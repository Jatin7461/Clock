package com.example.clock.Adapters;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.lifecycle.LiveData;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.clock.MyWork;
import com.example.clock.R;
import com.example.clock.provider.AlarmContract;
import com.example.clock.utils.AlarmUtils;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    //    public List<Alarm> alarmList;
    final String ACTIVE = "1";

    public Cursor cursor;
    //    private OnAlarmClickListener onAlarmClickListener;
    private WorkManager workManager;
    Context context;

    public AlarmAdapter(Context context) {
        this.context = context;
        workManager = WorkManager.getInstance(context);
//        this.onAlarmClickListener = onAlarmClickListener;
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


        holder.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isShown()) {
                    Toast.makeText(context, "clicked for id " + id, Toast.LENGTH_SHORT).show();


                    Uri uri = ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, id);
                    if (b) {
//                    Toast.makeText(context, "yes", Toast.LENGTH_SHORT).show();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(AlarmContract.AlarmEntry.ACTIVE, AlarmContract.AlarmEntry.ALARM_ACTIVE);
                        context.getContentResolver().update(uri, contentValues, null, null);

                        Data data = new Data.Builder()
                                .putInt(AlarmContract.AlarmEntry.HOUR, Integer.parseInt(hour))
                                .putInt(AlarmContract.AlarmEntry.MIN, Integer.parseInt(min))
                                .putInt("position",holder.getAdapterPosition())
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


            }
        });


    }

    @Override
    public int getItemCount() {
        if (cursor == null)
            return 0;
        return cursor.getCount();
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        public TextView alarmTime;
        public Switch alarmSwitch;
        public int id;

        public AlarmViewHolder(View view) {
            super(view);
            alarmTime = view.findViewById(R.id.alarm_time);
            alarmSwitch = view.findViewById(R.id.alarm_switch);
//            view.setOnClickListener(this);
//            view.setOnLongClickListener(this);
//            alarmSwitch.setChecked(true);
//            alarmSwitch.setOnClickListener(this);
//            alarmSwitch.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });


//            alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if (compoundButton.isShown()) {
//
////                    Log.v("", "switch on for " + id);
//                        Toast.makeText(context, "switch clicked for " + id, Toast.LENGTH_SHORT).show();
//
//                    }
//
//                }
//            });


        }

//        @Override
//        public void onClick(View view) {
//            Integer id = (Integer) view.getTag();
//            onAlarmClickListener.onAlarmClick(id);
//            alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    onAlarmClickListener.switchClick(id, b);
//
//
//                }
//            });
//            final GestureDetector gestureDetector = new GestureDetector(view.getContext(), new GestureDetector.SimpleOnGestureListener() {
//                public void onLongPress(MotionEvent e) {
//                    Log.e("", "Longpress detected");
//                }
//            });
    }

//        @Override
//        public boolean onLongClick(View view) {
////            Integer id = (Integer) view.getTag();
//            onAlarmClickListener.onLongPress(view, id);
//            return true;
//        }
//    }


    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

//    public interface OnAlarmClickListener {
//        void onAlarmClick(int id);
//
//        void onLongPress(View view, int id);
//
//        void switchClick(int id, boolean b);
//    }

//    public void setList(List<Alarm> list) {
//        this.alarmList = list;
////        notifyItemChanged(alarmList.size() - 1);
//        notifyDataSetChanged();
//    }


}
