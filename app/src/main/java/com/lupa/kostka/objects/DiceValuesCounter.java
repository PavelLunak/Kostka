package com.lupa.kostka.objects;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.lupa.kostka.MainActivity;
import com.lupa.kostka.R;

public class DiceValuesCounter extends View {

    MainActivity activity;

    int width;
    int height;

    Paint paintText;

    int textSize = 0;

    //Proměnné pro animaci
    ValueAnimator valueAnimator;
    ValueAnimator textSizeAnimator;
    ValueAnimator textHideAnimator;
    boolean valueAnimationIsRunning;
    boolean textSizeAnimationIsRunning;
    boolean textAlphaAnimationIsRunning;
    boolean afterRestoreInstanceState;
    //int oldValue;
    int valueToDraw;
    int textSizeToDraw = textSize;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        textSize = (int) ((float)height*0.4);
        //textSize = (int) ((float)width*0.5);

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
            paintText.setTypeface(customTypeface);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            customTypeface = ResourcesCompat.getFont(getContext(), R.font.font_christie);
            paintText.setTypeface(customTypeface);
        }

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

    private int scaleDownTextSize(int actualSize) {
        if (actualSize <= 0) return 0;
        return (int) (actualSize * 0.90);
    }

    public void setValue(boolean animate) {
        if (activity == null) return;

        if (!afterRestoreInstanceState/* && animate*/) {
            valueToDraw = activity.counter.getValue();
            if (animate) animateValueCounter();//animateTextSize();
        } else {
            if (animate) animateValueCounter();//animateTextSize();
            valueToDraw = activity.counter.getValue();
            init();
        }

        afterRestoreInstanceState = false;
    }

    public void animateTextSize() {

        //textAlphaToDraw = 255;

        if (textSizeAnimator != null) {
            textSizeAnimator.cancel();
        }

        if (textHideAnimator != null) {
            textHideAnimator.cancel();
        }

        textSizeAnimator = ValueAnimator.ofInt(0, (int)((float)textSize));

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
                textSizeToDraw = (int)((float)textSize);
                DiceValuesCounter.this.invalidate();
                textSizeAnimationIsRunning = false;
                animateTextHide();
            }

            @Override public void onAnimationCancel(Animator animator) {
                textSizeToDraw = (int)((float)textSize);
                DiceValuesCounter.this.invalidate();
                textSizeAnimationIsRunning = false;
            }

            @Override public void onAnimationRepeat(Animator animator) {}
        });

        textSizeAnimator.setInterpolator(new LinearInterpolator());
        textSizeAnimator.start();
    }

    public void animateTextHide() {

        if (textHideAnimator != null) {
            textHideAnimator.cancel();
        }

        textHideAnimator = ValueAnimator.ofInt(255, 0);
        textHideAnimator.setDuration(2000l);
        textHideAnimator.setStartDelay(100);

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
                //valueToDraw = 0;
                textAlphaToDraw = 255;
                DiceValuesCounter.this.invalidate();
                textAlphaAnimationIsRunning = false;
            }

            @Override public void onAnimationCancel(Animator animator) {
                //valueToDraw = 0;
                textAlphaToDraw = 255;
                DiceValuesCounter.this.invalidate();
                textAlphaAnimationIsRunning = false;
            }

            @Override public void onAnimationRepeat(Animator animator) {}
        });

        textHideAnimator.setInterpolator(new LinearInterpolator());
        textHideAnimator.start();
    }

    public void animateValueCounter() {
        final ValueAnimator valueAnimatorSize = ValueAnimator.ofInt(0, (int)((float)textSize));
        valueAnimatorSize.setDuration(200);
        valueAnimatorSize.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textSizeToDraw = (int) animation.getAnimatedValue();
                DiceValuesCounter.this.invalidate();
            }
        });

        final ValueAnimator valueAnimatorAlpha = ValueAnimator.ofInt(255, 0);
        //valueAnimatorAlpha.setStartDelay(100);
        valueAnimatorAlpha.setDuration(2000);
        valueAnimatorAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textAlphaToDraw = (int) animation.getAnimatedValue();
                DiceValuesCounter.this.invalidate();
            }
        });

        final AnimatorSet animatorSet = new AnimatorSet();
        //animatorSet.setDuration(500);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playSequentially(valueAnimatorSize, valueAnimatorAlpha);

        animatorSet.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animator) {
                textSizeToDraw = (int)((float)textSize);
                textSizeAnimationIsRunning = false;

                valueToDraw = 0;
                textAlphaToDraw = 255;
                textAlphaAnimationIsRunning = false;

                DiceValuesCounter.this.invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                textSizeToDraw = (int)((float)textSize);
                textSizeAnimationIsRunning = false;

                valueToDraw = 0;
                textAlphaToDraw = 255;
                textAlphaAnimationIsRunning = false;

                DiceValuesCounter.this.invalidate();
            }

            @Override public void onAnimationStart(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        });

        animatorSet.start();
    }

    public void setActivityContext(MainActivity activity) {
        this.activity = activity;
    }
}
