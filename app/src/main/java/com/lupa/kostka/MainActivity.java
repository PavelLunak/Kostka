package com.lupa.kostka;

import android.animation.AnimatorSet;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lupa.kostka.fragments.FragmentDices;
import com.lupa.kostka.fragments.FragmentInfo;
import com.lupa.kostka.fragments.FragmentMain;
import com.lupa.kostka.listeners.OnAnimationEndListener;
import com.lupa.kostka.objects.Counter;
import com.lupa.kostka.objects.Dice;
import com.lupa.kostka.objects.DicesColorSet;
import com.lupa.kostka.utils.Animators;
import com.lupa.kostka.utils.AppConstants;
import com.lupa.kostka.utils.AppUtils;
import com.lupa.kostka.utils.PrefsUtils;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        FragmentManager.OnBackStackChangedListener,
        AppConstants {

    int animShowFragment = R.anim.anim_fragment_show;
    int animHideFragment = R.anim.anim_fragment_hide;

    //Pozadí aplikace
    public enum Theme {LIGHT, DARK};

    public enum Language {CZ, ENG};

    //Zvolený počet kostek
    public int dicesCount = 1;

    //Objekt pro uložení zvolených barev kostek
    public DicesColorSet dicesColorSet;

    FragmentManager fragmentManager;
    FrameLayout container, layoutClick;
    RelativeLayout root, layoutSettings, layoutSettingsRoot, layoutColor, layoutInfo1;
    LinearLayout btnSettings, layoutLanguage;
    ImageView imgSettings, imgTheme, imgColor, imgLanguage, imgInfo, imgClose, imageView,
            imgCz, imgEng, imgCheckCz, imgCheckEng;
    View viewBlack, viewWhite, viewRed, viewBlue, viewGreen, viewYellow;
    TextView labelInfo1, labelClose;

    public boolean settingsShowed;
    public boolean settingsColorShowed;
    public boolean settingsLanguageShowed;
    boolean introShowed;
    boolean help1Showed;

    //Objekt pro ukládání a sčítání hodnot kostek při míchání
    public Counter counter;

    public Theme theme = Theme.DARK;
    public Language language = Language.ENG;

    FragmentDices fragmentDices;
    FragmentInfo fragmentInfo;
    FragmentMain fragmentMain;

    //Odpočet pro dobu zobrazení úvodního obrázku
    CountDownTimer introTimer;
    long introTimerCounter;

    //Počítadlo pro postupný cyklický  výběr barev kostek v sadě
    int dicesColorSelectCounter;

    boolean isSavedInstanceState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        imageView = findViewById(R.id.imageView);
        container = findViewById(R.id.container);
        layoutClick = findViewById(R.id.layoutClick);
        root = findViewById(R.id.root);
        btnSettings = findViewById(R.id.btnSettings);
        layoutLanguage = findViewById(R.id.layoutLanguage);
        imgSettings = findViewById(R.id.imgSettings);
        layoutSettingsRoot = findViewById(R.id.layoutSettingsRoot);
        layoutColor = findViewById(R.id.layoutColor);
        layoutSettings = findViewById(R.id.layoutSettings);
        imgTheme = findViewById(R.id.imgTheme);
        imgColor = findViewById(R.id.imgColor);
        imgLanguage = findViewById(R.id.imgLanguage);
        imgInfo = findViewById(R.id.imgInfo);
        imgClose = findViewById(R.id.imgClose);
        imgCz = findViewById(R.id.imgCz);
        imgEng = findViewById(R.id.imgEng);
        imgCheckCz = findViewById(R.id.imgCheckCz);
        imgCheckEng = findViewById(R.id.imgCheckEng);
        viewBlack = findViewById(R.id.viewBlack);
        viewWhite = findViewById(R.id.viewWhite);
        viewRed = findViewById(R.id.viewRed);
        viewBlue = findViewById(R.id.viewBlue);
        viewGreen = findViewById(R.id.viewGreen);
        viewYellow = findViewById(R.id.viewYellow);
        layoutInfo1 = findViewById(R.id.layoutInfo1);
        labelInfo1 = findViewById(R.id.labelInfo1);
        labelClose = findViewById(R.id.labelClose);

        btnSettings.setOnClickListener(this);
        imgTheme.setOnClickListener(this);
        imgColor.setOnClickListener(this);
        imgLanguage.setOnClickListener(this);
        imgInfo.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        imgCz.setOnClickListener(this);
        imgEng.setOnClickListener(this);
        layoutClick.setOnClickListener(this);
        labelClose.setOnClickListener(this);

        viewBlack.setOnClickListener(this);
        viewWhite.setOnClickListener(this);
        viewRed.setOnClickListener(this);
        viewBlue.setOnClickListener(this);
        viewGreen.setOnClickListener(this);
        viewYellow.setOnClickListener(this);

        //Objekt pro uložení zvolených barev kostek
        dicesColorSet = new DicesColorSet();

        language = PrefsUtils.getEngLanguage(this) ? Language.ENG : Language.CZ;
        theme = PrefsUtils.getLightTheme(this) ? Theme.LIGHT : Theme.DARK;

        //Načtení uložených barev kostek v každé sadě
        dicesColorSet.setDiceColorSetFromString(PrefsUtils.getDiceColorSet(this, 1));
        dicesColorSet.setDiceColorSetFromString(PrefsUtils.getDiceColorSet(this, 2));
        dicesColorSet.setDiceColorSetFromString(PrefsUtils.getDiceColorSet(this, 3));
        dicesColorSet.setDiceColorSetFromString(PrefsUtils.getDiceColorSet(this, 4));
        dicesColorSet.setDiceColorSetFromString(PrefsUtils.getDiceColorSet(this, 5));
        dicesColorSet.setDiceColorSetFromString(PrefsUtils.getDiceColorSet(this, 6));

        if (savedInstanceState != null) {
            dicesCount = savedInstanceState.getInt(PREFS_DICE_COUNT, 1);
            isSavedInstanceState = true;
        }

        fragmentManager.addOnBackStackChangedListener(this);

        layoutClick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (settingsColorShowed || settingsShowed) {
                            if (settingsShowed) showSettings(false, true);
                            return true;
                        }

                        if (fragmentDices != null) {
                            if (AppUtils.isFragmentCurrent(FRAGMENT_DICE, fragmentManager)) {
                                counter.clear();
                                fragmentDices.startShuffle();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (fragmentDices != null) {
                            if (AppUtils.isFragmentCurrent(FRAGMENT_DICE, fragmentManager)) {
                                counter.clear();
                                fragmentDices.stopShuffle();
                            }
                        }
                        break;
                }

                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (introTimer != null) introTimer.cancel();
        introTimer = null;

        //Uložení barev kostek
        PrefsUtils.updateDiceColor(
                this,
                dicesCount,
                dicesColorSet.getDiceColorSetToString(dicesCount));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (counter == null) counter = new Counter();

        updateTheme();
        updateImage();
        showSettings(settingsShowed, false);
        showSettingsColor(settingsColorShowed, false);
        showSettingsLanguage(settingsLanguageShowed, false);

        if (!introShowed || introTimerCounter > 0) {
            btnSettings.setVisibility(View.GONE);
            introShowed = true;

            introTimer = new CountDownTimer(INTRO_DISPLAY_DELAY - introTimerCounter, 200) {
                @Override
                public void onTick(long time) {
                    introTimerCounter = time;
                }

                @Override
                public void onFinish() {
                    imageView.setVisibility(View.GONE);
                    introTimerCounter = 0;
                    introTimer = null;
                    showFragmentMain();
                    btnSettings.setVisibility(View.VISIBLE);
                    Animators.showViewScaleSmoothly(btnSettings, true, true, 100);
                }
            };

            introTimer.start();
        } else {
            imageView.setVisibility(View.GONE);
            btnSettings.setVisibility(View.VISIBLE);
            AppUtils.setMaxScale(btnSettings);
        }

        fragmentMain = (FragmentMain) fragmentManager.findFragmentByTag(FRAGMENT_MAIN);
        fragmentDices = (FragmentDices) fragmentManager.findFragmentByTag(FRAGMENT_DICE);
        fragmentInfo = (FragmentInfo) fragmentManager.findFragmentByTag(FRAGMENT_INFO);

        if (isSavedInstanceState) {
            if (fragmentDices != null) {
                if (fragmentDices.getValueCounter() != null) {
                    fragmentDices.getValueCounter().setAfterRestoreInstanceState();
                }
            }
        }

        if (AppUtils.isFragmentCurrent(FRAGMENT_DICE, fragmentManager)) {
            layoutClick.setVisibility(View.VISIBLE);

            if (PrefsUtils.canShowHelp1(this)) {
                showHelp1(true, !help1Showed);
            }
        } else {
            layoutClick.setVisibility(View.GONE);
        }

        if (fragmentDices != null) {
            if (dicesCount > 1) {
                if (counter != null) {
                    if (counter.getValue() > 0) {
                        fragmentDices.setCounter(false);
                    }
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(INSTANCE_STATE_SETTINGS_SHOWED, settingsShowed);
        outState.putBoolean(INSTANCE_STATE_SETTINGS_COLOR_SHOWED, settingsColorShowed);
        outState.putBoolean(INSTANCE_STATE_SETTINGS_LANGUAGE_SHOWED, settingsLanguageShowed);
        outState.putBoolean(INSTANCE_STATE_INTRO_SHOWED, introShowed);
        outState.putLong(INSTANCE_STATE_INTRO_TIMER_COUNTER, introTimerCounter);
        outState.putParcelable(INSTANCE_STATE_COUNTER, counter);
        outState.putInt(INSTANCE_STATE_DICE_COUNT, dicesCount);
        outState.putInt(INSTANCE_STATE_DICE_COLOR_SELECT_COUNTER, dicesColorSelectCounter);
        outState.putBoolean(INSTANCE_STATE_HELP1_SHOWED, help1Showed);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        settingsShowed = savedInstanceState.getBoolean(INSTANCE_STATE_SETTINGS_SHOWED, false);
        settingsColorShowed = savedInstanceState.getBoolean(INSTANCE_STATE_SETTINGS_COLOR_SHOWED, false);
        settingsLanguageShowed = savedInstanceState.getBoolean(INSTANCE_STATE_SETTINGS_LANGUAGE_SHOWED, false);
        introShowed = savedInstanceState.getBoolean(INSTANCE_STATE_INTRO_SHOWED, false);
        introTimerCounter = savedInstanceState.getLong(INSTANCE_STATE_INTRO_TIMER_COUNTER, 0);
        counter = savedInstanceState.getParcelable(INSTANCE_STATE_COUNTER);
        dicesCount = savedInstanceState.getInt(INSTANCE_STATE_DICE_COUNT, 1);
        dicesColorSelectCounter = savedInstanceState.getInt(INSTANCE_STATE_DICE_COLOR_SELECT_COUNTER, 0);
        help1Showed = savedInstanceState.getBoolean(INSTANCE_STATE_HELP1_SHOWED, false);
    }

    @Override
    public void onBackPressed() {
        if (settingsLanguageShowed) {
            showSettingsLanguage(false, true);
            return;
        }

        if (settingsColorShowed) {
            showSettingsColor(false, true);
            return;
        }

        if (settingsShowed) {
            showSettings(false, true);
            return;
        }

        if (introTimer != null) {
            imageView.setVisibility(View.GONE);
            introTimer.cancel();
            introTimer = null;
            introTimerCounter = 0;
            showFragmentMain();
            btnSettings.setVisibility(View.VISIBLE);
            Animators.showViewScaleSmoothly(btnSettings, true, true, 100);
            return;
        }

        if (fragmentManager.getBackStackEntryCount() == 1) finish();

        super.onBackPressed();
    }

    @Override
    public void onBackStackChanged() {
        if (AppUtils.isFragmentCurrent(FRAGMENT_DICE, fragmentManager)) {
            layoutClick.setVisibility(View.VISIBLE);

            if (PrefsUtils.canShowHelp1(this)) {
                showHelp1(true, !help1Showed);
            }
        } else {
            layoutClick.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (settingsColorShowed &&
                view.getId() != R.id.imgColor &&
                view.getId() != R.id.viewBlack &&
                view.getId() != R.id.viewWhite &&
                view.getId() != R.id.viewBlue &&
                view.getId() != R.id.viewGreen &&
                view.getId() != R.id.viewRed &&
                view.getId() != R.id.viewYellow) {

            showSettingsColor(false, true);
        }

        if (settingsLanguageShowed &&
                view.getId() != R.id.imgLanguage &&
                view.getId() != R.id.imgCz &&
                view.getId() != R.id.imgEng) {

            showSettingsLanguage(false, true);
        }


        if (settingsShowed
                && !settingsColorShowed
                && !settingsLanguageShowed
                && view.getId() != R.id.imgTheme
                && view.getId() != R.id.btnSettings
                && view.getId() != R.id.imgColor
                && view.getId() != R.id.imgLanguage) {

            showSettings(false, true);
        }

        switch (view.getId()) {
            case R.id.btnSettings:
                showSettings(!settingsShowed, true);
                break;
            case R.id.imgTheme:
                Animators.animateButtonClick2(imgTheme, 1f);
                switchTheme();
                PrefsUtils.updateTheme(this, isLightTheme());
                updateTheme();
                break;
            case R.id.imgColor:
                Animators.animateButtonClick2(imgColor, 1f);

                if (!AppUtils.isFragmentCurrent(FRAGMENT_DICE, fragmentManager)) {
                    Toast.makeText(
                            MainActivity.this,
                            getResources().getString(
                                    language == Language.ENG ?
                                            R.string.toast_color :
                                            R.string.toast_color_cz),
                            Toast.LENGTH_SHORT).show();
                } else {
                    showSettingsColor(!settingsColorShowed, true);
                }
                break;
            case R.id.imgLanguage:
                Animators.animateButtonClick2(imgLanguage, 1f);
                showSettingsLanguage(!settingsLanguageShowed, true);
                break;
            case R.id.imgInfo:
                Animators.animateButtonClick2(imgInfo, 1f);
                showFragmentInfo();
                break;
            case R.id.imgClose:
                Animators.animateButtonClick2(imgClose, 1f);
                finish();
                break;
            case R.id.imgCz:
                Animators.animateButtonClick2(imgCz, LANGUAGE_FLAG_MAX_ALPHA);

                if (this.language == Language.ENG) {
                    Animators.showViewScaleSmoothly(imgCheckCz, true, true, 0);
                    Animators.hideViewScaleSmoothly(imgCheckEng, true, true, 0);
                }

                setLanguage(Language.CZ);
                imgCz.setAlpha(LANGUAGE_FLAG_MAX_ALPHA);
                break;
            case R.id.imgEng:
                Animators.animateButtonClick2(imgEng, LANGUAGE_FLAG_MAX_ALPHA);

                if (this.language == Language.CZ) {
                    Animators.showViewScaleSmoothly(imgCheckEng, true, true, 0);
                    Animators.hideViewScaleSmoothly(imgCheckCz, true, true, 0);;
                }

                setLanguage(Language.ENG);
                imgEng.setAlpha(LANGUAGE_FLAG_MAX_ALPHA);
                break;
            case R.id.viewBlack:
                Animators.animateButtonClick2(viewBlack, LANGUAGE_FLAG_MAX_ALPHA);

                if (fragmentDices != null) {
                    fragmentDices.setDiceColor(incrementDicesColorSelectCounter(), Dice.DiceColor.BLACK);
                    dicesColorSet.setDiceItemColor(dicesCount, dicesColorSelectCounter - 1, BLACK);
                }

                //if (dicesCount < 2) showSettingsColor(false, true);
                break;
            case R.id.viewWhite:
                Animators.animateButtonClick2(viewWhite, 1f);
                if (fragmentDices != null) {
                    fragmentDices.setDiceColor(incrementDicesColorSelectCounter(), Dice.DiceColor.WHITE);
                    dicesColorSet.setDiceItemColor(dicesCount, dicesColorSelectCounter - 1, WHITE);
                }

                //if (dicesCount < 2) showSettingsColor(false, true);
                break;
            case R.id.viewRed:
                Animators.animateButtonClick2(viewRed, 1f);
                if (fragmentDices != null) {
                    fragmentDices.setDiceColor(incrementDicesColorSelectCounter(), Dice.DiceColor.RED);
                    dicesColorSet.setDiceItemColor(dicesCount, dicesColorSelectCounter - 1, RED);
                }

                //if (dicesCount < 2) showSettingsColor(false, true);
                break;
            case R.id.viewBlue:
                Animators.animateButtonClick2(viewBlue, 1f);
                if (fragmentDices != null) {
                    fragmentDices.setDiceColor(incrementDicesColorSelectCounter(), Dice.DiceColor.BLUE);
                    dicesColorSet.setDiceItemColor(dicesCount, dicesColorSelectCounter - 1, BLUE);
                }

                //if (dicesCount < 2) showSettingsColor(false, true);
                break;
            case R.id.viewGreen:
                Animators.animateButtonClick2(viewGreen, 1f);
                if (fragmentDices != null) {
                    fragmentDices.setDiceColor(incrementDicesColorSelectCounter(), Dice.DiceColor.GREEN);
                    dicesColorSet.setDiceItemColor(dicesCount, dicesColorSelectCounter - 1, GREEN);
                }

                //if (dicesCount < 2) showSettingsColor(false, true);
                break;
            case R.id.viewYellow:
                Animators.animateButtonClick2(viewYellow, 1f);
                if (fragmentDices != null) {
                    fragmentDices.setDiceColor(incrementDicesColorSelectCounter(), Dice.DiceColor.YELLOW);
                    dicesColorSet.setDiceItemColor(dicesCount, dicesColorSelectCounter - 1, YELLOW);
                }

                //if (dicesCount < 2) showSettingsColor(false, true);
                break;
            case R.id.labelClose:
                Animators.animateButtonClick2(labelClose, 1f);

                Animators.animateHideLayoutSettings(layoutInfo1, new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        layoutInfo1.setVisibility(View.GONE);
                        AppUtils.setMinScale(layoutInfo1);

                    }
                });

                PrefsUtils.hideHelp1(MainActivity.this);
                break;
        }
    }

    public void showFragmentMain() {
        fragmentMain = (FragmentMain) fragmentManager.findFragmentByTag(FRAGMENT_MAIN);

        if (fragmentMain == null) {
            fragmentMain = new FragmentMain();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(animShowFragment, animHideFragment, animShowFragment, animHideFragment);
            fragmentTransaction.add(R.id.container, fragmentMain, FRAGMENT_MAIN);
            fragmentTransaction.addToBackStack(FRAGMENT_MAIN);
            fragmentTransaction.commit();
        } else {
            int beCount = fragmentManager.getBackStackEntryCount();
            if (beCount == 0) return;
            fragmentManager.popBackStack(FRAGMENT_MAIN, 0);
        }
    }

    public void showFragmentDices() {
        fragmentDices = (FragmentDices) fragmentManager.findFragmentByTag(FRAGMENT_DICE);

        if (fragmentDices == null) {
            fragmentDices = new FragmentDices();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(animShowFragment, animHideFragment, animShowFragment, animHideFragment);
            fragmentTransaction.add(R.id.container, fragmentDices, FRAGMENT_DICE);
            fragmentTransaction.addToBackStack(FRAGMENT_DICE);
            fragmentTransaction.commit();
        } else {
            int beCount = fragmentManager.getBackStackEntryCount();
            if (beCount == 0) return;
            fragmentManager.popBackStack(FRAGMENT_DICE, 0);
        }
    }

    public void showFragmentInfo() {
        fragmentInfo = (FragmentInfo) fragmentManager.findFragmentByTag(FRAGMENT_INFO);

        if (fragmentInfo == null) {
            fragmentInfo = new FragmentInfo();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(animShowFragment, animHideFragment, animShowFragment, animHideFragment);
            fragmentTransaction.add(R.id.container, fragmentInfo, FRAGMENT_INFO);
            fragmentTransaction.addToBackStack(FRAGMENT_INFO);
            fragmentTransaction.commit();
        } else {
            int beCount = fragmentManager.getBackStackEntryCount();
            if (beCount == 0) return;
            fragmentManager.popBackStack(FRAGMENT_INFO, 0);
        }
    }

    private void updateTheme() {
        if (isLightTheme()) {
            root.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
            AppUtils.setImage(this, imgSettings, R.drawable.ic_settings_dark);
            layoutSettings.setBackground(AppUtils.getDrawableBySdk(this, R.drawable.bg_layout_settings_dark));
            layoutColor.setBackground(AppUtils.getDrawableBySdk(this, R.drawable.bg_layout_settings_dark));
            layoutLanguage.setBackground(AppUtils.getDrawableBySdk(this, R.drawable.bg_layout_settings_dark));
            AppUtils.setImage(this, imgClose, R.drawable.ic_power_light);
            AppUtils.setImage(this, imgInfo, R.drawable.ic_info_light);
            AppUtils.setImage(this, imgTheme, R.drawable.ic_brightness_light);
            AppUtils.setImage(this, imgColor, R.drawable.ic_color_light);
            AppUtils.setImage(this, imgLanguage, R.drawable.ic_language_light);

            if (fragmentMain != null) fragmentMain.setColor(Theme.LIGHT);
            if (fragmentDices != null) fragmentDices.setColor(Theme.LIGHT);
            if (fragmentInfo != null) fragmentInfo.setColor(Theme.LIGHT);
        } else {
            root.setBackgroundColor(getResources().getColor(R.color.colorBackgroundDark));
            AppUtils.setImage(this, imgSettings, R.drawable.ic_settings);
            layoutSettings.setBackground(AppUtils.getDrawableBySdk(this, R.drawable.bg_layout_settings));
            layoutColor.setBackground(AppUtils.getDrawableBySdk(this, R.drawable.bg_layout_settings));
            layoutLanguage.setBackground(AppUtils.getDrawableBySdk(this, R.drawable.bg_layout_settings));
            AppUtils.setImage(this, imgClose, R.drawable.ic_power);
            AppUtils.setImage(this, imgInfo, R.drawable.ic_info);
            AppUtils.setImage(this, imgTheme, R.drawable.ic_brightness);
            AppUtils.setImage(this, imgColor, R.drawable.ic_color);
            AppUtils.setImage(this, imgLanguage, R.drawable.ic_language);

            if (fragmentMain != null) fragmentMain.setColor(Theme.DARK);
            if (fragmentDices != null) fragmentDices.setColor(Theme.DARK);
            if (fragmentInfo != null) fragmentInfo.setColor(Theme.DARK);
        }
    }

    public void showSettings(boolean show, boolean animate) {
        Animators.animateRotate(imgSettings, show ? 90 : -90);

        if (show) {
            settingsShowed = true;
            layoutSettingsRoot.setVisibility(View.VISIBLE);

            if (animate) {
                Animators.animateShowLayoutSettings(layoutSettingsRoot, new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        AppUtils.setMaxScale(layoutSettingsRoot);
                    }
                });
            } else {
                AppUtils.setMaxScale(layoutSettingsRoot);
                layoutSettingsRoot.setVisibility(View.VISIBLE);
            }
        } else {
            settingsShowed = false;

            if (settingsColorShowed) showSettingsColor(false, true);
            if (settingsLanguageShowed) showSettingsLanguage(false, true);

            if (animate) {
                Animators.animateHideLayoutSettings(layoutSettingsRoot, new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        AppUtils.setMinScale(layoutSettingsRoot);
                        layoutSettingsRoot.setVisibility(View.GONE);
                    }
                });
            } else {
                layoutSettingsRoot.setVisibility(View.GONE);
                AppUtils.setMinScale(layoutSettingsRoot);
            }
        }
    }

    public void showSettingsColor(boolean show, boolean animate) {
        if (!AppUtils.isFragmentCurrent("FragmentDices", fragmentManager)) return;

        if (show) {
            settingsColorShowed = true;
            layoutColor.setVisibility(View.VISIBLE);

            if (animate) {
                Animators.animateShowLayoutSettingsColor(layoutColor, new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        layoutColor.setScaleX(1.0f);
                    }
                });
            } else {
                layoutColor.setScaleX(1.0f);
                layoutColor.setVisibility(View.VISIBLE);
            }
        } else {
            settingsColorShowed = false;
            dicesColorSelectCounter = 0;
            PrefsUtils.updateDiceColor(this, dicesCount, dicesColorSet.getDiceColorSetToString(dicesCount));

            if (animate) {
                Animators.animateHideLayoutSettingsColor(layoutColor, new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        layoutColor.setScaleX(0.0f);
                        layoutColor.setVisibility(View.GONE);
                    }
                });
            } else {
                layoutColor.setVisibility(View.GONE);
                layoutColor.setScaleX(0.0f);
            }
        }
    }

    public void showSettingsLanguage(boolean show, boolean animate) {
        if (show) {
            if (this.language == Language.CZ) {
                AppUtils.setMaxScale(imgCheckCz);
                AppUtils.setMinScale(imgCheckEng);
            } else {
                AppUtils.setMaxScale(imgCheckEng);
                AppUtils.setMinScale(imgCheckCz);
            }

            settingsLanguageShowed = true;
            layoutLanguage.setVisibility(View.VISIBLE);

            if (animate) {
                Animators.animateShowLayoutSettingsColor(layoutLanguage, new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        layoutLanguage.setScaleX(1.0f);
                    }
                });
            } else {
                layoutLanguage.setVisibility(View.VISIBLE);
                layoutLanguage.setScaleX(1.0f);
            }
        } else {
            settingsLanguageShowed = false;

            if (animate) {
                Animators.animateHideLayoutSettingsColor(layoutLanguage, new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        layoutLanguage.setScaleX(0.0f);
                        layoutLanguage.setVisibility(View.GONE);
                    }
                });
            } else {
                layoutLanguage.setVisibility(View.GONE);
                layoutLanguage.setScaleX(0.0f);
            }
        }
    }

    public void showHelp1(boolean show, boolean animate) {
        if (show) {
            help1Showed = true;
            layoutInfo1.setVisibility(View.VISIBLE);

            if (animate) {
                Animators.animateShowLayoutSettings(layoutInfo1, new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        AppUtils.setMaxScale(layoutInfo1);
                    }
                });
            } else {
                AppUtils.setMaxScale(layoutInfo1);
            }
        } else {
            help1Showed = true;

            if (animate) {
                Animators.animateHideLayoutSettings(layoutInfo1, new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        AppUtils.setMaxScale(layoutInfo1);
                        layoutInfo1.setVisibility(View.GONE);
                    }
                });
            } else {
                AppUtils.setMaxScale(layoutInfo1);
                layoutInfo1.setVisibility(View.GONE);
            }
        }
    }

    private int incrementDicesColorSelectCounter() {
        if (dicesColorSelectCounter < dicesCount) dicesColorSelectCounter ++;
        else dicesColorSelectCounter = 1;

        return dicesColorSelectCounter;
    }

    public void switchTheme() {
        if (isLightTheme()) theme = Theme.DARK;
        else theme = Theme.LIGHT;
    }

    public boolean isLightTheme() {
        return theme == Theme.LIGHT;
    }

    public void setLanguage(Language language) {
        this.language = language;
        PrefsUtils.updateLanguage(this, language);

        if (language == MainActivity.Language.CZ) {
            labelInfo1.setText(getResources().getString(R.string.text_help_1_cz));
            labelClose.setText(getResources().getString(R.string.close_cz));
        } else {
            labelInfo1.setText(getResources().getString(R.string.text_help_1));
            labelClose.setText(getResources().getString(R.string.close));
        }

        if (fragmentMain != null) {
            fragmentMain.updateLanguage();
        }

        if (fragmentInfo != null) {
            fragmentInfo.updateLanguage();
            fragmentInfo.updateImage();
        }
    }

    public void updateImage() {
        if (language == MainActivity.Language.CZ) {
            AppUtils.setImage(this, imageView, R.drawable.itnetwork_winter_2019);
        } else {
            AppUtils.setImage(this, imageView, R.drawable.itnetwork_winter_2019_1);
        }
    }
}
