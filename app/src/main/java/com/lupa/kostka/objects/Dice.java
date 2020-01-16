package com.lupa.kostka.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.lupa.kostka.R;
import com.lupa.kostka.utils.AppConstants;

import java.util.Random;

public class Dice extends View {

    public enum DiceColor {BLACK, WHITE, RED, BLUE, GREEN, YELLOW}
    DiceColor diceColor = DiceColor.BLACK;

    int width;                          //Šířka prostoru pro vykreslení kostky
    int height;                         //Výška prostoru pro vykreslení kostky

    int centerX;                        //Střed kostky (souřadnice X)
    int centerY;                        //Střed kostky (souřadnice Y)

    int diceEdgeLength;                 //Délka hrany kostky
    int sideCircleRadius;               //Poloměr kruhové části, ve které jsou tečky
    int sideCircleSeenRadius;           //Poloměr kruhové části pro lesk
    int diagonal;                       //Úhlopříčka strany kostky
    int diceMargin;                     //Minimální odsazení kostky od okrajů
    int leftRightPointIndent;           //Odsazení teček vlevo a vpravo
    int topBottomPointIndent;           //Odsazení teček nahoře a dole

    int pointRadius;                    //Poloměr teček
    Point[] points;                     //Pole teček pro kostku

    private Paint paintDice = new Paint();          //Hlavní barva kostky
    private Paint paintDice2 = new Paint();         //Barva kostky s gradientem do stínu (rohy)
    private Paint paintPointSheen = new Paint();    //Lesk
    private Paint paintCircleSheen = new Paint();   //Lesk

    int number = 6;                     //Zobrazená hodnota kostky
    CountDownTimer countDownTimer;      //Odpočet do konce míchání kostky
    boolean isShuffle;                  //TRUE = právě probíhá míchání kostky

    OnDiceShuffledListener listener;    //listener ukončení míchání kostky


    public Dice(Context context) {
        super(context);
        setNumber(6);
    }

    public Dice(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setNumber(6);
    }

    public Dice(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setNumber(6);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Dice(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setNumber(6);
    }

    /*
    Pozice teček podle indexů v proměnné points
    0     4
    1  3  5
    2     6
    */
    private void initPoints() {
        points = new Point[7];
        points[0] = new Point(
                centerX - (diceEdgeLength / 2) + pointRadius + leftRightPointIndent,
                centerY - (diceEdgeLength / 2) + pointRadius + topBottomPointIndent);

        points[1] = new Point(
                centerX - (diceEdgeLength / 2) + pointRadius + leftRightPointIndent,
                centerY);

        points[2] = new Point(
                centerX - (diceEdgeLength / 2) + pointRadius + leftRightPointIndent,
                centerY + (diceEdgeLength / 2) - pointRadius - topBottomPointIndent);

        points[3] = new Point(centerX, centerY);

        points[4] = new Point(
                centerX + (diceEdgeLength / 2) - pointRadius - leftRightPointIndent,
                centerY - (diceEdgeLength / 2) + pointRadius + topBottomPointIndent);

        points[5] = new Point(
                centerX + (diceEdgeLength / 2) - pointRadius - leftRightPointIndent,
                centerY);

        points[6] = new Point(
                centerX + (diceEdgeLength / 2) - pointRadius - leftRightPointIndent,
                centerY + (diceEdgeLength / 2) - pointRadius - topBottomPointIndent);
    }

    private void showPoints(Canvas canvas, int number) {
        //Zobrazení příslušných teček podle hodnoty kostky
        switch (number) {
            case 1:
                showPoint(canvas, points[3]);
                break;
            case 2:
                showPoint(canvas, points[2]);
                showPoint(canvas, points[4]);
                break;
            case 3:
                showPoint(canvas, points[0]);
                showPoint(canvas, points[3]);
                showPoint(canvas, points[6]);
                break;
            case 4:
                showPoint(canvas, points[0]);
                showPoint(canvas, points[2]);
                showPoint(canvas, points[4]);
                showPoint(canvas, points[6]);
                break;
            case 5:
                showPoint(canvas, points[0]);
                showPoint(canvas, points[2]);
                showPoint(canvas, points[3]);
                showPoint(canvas, points[4]);
                showPoint(canvas, points[6]);
                break;
            default:
                for (int i = 0; i < points.length; i++) {
                    if (i == 3) continue;
                    showPoint(canvas, points[i]);
                }
        }
    }

    //Zobrazí jednu tečku zadanou v parametru
    private void showPoint(Canvas canvas, Point point) {
        paintPointSheen.setAntiAlias(true);
        paintPointSheen.setStyle(Paint.Style.FILL);

        paintPointSheen.setShader(new RadialGradient(
                point.getX(),
                point.getY(),
                pointRadius,
                new int[]{
                        getPointStartColorRes(diceColor),
                        getPointEndColorRes(diceColor),
                        getPointStartColorRes(diceColor)},
                new float[]{
                        0.3f,
                        0.95f,
                        1.0f},
                Shader.TileMode.CLAMP));

        RectF rectFpoint = new RectF(
                point.getX() - pointRadius,
                point.getY() - pointRadius,
                point.getX() + pointRadius,
                point.getY() + pointRadius);

        canvas.drawArc(rectFpoint, 0, 360, true, paintPointSheen);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        int lessSize = Math.min(width, height);

        centerX = width / 2;
        centerY = height / 2;
        diceMargin = (int) ((float) lessSize * 0.1f);
        diceEdgeLength = Math.min(width, height) - (2 * diceMargin);
        diagonal = (int) (Math.sqrt(Math.pow((double) diceEdgeLength / 2, 2) + Math.pow((double) diceEdgeLength / 2, 2)));
        pointRadius = (int) ((float) diceEdgeLength * 0.1);
        leftRightPointIndent = (int) ((float) pointRadius * 2 * 0.9f);
        topBottomPointIndent = (int) ((float) pointRadius * 2 * 0.9f);
        sideCircleRadius = (int) ((float) diceEdgeLength / 2 * 0.85);
        sideCircleSeenRadius = (int) ((float) sideCircleRadius * 1.1);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paintCircleSheen.setAntiAlias(true);
        paintCircleSheen.setStyle(Paint.Style.FILL);

        paintCircleSheen.setShader(new LinearGradient(
                centerX,
                centerY,
                centerX + sideCircleSeenRadius,
                centerY,
                new int[]{
                        getDiceColor1(diceColor),
                        getContext().getResources().getColor(R.color.colorWhite)},
                new float[]{
                        0.7f,
                        1f},
                Shader.TileMode.CLAMP));

        paintDice.setColor(getDiceColor1(diceColor));
        paintDice.setAntiAlias(true);
        paintDice.setStyle(Paint.Style.FILL);

        paintDice2.setAntiAlias(true);
        paintDice2.setStyle(Paint.Style.FILL);

        paintDice2.setShader(new RadialGradient(
                centerX,
                centerY,
                diagonal,
                new int[]{
                        getDiceColor1(diceColor),
                        getDiceColor2(diceColor)},
                new float[]{
                        0.6f,
                        1f},
                Shader.TileMode.REPEAT));

        initPoints();

        RectF rectf= new RectF(
                centerX - (diceEdgeLength / 2),
                centerY - (diceEdgeLength / 2),
                centerX + (diceEdgeLength / 2),
                centerY + (diceEdgeLength / 2));
        canvas.drawRoundRect(rectf,(int)(((float)diceEdgeLength)*0.2), (int)(((float)diceEdgeLength)*0.2), paintDice2);

        canvas.drawCircle(centerX, centerY, sideCircleSeenRadius, paintCircleSheen);
        canvas.drawCircle(centerX, centerY, sideCircleRadius, paintDice);

        showPoints(canvas, this.number);
    }

    public void startShuffle() {
        final Random random = new Random();
        isShuffle = true;

        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(random.nextInt(AppConstants.MAX_SHUFFLE_DELAY) + 10, 10) {
            @Override
            public void onTick(long l) {
                number = random.nextInt(6) + 1;
                setNumber(number);
            }

            @Override
            public void onFinish() {
                if (isShuffle) {
                    startShuffle();
                } else {
                    setNumber(number);
                    countDownTimer = null;

                    if (listener != null)
                        listener.onDiceShuffled(getId(), number);
                }
            }
        };

        countDownTimer.start();
    }

    public void stopShuffle() {
        isShuffle = false;
    }

    private int getPointStartColorRes(Dice.DiceColor color) {
        if (color == DiceColor.WHITE || color == DiceColor.YELLOW) {
            return getContext().getResources().getColor(R.color.pointColorBlackStart);
        } else {
            return getContext().getResources().getColor(R.color.pointColorWhiteStart);
        }
    }

    private int getPointEndColorRes(Dice.DiceColor color) {
        if (color == DiceColor.WHITE || color == DiceColor.YELLOW) {
            return getContext().getResources().getColor(R.color.pointColorBlackEnd);
        } else {
            return getContext().getResources().getColor(R.color.pointColorWhiteEnd);
        }
    }

    private int getDiceColor1(Dice.DiceColor color) {
        switch (color) {
            case BLACK:
                return getContext().getResources().getColor(R.color.diceColorBlack);
            case WHITE:
                return getContext().getResources().getColor(R.color.diceColorWhite);
            case RED:
                return getContext().getResources().getColor(R.color.diceColorRed);
            case BLUE:
                return getContext().getResources().getColor(R.color.diceColorBlue);
            case GREEN:
                return getContext().getResources().getColor(R.color.diceColorGreen);
            case YELLOW:
                return getContext().getResources().getColor(R.color.diceColorYellow);
        }

        return getContext().getResources().getColor(R.color.diceColorBlack);
    }

    private int getDiceColor2(Dice.DiceColor color) {
        switch (color) {
            case BLACK:
                return getContext().getResources().getColor(R.color.diceColorBlack2);
            case WHITE:
                return getContext().getResources().getColor(R.color.diceColorWhite2);
            case RED:
                return getContext().getResources().getColor(R.color.diceColorRed2);
            case BLUE:
                return getContext().getResources().getColor(R.color.diceColorBlue2);
            case GREEN:
                return getContext().getResources().getColor(R.color.diceColorGreen2);
            case YELLOW:
                return getContext().getResources().getColor(R.color.diceColorYellow2);
        }

        return getContext().getResources().getColor(R.color.diceColorBlack2);
    }

    public void setOnDiceShuffledListener(OnDiceShuffledListener listener) {
        this.listener = listener;
    }

    public void setNumber(int number) {
        this.number = number;
        invalidate();
    }

    public void setDiceColor(DiceColor diceColor) {
        this.diceColor = diceColor;
        invalidate();
    }

    public void setDiceColor(int diceColor) {
        this.diceColor = colorFromInt(diceColor);
        invalidate();
    }

    int diceColorToInt() {
        switch (diceColor) {
            case BLACK:
                return 1;
            case WHITE:
                return 2;
            case RED:
                return 3;
            case BLUE:
                return 4;
            case GREEN:
                return 5;
            case YELLOW:
                return 6;
            default:
                return 1;
        }
    }

    private DiceColor colorFromInt(int i) {
        switch (i) {
            case 1:
                return DiceColor.BLACK;
            case 2:
                return DiceColor.WHITE;
            case 3:
                return DiceColor.RED;
            case 4:
                return DiceColor.BLUE;
            case 5:
                return DiceColor.GREEN;
            case 6:
                return DiceColor.YELLOW;
            default:
                return DiceColor.BLACK;
        }
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState myState = new SavedState(superState);
        myState.value = this.number;
        myState.color = diceColorToInt();
        return myState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setNumber(savedState.value);
        setDiceColor(colorFromInt(savedState.color));
    }

    //----------------------------------------------------------------------------------------------

    private static class SavedState extends BaseSavedState {
        int value;
        int color;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            value = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(value);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public interface OnDiceShuffledListener {
        void onDiceShuffled(int diceId, int value);
    }
}
