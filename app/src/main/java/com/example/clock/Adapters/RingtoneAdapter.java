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
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;

import java.util.List;
import java.util.Map;

public class RingtoneAdapter extends BaseAdapter {

    Context context;
    List<String> ringtones;
    Map<String, String> map;
    List<Boolean> selected;

    public RingtoneAdapter(Context context, List<String> ringtones, List<Boolean> s, Map<String, String> map) {
        this.ringtones = ringtones;
        this.map = map;
        this.selected = s;
        this.context = context;
    }

    @Override
    public int getCount() {
        return ringtones.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View listview = view;
        if (listview == null) {
            listview = LayoutInflater.from(context).inflate(R.layout.ringtone_list, viewGroup, false);
        }

        TextView ringtoneName = listview.findViewById(R.id.ringtone_name);
        ringtoneName.setText(ringtones.get(i));
        RadioButton radioButton = listview.findViewById(R.id.ringtone_selected);
        if (selected.get(i)) {
            radioButton.setVisibility(View.VISIBLE);
        } else {
            radioButton.setVisibility(View.INVISIBLE);
        }
        return listview;
    }

    public void setSelected(List<Boolean> b) {
        this.selected = b;
        notifyDataSetChanged();
    }
}
