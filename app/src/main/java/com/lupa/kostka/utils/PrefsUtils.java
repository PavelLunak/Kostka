package com.lupa.kostka.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lupa.kostka.MainActivity;


public class PrefsUtils {

    public static void updateLanguage(Context context, MainActivity.Language language) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(AppConstants.PREFS_LANGUAGE_ENG, language == MainActivity.Language.ENG);
        editor.commit();
    }

    public static void updateDiceColor(Context context, int dicesCount, String colorCodes) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getTagByDicesCount(dicesCount), colorCodes);
        editor.commit();
    }

    public static void updateTheme(Context context, boolean isLightTheme) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(AppConstants.PREFS_THEME, isLightTheme);
        editor.commit();
    }

    public static boolean getLightTheme(Context context) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.PREFS_NAME, context.MODE_PRIVATE);
        return pref.getBoolean(AppConstants.PREFS_THEME, false);
    }

    public static boolean getEngLanguage(Context context) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.PREFS_NAME, context.MODE_PRIVATE);
        return pref.getBoolean(AppConstants.PREFS_LANGUAGE_ENG, true);
    }

    public static String getDiceColorSet(Context context, int dicesCount) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.PREFS_NAME, context.MODE_PRIVATE);
        return pref.getString(getTagByDicesCount(dicesCount), "oneDice");
    }

    public static String getTagByDicesCount(int dicesCount) {
        switch (dicesCount) {
            case 1:
                return "oneDice";
            case 2:
                return "twoDice";
            case 3:
                return "threeDice";
            case 4:
                return "fourDice";
            case 5:
                return "fiveDice";
            case 6:
                return "sixDice";
            default:
                return "oneDice";
        }
    }

    public static boolean canShowHelp1(Context context) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.PREFS_NAME, context.MODE_PRIVATE);
        return pref.getBoolean(AppConstants.PREFS_HELP1, true);
    }

    public static void hideHelp1(Context context) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(AppConstants.PREFS_HELP1, false);
        editor.commit();
    }
}
