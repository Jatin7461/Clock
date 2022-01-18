package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.clock.Fragments.AlarmFragment;
import com.example.clock.Fragments.StopwatchFragment;

public class MainActivity extends AppCompatActivity {

    private Button AlarmButton, StopwatchButton, TimerButton;
    private Fragment mAlarmFragment, mStopwatchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, AlarmFragment.class, null)
                .commit();
        mAlarmFragment = new AlarmFragment();
        mStopwatchFragment = new StopwatchFragment();

        AlarmButton = findViewById(R.id.alarm_button);
        StopwatchButton = findViewById(R.id.stopwatch_button);
        TimerButton = findViewById(R.id.timer_button);

        AlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mAlarmFragment).commit();
            }
        });
        StopwatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mStopwatchFragment).commit();

            }
        });

    }


}