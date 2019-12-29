package com.lupa.kostka.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lupa.kostka.MainActivity;


public class PrefsUtils {

    public static void updateLanguage(Context context, MainActivity.Language language) {
        SharedPreferences pref = context.getSharedPreferences("DicePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isLanguageEng", language == MainActivity.Language.ENG);
        editor.commit();
    }

    public static void updateDiceColor(Context context, int dicesCount, String colorCodes) {
        SharedPreferences pref = context.getSharedPreferences("DicePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String tag = "oneDice";

        switch (dicesCount) {
            case 2:
                tag = "twoDice";
                break;
            case 3:
                tag = "threeDice";
                break;
            case 4:
                tag = "fourDice";
                break;
            case 5:
                tag = "fiveDice";
                break;
            case 6:
                tag = "sixDice";
                break;
        }

        editor.putString(tag, colorCodes);
        editor.commit();
    }

    public static boolean getLightTheme(Context context) {
        SharedPreferences pref = context.getSharedPreferences("DicePref", context.MODE_PRIVATE);
        return pref.getBoolean("isLightTheme", true);
    }

    public static boolean getEngLanguage(Context context) {
        SharedPreferences pref = context.getSharedPreferences("DicePref", context.MODE_PRIVATE);
        return pref.getBoolean("isLanguageEng", true);
    }

    public static String getDiceColorSet(Context context, int dicesCount) {
        SharedPreferences pref = context.getSharedPreferences("DicePref", context.MODE_PRIVATE);
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
}
