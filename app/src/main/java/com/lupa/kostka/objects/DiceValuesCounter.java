package com.lupa.kostka.objects;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lupa.kostka.MainActivity;
import com.lupa.kostka.R;

public class DiceValuesCounter extends View {

    MainActivity activity;

    int width;
    int height;

    Paint paintFillText;
    Paint paintStrokeText;
    int textSize = 0;
    int textPositionY;

    //Proměnné pro animaci
    ValueAnimator valueAnimatorAlpha;
    boolean textSizeAnimationIsRunning;
    boolean textAlphaAnimationIsRunning;
    boolean afterRestoreInstanceState;
    int valueToDraw;
    int textAlphaToDraw = 0;

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
        textSize = (int) ((float)height*0.9);

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

        paintFillText = new Paint();
        paintFillText.setStyle(Paint.Style.FILL);
        paintFillText.setColor(getContext().getResources().getColor(R.color.colorCounter));
        paintFillText.setTextSize(textSize);
        paintFillText.setAntiAlias(true);
        paintFillText.setTextAlign(Paint.Align.CENTER);
        paintFillText.setAlpha(textAlphaToDraw);
        paintFillText.setTextSize(textSize);

        paintStrokeText = new Paint();
        paintStrokeText.setStyle(Paint.Style.STROKE);
        paintStrokeText.setStrokeWidth(10);
        paintStrokeText.setColor(Color.BLACK);
        paintStrokeText.setTextSize(textSize);
        paintStrokeText.setAntiAlias(true);
        paintStrokeText.setTextAlign(Paint.Align.CENTER);
        paintStrokeText.setAlpha(textAlphaToDraw);
        paintStrokeText.setTextSize(textSize);

        Typeface customTypeface;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            customTypeface = getResources().getFont(R.font.font_christie);
            paintFillText.setTypeface(customTypeface);
            paintStrokeText.setTypeface(customTypeface);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            customTypeface = ResourcesCompat.getFont(getContext(), R.font.font_christie);
            paintFillText.setTypeface(customTypeface);
            paintStrokeText.setTypeface(customTypeface);
        }

        Rect boundsFillText = new Rect();
        Rect boundsStrokeText = new Rect();

        /*
        V následující části zjišťuji rozměry textu, který bude vykreslen. Hodnota 36 je zde proto,
        že je to maximální součet maximálního počtu kostek (6x6). Pokud toto neprovedu,
        není zaručeno, že zobrazovaný součet kostek nebude mít pokaždé stejnou velikost.
        */
        paintFillText.getTextBounds("36", 0, 2, boundsFillText);
        paintStrokeText.getTextBounds("36", 0, 2, boundsStrokeText);

        float textWidth = paintFillText.measureText("36");
        int maxTextWidth = Math.min(width, height);

        textPositionY = (int) ((height/2) - ((paintFillText.descent() + paintFillText.ascent()) / 2)) ;

        if (textWidth > maxTextWidth) {
            float ratio = (float)maxTextWidth / textWidth;
            textSize = (int) ((float)textSize * ratio * 0.9);
            return;
        } else {
            paintFillText.setTextSize(textSize);
            paintStrokeText.setTextSize(textSize);
        }

        canvas.drawText(text, width / 2, textPositionY, paintFillText);
        canvas.drawText(text, width / 2, textPositionY, paintStrokeText);
    }

    public void setValue(boolean animate) {
        if (activity == null) return;

        valueToDraw = activity.counter.getValue();

        if (animate) {
            animateValueCounter();
        } else {
            if (!afterRestoreInstanceState) {
                textAlphaToDraw = 255;
                afterRestoreInstanceState = false;
            }
        }

        init();
    }

    public void animateValueCounter() {

        if (valueAnimatorAlpha != null) {
            valueAnimatorAlpha.cancel();
        }

        valueAnimatorAlpha = ValueAnimator.ofInt(255, 0);
        valueAnimatorAlpha.setDuration(2000);
        valueAnimatorAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textAlphaToDraw = (int) animation.getAnimatedValue();
                DiceValuesCounter.this.invalidate();
            }
        });

        valueAnimatorAlpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                valueAnimatorAlpha = null;
                textAlphaToDraw = 0;
                textAlphaAnimationIsRunning = false;
                DiceValuesCounter.this.invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                valueAnimatorAlpha = null;
                textAlphaToDraw = 0;
                textSizeAnimationIsRunning = false;
                DiceValuesCounter.this.invalidate();
            }

            @Override public void onAnimationStart(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        });

        valueAnimatorAlpha.start();
    }

    public void setActivityContext(MainActivity activity) {
        this.activity = activity;
    }

    public void setAfterRestoreInstanceState() {
        this.afterRestoreInstanceState = true;
    }
}
