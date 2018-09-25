package com.example.demoCollection.widget.leCheckBox;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;

public class LeArrowShape extends Shape {
    private static final float RATE_ONE_THIRD = 1.0f / 3.0f;
    private static final float RATE_TWO_THIRD = 2.0f / 3.0f;

    private float mOneThirdBoxSize;
    private float mArrowStrokeWidth;
    private float mHalfStrokeWidth;
    private float mArrowRadius;
    private float mArrowLeft;
    private float mArrowTop;
    private float mBoxSize;
    private float mInterpolatedTime;
    private boolean mIsInverseAnimation = false;
    private boolean mIsShowUp = false;

    public LeArrowShape(float boxSize, boolean isRoundEdge) {
        setArrowShape(boxSize, isRoundEdge);
    }

    LeArrowShape(float boxSize, boolean isRoundEdge, boolean isWithoutBorder) {
        setCheckBoxArrowShape(boxSize, isRoundEdge, isWithoutBorder);
    }

    public void setArrowShape(float boxSize, boolean isRoundEdge) {
        mBoxSize = boxSize;
        mOneThirdBoxSize = boxSize / 3.0f;
        mArrowStrokeWidth = boxSize / 9.0f;
        mHalfStrokeWidth = mArrowStrokeWidth / 2;
        mArrowRadius = isRoundEdge ? 0 : mHalfStrokeWidth;
        mArrowLeft = boxSize / 4;
        mArrowTop = boxSize / 4;

        resize(boxSize, boxSize);
    }

    public float getInterpolatedTime() {
        return mInterpolatedTime;
    }

    public void setInterpolatedTime(float interpolatedTime) {
        mInterpolatedTime = interpolatedTime;
    }

    public void setIsInverseAnimation(boolean isInverse) {
        mIsInverseAnimation = isInverse;
    }

    public boolean isInverseAnimation() {
        return mIsInverseAnimation;
    }

    public void setIsShowUp(boolean isShowUp) {
        mIsShowUp = isShowUp;
    }

    public boolean isShowUp() {
        return mIsShowUp;
    }

    public void draw(Canvas canvas, Paint paint, float interpolatedTime) {
        setInterpolatedTime(interpolatedTime);
        draw(canvas, paint);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        drawCheckArrow(canvas, paint, mInterpolatedTime, mBoxSize, mIsShowUp,
                mIsInverseAnimation);
    }

    public void drawCheckArrow(Canvas canvas, Paint paint,
                               float interpolatedTime, float boxSize, boolean isShow,
                               boolean inverseAnimate) {

        final float boxRadius = boxSize / 2;
        canvas.rotate(-45, boxRadius, boxRadius);

        if (inverseAnimate) {
            interpolatedTime = 1 - interpolatedTime;
        }
        if (interpolatedTime < 0) {
            interpolatedTime = 0;
        }
        if (interpolatedTime > 1) {
            interpolatedTime = 1;
        }

        if (isShow) {
            if (interpolatedTime < RATE_ONE_THIRD) {
                canvas.drawRoundRect(new RectF(mArrowLeft, mArrowTop,
                                mArrowLeft + mArrowStrokeWidth, mArrowTop + boxSize
                                * interpolatedTime), mArrowRadius,
                        mArrowRadius, paint);
            } else {
                canvas.drawRoundRect(new RectF(mArrowLeft, mArrowTop,
                                mArrowLeft + mArrowStrokeWidth, mArrowTop
                                + mOneThirdBoxSize), mArrowRadius,
                        mArrowRadius, paint);
                canvas.drawRoundRect(new RectF(mArrowLeft, mArrowTop
                                + mOneThirdBoxSize - mHalfStrokeWidth, mArrowLeft
                                + boxSize * (interpolatedTime - RATE_ONE_THIRD),
                                mArrowTop + mOneThirdBoxSize + mHalfStrokeWidth),
                        mArrowRadius, mArrowRadius, paint);
            }
        } else {
            if (interpolatedTime < RATE_TWO_THIRD) {
                canvas.drawRoundRect(new RectF(mArrowLeft + boxSize
                                - mOneThirdBoxSize - boxSize * interpolatedTime,
                                mArrowTop + mOneThirdBoxSize - mHalfStrokeWidth,
                                mArrowLeft + boxSize - mOneThirdBoxSize, mArrowTop
                                + mOneThirdBoxSize + mHalfStrokeWidth),
                        mArrowRadius, mArrowRadius, paint);
            } else {
                canvas.drawRoundRect(new RectF(mArrowLeft, mArrowTop
                                + mOneThirdBoxSize - boxSize
                                * (interpolatedTime - RATE_TWO_THIRD), mArrowLeft
                                + mArrowStrokeWidth, mArrowTop + mOneThirdBoxSize),
                        mArrowRadius, mArrowRadius, paint);

                canvas.drawRoundRect(new RectF(mArrowLeft, mArrowTop
                                + mOneThirdBoxSize - mHalfStrokeWidth, mArrowLeft
                                + boxSize - mOneThirdBoxSize, mArrowTop
                                + mOneThirdBoxSize + mHalfStrokeWidth), mArrowRadius,
                        mArrowRadius, paint);
            }
        }
        canvas.rotate(45, boxRadius, boxRadius);
    }

    // only for using in LeCheckBox
    void setCheckBoxArrowShape(float boxSize, boolean isRoundEdge,
                               boolean isWithoutBorder) {
        mBoxSize = boxSize;
        mOneThirdBoxSize = mBoxSize / 3.0f;
        mArrowStrokeWidth = mBoxSize / 9.0f;
        mHalfStrokeWidth = mArrowStrokeWidth / 2;
        mArrowRadius = isWithoutBorder ? 0 : mHalfStrokeWidth;
        mArrowLeft = isWithoutBorder ? mBoxSize / 4 : mOneThirdBoxSize;
        mArrowTop = isWithoutBorder ? mBoxSize / 4 : mOneThirdBoxSize
                - mHalfStrokeWidth;

        resize(boxSize, boxSize);
    }
}
