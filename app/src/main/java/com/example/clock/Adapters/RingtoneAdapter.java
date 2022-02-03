package com.example.clock.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;

public class RingtoneAdapter extends RecyclerView.Adapter<RingtoneAdapter.RingtoneViewHolder> {


    Cursor cursor;

    public RingtoneAdapter(Context context) {

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
        cursor.moveToPosition(position);


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RingtoneViewHolder extends RecyclerView.ViewHolder {
        public TextView ringtoneName;

        public RingtoneViewHolder(View view) {
            super(view);

            ringtoneName = view.findViewById(R.id.ringtone_name);
        }

    }

}
