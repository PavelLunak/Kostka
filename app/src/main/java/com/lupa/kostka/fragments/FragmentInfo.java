package com.lupa.kostka.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lupa.kostka.Animators;
import com.lupa.kostka.MainActivity;
import com.lupa.kostka.R;
import com.lupa.kostka.utils.AppUtils;

public class FragmentInfo extends Fragment implements View.OnClickListener {

    MainActivity activity;

    View view;
    LinearLayout root;

    TextView labelTitle,
            textView4,
            textView6,
            textView7,
            textView9,
            titleSourceCode,
            labelSourceCode,
            textView5,
            labelVersion;

    View divider1;
    ImageView imageView6;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity)
            activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);

        root = view.findViewById(R.id.root);
        labelTitle = view.findViewById(R.id.labelTitle);
        divider1 = view.findViewById(R.id.divider1);
        textView4 = view.findViewById(R.id.textView4);
        textView6 = view.findViewById(R.id.textView6);
        textView7 = view.findViewById(R.id.textView7);
        textView9 = view.findViewById(R.id.textView9);
        titleSourceCode = view.findViewById(R.id.titleSourceCode);
        labelSourceCode = view.findViewById(R.id.labelSourceCode);
        textView5 = view.findViewById(R.id.textView5);
        labelVersion = view.findViewById(R.id.labelVersion);
        imageView6 = view.findViewById(R.id.imageView6);

        root.setOnClickListener(this);
        labelTitle.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView5.setOnClickListener(this);
        textView6.setOnClickListener(this);
        textView7.setOnClickListener(this);
        textView9.setOnClickListener(this);
        titleSourceCode.setOnClickListener(this);
        labelSourceCode.setOnClickListener(this);
        labelVersion.setOnClickListener(this);
        imageView6.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setColor(activity.theme);
        updateLanguage();
        updateImage();
        labelVersion.setText(AppUtils.getVersion());
    }

    public void setColor(MainActivity.Theme theme) {
        if (theme == MainActivity.Theme.LIGHT) {
            root.setBackgroundColor(activity.getResources().getColor(R.color.colorBackgroundLight));
            labelTitle.setTextColor(activity.getResources().getColor(R.color.colorBackgroundDark));
            divider1.setBackgroundColor(activity.getResources().getColor(R.color.colorBackgroundDark));
            textView4.setTextColor(activity.getResources().getColor(R.color.colorBackgroundDark));
            textView6.setTextColor(activity.getResources().getColor(R.color.colorBackgroundDark));
            textView7.setTextColor(activity.getResources().getColor(R.color.colorBackgroundDark));
            textView9.setTextColor(activity.getResources().getColor(R.color.colorBackgroundDark));
            titleSourceCode.setTextColor(activity.getResources().getColor(R.color.colorBackgroundDark));
            labelSourceCode.setTextColor(activity.getResources().getColor(R.color.colorBackgroundDark));
            textView5.setTextColor(activity.getResources().getColor(R.color.colorBackgroundDark));
            labelVersion.setTextColor(activity.getResources().getColor(R.color.colorBackgroundDark));
        } else {
            root.setBackgroundColor(activity.getResources().getColor(R.color.colorBackgroundDark));
            labelTitle.setTextColor(activity.getResources().getColor(R.color.colorBackgroundLight));
            divider1.setBackgroundColor(activity.getResources().getColor(R.color.colorBackgroundLight));
            textView4.setTextColor(activity.getResources().getColor(R.color.colorBackgroundLight));
            textView6.setTextColor(activity.getResources().getColor(R.color.colorBackgroundLight));
            textView7.setTextColor(activity.getResources().getColor(R.color.colorBackgroundLight));
            textView9.setTextColor(activity.getResources().getColor(R.color.colorBackgroundLight));
            titleSourceCode.setTextColor(activity.getResources().getColor(R.color.colorBackgroundLight));
            labelSourceCode.setTextColor(activity.getResources().getColor(R.color.colorBackgroundLight));
            textView5.setTextColor(activity.getResources().getColor(R.color.colorBackgroundLight));
            labelVersion.setTextColor(activity.getResources().getColor(R.color.colorBackgroundLight));
        }
    }

    public void updateLanguage() {
        AppUtils.setTextByLanguage(activity, labelTitle, R.string.app_name_cz, R.string.app_name);
        AppUtils.setTextByLanguage(activity, textView4, R.string.text_info_1_cz, R.string.text_info_1);
        AppUtils.setTextByLanguage(activity, textView6, R.string.text_info_2_cz, R.string.text_info_2);
        AppUtils.setTextByLanguage(activity, textView7, R.string.text_info_3_cz, R.string.text_info_3);
        AppUtils.setTextByLanguage(activity, titleSourceCode, R.string.source_code_cz, R.string.source_code);
        AppUtils.setTextByLanguage(activity, labelSourceCode, R.string.here_cz, R.string.here);
        AppUtils.setTextByLanguage(activity, textView5, R.string.version_cz, R.string.version);
    }

    public void updateImage() {
        if (activity.language == MainActivity.Language.CZ) imageView6.setImageDrawable(activity.getResources().getDrawable(R.drawable.itnetwork_winter_2019));
        else imageView6.setImageDrawable(activity.getResources().getDrawable(R.drawable.itnetwork_winter_2019_1, activity.getTheme()));
    }

    @Override
    public void onClick(View view) {

        if (activity.settingsShowed) activity.showSettings(false, true);

        switch (view.getId()) {
            case R.id.root:
                if (activity.settingsShowed) activity.showSettings(false, true);
                break;
            case R.id.textView9:
                Animators.animateButtonClick2(textView9, 1f);
                String url = "https://www.itnetwork.cz/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.imageView6:
                String url2 = "https://www.itnetwork.cz/programovani/programatorske-souteze/itnetwork-winter-2019-ceny-v-hodnote-10-000-kc";
                Intent i2 = new Intent(Intent.ACTION_VIEW);
                i2.setData(Uri.parse(url2));
                startActivity(i2);
                break;
            case R.id.labelSourceCode:
                Animators.animateButtonClick2(labelSourceCode, 1f);
                String url3 = "https://github.com/PavelLunak/Kostka";
                Intent i3 = new Intent(Intent.ACTION_VIEW);
                i3.setData(Uri.parse(url3));
                startActivity(i3);
                break;
        }
    }
}
