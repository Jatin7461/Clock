package com.example.clock.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.clock.R;

public class StopwatchFragment extends Fragment {

    private Button mStartButton, mResetButton;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartButton = view.findViewById(R.id.start_stopwatch);
        mResetButton = view.findViewById(R.id.reset_stopwatch);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Stopwatch Started", Toast.LENGTH_SHORT).show();
            }
        });

    }
}