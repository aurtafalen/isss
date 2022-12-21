package com.example.isss.SI.ui.loogsheet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.isss.R;
import com.example.isss.SI.ListInduction;
import com.example.isss.SI.ListToolbox;
import com.example.isss.SI.ListTraining;


public class LoogSheetFragment extends Fragment {

    private LoogSheetViewModel loogSheetViewModel;
    CardView induction,toolbox,training;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loogSheetViewModel = new ViewModelProvider(this).get(LoogSheetViewModel.class);
        View root = inflater.inflate(R.layout.fragment_loogsheet, container, false);

        induction = root.findViewById(R.id.induction);
        toolbox = root.findViewById(R.id.toolbox);
        training = root.findViewById(R.id.training);

        induction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListInduction.class);
                startActivity(intent);
            }
        });

        toolbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListToolbox.class);
                startActivity(intent);
            }
        });

        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListTraining.class);
                startActivity(intent);
            }
        });

        return root;
    }

}