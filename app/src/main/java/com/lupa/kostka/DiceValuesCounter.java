package com.lupa.kostka;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class DiceValuesCounter extends View {

    MainActivity activity;

    int width;
    int height;

    Paint paintText;
    Paint paintTextShadow;
    Paint paintParticle;

    int textSize = 0;

    //Proměnné pro animaci
    ValueAnimator valueAnimator;
    ValueAnimator valueShadowAnimator;
    ValueAnimator textSizeAnimator;
    ValueAnimator textShadowSizeAnimator;
    ValueAnimator textHideAnimator;
    boolean valueAnimationIsRunning;
    boolean textSizeAnimationIsRunning;
    boolean textShadowSizeAnimationIsRunning;
    boolean textAlphaAnimationIsRunning;
    boolean animationDisabled;
    boolean afterRestoreInstanceState;
    int oldValue;
    int valueToDraw;
    int textSizeToDraw = textSize;
    int textShadowSizeToDraw = textSize;
    int textAlphaToDraw = 255;

    public DiceValuesCounter(Context context) {
        super(context);
    }

    public DiceValuesCounter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DiceValuesCounter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DiceValuesCounter(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        setSaveEnabled(true);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        textSize = (int) ((float)height*0.5);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
        //drawTextShadow(canvas);
        //drawParticle(canvas);
    }

    private void drawText(Canvas canvas) {

        if (valueToDraw == 0) return;

        String text = String.valueOf(valueToDraw);

        paintText = new Paint();
        paintText.setStyle(Paint.Style.FILL);
        paintText.setColor(getContext().getResources().getColor(R.color.colorCounter));
        paintText.setTextSize(textSizeToDraw);
        paintText.setAntiAlias(true);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setAlpha(textAlphaToDraw);

        Typeface customTypeface;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            customTypeface = getResources().getFont(R.font.font_christie);
        } else{
            customTypeface = ResourcesCompat.getFont(getContext(), R.font.font_christie);
        }

        paintText.setTypeface(customTypeface);

        Rect bounds = new Rect();
        paintText.getTextBounds(text, 0, text.length(), bounds);

        float textWidth = paintText.measureText(text);
        int maxTextWidth = width;

        int yPos = (int) ((height/2) - ((paintText.descent() + paintText.ascent()) / 2)) ;

        if (textWidth > maxTextWidth) {
            int newTextSize = textSizeToDraw;
            while (textWidth > maxTextWidth) {
                newTextSize = scaleDownTextSize(newTextSize);
                paintText.setTextSize(newTextSize);
                textWidth = paintText.measureText(text);
                yPos = (int) ((height / 2) - ((paintText.descent() + paintText.ascent()) / 2)) ;
            }
        } else {
            paintText.setTextSize(textSizeToDraw);
        }

        canvas.drawText(text, width / 2, yPos, paintText);
    }

    private void drawTextShadow(Canvas canvas) {

        if (valueToDraw == 0) return;

        String text = String.valueOf(valueToDraw);

        paintTextShadow = new Paint();
        paintTextShadow.setStyle(Paint.Style.FILL);
        paintTextShadow.setColor(getContext().getResources().getColor(R.color.colorCounterShadow));
        paintTextShadow.setTextSize(textShadowSizeToDraw);
        paintTextShadow.setAntiAlias(true);
        paintTextShadow.setTextAlign(Paint.Align.CENTER);

        Typeface customTypeface;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            customTypeface = getResources().getFont(R.font.font_christie);
        } else{
            customTypeface = ResourcesCompat.getFont(getContext(), R.font.font_christie);
        }

        paintTextShadow.setTypeface(customTypeface);

        Rect bounds = new Rect();
        paintTextShadow.getTextBounds(text, 0, text.length(), bounds);

        float textWidth = paintTextShadow.measureText(text);
        int maxTextWidth = width;

        int yPos = (int) ((height/2) - ((paintTextShadow.descent() + paintTextShadow.ascent()) / 2)) ;

        /*
        if (textWidth > maxTextWidth) {
            int newTextSize = textSizeToDraw;
            while (textWidth > maxTextWidth) {
                newTextSize = scaleDownTextSize(newTextSize);
                paintTextShadow.setTextSize(newTextSize);
                textWidth = paintTextShadow.measureText(text);
                yPos = (int) ((height / 2) - ((paintTextShadow.descent() + paintTextShadow.ascent()) / 2)) ;
            }
        } else {
            paintTextShadow.setTextSize(textSizeToDraw);
        }
        */
        paintTextShadow.setTextSize(textShadowSizeToDraw);

        canvas.drawText(text, width / 2, yPos, paintTextShadow);
    }

    private void drawParticle(Canvas canvas) {
        paintParticle = new Paint();
        paintParticle.setStyle(Paint.Style.FILL);
        paintParticle.setColor(Color.RED);
        paintParticle.setAntiAlias(true);

        int particleWidth = 100;
        int particleHeight = 30;

        canvas.drawRect(
                width/2 - particleWidth/2,
                height/2 - particleHeight/2,
                width/2 + particleWidth/2,
                height/2 + particleHeight/2,
                paintParticle);
    }

    private int scaleDownTextSize(int actualSize) {
        if (actualSize <= 0) return 0;
        return (int) (actualSize * 0.90);
    }

    public void setValue(int newValue, boolean animate) {
        if (activity == null) return;

        //Nastavení počáteční hodnoty pro animaci po změně hodnoty v grafu.
        if (valueAnimationIsRunning) {
            //Během změny hodnoty ještě běží animace z předchozí změny hodnoty
            oldValue = valueToDraw;
        } else  {
            //Animace neběží
            oldValue = activity.counter.getValue();
        }

        //value = newValue;

        //Není-li animace zakázána uživatelsky a nedšlo-li, například, k otočení telefonu,
        //bude se nárůst (pokles) hodnoty animovat.
        if (!animationDisabled && !afterRestoreInstanceState && animate) {
            //animateValueShadow();
            animateValue();
            //animateTextShadowSize1();
            animateTextSize1();
        } else {
            valueToDraw = activity.counter.getValue();
            init();
        }

        afterRestoreInstanceState = false;
    }

    public void addValue(int value) {
        if (activity == null) return;
        activity.counter.add(value);
        setValue(activity.counter.getValue(), true);
    }

    public void animateValue() {

        if (valueAnimator != null) {
            valueAnimator.cancel();
        }

        valueAnimator = ValueAnimator.ofInt(this.oldValue, /*this.value*/activity.counter.getValue());
        valueAnimator.setDuration(200l);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                valueToDraw = (int) animation.getAnimatedValue();
                DiceValuesCounter.this.invalidate();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {
                valueAnimationIsRunning = true;
            }

            @Override public void onAnimationEnd(Animator animator) {
                valueToDraw = activity.counter.getValue();
                DiceValuesCounter.this.invalidate();
                valueAnimationIsRunning = false;
            }

            @Override public void onAnimationCancel(Animator animator) {
                valueAnimationIsRunning = false;
            }

            @Override public void onAnimationRepeat(Animator animator) {}
        });

        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    public void animateValueShadow() {

        if (valueShadowAnimator != null) {
            valueShadowAnimator.cancel();
        }

        valueShadowAnimator = ValueAnimator.ofInt(this.oldValue, activity.counter.getValue());
        valueShadowAnimator.setDuration(200l);

        valueShadowAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                valueToDraw = (int) animation.getAnimatedValue();
                DiceValuesCounter.this.invalidate();
            }
        });

        valueShadowAnimator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {
                valueAnimationIsRunning = true;
            }

            @Override public void onAnimationEnd(Animator animator) {
                valueToDraw = activity.counter.getValue();
                DiceValuesCounter.this.invalidate();
                valueAnimationIsRunning = false;
            }

            @Override public void onAnimationCancel(Animator animator) {
                valueAnimationIsRunning = false;
            }

            @Override public void onAnimationRepeat(Animator animator) {}
        });

        valueShadowAnimator.setInterpolator(new LinearInterpolator());
        valueShadowAnimator.start();
    }

    public void animateTextSize1() {

        if (textSizeAnimator != null) {
            textSizeAnimator.cancel();
        }

        textSizeAnimator = ValueAnimator.ofInt(textSize, (int)((float)textSize*1.25));
        textSizeAnimator.setDuration(100l);

        textSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textSizeToDraw = (int) animation.getAnimatedValue();
                DiceValuesCounter.this.invalidate();
            }
        });

        textSizeAnimator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {
                textSizeAnimationIsRunning = true;
            }

            @Override public void onAnimationEnd(Animator animator) {
                textSizeToDraw = (int)((float)textSize*1.25);
                DiceValuesCounter.this.invalidate();
                textSizeAnimationIsRunning = false;
                //animateTextSize2();
                animateTextHide();
            }

            @Override public void onAnimationCancel(Animator animator) {
                textSizeToDraw = (int)((float)textSize*1.25);
                DiceValuesCounter.this.invalidate();
                textSizeAnimationIsRunning = false;
            }

            @Override public void onAnimationRepeat(Animator animator) {}
        });

        textSizeAnimator.setInterpolator(new LinearInterpolator());
        textSizeAnimator.start();
    }

    public void animateTextShadowSize1() {

        if (textShadowSizeAnimator != null) {
            textShadowSizeAnimator.cancel();
        }

        textShadowSizeAnimator = ValueAnimator.ofInt(textSize, (int)((float)textSize*5));
        textShadowSizeAnimator.setDuration(500l);

        textShadowSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textShadowSizeToDraw = (int) animation.getAnimatedValue();
                DiceValuesCounter.this.invalidate();
            }
        });

        textShadowSizeAnimator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {
                textShadowSizeAnimationIsRunning = true;
            }

            @Override public void onAnimationEnd(Animator animator) {
                /*
                Při ukončení animace pro jistotu uděláme jeden krok navíc,
                protože při animaci větších hodnot dochází, díky práci s desetinnými čísli,
                k malým odchylkám díky zaokrouhlování
                */

                textShadowSizeToDraw = (int)((float)textSize*5);
                DiceValuesCounter.this.invalidate();
                textShadowSizeAnimationIsRunning = false;
                //animateTextShadowSize2();
            }

            @Override public void onAnimationCancel(Animator animator) {
                textShadowSizeToDraw = (int)((float)textSize*5);
                DiceValuesCounter.this.invalidate();
                textShadowSizeAnimationIsRunning = false;
            }

            @Override public void onAnimationRepeat(Animator animator) {}
        });

        textShadowSizeAnimator.setInterpolator(new LinearInterpolator());
        textShadowSizeAnimator.start();
    }

    public void animateTextSize2() {

        if (textSizeAnimator != null) {
            textSizeAnimator.cancel();
        }

        textSizeAnimator = ValueAnimator.ofInt((int)((float)textSize*1.25), textSize);
        textSizeAnimator.setDuration(50l);

        textSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textSizeToDraw = (int) animation.getAnimatedValue();
                DiceValuesCounter.this.invalidate();
            }
        });

        textSizeAnimator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {
                textSizeAnimationIsRunning = true;
            }

            @Override public void onAnimationEnd(Animator animator) {
                textSizeToDraw = textSize;
                DiceValuesCounter.this.invalidate();
                textSizeAnimationIsRunning = false;
                animateTextHide();
            }

            @Override public void onAnimationCancel(Animator animator) {
                textSizeToDraw = textSize;
                DiceValuesCounter.this.invalidate();
                textSizeAnimationIsRunning = false;
            }

            @Override public void onAnimationRepeat(Animator animator) {}
        });

        textSizeAnimator.setInterpolator(new LinearInterpolator());
        textSizeAnimator.start();
    }

    public void animateTextShadowSize2() {

        if (textShadowSizeAnimator != null) {
            textShadowSizeAnimator.cancel();
        }

        textShadowSizeAnimator = ValueAnimator.ofInt((int)((float)textSize*1.25), (int) ((float)textSize*500f));
        textShadowSizeAnimator.setDuration(500l);

        textShadowSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textShadowSizeToDraw = (int) animation.getAnimatedValue();
                DiceValuesCounter.this.invalidate();
            }
        });

        textShadowSizeAnimator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {
                textShadowSizeAnimationIsRunning = true;
            }

            @Override public void onAnimationEnd(Animator animator) {
                textShadowSizeToDraw = textSize*500;
                DiceValuesCounter.this.invalidate();
                textShadowSizeAnimationIsRunning = false;
            }

            @Override public void onAnimationCancel(Animator animator) {
                textShadowSizeToDraw = textSize*500;
                DiceValuesCounter.this.invalidate();
                textShadowSizeAnimationIsRunning = false;
            }

            @Override public void onAnimationRepeat(Animator animator) {}
        });

        textShadowSizeAnimator.setInterpolator(new LinearInterpolator());
        textShadowSizeAnimator.start();
    }

    public void animateTextHide() {

        if (textHideAnimator != null) {
            textHideAnimator.cancel();
        }

        textHideAnimator = ValueAnimator.ofInt(255, 0);
        textHideAnimator.setDuration(2000l);

        textHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textAlphaToDraw = (int) animation.getAnimatedValue();
                DiceValuesCounter.this.invalidate();
            }
        });

        textHideAnimator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {
                textAlphaAnimationIsRunning = true;
            }

            @Override public void onAnimationEnd(Animator animator) {
                textAlphaToDraw = 0;
                DiceValuesCounter.this.invalidate();
                textAlphaAnimationIsRunning = false;
            }

            @Override public void onAnimationCancel(Animator animator) {
                textAlphaToDraw = 0;
                DiceValuesCounter.this.invalidate();
                textAlphaAnimationIsRunning = false;
            }

            @Override public void onAnimationRepeat(Animator animator) {}
        });

        textHideAnimator.setInterpolator(new LinearInterpolator());
        textHideAnimator.start();
    }

    public void setActivityContext(MainActivity activity) {
        this.activity = activity;
    }

    //----------------------------------------------------------------------------------------------

    /*
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState myState = new SavedState(superState);
        myState.textSizeToDraw = textSizeToDraw;
        return myState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        afterRestoreInstanceState = true;
        textSizeToDraw = savedState.textSizeToDraw;
    }

    //----------------------------------------------------------------------------------------------

    private static class SavedState extends BaseSavedState {
        int textSizeToDraw;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            textSizeToDraw = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(textSizeToDraw);
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
    */
}
