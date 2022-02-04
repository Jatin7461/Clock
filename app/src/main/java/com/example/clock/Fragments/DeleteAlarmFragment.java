package com.example.clock.Fragments;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.clock.R;
import com.example.clock.provider.AlarmContract;


public class DeleteAlarmFragment extends Fragment {

    LinearLayout transparent;
    LinearLayout deleteOption;
    Button deleteAlarm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_delete_alarm, container, false);
        deleteAlarm = view.findViewById(R.id.delete_alarm);
        deleteOption = view.findViewById(R.id.delete_option);
        transparent = view.findViewById(R.id.transparent);
        Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                R.anim.bottom_up);
        deleteOption.setAnimation(slide_up);


        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, getArguments().getLong(AlarmContract.AlarmEntry._ID));
                Toast.makeText(getContext(), "Alarm deleted", Toast.LENGTH_SHORT).show();
                getContext().getContentResolver().delete(uri, null, null);
                removeFragment();
            }
        });

        transparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();

                removeFragment();

            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade));

    }

    void removeFragment() {
        Animation slide_down = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down);
        deleteOption.setAnimation(slide_down);
        deleteOption.setVisibility(View.INVISIBLE);

        getActivity().getSupportFragmentManager().popBackStack();
    }
}