package com.example.clock.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;
import com.example.clock.data.StopwatchCount;

import java.util.ArrayList;

public class StopwatchAdapter extends RecyclerView.Adapter<StopwatchAdapter.StopwatchViewHolder> {

    private ArrayList<StopwatchCount> list;


    public StopwatchAdapter(ArrayList<StopwatchCount> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public StopwatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflate = LayoutInflater.from(context);
        View view = inflate.inflate(R.layout.stopwatch_countlist, parent, false);
        return new StopwatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StopwatchViewHolder holder, int position) {

        StopwatchCount obj = list.get(position);
        holder.lapCount.setText(R.string.count + obj.getCount());
        obj.incrementCount();
        holder.presentTime.setText(obj.getPresentTime());
        holder.timeDiff.setText(obj.getPreviousTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class StopwatchViewHolder extends RecyclerView.ViewHolder {
        public TextView lapCount, presentTime, timeDiff;

        StopwatchViewHolder(View view) {
            super(view);
            lapCount = view.findViewById(R.id.count_number);
            presentTime = view.findViewById(R.id.present_time);
            timeDiff = view.findViewById(R.id.time_difference);

        }

    }

}
