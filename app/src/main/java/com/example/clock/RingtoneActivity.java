package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.clock.Adapters.RingtoneAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RingtoneActivity extends AppCompatActivity {

    Map<String, String> map;
    List<String> list;

    RecyclerView ringtoneList;
    RingtoneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone);
        map = new HashMap<>();
        list = new ArrayList<>();
        ringtoneList = findViewById(R.id.ringtone_list);
        RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = manager.getCursor();

        while (cursor.moveToNext()) {
            String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
//            Uri uri = Uri.parse(notificationUri);
            list.add(notificationTitle);
            map.put(notificationTitle, notificationUri);
        }

        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        ringtoneList.setLayoutManager(linearLayout);
        adapter = new RingtoneAdapter(this, list,map);
        ringtoneList.setAdapter(adapter);

    }
}