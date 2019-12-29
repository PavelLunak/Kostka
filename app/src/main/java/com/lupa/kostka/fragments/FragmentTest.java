package com.lupa.kostka.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.lupa.kostka.DiceValuesCounter;
import com.lupa.kostka.MainActivity;
import com.lupa.kostka.R;

public class FragmentTest extends Fragment {

    MainActivity activity;

    View view;
    RelativeLayout root;
    DiceValuesCounter dvc;
    Button btnPlus, btnMinus;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity)
            activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, container, false);

        dvc = view.findViewById(R.id.dvc);

        btnPlus = view.findViewById(R.id.btnPlus);
        btnMinus = view.findViewById(R.id.btnMinus);

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setValue(activity.getValue() + 5);
                dvc.setValue(activity.getValue(), true);
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setValue(activity.getValue() - 5);
                dvc.setValue(activity.getValue(), true);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
