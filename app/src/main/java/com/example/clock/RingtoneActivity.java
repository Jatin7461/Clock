package com.example.clock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.clock.Adapters.RingtoneAdapter;
import com.example.clock.provider.AlarmContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RingtoneActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>> {

    Map<String, String> map;
    List<String> list;
    List<Boolean> selected;
    ListView ringtoneList;
    RingtoneAdapter adapter;
    Intent intent;
    String alarmRingtone;
    int intentCode;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone);
        try {

            progressBar = findViewById(R.id.loading_ringtones);
            map = new HashMap<>();
            list = new ArrayList<>();
            ringtoneList = findViewById(R.id.ringtone_list);
            selected = new ArrayList<>();

            intent = getIntent();
            alarmRingtone = intent.getStringExtra(AlarmContract.AlarmEntry.RINGTONE);


//            RingtoneManager manager = new RingtoneManager(this);
//            manager.setType(RingtoneManager.TYPE_ALARM);
//            Cursor cursor = manager.getCursor();
//            Uri uri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
//            Ringtone r = RingtoneManager.getRingtone(this, uri);
//            list.add("None");
//            selected.add(false);
//            while (cursor.moveToNext()) {
//                String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
//                String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
//                Uri ringtoneuri = Uri.parse(notificationUri);
//                Ringtone ringtone = RingtoneManager.getRingtone(this, ringtoneuri);
//                if (intentCode == -1) {
//
//                    if (ringtoneuri.toString().equals(uri.toString())) {
//                        selected.add(true);
//                    } else {
//                        selected.add(false);
//                    }
//                } else {
//                    if (notificationTitle.equals(alarmRingtone)) {
//                        selected.add(true);
//                    } else
//                        selected.add(false);
//                }
//                list.add(notificationTitle);
//                map.put(notificationTitle, notificationUri);
//            }
//            cursor.close();

//            if (intentCode != -1)
//                r = RingtoneManager.getRingtone(this, Uri.parse(map.get(alarmRingtone)));

            map.put("None", "");

            adapter = new RingtoneAdapter(this, list, selected, map);
            ringtoneList.setAdapter(adapter);

            LoaderManager.getInstance(this).initLoader(100, null, RingtoneActivity.this).forceLoad();

//        RingtoneManager ringtoneManager = new RingtoneManager(RingtoneActivity.this);
//        Uri rUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            ringtoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {

                        Intent intent = new Intent(RingtoneActivity.this, AlarmService.class);
                        stopService(intent);
                        TextView song = view.findViewById(R.id.ringtone_name);
                        String uri = map.get(song.getText().toString());
                        intent.putExtra("id", R.id.ringtone_selected);
//                        if (song.getText().toString().equals("None"))
//                            intent.putExtra("ringtone", "None");
//                        else
                        intent.putExtra("ringtone", uri);
                        Intent in = new Intent();
                        in.putExtra(AlarmContract.AlarmEntry.RINGTONE_URI, (map.get(song.getText().toString())));
                        in.putExtra(AlarmContract.AlarmEntry.RINGTONE, song.getText().toString());
                        setResult(RESULT_OK, in);
                        for (int j = 0; j < selected.size(); j++) {
                            if (j == i) selected.set(j, true);
                            else selected.set(j, false);
                        }
                        adapter.setSelected(selected);
                        if (!song.getText().toString().equals("None"))
                            startService(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//        Intent i = new Intent();
//        i.putExtra("bruh", "lol");
//        setResult(RESULT_OK, i);
//        finish();
//    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(RingtoneActivity.this, AlarmService.class);
        stopService(intent);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public Loader<List<String>> onCreateLoader(int id, @Nullable Bundle args) {

        AsyncTaskLoader<List<String>> asyncTaskLoader = new AsyncTaskLoader<List<String>>(this) {
            @Override
            protected void onStartLoading() {
                progressBar.setVisibility(View.VISIBLE);
                super.onStartLoading();
            }

            @Override
            protected void onStopLoading() {

//                progressBar.setVisibility(View.INVISIBLE);
                super.onStopLoading();
            }

            @Nullable
            @Override
            public List<String> loadInBackground() {
                RingtoneManager manager = new RingtoneManager(RingtoneActivity.this);
                manager.setType(RingtoneManager.TYPE_ALARM);
                Cursor cursor = manager.getCursor();
                Uri uri = RingtoneManager.getActualDefaultRingtoneUri(RingtoneActivity.this, RingtoneManager.TYPE_ALARM);
                Ringtone r = RingtoneManager.getRingtone(RingtoneActivity.this, uri);
                list.add("None");
                selected.add(false);
                while (cursor.moveToNext()) {
                    String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                    String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
                    Uri ringtoneuri = Uri.parse(notificationUri);
                    Ringtone ringtone = RingtoneManager.getRingtone(RingtoneActivity.this, ringtoneuri);
                    if (intentCode == -1) {

                        if (ringtoneuri.toString().equals(uri.toString())) {
                            selected.add(true);
                        } else {
                            selected.add(false);
                        }
                    } else {
                        if (notificationTitle.equals(alarmRingtone)) {
                            selected.add(true);
                        } else
                            selected.add(false);
                    }
                    list.add(notificationTitle);
                    map.put(notificationTitle, notificationUri);
                }
//                progressBar.setVisibility(View.INVISIBLE);
                return list;
            }
        };

        return asyncTaskLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<String>> loader, List<String> data) {
        progressBar.setVisibility(View.INVISIBLE);
        adapter.updateList(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<String>> loader) {

    }


}