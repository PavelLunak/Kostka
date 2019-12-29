package com.lupa.kostka.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lupa.kostka.Animators;
import com.lupa.kostka.MainActivity;
import com.lupa.kostka.R;
import com.lupa.kostka.utils.AppUtils;

public class FragmentMain extends Fragment implements View.OnClickListener {

    MainActivity activity;

    View view;
    RelativeLayout root;
    TextView btn01, btn02, btn03, btn04, btn05, btn06, titleCount;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity)
            activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        root = view.findViewById(R.id.root);

        btn01 = view.findViewById(R.id.btn01);
        btn02 = view.findViewById(R.id.btn02);
        btn03 = view.findViewById(R.id.btn03);
        btn04 = view.findViewById(R.id.btn04);
        btn05 = view.findViewById(R.id.btn05);
        btn06 = view.findViewById(R.id.btn06);

        titleCount = view.findViewById(R.id.titleCount);

        root.setOnClickListener(this);
        btn01.setOnClickListener(this);
        btn02.setOnClickListener(this);
        btn03.setOnClickListener(this);
        btn04.setOnClickListener(this);
        btn05.setOnClickListener(this);
        btn06.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            Animators.showViewScaleSmoothly(titleCount, true, false, 100);
            Animators.showViewScaleSmoothly(btn01, true, true, 100);
            Animators.showViewScaleSmoothly(btn02, true, true, 150);
            Animators.showViewScaleSmoothly(btn03, true, true, 200);
            Animators.showViewScaleSmoothly(btn04, true, true, 350);
            Animators.showViewScaleSmoothly(btn05, true, true, 300);
            Animators.showViewScaleSmoothly(btn06, true, true, 250);
        } else {
            titleCount.setScaleX(1f);
            titleCount.setScaleY(1f);

            btn01.setScaleX(1f);
            btn01.setScaleY(1f);

            btn02.setScaleX(1f);
            btn02.setScaleY(1f);

            btn03.setScaleX(1f);
            btn03.setScaleY(1f);

            btn04.setScaleX(1f);
            btn04.setScaleY(1f);

            btn05.setScaleX(1f);
            btn05.setScaleY(1f);

            btn06.setScaleX(1f);
            btn06.setScaleY(1f);
        }

        setColor(activity.theme);
        updateLanguage();
    }

    @Override
    public void onClick(View view) {
        if (activity.settingsShowed || activity.settingsColorShowed) {
            if (activity.settingsColorShowed) activity.showSettingsColor(false, true);
            if (activity.settingsShowed) activity.showSettings(false, true);
            return;
        }

        switch (view.getId()) {
            case R.id.btn01:
                Animators.animateButtonClick2(btn01, 1f);
                activity.dicesCount = 1;
                activity.showFragmentDices();
                break;
            case R.id.btn02:
                Animators.animateButtonClick2(btn02, 1f);
                activity.dicesCount = 2;
                activity.showFragmentDices();
                break;
            case R.id.btn03:
                Animators.animateButtonClick2(btn03, 1f);
                activity.dicesCount = 3;
                activity.showFragmentDices();
                break;
            case R.id.btn04:
                Animators.animateButtonClick2(btn04, 1f);
                activity.dicesCount = 4;
                activity.showFragmentDices();
                break;
            case R.id.btn05:
                Animators.animateButtonClick2(btn05, 1f);
                activity.dicesCount = 5;
                activity.showFragmentDices();
                break;
            case R.id.btn06:
                Animators.animateButtonClick2(btn06, 1f);
                activity.dicesCount = 6;
                activity.showFragmentDices();
                break;
        }
    }

    public void setColor(MainActivity.Theme theme) {
        if (theme == MainActivity.Theme.LIGHT) {
            root.setBackgroundColor(activity.getResources().getColor(R.color.colorBackgroundLight));
            titleCount.setTextColor(activity.getResources().getColor(R.color.colorBackgroundDark));
        } else {
            root.setBackgroundColor(activity.getResources().getColor(R.color.colorBackgroundDark));
            titleCount.setTextColor(activity.getResources().getColor(R.color.colorBackgroundLight));
        }
    }

    public void updateLanguage() {
        AppUtils.setTextByLanguage(activity, titleCount, R.string.dices_count_cz, R.string.dices_count);
    }
}
