package com.example.clock.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.clock.Adapters.AlarmAdapter;
import com.example.clock.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class AlarmFragment extends Fragment {

    private FloatingActionButton addAlarm;
    private RecyclerView recyclerView;
    private AlarmAdapter adapter;
    private ArrayList<String> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        addAlarm = view.findViewById(R.id.add_alarm);

        //initialize recycler view and adapter
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.alarm_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AlarmAdapter(list);
        recyclerView.setAdapter(adapter);


        //onclick listener for FAB
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Add Alarm", Toast.LENGTH_SHORT).show();
                list.add("9:00");
                adapter.addNewAlarm(list);
            }
        });


        return view;
    }


}