package com.example.demoCollection.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Rotate3dView1 extends ImageView implements ObjectAnimator.AnimatorUpdateListener {

    public static final String ANGLE = "angle";
    private static final int POINTS_NUMBER = 8;
    private static final int DEPTH_CONSTANT = 1500;
    private ObjectAnimator rotate3dAnim;
    private float angle;
    private float mStartAngel;
    private float mEndAngel;
    private int mDuration;

    private float[] srcPoly, dstPoly;
    private int w, h;
    private Matrix matrix;

    public Rotate3dView1(Context context) {
        this(context, null);
    }

    public Rotate3dView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Rotate3dView1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setScaleType(ScaleType.MATRIX);
    }

    /**
     * start animation
     *
     * @param startAngle animation start angle
     * @param endAngle   animation end angle
     * @param duration   animation run time
     */
    public ObjectAnimator initAnim(double startAngle, double endAngle, int duration) {
        mStartAngel = (float) startAngle;
        mEndAngel = (float) endAngle;
        mDuration = duration;

        cancelAnim();
        createAnim();

        return rotate3dAnim;
    }

    private void createAnim() {
        init();
        if (null == rotate3dAnim) {
            rotate3dAnim = ObjectAnimator.ofFloat(this, ANGLE, mStartAngel, mEndAngel);
        } else {
            rotate3dAnim.setFloatValues(mStartAngel, mEndAngel);
        }
        rotate3dAnim.setDuration(mDuration);
        rotate3dAnim.addUpdateListener(this);
    }

    public void cancelAnim() {
        if (null != rotate3dAnim && rotate3dAnim.isRunning()) {
            rotate3dAnim.cancel();
        }
    }

    private void init() {
        w = getWidth();
        h = getHeight();
        srcPoly = new float[POINTS_NUMBER];
        dstPoly = new float[POINTS_NUMBER];
    }

    public void calculateMatrix() {
        // point 3, 4 x
        float scaleWidth = w * (float) Math.cos(angle);

        float deepth = (float) Math.sqrt(w * w - scaleWidth * scaleWidth);
        float scaleFactor = 0;
        if (angle >= 0 && angle <= (Math.PI / 2)) {  // zoom in
            scaleFactor = DEPTH_CONSTANT / (DEPTH_CONSTANT + deepth);
        } else if (angle >= (3 / 2 * (Math.PI)) && angle <= (2 * (Math.PI))) {   // zoom out
            scaleFactor = (DEPTH_CONSTANT + deepth) / DEPTH_CONSTANT;
        }
        float scaleHeight = h * scaleFactor;
        // point 3 y
        float topScalePoint = (h - scaleHeight) / 2.f;
        // point 4 y
        float bottomScalePoint = topScalePoint + scaleHeight;

        srcPoly[0] = 0;
        srcPoly[1] = 0;
        srcPoly[2] = 0;
        srcPoly[3] = h;
        srcPoly[4] = w;
        srcPoly[5] = 0;
        srcPoly[6] = w;
        srcPoly[7] = h;

        dstPoly[0] = 0;
        dstPoly[1] = 0;
        dstPoly[2] = dstPoly[0];
        dstPoly[3] = h;
        dstPoly[4] = scaleWidth;
        dstPoly[5] = topScalePoint;
        dstPoly[6] = dstPoly[4];
        dstPoly[7] = bottomScalePoint;

        matrix = getImageMatrix();
        matrix.setPolyToPoly(srcPoly, 0, dstPoly, 0, POINTS_NUMBER / 2);

        setImageMatrix(matrix);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        invalidate();
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        calculateMatrix();
    }
}
