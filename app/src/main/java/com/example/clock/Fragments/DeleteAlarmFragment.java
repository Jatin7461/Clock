package com.example.clock.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.clock.R;


public class DeleteAlarmFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        onCreateAnimation(1, true, 1);

        return inflater.inflate(R.layout.fragment_delete_alarm, container, false);
    }


}