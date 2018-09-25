package com.example.demoCollection.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.example.demoCollection.R;

public class SimCardView extends View implements ObjectAnimator.AnimatorUpdateListener {
    public static final int HAS_SIMCARD = 0;
    public static final int SIMCARD_ERROR = 1;
    public static final int SIMCARD_LOCK = 2;
    public static final int NO_SIMCARD = 3;

    private float centerX;
    private float centerY;

    private AnimatorSet simCardAnim;

    private static final int NORMAL = 0;
    private static final int CENTER = 1;
    private static final int CENTER_VERTICAL = 2;
    private static final int CENTER_HORIZONTAL = 3;

    // backBall
    private float radius;
    private int mGravity = NORMAL;
    private float mStrokeWidth = 8;
    private int mBackBallColor = Color.parseColor("#43AD69");
    private float NORMAL_BACK_BALL_RADIUS = 127;
    private int CIRCLE_ZOOM_OUT_DURATION = 1000;

    // simcard
    private Path mPath;
    private int mSimCardColor = Color.parseColor("#FFFFFF");
    private float rectWidth;
    private float rectHeight;
    private float NORMAL_RECT_WIDTH = 76;
    private float NORMAL_RECT_HEIGHT = 96;
    private static final int SIMCARD_DELAY = 175;
    private int mSimCardStatus;

    // hook
    private float hookDownSpeed;
    private float hookUpSpeed;
    private static final int HOOK_DELAY = 500;

    // exclamation mark
    private float markDownSpeed;
    private float markUpSpeed;
    private static final int MARK_DOWN_DURATION = 175;
    private static final int MARK_UP_DURATION = 500;
    private static final int MARK_DELAY = 500;

    // lock
    private float lockScale;
    private static final int LOCK_ZOOM_DURATION = 500;
    private static final int LOCK_ARC_DURATION = 350;
    private static final int LOCK_ZOOM_DELAY = 500;
    private static final int LOCK_ARC_DELAY = 650;
    private float arc;

    private Paint mPaint;

    public SimCardView(Context context) {
        this(context, null);
    }

    public SimCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // read attr
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimCardView);
        mGravity = a.getInt(R.styleable.SimCardView_simCardGravity, 0);
        a.recycle();

        // init paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();
    }

    public void startAnimation(int simCardStatus) {
        cancelAnimation();

        mSimCardStatus = simCardStatus;

        createAnimation();
        simCardAnim.start();
    }

    public void cancelAnimation() {
        if (null != simCardAnim && simCardAnim.isRunning()) {
            simCardAnim.cancel();
        }
        simCardAnim = null;

        radius = 0;
        rectWidth = 0;
        rectHeight = 0;
        hookDownSpeed = 0.0f;
        hookUpSpeed = 0.0f;
        markUpSpeed = 0.0f;
        markDownSpeed = 0.0f;
        lockScale = 0.0f;
        arc = 0.0f;

        invalidate();
    }

    private void createAnimation() {
        if (null == simCardAnim) {
            ObjectAnimator circleZoomOut = ObjectAnimator.ofFloat(this, "radius", 0, NORMAL_BACK_BALL_RADIUS);
            circleZoomOut.setDuration(CIRCLE_ZOOM_OUT_DURATION);
            circleZoomOut.addUpdateListener(this);
            circleZoomOut.setInterpolator(AnimationUtils.loadInterpolator(getContext(), R.interpolator.le_c_interpolator));

            ObjectAnimator simCardWidth = ObjectAnimator.ofFloat(this, "rectWidth", 0, NORMAL_RECT_WIDTH);
            simCardWidth.setDuration(CIRCLE_ZOOM_OUT_DURATION - SIMCARD_DELAY);
            simCardWidth.setStartDelay(SIMCARD_DELAY);
            simCardWidth.setInterpolator(AnimationUtils.loadInterpolator(getContext(), R.interpolator.le_c_interpolator));
            simCardWidth.addUpdateListener(this);

            ObjectAnimator simCardHeight = ObjectAnimator.ofFloat(this, "rectHeight", 0, NORMAL_RECT_HEIGHT);
            simCardHeight.setDuration(CIRCLE_ZOOM_OUT_DURATION - SIMCARD_DELAY);
            simCardHeight.setStartDelay(SIMCARD_DELAY);
            simCardHeight.setInterpolator(AnimationUtils.loadInterpolator(getContext(), R.interpolator.le_c_interpolator));
            simCardHeight.addUpdateListener(this);

            simCardAnim = new AnimatorSet();
            switch (mSimCardStatus) {
                case HAS_SIMCARD:
                    ObjectAnimator hookDown = ObjectAnimator.ofFloat(this, "hookDownSpeed", 0.0f, 1.0f)
                            .setDuration((CIRCLE_ZOOM_OUT_DURATION - HOOK_DELAY) / 3);
                    hookDown.setStartDelay(HOOK_DELAY);
                    hookDown.setInterpolator(AnimationUtils.loadInterpolator(getContext(), R.interpolator.le_c_interpolator));
                    hookDown.addUpdateListener(this);

                    ObjectAnimator hookUp = ObjectAnimator.ofFloat(this, "hookUpSpeed", 0.0f, 1.0f)
                            .setDuration((CIRCLE_ZOOM_OUT_DURATION - HOOK_DELAY) / 3 * 2);
                    hookUp.setStartDelay(HOOK_DELAY + (CIRCLE_ZOOM_OUT_DURATION - HOOK_DELAY) / 3);
                    hookUp.setInterpolator(AnimationUtils.loadInterpolator(getContext(), R.interpolator.le_c_interpolator));
                    hookUp.addUpdateListener(this);

                    simCardAnim.playTogether(circleZoomOut, simCardWidth, simCardHeight, hookDown, hookUp);
                    break;

                case SIMCARD_ERROR:
                    ObjectAnimator markUp = ObjectAnimator.ofFloat(this, "markUpSpeed", 0.0f, 1.0f);
                    markUp.setDuration(MARK_UP_DURATION);
                    markUp.setStartDelay(MARK_DELAY);
                    markUp.addUpdateListener(this);

                    ObjectAnimator markDown = ObjectAnimator.ofFloat(this, "markDownSpeed", 0.0f, 1.0f);
                    markDown.setDuration(MARK_DOWN_DURATION);
                    markDown.setStartDelay(MARK_DELAY);
                    markDown.addUpdateListener(this);

                    simCardAnim.playTogether(circleZoomOut, simCardWidth, simCardHeight, markUp, markDown);
                    break;

                case SIMCARD_LOCK:
                    ObjectAnimator lockZoom = ObjectAnimator.ofFloat(this, "lockScale", 0.0f, 1.0f);
                    lockZoom.setDuration(LOCK_ZOOM_DURATION);
                    lockZoom.setStartDelay(LOCK_ZOOM_DELAY);

                    ObjectAnimator lockArc = ObjectAnimator.ofFloat(this, "arc", 0.0f, 180.0f);
                    lockArc.setDuration(LOCK_ARC_DURATION);
                    lockArc.setStartDelay(LOCK_ARC_DELAY);

                    simCardAnim.playTogether(circleZoomOut, simCardWidth, simCardHeight, lockZoom, lockArc);
                    break;

                case NO_SIMCARD:
                    simCardAnim.playTogether(circleZoomOut, simCardWidth, simCardHeight);
                    break;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int measureSpec) {
        float result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            result = NORMAL_BACK_BALL_RADIUS * 2;
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return (int) result;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = NORMAL_BACK_BALL_RADIUS;
        centerY = NORMAL_BACK_BALL_RADIUS;

        switch (mGravity) {
            case CENTER: {
                centerX = getWidth() / 2;
                centerY = getHeight() / 2;
            }
            break;
            case CENTER_VERTICAL: {
                centerY = getHeight() / 2;
            }
            break;
            case CENTER_HORIZONTAL: {
                centerX = getWidth() / 2;
            }
            break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw circle
        mPaint.setColor(mBackBallColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, radius, mPaint);

        // SIM Card
        mPath.reset();
        mPath.moveTo(centerX - rectWidth / 2, centerY - rectHeight / 2);
        mPath.lineTo(centerX + rectWidth / 6, centerY - rectHeight / 2);
        mPath.lineTo(centerX + rectWidth / 2, centerY - rectHeight / 4);
        mPath.lineTo(centerX + rectWidth / 2, centerY + rectHeight / 2);
        mPath.lineTo(centerX - rectWidth / 2, centerY + rectHeight / 2);
        mPath.lineTo(centerX - rectWidth / 2, centerY - rectHeight / 2);
        mPaint.setColor(mSimCardColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth * radius / NORMAL_BACK_BALL_RADIUS);
        canvas.drawPath(mPath, mPaint);

        switch (mSimCardStatus) {
            case HAS_SIMCARD:   // draw hook
                float hookDownXStart = centerX - NORMAL_RECT_WIDTH / 4;
                float hookDownYStart = centerY;
                float hookDownXEnd = centerX;
                float hookDownYEnd = centerY + (NORMAL_RECT_HEIGHT / 4);
                mPath.moveTo(hookDownXStart, hookDownYStart);
                mPath.lineTo(
                        (hookDownXStart + hookDownSpeed * (hookDownXEnd - hookDownXStart)),
                        (hookDownYStart + hookDownSpeed * (hookDownYEnd - hookDownYStart)));
                canvas.drawPath(mPath, mPaint);

                float hookUpXStart = hookDownXEnd;
                float hookUpYStart = hookDownYEnd;
                float hookUpXEnd = centerX + NORMAL_RECT_WIDTH / 2;
                float hookUpYEnd = centerY - NORMAL_RECT_HEIGHT / 4;
                mPath.moveTo(hookUpXStart, hookUpYStart);
                mPath.lineTo(
                        (hookUpXStart + hookUpSpeed * (hookUpXEnd - hookUpXStart)),
                        (hookUpYStart + hookUpSpeed * (hookUpYEnd - hookUpYStart)));
                canvas.drawPath(mPath, mPaint);
                break;
            case SIMCARD_ERROR: // exclamation mark
                canvas.drawLine(
                        centerX, centerY + (rectHeight / 4) - mStrokeWidth * 2,
                        centerX, centerY + (rectHeight / 4) - mStrokeWidth * 2 - (rectHeight * markUpSpeed * 0.3f),
                        mPaint);

                canvas.drawLine(
                        centerX, centerY + (rectHeight / 4) + mStrokeWidth / 2,
                        centerX, centerY + (rectHeight / 4) + mStrokeWidth / 2 - (markDownSpeed * mStrokeWidth / 2),
                        mPaint);
                break;
            case SIMCARD_LOCK: // lock
                mPaint.setColor(mSimCardColor);
                mPaint.setStyle(Paint.Style.FILL);
                RectF rectF = new RectF(
                        centerX - rectWidth / 3 * lockScale,
                        centerY + mStrokeWidth * lockScale * 0.5f,
                        centerX + rectWidth / 3 * lockScale,
                        centerY + mStrokeWidth * lockScale * 0.5f + rectHeight / 2 * 3 / 5 * lockScale);
                canvas.drawRoundRect(rectF, 5f, 5f, mPaint);

                mPaint.setStyle(Paint.Style.STROKE);
                RectF oval = new RectF(
                        centerX - rectWidth / 5 * lockScale,
                        centerY + mStrokeWidth * lockScale - rectHeight * 1 / 5,
                        centerX + rectWidth / 5 * lockScale,
                        centerY + mStrokeWidth * 2 * lockScale
                );
                canvas.drawArc(oval, 180, arc, false, mPaint);

                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mBackBallColor);
                canvas.drawCircle(
                        centerX,
                        centerY + mStrokeWidth * lockScale * 0.5f + +rectHeight / 2 * 3 / 10 * lockScale,
                        rectHeight / 12 * lockScale, mPaint);
                break;
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        invalidate();
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRectWidth() {
        return rectWidth;
    }

    public void setRectWidth(float rectWidth) {
        this.rectWidth = rectWidth;
    }

    public float getRectHeight() {
        return rectHeight;
    }

    public void setRectHeight(float rectHeight) {
        this.rectHeight = rectHeight;
    }

    public float getHookDownSpeed() {
        return hookDownSpeed;
    }

    public void setHookDownSpeed(float hookDownSpeed) {
        this.hookDownSpeed = hookDownSpeed;
    }

    public float getHookUpSpeed() {
        return hookUpSpeed;
    }

    public void setHookUpSpeed(float hookUpSpeed) {
        this.hookUpSpeed = hookUpSpeed;
    }

    public float getMarkUpSpeed() {
        return markUpSpeed;
    }

    public void setMarkUpSpeed(float markUpSpeed) {
        this.markUpSpeed = markUpSpeed;
    }

    public float getMarkDownSpeed() {
        return markDownSpeed;
    }

    public void setMarkDownSpeed(float markDownSpeed) {
        this.markDownSpeed = markDownSpeed;
    }

    public float getLockScale() {
        return lockScale;
    }

    public void setLockScale(float lockScale) {
        this.lockScale = lockScale;
    }

    public float getArc() {
        return arc;
    }

    public void setArc(float arc) {
        this.arc = arc;
    }

    public SimCardView setBackBallColor(int color) {
        this.mBackBallColor = color;
        return this;
    }

    public SimCardView setBackBall(int radius) {
        NORMAL_BACK_BALL_RADIUS = radius;
        return this;
    }

    public SimCardView setSimCardColor(int color) {
        this.mSimCardColor = color;
        return this;
    }

    public SimCardView setRect(float width, float height) {
        NORMAL_RECT_WIDTH = width;
        NORMAL_RECT_HEIGHT = height;
        return this;
    }

}
