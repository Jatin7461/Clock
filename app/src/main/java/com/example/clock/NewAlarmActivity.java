package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NewAlarmActivity extends AppCompatActivity {

    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
    }
}