package com.lupa.kostka.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lupa.kostka.MainActivity;
import com.lupa.kostka.R;

public class FragmentIntro extends Fragment {

    MainActivity activity;

    View view;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity)
            activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_intro, container, false);
        return view;
    }
}
