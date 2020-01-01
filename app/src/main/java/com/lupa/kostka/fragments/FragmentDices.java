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

import com.lupa.kostka.utils.Animators;
import com.lupa.kostka.objects.Dice;
import com.lupa.kostka.objects.DiceValuesCounter;
import com.lupa.kostka.MainActivity;
import com.lupa.kostka.R;


public class FragmentDices extends Fragment implements View.OnClickListener {

    MainActivity activity;

    View view;
    RelativeLayout root;
    Dice dice1, dice2, dice3, dice4, dice5, dice6;
    DiceValuesCounter valueCounter;

    Dice.OnDiceShuffledListener onDiceShuffledListener = new Dice.OnDiceShuffledListener() {
        @Override
        public void onDiceShuffled(int diceId, int value) {
            if (valueCounter != null) {
                activity.counter.add(value);

                if (activity.counter.getValuesCount() == activity.dicesCount) {
                    valueCounter.setValue(true);
                }
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity)
            activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        switch (activity.dicesCount) {
            case 1:
                view = inflater.inflate(R.layout.fragment_one, container, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.fragment_two, container, false);
                break;
            case 3:
                view = inflater.inflate(R.layout.fragment_three, container, false);
                break;
            case 4:
                view = inflater.inflate(R.layout.fragment_four, container, false);
                break;
            case 5:
                view = inflater.inflate(R.layout.fragment_five, container, false);
                break;
            case 6:
                view = inflater.inflate(R.layout.fragment_six, container, false);
                break;
        }

        root = view.findViewById(R.id.root);
        dice1 = view.findViewById(R.id.dice1);

        if (activity.dicesCount > 1) {
            dice2 = view.findViewById(R.id.dice2);
            if (activity.dicesCount > 2) {
                dice3 = view.findViewById(R.id.dice3);
                if (activity.dicesCount > 3) {
                    dice4 = view.findViewById(R.id.dice4);
                    if (activity.dicesCount > 4) {
                        dice5 = view.findViewById(R.id.dice5);
                        if (activity.dicesCount > 5) {
                            dice6 = view.findViewById(R.id.dice6);
                        }
                    }
                }
            }
        }

        if (activity.dicesCount > 1) {
            valueCounter = view.findViewById(R.id.valueCounter);
            valueCounter.setActivityContext(activity);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        dice1.setOnDiceShuffledListener(onDiceShuffledListener);
        dice1.setDiceColor(activity.dicesColorSet.getDiceItemColor(activity.dicesCount, 1));

        if (activity.dicesCount > 1) {
            dice2 = view.findViewById(R.id.dice2);
            dice2.setOnDiceShuffledListener(onDiceShuffledListener);
            dice2.setDiceColor(activity.dicesColorSet.getDiceItemColor(activity.dicesCount, 2));
            if (activity.dicesCount > 2) {
                dice3 = view.findViewById(R.id.dice3);
                dice3.setOnDiceShuffledListener(onDiceShuffledListener);
                dice3.setDiceColor(activity.dicesColorSet.getDiceItemColor(activity.dicesCount, 3));
                if (activity.dicesCount > 3) {
                    dice4 = view.findViewById(R.id.dice4);
                    dice4.setOnDiceShuffledListener(onDiceShuffledListener);
                    dice4.setDiceColor(activity.dicesColorSet.getDiceItemColor(activity.dicesCount, 4));
                    if (activity.dicesCount > 4) {
                        dice5 = view.findViewById(R.id.dice5);
                        dice5.setOnDiceShuffledListener(onDiceShuffledListener);
                        dice5.setDiceColor(activity.dicesColorSet.getDiceItemColor(activity.dicesCount, 5));
                        if (activity.dicesCount > 5) {
                            dice6 = view.findViewById(R.id.dice6);
                            dice6.setOnDiceShuffledListener(onDiceShuffledListener);
                            dice6.setDiceColor(activity.dicesColorSet.getDiceItemColor(activity.dicesCount, 6));
                        }
                    }
                }
            }
        }

        Animators.animateDice(dice1);

        if (activity.dicesCount > 1) {
            Animators.animateDice(dice2);
            if (activity.dicesCount > 2) {
                Animators.animateDice(dice3);
                if (activity.dicesCount > 3) {
                    Animators.animateDice(dice4);
                    if (activity.dicesCount > 4) {
                        Animators.animateDice(dice5);
                        if (activity.dicesCount > 5) {
                            Animators.animateDice(dice6);
                        }
                    }
                }
            }
        }

        setColor(activity.theme);
    }

    @Override
    public void onClick(View view) {
        if (activity.settingsColorShowed) activity.showSettingsColor(false, true);
        if (activity.settingsShowed) activity.showSettings(false, true);
    }

    public void setColor(MainActivity.Theme theme) {
        root.setBackgroundColor(
                activity.getResources().getColor(theme == MainActivity.Theme.LIGHT ?
                        R.color.colorBackgroundLight :
                        R.color.colorBackgroundDark));
    }

    public void startShuffle() {
        if (valueCounter != null) valueCounter.setValue(false);

        dice1.startShuffle();

        if (activity.dicesCount > 1) {
            dice2.startShuffle();
            if (activity.dicesCount > 2) {
                dice3.startShuffle();
                if (activity.dicesCount > 3) {
                    dice4.startShuffle();
                    if (activity.dicesCount > 4) {
                        dice5.startShuffle();
                        if (activity.dicesCount > 5) {
                            dice6.startShuffle();
                        }
                    }
                }
            }
        }
    }

    public void stopShuffle() {
        if (valueCounter != null) valueCounter.setValue(false);

        dice1.stopShuffle();

        if (activity.dicesCount > 1) {
            dice2.stopShuffle();
            if (activity.dicesCount > 2) {
                dice3.stopShuffle();
                if (activity.dicesCount > 3) {
                    dice4.stopShuffle();
                    if (activity.dicesCount > 4) {
                        dice5.stopShuffle();
                        if (activity.dicesCount > 5) {
                            dice6.stopShuffle();
                        }
                    }
                }
            }
        }
    }

    public void setCounter(boolean animate) {
        if (valueCounter == null) return;
        valueCounter.setValue(animate);
    }

    public void setDiceColor(int diceNumber, Dice.DiceColor color) {
        if (diceNumber > activity.dicesCount) return;

        switch (diceNumber) {
            case 1:
                dice1.setDiceColor(color);
                break;
            case 2:
                dice2.setDiceColor(color);
                break;
            case 3:
                dice3.setDiceColor(color);
                break;
            case 4:
                dice4.setDiceColor(color);
                break;
            case 5:
                dice5.setDiceColor(color);
                break;
            case 6:
                dice6.setDiceColor(color);
                break;
        }
    }
}
