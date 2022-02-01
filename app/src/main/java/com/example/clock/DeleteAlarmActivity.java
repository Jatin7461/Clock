package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

public class DeleteAlarmActivity extends AppCompatActivity {

    private LinearLayout deleteButton,transparent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_alarm);

        transparent = findViewById(R.id.transparent_screen);


    }
}