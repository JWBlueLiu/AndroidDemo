package com.example.demoCollection.animation.SetupWizard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import com.example.demoCollection.animation.ShapeHolder;

public class BaseView extends View {

    public static final float TRANSPARENCY = 0;
    public static final float OPACITY = 1;

    protected int width;
    protected int height;
    private Paint mPaint;
    private boolean cancelEndEnable;

    public BaseView(Context context) {
        this(context, null);
    }

    public BaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        init();
    }

    protected void init() {
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = Math.min(getWidth(), getHeight());
        height = Math.max(getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, Math.round(height / 2f), width, Math.round(height / 2f), mPaint);
        canvas.drawLine(Math.round(width / 2f), 0, Math.round(width / 2f), height, mPaint);
    }

    protected void drawShape(Canvas canvas, ShapeHolder shapeHolder) {
        if (shapeHolder.getAlpha() != TRANSPARENCY) {
            float translationX = shapeHolder.getX() - Math.round(shapeHolder.getWidth() / 2);
            float translationY = shapeHolder.getY() - Math.round(shapeHolder.getHeight() / 2);
            canvas.translate(translationX, translationY);
            shapeHolder.getShape().draw(canvas);
            canvas.translate(-translationX, -translationY);
        }
    }

    protected Animator initAnimator(Animator animator, Interpolator interpolator, int duration) {
        return initAnimator(animator, interpolator, 0, duration);
    }

    protected Animator initAnimator(Animator animator, Interpolator interpolator, int delay, int duration) {
        if (animator instanceof ObjectAnimator) {
            ((ObjectAnimator) animator).setAutoCancel(true);
        }
        if (null != interpolator) {
            animator.setInterpolator(interpolator);
        }
        if (delay > 0) {
            animator.setStartDelay(delay);
        }
        animator.setDuration(duration);
        return animator;
    }

    protected void setCancelAnimSet(Animator animator) {
        if (null != animator) {
            setCancelEndIsAble(false);
            animator.cancel();
            setCancelEndIsAble(true);
        }
    }

    protected void setCancelEndIsAble(boolean enable) {
        cancelEndEnable = enable;
    }

    protected boolean getCancelEndIsAble() {
        return cancelEndEnable;
    }
}
