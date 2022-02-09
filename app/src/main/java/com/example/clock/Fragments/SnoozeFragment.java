package com.example.clock.Fragments;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.clock.R;
import com.example.clock.provider.AlarmContract;

public class SnoozeFragment extends Fragment {

    TextView fiveMin, tenMin, fifteenMin, thirtyMin;
    ConstraintLayout snoozeLayout;
    TextView transparent;
    boolean five, ten, fifteen, thirty;
    SnoozeTimeHelper snoozeTimeHelper;
    RadioButton buttonFive, buttonTen, buttonFifteen, buttonThirty;
    private final int FIVE = 5, TEN = 10, FIFTEEN = 15, THIRTY = 30;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        five = ten = fifteen = thirty = false;


        View view = inflater.inflate(R.layout.fragment_snooze, container, false);

        try {

            buttonFive = view.findViewById(R.id.button_five);
            buttonTen = view.findViewById(R.id.button_ten);
            buttonFifteen = view.findViewById(R.id.button_fifteen);
            buttonThirty = view.findViewById(R.id.button_thirty);

            transparent = view.findViewById(R.id.textView);
            snoozeLayout = view.findViewById(R.id.snooze_layout);
            fiveMin = view.findViewById(R.id.min_5);
            tenMin = view.findViewById(R.id.min_10);
            fifteenMin = view.findViewById(R.id.min_15);
            thirtyMin = view.findViewById(R.id.min_30);


            Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                    R.anim.bottom_up);
            snoozeLayout.setAnimation(slide_up);

            Bundle bundle = getArguments();
            int code = bundle.getInt(AlarmContract.AlarmEntry.EDIT_ALARM, -1);

            if (code == -1) {
                Log.v("", "code -1");
                int snoozeTime = bundle.getInt(AlarmContract.AlarmEntry.SNOOZE_TIME, -1);
                if (snoozeTime == -1)
                    buttonFive.setVisibility(View.VISIBLE);
                else
                    updateRadioButtons(snoozeTime);
            } else {
                Log.v("", "code not -1");

                int snoozeTime = bundle.getInt(AlarmContract.AlarmEntry.SNOOZE_TIME, -1);
//                if (snoozeTime == AlarmContract.AlarmEntry.FIVE_MINUTES) updateRadioButtons(FIVE);
//                else if (snoozeTime == AlarmContract.AlarmEntry.TEN_MINUTES)
//                    updateRadioButtons(TEN);
//                else if (snoozeTime == AlarmContract.AlarmEntry.FIFTEEN_MINUTES)
//                    updateRadioButtons(FIFTEEN);
//                else if (snoozeTime == AlarmContract.AlarmEntry.THIRTY_MINUTES)
//                    updateRadioButtons(THIRTY);
                updateRadioButtons(snoozeTime);


            }


            transparent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeFragment();
                }
            });

            fiveMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snoozeTimeHelper.getSnoozeInterval(AlarmContract.AlarmEntry.FIVE_MINUTES);
                    updateRadioButtons(FIVE);
                    removeFragment();
                    Log.v("", "5 min");
                }
            });

            tenMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snoozeTimeHelper.getSnoozeInterval(AlarmContract.AlarmEntry.TEN_MINUTES);
                    updateRadioButtons(TEN);
                    removeFragment();
                    Log.v("", "10 min");

                }
            });

            fifteenMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snoozeTimeHelper.getSnoozeInterval(AlarmContract.AlarmEntry.FIFTEEN_MINUTES);
                    updateRadioButtons(FIFTEEN);
                    removeFragment();
                    Log.v("", "15 min");
                }
            });

            thirtyMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snoozeTimeHelper.getSnoozeInterval(AlarmContract.AlarmEntry.THIRTY_MINUTES);
                    updateRadioButtons(THIRTY);
                    removeFragment();
                    Log.v("", "15 min");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            snoozeTimeHelper = (SnoozeTimeHelper) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    private void removeFragment() {
        Animation slide_down = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down);
        snoozeLayout.setAnimation(slide_down);
        snoozeLayout.setVisibility(View.INVISIBLE);
        getActivity().getSupportFragmentManager().popBackStack();
    }


    private void updateRadioButtons(int time) {
        buttonThirty.setVisibility(View.INVISIBLE);
        buttonTen.setVisibility(View.INVISIBLE);
        buttonFifteen.setVisibility(View.INVISIBLE);
        buttonFive.setVisibility(View.INVISIBLE);

        if (time == AlarmContract.AlarmEntry.FIVE_MINUTES)
            buttonFive.setVisibility(View.VISIBLE);
        else if (time == AlarmContract.AlarmEntry.TEN_MINUTES)
            buttonTen.setVisibility(View.VISIBLE);
        else if (time == AlarmContract.AlarmEntry.FIFTEEN_MINUTES)
            buttonFifteen.setVisibility(View.VISIBLE);
        else if (time == AlarmContract.AlarmEntry.THIRTY_MINUTES)
            buttonThirty.setVisibility(View.VISIBLE);


    }

    public interface SnoozeTimeHelper {
        void getSnoozeInterval(int time);
    }
}