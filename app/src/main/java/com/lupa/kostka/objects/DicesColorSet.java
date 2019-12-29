package com.lupa.kostka.objects;


import android.util.Log;

public class DicesColorSet {

    private final int BLACK = 1;
    private final int WHITE = 2;
    private final int RED = 3;
    private final int BLUE = 4;
    private final int GREEN = 5;
    private final int YELLOW = 6;

    //-----------------------------------------------------------------

    private int oneDice;
    private int[] twoDice;
    private int[] threeDice;
    private int[] fourDice;
    private int[] fiveDice;
    private int[] sixDice;

    //-----------------------------------------------------------------

    public DicesColorSet() {
        this.oneDice = BLACK;
        this.twoDice = new int[] {BLACK, BLACK};
        this.threeDice = new int[] {BLACK, BLACK, BLACK};
        this.fourDice = new int[] {BLACK, BLACK, BLACK, BLACK};
        this.fiveDice = new int[] {BLACK, BLACK, BLACK, BLACK, BLACK};
        this.sixDice = new int[] {BLACK, BLACK, BLACK, BLACK, BLACK, BLACK};
    }

    //-----------------------------------------------------------------

    public void setDiceItemColor(int dicesCount, int diceOrder, int colorCode) {
        if (dicesCount == 1) {
            oneDice = colorCode;
        } else {
            int[] colorSetToUpdate = twoDice;

            switch (dicesCount) {
                case 3:
                    colorSetToUpdate = threeDice;
                    break;
                case 4:
                    colorSetToUpdate = fourDice;
                    break;
                case 5:
                    colorSetToUpdate = fiveDice;
                    break;
                case 6:
                    colorSetToUpdate = sixDice;
                    break;
            }

            if (diceOrder >= 0 && diceOrder < colorSetToUpdate.length)
                colorSetToUpdate[diceOrder] = colorCode;
        }
    }

    public int getDiceItemColor(int dicesCount, int diceOrder) {
        if (dicesCount == 1) {
            return oneDice;
        } else {
            int[] colorSetToReturn = twoDice;

            switch (dicesCount) {
                case 3:
                    colorSetToReturn = threeDice;
                    break;
                case 4:
                    colorSetToReturn = fourDice;
                    break;
                case 5:
                    colorSetToReturn = fiveDice;
                    break;
                case 6:
                    colorSetToReturn = sixDice;
                    break;
            }

            return colorSetToReturn[diceOrder - 1];
        }
    }

    //-----------------------------------------------------------------

    public String getDiceColorSetToString(int diceCount) {
        StringBuilder sb = new StringBuilder("");
        int[] diceColorSet = getDiceColorSetByDiceCount(diceCount);

        for (int i = 0; i < diceColorSet.length; i++) {
            sb.append(diceColorSet[i]).append(",");
        }

        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public void setDiceColorSetFromString(String colorCodesString) {
        if (colorCodesString == null) return;
        if (colorCodesString.equals("")) return;

        String[] separated = colorCodesString.split(",");
        if (separated.length < 1 || separated.length > 6) return;

        try {
            if (separated.length == 1) {
                oneDice = Integer.parseInt(separated[0]);
            } else {
                int[] colorSetToUpdate = twoDice;

                switch (separated.length) {
                    case 3:
                        colorSetToUpdate = threeDice;
                        break;
                    case 4:
                        colorSetToUpdate = fourDice;
                        break;
                    case 5:
                        colorSetToUpdate = fiveDice;
                        break;
                    case 6:
                        colorSetToUpdate = sixDice;
                        break;
                }

                for (int i = 0; i < separated.length; i ++) {
                    colorSetToUpdate[i] = Integer.parseInt(separated[i]);
                }
            }
        } catch (NumberFormatException e) {
            Log.d("color set error", e.getMessage());
        }
    }

    private int[] getDiceColorSetByDiceCount(int diceCount) {
        switch (diceCount) {
            case 2:
                return twoDice;
            case 3:
                return threeDice;
            case 4:
                return fourDice;
            case 5:
                return fiveDice;
            case 6:
                return sixDice;
            default:
                return new int[]{oneDice};
        }
    }
}
