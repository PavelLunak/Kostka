package com.lupa.kostka;

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
import com.lupa.kostka.fragments.FragmentTest;
import com.lupa.kostka.listeners.OnAnimationEndListener;
import com.lupa.kostka.objects.Counter;
import com.lupa.kostka.objects.DicesColorSet;
import com.lupa.kostka.utils.AppConstants;
import com.lupa.kostka.utils.PrefsUtils;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        FragmentManager.OnBackStackChangedListener,
        AppConstants {

    int animShowFragment = R.anim.anim_fragment_show;
    int animHideFragment = R.anim.anim_fragment_hide;

    public enum Theme {LIGHT, DARK};
    public enum Language {CZ, ENG};

    public int dicesCount = 1;
    public DicesColorSet dicesColorSet;

    FragmentManager fragmentManager;
    FrameLayout container, layoutClick;
    RelativeLayout root, layoutSettings, layoutSettingsRoot, layoutColor, layoutInfo1;
    LinearLayout btnSettings, layoutLanguage;
    ImageView imgSettings, imgTheme, imgColor, imgLanguage, imgInfo, imgClose, imageView, imgCz, imgEng;
    View viewBlack, viewWhite, viewRed, viewBlue, viewGreen, viewYellow;
    TextView labelInfo1, labelClose;

    public boolean settingsShowed;
    public boolean settingsColorShowed;
    public boolean settingsLanguageShowed;
    boolean introShowed;
    boolean help1Showed;

    public Counter counter;

    public Theme theme = Theme.DARK;
    public Language language = Language.ENG;

    FragmentDices fragmentDices;
    FragmentInfo fragmentInfo;
    FragmentTest fragmentTest;
    FragmentMain fragmentMain;

    CountDownTimer introTimer;
    long introTimerCounter;

    int value;

    int dicesColorSelectCounter;


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

        dicesColorSet = new DicesColorSet();

        language = PrefsUtils.getEngLanguage(this) ? Language.ENG : Language.CZ;
        theme = PrefsUtils.getLightTheme(this) ? Theme.LIGHT : Theme.DARK;

        dicesColorSet.setDiceColorSetFromString(PrefsUtils.getDiceColorSet(this, 1));
        dicesColorSet.setDiceColorSetFromString(PrefsUtils.getDiceColorSet(this, 2));
        dicesColorSet.setDiceColorSetFromString(PrefsUtils.getDiceColorSet(this, 3));
        dicesColorSet.setDiceColorSetFromString(PrefsUtils.getDiceColorSet(this, 4));
        dicesColorSet.setDiceColorSetFromString(PrefsUtils.getDiceColorSet(this, 5));
        dicesColorSet.setDiceColorSetFromString(PrefsUtils.getDiceColorSet(this, 6));

        if (savedInstanceState != null)
            dicesCount = savedInstanceState.getInt("dicesCount", 1);

        fragmentManager.addOnBackStackChangedListener(this);

        layoutClick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (settingsColorShowed || settingsShowed) {
                            if (settingsColorShowed) showSettingsColor(false, true);
                            if (settingsShowed) showSettings(false, true);
                            return true;
                        }

                        if (fragmentDices != null) {
                            if (isFragmentCurrent("FragmentDices", fragmentManager)) {
                                counter.clear();
                                fragmentDices.startShuffle();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (fragmentDices != null) {
                            if (isFragmentCurrent("FragmentDices", fragmentManager)) {
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

            introTimer = new CountDownTimer(5000 - introTimerCounter, 200) {
                @Override
                public void onTick(long l) {
                    introTimerCounter = l;
                }

                @Override
                public void onFinish() {
                    imageView.setVisibility(View.GONE);
                    introTimerCounter = 0;
                    introTimer = null;
                    btnSettings.setVisibility(View.VISIBLE);
                    showFragmentMain();
                    Animators.showViewScaleSmoothly(btnSettings, true, true, 100);
                }
            };

            introTimer.start();
        } else {
            imageView.setVisibility(View.GONE);
            btnSettings.setVisibility(View.VISIBLE);
            btnSettings.setScaleX(1f);
            btnSettings.setScaleY(1f);
        }

        fragmentMain = (FragmentMain) fragmentManager.findFragmentByTag("FragmentMain");
        fragmentDices = (FragmentDices) fragmentManager.findFragmentByTag("FragmentDices");
        fragmentInfo = (FragmentInfo) fragmentManager.findFragmentByTag("FragmentInfo");

        if (isFragmentCurrent("FragmentDices", fragmentManager)) {
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
                        fragmentDices.setCounter(counter.getValue(), false);
                    }
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("settingsShowed", settingsShowed);
        outState.putBoolean("settingsColorShowed", settingsColorShowed);
        outState.putBoolean("settingsLanguageShowed", settingsLanguageShowed);
        outState.putInt("value", value);
        outState.putBoolean("introShowed", introShowed);
        outState.putLong("introTimerCounter", introTimerCounter);
        outState.putParcelable("counter", counter);
        outState.putInt("dicesCount", dicesCount);
        outState.putInt("dicesColorSelectCounter", dicesColorSelectCounter);
        outState.putBoolean("help1Showed", help1Showed);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        settingsShowed = savedInstanceState.getBoolean("settingsShowed", false);
        settingsColorShowed = savedInstanceState.getBoolean("settingsColorShowed", false);
        settingsLanguageShowed = savedInstanceState.getBoolean("settingsLanguageShowed", false);
        value = savedInstanceState.getInt("value", 0);
        introShowed = savedInstanceState.getBoolean("introShowed", false);
        introTimerCounter = savedInstanceState.getLong("introTimerCounter", 0);
        counter = savedInstanceState.getParcelable("counter");
        dicesCount = savedInstanceState.getInt("dicesCount", 1);
        dicesColorSelectCounter = savedInstanceState.getInt("dicesColorSelectCounter", 0);
        help1Showed = savedInstanceState.getBoolean("help1Showed", false);
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
            btnSettings.setVisibility(View.VISIBLE);
            showFragmentMain();
            Animators.showViewScaleSmoothly(btnSettings, true, true, 100);
            return;
        }

        int fragmentsInStack = fragmentManager.getBackStackEntryCount();
        if (fragmentsInStack == 1) finish();

        super.onBackPressed();
    }

    @Override
    public void onBackStackChanged() {
        int fragmentsInStack = fragmentManager.getBackStackEntryCount();

        if (isFragmentCurrent("FragmentDices", fragmentManager)) {
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
                view.getId() != R.id.imgCz &&
                view.getId() != R.id.imgEng) {

            showSettingsLanguage(false, true);
        }


        if (settingsShowed
                && !settingsColorShowed
                && !settingsLanguageShowed
                && (view.getId() != R.id.btnSettings
                && view.getId() != R.id.imgColor
                && view.getId() != R.id.imgLanguage)) {

            showSettings(false, true);
        }

        switch (view.getId()) {
            case R.id.btnSettings:
                showSettings(!settingsShowed, true);
                break;
            case R.id.imgTheme:
                Animators.animateButtonClick2(imgTheme, 1f);
                switchTheme();

                SharedPreferences pref = getSharedPreferences("DicePref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isLightTheme", isLightTheme());
                editor.commit();

                updateTheme();
                break;
            case R.id.imgColor:
                Animators.animateButtonClick2(imgColor, 1f);
                if (!isFragmentCurrent("FragmentDices", fragmentManager)) {
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
                Animators.animateButtonClick2(imgCz, 0.5f);
                setLanguage(Language.CZ);
                imgCz.setAlpha(0.5f);
                showSettingsLanguage(false, true);
                break;
            case R.id.imgEng:
                Animators.animateButtonClick2(imgEng, 0.5f);
                setLanguage(Language.ENG);
                imgEng.setAlpha(0.5f);
                showSettingsLanguage(false, true);
                break;
            case R.id.viewBlack:
                Animators.animateButtonClick2(viewBlack, 0.5f);
                if (fragmentDices != null) {
                    fragmentDices.setDiceColor(incrementDicesColorSelectCounter(), Dice.DiceColor.BLACK);
                    dicesColorSet.setDiceItemColor(dicesCount, dicesColorSelectCounter - 1, BLACK);
                }

                if (dicesCount < 2)
                    showSettingsColor(false, true);
                break;
            case R.id.viewWhite:
                Animators.animateButtonClick2(viewWhite, 1f);
                if (fragmentDices != null) {
                    fragmentDices.setDiceColor(incrementDicesColorSelectCounter(), Dice.DiceColor.WHITE);
                    dicesColorSet.setDiceItemColor(dicesCount, dicesColorSelectCounter - 1, WHITE);
                }

                if (dicesCount < 2)
                    showSettingsColor(false, true);
                break;
            case R.id.viewRed:
                Animators.animateButtonClick2(viewRed, 1f);
                if (fragmentDices != null) {
                    fragmentDices.setDiceColor(incrementDicesColorSelectCounter(), Dice.DiceColor.RED);
                    dicesColorSet.setDiceItemColor(dicesCount, dicesColorSelectCounter - 1, RED);
                }

                if (dicesCount < 2)
                    showSettingsColor(false, true);
                break;
            case R.id.viewBlue:
                Animators.animateButtonClick2(viewBlue, 1f);
                if (fragmentDices != null) {
                    fragmentDices.setDiceColor(incrementDicesColorSelectCounter(), Dice.DiceColor.BLUE);
                    dicesColorSet.setDiceItemColor(dicesCount, dicesColorSelectCounter - 1, BLUE);
                }

                if (dicesCount < 2)
                    showSettingsColor(false, true);
                break;
            case R.id.viewGreen:
                Animators.animateButtonClick2(viewGreen, 1f);
                if (fragmentDices != null) {
                    fragmentDices.setDiceColor(incrementDicesColorSelectCounter(), Dice.DiceColor.GREEN);
                    dicesColorSet.setDiceItemColor(dicesCount, dicesColorSelectCounter - 1, GREEN);
                }

                if (dicesCount < 2)
                    showSettingsColor(false, true);
                break;
            case R.id.viewYellow:
                Animators.animateButtonClick2(viewYellow, 1f);
                if (fragmentDices != null) {
                    fragmentDices.setDiceColor(incrementDicesColorSelectCounter(), Dice.DiceColor.YELLOW);
                    dicesColorSet.setDiceItemColor(dicesCount, dicesColorSelectCounter - 1, YELLOW);
                }

                if (dicesCount < 2)
                    showSettingsColor(false, true);
                break;
            case R.id.labelClose:
                Animators.animateButtonClick2(labelClose, 1f);

                Animators.animateHideLayoutSettings(layoutInfo1, new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        layoutInfo1.setVisibility(View.GONE);
                        layoutInfo1.setScaleX(0);
                        layoutInfo1.setScaleY(0);

                    }
                });

                PrefsUtils.hideHelp1(MainActivity.this);
                break;
        }
    }

    public void showFragmentMain() {
        fragmentMain = (FragmentMain) fragmentManager.findFragmentByTag("FragmentMain");

        if (fragmentMain == null) {
            fragmentMain = new FragmentMain();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(animShowFragment, animHideFragment, animShowFragment, animHideFragment);
            fragmentTransaction.add(R.id.container, fragmentMain, "FragmentMain");
            fragmentTransaction.addToBackStack("FragmentMain");
            fragmentTransaction.commit();
        } else {
            int beCount = fragmentManager.getBackStackEntryCount();
            if (beCount == 0) return;
            fragmentManager.popBackStack("FragmentMain", 0);
        }
    }

    public void showFragmentDices() {
        fragmentDices = (FragmentDices) fragmentManager.findFragmentByTag("FragmentDices");

        if (fragmentDices == null) {
            fragmentDices = new FragmentDices();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(animShowFragment, animHideFragment, animShowFragment, animHideFragment);
            fragmentTransaction.add(R.id.container, fragmentDices, "FragmentDices");
            fragmentTransaction.addToBackStack("FragmentDices");
            fragmentTransaction.commit();
        } else {
            int beCount = fragmentManager.getBackStackEntryCount();
            if (beCount == 0) return;
            fragmentManager.popBackStack("FragmentDices", 0);
        }
    }

    public void showFragmentInfo() {
        fragmentInfo = (FragmentInfo) fragmentManager.findFragmentByTag("FragmentInfo");

        if (fragmentInfo == null) {
            fragmentInfo = new FragmentInfo();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(animShowFragment, animHideFragment, animShowFragment, animHideFragment);
            fragmentTransaction.add(R.id.container, fragmentInfo, "FragmentInfo");
            fragmentTransaction.addToBackStack("FragmentInfo");
            fragmentTransaction.commit();
        } else {
            int beCount = fragmentManager.getBackStackEntryCount();
            if (beCount == 0) return;
            fragmentManager.popBackStack("FragmentInfo", 0);
        }
    }

    public boolean isFragmentCurrent(String name, FragmentManager fragmentManager) {
        if (fragmentManager.getBackStackEntryCount() != 0) {
            FragmentManager.BackStackEntry be = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            return be.getName().equals(name);
        }
        return false;
    }

    private void updateTheme() {
        if (isLightTheme()) {
            root.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
            imgSettings.setImageDrawable(getResources().getDrawable(R.drawable.ic_settings_dark, getTheme()));
            layoutSettings.setBackground(getResources().getDrawable(R.drawable.bg_layout_settings_dark, getTheme()));
            layoutColor.setBackground(getResources().getDrawable(R.drawable.bg_layout_settings_dark, getTheme()));
            layoutLanguage.setBackground(getResources().getDrawable(R.drawable.bg_layout_settings_dark, getTheme()));
            imgClose.setImageDrawable(getResources().getDrawable(R.drawable.ic_power_light, getTheme()));
            imgInfo.setImageDrawable(getResources().getDrawable(R.drawable.ic_info_light, getTheme()));
            imgTheme.setImageDrawable(getResources().getDrawable(R.drawable.ic_brightness_light, getTheme()));
            imgColor.setImageDrawable(getResources().getDrawable(R.drawable.ic_color_light, getTheme()));
            imgLanguage.setImageDrawable(getResources().getDrawable(R.drawable.ic_language_light, getTheme()));

            if (fragmentMain != null) fragmentMain.setColor(Theme.LIGHT);
            if (fragmentDices != null) fragmentDices.setColor(Theme.LIGHT);
            if (fragmentInfo != null) fragmentInfo.setColor(Theme.LIGHT);
        } else {
            root.setBackgroundColor(getResources().getColor(R.color.colorBackgroundDark));
            imgSettings.setImageDrawable(getResources().getDrawable(R.drawable.ic_settings, getTheme()));
            layoutSettings.setBackground(getResources().getDrawable(R.drawable.bg_layout_settings, getTheme()));
            layoutColor.setBackground(getResources().getDrawable(R.drawable.bg_layout_settings, getTheme()));
            layoutLanguage.setBackground(getResources().getDrawable(R.drawable.bg_layout_settings, getTheme()));
            imgClose.setImageDrawable(getResources().getDrawable(R.drawable.ic_power, getTheme()));
            imgInfo.setImageDrawable(getResources().getDrawable(R.drawable.ic_info, getTheme()));
            imgTheme.setImageDrawable(getResources().getDrawable(R.drawable.ic_brightness, getTheme()));
            imgColor.setImageDrawable(getResources().getDrawable(R.drawable.ic_color, getTheme()));
            imgLanguage.setImageDrawable(getResources().getDrawable(R.drawable.ic_language, getTheme()));

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
                        layoutSettingsRoot.setScaleX(1.0f);
                        layoutSettingsRoot.setScaleY(1.0f);
                    }
                });
            } else {
                layoutSettingsRoot.setScaleX(1.0f);
                layoutSettingsRoot.setScaleY(1.0f);
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
                        layoutSettingsRoot.setScaleX(0.0f);
                        layoutSettingsRoot.setScaleY(0.0f);
                        layoutSettingsRoot.setVisibility(View.GONE);
                    }
                });
            } else {
                layoutSettingsRoot.setVisibility(View.GONE);
                layoutSettingsRoot.setScaleX(0.0f);
                layoutSettingsRoot.setScaleY(0.0f);
            }
        }
    }

    public void showSettingsColor(boolean show, boolean animate) {
        if (!isFragmentCurrent("FragmentDices", fragmentManager)) return;

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
                layoutLanguage.setScaleX(1.0f);
                layoutLanguage.setVisibility(View.VISIBLE);
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
                        layoutInfo1.setScaleX(1f);
                        layoutInfo1.setScaleY(1f);
                    }
                });
            } else {
                layoutInfo1.setScaleX(1f);
                layoutInfo1.setScaleY(1f);
            }
        } else {
            help1Showed = true;

            if (animate) {
                Animators.animateHideLayoutSettings(layoutInfo1, new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        layoutInfo1.setScaleX(1f);
                        layoutInfo1.setScaleY(1f);
                        layoutInfo1.setVisibility(View.GONE);
                    }
                });
            } else {
                layoutInfo1.setScaleX(1f);
                layoutInfo1.setScaleY(1f);
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
        if (theme == Theme.LIGHT) theme = Theme.DARK;
        else theme = Theme.LIGHT;
    }

    public boolean isLightTheme() {
        return theme == Theme.LIGHT;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setLanguage(Language language) {
        this.language = language;
        PrefsUtils.updateLanguage(this, language);

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
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.itnetwork_winter_2019, getTheme()));
            labelInfo1.setText(getResources().getString(R.string.text_help_1_cz));
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.itnetwork_winter_2019_1, getTheme()));
            labelInfo1.setText(getResources().getString(R.string.text_help_1));
        }
    }
}
