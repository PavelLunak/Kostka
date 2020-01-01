package com.lupa.kostka.utils;

public interface AppConstants {

    int BLACK = 1;
    int WHITE = 2;
    int RED = 3;
    int BLUE = 4;
    int GREEN = 5;
    int YELLOW = 6;

    long INTRO_DISPLAY_DELAY = 5000;

    int MAX_SHUFFLE_DELAY = 1200;

    float LANGUAGE_FLAG_MAX_ALPHA = 0.5f;

    String FRAGMENT_MAIN = "FragmentMain";
    String FRAGMENT_DICE = "FragmentDices";
    String FRAGMENT_INFO = "FragmentInfo";

    String PREFS_NAME  = "DicePref";
    String PREFS_THEME = "isLightTheme";
    String PREFS_LANGUAGE_ENG = "isLanguageEng";
    String PREFS_DICE_COUNT = "dicesCount";
    String PREFS_HELP1 = "help1";

    String INSTANCE_STATE_DICE_COUNT = "dicesCount";
    String INSTANCE_STATE_SETTINGS_SHOWED = "settingsShowed";
    String INSTANCE_STATE_SETTINGS_COLOR_SHOWED = "settingsColorShowed";
    String INSTANCE_STATE_SETTINGS_LANGUAGE_SHOWED = "settingsLanguageShowed";
    String INSTANCE_STATE_INTRO_SHOWED = "introShowed";
    String INSTANCE_STATE_INTRO_TIMER_COUNTER = "introTimerCounter";
    String INSTANCE_STATE_COUNTER = "counter";
    String INSTANCE_STATE_DICE_COLOR_SELECT_COUNTER = "dicesColorSelectCounter";
    String INSTANCE_STATE_HELP1_SHOWED = "help1Showed";
}
