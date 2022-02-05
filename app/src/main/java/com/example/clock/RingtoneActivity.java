package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.clock.Adapters.RingtoneAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RingtoneActivity extends AppCompatActivity {

    Map<String, String> map;
    List<String> list;
    List<Boolean> selected;
    ListView ringtoneList;
    RingtoneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone);
        map = new HashMap<>();
        list = new ArrayList<>();
        ringtoneList = findViewById(R.id.ringtone_list);
        selected = new ArrayList<>();

        RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = manager.getCursor();
        Uri uri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
        Ringtone r = RingtoneManager.getRingtone(this, uri);
        list.add("None");
        selected.add(false);
        while (cursor.moveToNext()) {
            String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            Uri ringtoneuri = Uri.parse(notificationUri);
            Ringtone ringtone = RingtoneManager.getRingtone(this, ringtoneuri);
            if (ringtoneuri.toString().equals(uri.toString())) {
                selected.add(true);
            } else {
                selected.add(false);
            }
            list.add(notificationTitle);
            map.put(notificationTitle, notificationUri);
        }


        map.put("None", "");

        adapter = new RingtoneAdapter(this, list, selected, map);
        ringtoneList.setAdapter(adapter);
        RingtoneManager ringtoneManager = new RingtoneManager(RingtoneActivity.this);
        Uri rUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        ringtoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(RingtoneActivity.this, AlarmService.class);
                stopService(intent);
                TextView song = view.findViewById(R.id.ringtone_name);
                String uri = map.get(song.getText().toString());
                intent.putExtra("id", R.id.ringtone_selected);
                intent.putExtra("ringtone", uri);
                for (int j = 0; j < selected.size(); j++) {
                    if (j == i) selected.set(j, true);
                    else selected.set(j, false);
                }
                adapter.setSelected(selected);
                if (!song.getText().toString().equals("None"))
                    startService(intent);
//                r = ringtoneManager.getRingtone(RingtoneActivity.this, uri);
//                r.play();

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(RingtoneActivity.this, AlarmService.class);
        stopService(intent);
    }
}