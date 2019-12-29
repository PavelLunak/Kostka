package com.lupa.kostka.utils;

import android.widget.TextView;

import com.lupa.kostka.BuildConfig;
import com.lupa.kostka.MainActivity;

public class AppUtils {

    public static void setTextByLanguage(MainActivity activity, TextView view, int textCzResourceId, int textEngResourceId) {
        view.setText(activity.getString(activity.language == MainActivity.Language.CZ ? textCzResourceId : textEngResourceId));
    }

    public static String getTextByLanguage(MainActivity activity, int textCzResourceId, int textEngResourceId) {
        return activity.language == MainActivity.Language.CZ ? activity.getResources().getString(textCzResourceId) : activity.getResources().getString(textEngResourceId);
    }

    public static String getVersion() {
        return BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")";
    }
}
