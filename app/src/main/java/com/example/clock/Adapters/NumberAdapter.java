package com.example.clock.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;

import java.util.ArrayList;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.NumberAdapterViewHolder> {

    private ArrayList<String> list = new ArrayList<>();

    public NumberAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NumberAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.number, parent, false);
        return new NumberAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberAdapterViewHolder holder, int position) {
        int realpos = position % list.size();
        String num = list.get(realpos);
        holder.numberView.setText(num);


    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public class NumberAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView numberView;

        public NumberAdapterViewHolder(View view) {
            super(view);
            numberView = view.findViewById(R.id.number_list);
        }


    }


}
