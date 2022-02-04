package com.example.clock.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;

import java.util.List;
import java.util.Map;

public class RingtoneAdapter extends RecyclerView.Adapter<RingtoneAdapter.RingtoneViewHolder> {


    List<String> list;
    Map<String, String> map;

    public RingtoneAdapter(Context context, List<String> list, Map<String, String> map) {
        this.list = list;
        this.map = map;
    }

    @NonNull
    @Override
    public RingtoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ringtone_list, parent, false);
        return new RingtoneViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RingtoneViewHolder holder, int position) {


        holder.ringtoneName.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RingtoneViewHolder extends RecyclerView.ViewHolder {
        public TextView ringtoneName;

        public RingtoneViewHolder(View view) {
            super(view);

            ringtoneName = view.findViewById(R.id.ringtone_name);
        }


    }


}
