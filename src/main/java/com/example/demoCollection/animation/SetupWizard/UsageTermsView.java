package com.example.demoCollection.animation.SetupWizard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import com.example.demoCollection.animation.ShapeHolder;

public class UsageTermsView extends BaseView {

    private static final int BACK_CIRCLE_COLOR = Color.parseColor("#43AD69");
    private static final int BACK_CIRCLE_ANIM_DURATION = 1000;
    private static final int POINT_ANIM_DELAY = 500;
    private static final int POINT_ANIM_DURATION = 500;
    private static final int LINE_ANIM_DELAY = 200;
    private static final int LINE_ANIM_DURATION = 350;

    private AnimatorSet usageTermsAnimSet;
    private ObjectAnimator mCircleAnim;
    private ObjectAnimator mPointAnim;
    private ObjectAnimator mLineAnim;

    private ShapeHolder mBackCircle;
    private ShapeHolder mPoint;
    private ShapeHolder mLine;
    private float BACK_CIRCLE_WIDTH;
    private float POINT_WIDTH;
    private float POINT_START_Y;
    private float POINT_END_Y;
    private float LINE_WIDTH;
    private float LINE_HEIGHT;

    public UsageTermsView(Context context) {
        this(context, null);
    }

    public UsageTermsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UsageTermsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void init() {
        mBackCircle = new ShapeHolder(new ShapeDrawable(new OvalShape()));
        mBackCircle.setColor(BACK_CIRCLE_COLOR);

        mPoint = new ShapeHolder(new ShapeDrawable(new RectShape()));
        mPoint.setColor(Color.WHITE);
        mPoint.setAlpha(TRANSPARENCY);

        mLine = new ShapeHolder(new ShapeDrawable(new RectShape()));
        mLine.setColor(Color.WHITE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        float halfWidth = Math.round(width / 2f);
        float halfHeight = Math.round(height / 2f);

        // circle
        BACK_CIRCLE_WIDTH = halfWidth;
        mBackCircle.setXY(halfWidth, halfHeight);

        // point (37, 52) w6 h6
        POINT_WIDTH = Math.round(width * 6 / 80f);
        POINT_START_Y = Math.round(width * 52 / 80f) + Math.round(POINT_WIDTH / 2f);
        POINT_END_Y = Math.round(width * 22 / 80f) + Math.round(POINT_WIDTH / 2f);
        mPoint.setX(halfWidth);
        mPoint.setWidth(POINT_WIDTH);
        mPoint.setHeight(POINT_WIDTH);

        // line (37, 34) w6 h24
        LINE_WIDTH = Math.round(width * 6 / 80f);
        LINE_HEIGHT = Math.round(width * 24 / 80f);
        mLine.setXY(halfWidth, Math.round(width * 34 / 80f) + LINE_HEIGHT);
        mLine.setWidth(LINE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw back circle
        drawShape(canvas, mBackCircle);
        // draw point
        drawShape(canvas, mPoint);
        // draw line
        if (mLine.getAlpha() != TRANSPARENCY) {
            float translationX = mLine.getX() - Math.round(mLine.getWidth() / 2f);
            float translationY = mLine.getY() - mLine.getHeight();
            canvas.translate(translationX, translationY);
            mLine.getShape().draw(canvas);
            canvas.translate(-translationX, -translationY);
        }
    }

    public void startAnim() {
        cancelAnim();

        if (null == usageTermsAnimSet) {
            usageTermsAnimSet = new AnimatorSet();

            Interpolator cInterpolator = AnimationUtils.loadInterpolator(getContext(), android.R.interpolator.fast_out_slow_in);

            // circle scale anim
            mCircleAnim = ObjectAnimator.ofFloat(mBackCircle, ShapeHolder.RADIUS, 0, BACK_CIRCLE_WIDTH);
            initAnimator(mCircleAnim, cInterpolator, BACK_CIRCLE_ANIM_DURATION);
            mCircleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    invalidate();
                }
            });
            // point move anim
            mPointAnim = ObjectAnimator.ofFloat(mPoint, ShapeHolder.Y, POINT_START_Y, POINT_END_Y);
//            initAnimator(mPointAnim, LeAccelerateDecelerateDampingInterpolator.getDefaultInterpolator(), POINT_ANIM_DELAY, POINT_ANIM_DURATION);
            initAnimator(mPointAnim, new BounceInterpolator(), POINT_ANIM_DELAY, POINT_ANIM_DURATION);
            mPointAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (Math.round((Float) animation.getAnimatedValue()) == POINT_START_Y) {
                        mPoint.setAlpha(OPACITY);
                    }
                }
            });
            // line scale anim
            mLineAnim = ObjectAnimator.ofFloat(mLine, ShapeHolder.HEIGHT, 0, LINE_HEIGHT);
            initAnimator(mLineAnim, cInterpolator, LINE_ANIM_DELAY, LINE_ANIM_DURATION);

            usageTermsAnimSet.playTogether(mCircleAnim, mPointAnim, mLineAnim);
        }

        usageTermsAnimSet.start();
    }

    public void cancelAnim() {
        mBackCircle.setRadius(0);
        mPoint.setY(POINT_START_Y);
        mPoint.setAlpha(TRANSPARENCY);
        mLine.setHeight(0);

        setCancelAnimSet(usageTermsAnimSet);
    }

}
