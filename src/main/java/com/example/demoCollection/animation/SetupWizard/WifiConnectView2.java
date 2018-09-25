package com.example.demoCollection.animation.SetupWizard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import com.example.demoCollection.R;
import com.example.demoCollection.animation.ShapeHolder;

public class WifiConnectView2 extends BaseView {

    private static final int BACK_CIRCLE_COLOR = Color.parseColor("#43AD69");
    private static final int BACK_CIRCLE_ANIM_DURATION = 1000;
    private static final int WIFI_LINE1_DELAY = 175;
    private static final int WIFI_LINE2_DELAY = 245;
    private static final int WIFI_LINE3_DELAY = 315;
    private static final int WIFI_LINES_DURATION = 500;
    private static final int WIFI_LINES_START_ALPHA = 0;
    private static final int WIFI_LINES_END_ALPHA = 255;
    private float WIFI_LINE_START_TRANSLATE_Y;
    private float WIFI_LINE_END_TRANSLATE_Y = 0;
    private boolean mHasConnect;

    private AnimatorSet wifiHasConnectAnimSet;

    private float BACK_CIRCLE_WIDTH;
    private ShapeHolder mBackCircle;
    private Bitmap wifiLine1;
    private Bitmap wifiLine2;
    private Bitmap wifiLine3;
    private static final String WIFI_LINE1_TRANSLATE_Y = "wifiLine1TranslateY";
    private static final String WIFI_LINE2_TRANSLATE_Y = "wifiLine2TranslateY";
    private static final String WIFI_LINE3_TRANSLATE_Y = "wifiLine3TranslateY";
    private float wifiLine1TranslateY;
    private float wifiLine2TranslateY;
    private float wifiLine3TranslateY;
    private static final String WIFI_LINE1_ALPHA = "wifiLine1Alpha";
    private static final String WIFI_LINE2_ALPHA = "wifiLine2Alpha";
    private static final String WIFI_LINE3_ALPHA = "wifiLine3Alpha";
    private int wifiLine1Alpha;
    private int wifiLine2Alpha;
    private int wifiLine3Alpha;

    private Paint mWifiLine1Paint;
    private Paint mWifiLine2Paint;
    private Paint mWifiLine3Paint;

    public WifiConnectView2(Context context) {
        this(context, null);
    }

    public WifiConnectView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WifiConnectView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void init() {
        mBackCircle = new ShapeHolder(new ShapeDrawable(new OvalShape()));
        mBackCircle.setColor(BACK_CIRCLE_COLOR);

        mWifiLine1Paint = new Paint();
        mWifiLine2Paint = new Paint();
        mWifiLine3Paint = new Paint();

        cancelAnimation();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        BACK_CIRCLE_WIDTH = Math.round(width / 2f);
        mBackCircle.setXY(BACK_CIRCLE_WIDTH, BACK_CIRCLE_WIDTH);

        wifiLine1 = drawableToBitmap(getResources().getDrawable(R.drawable.wifi_line1));
        wifiLine2 = drawableToBitmap(getResources().getDrawable(R.drawable.wifi_line2));
        wifiLine3 = drawableToBitmap(getResources().getDrawable(R.drawable.wifi_line3));

        WIFI_LINE_START_TRANSLATE_Y = Math.round(width / 3.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mHasConnect) {
            drawShape(canvas, mBackCircle);

            if (null != wifiLine1) {
                canvas.save();
                canvas.translate(0, wifiLine1TranslateY);
                canvas.drawBitmap(wifiLine1, 0, 0, mWifiLine1Paint);
                canvas.restore();
            }
            if (null != wifiLine2) {
                canvas.save();
                canvas.translate(0, wifiLine2TranslateY);
                canvas.drawBitmap(wifiLine2, 0, 0, mWifiLine2Paint);
                canvas.restore();
            }
            if (null != wifiLine3) {
                canvas.save();
                canvas.translate(0, wifiLine3TranslateY);
                canvas.drawBitmap(wifiLine3, 0, 0, mWifiLine3Paint);
                canvas.restore();
            }
        }
    }

    public void startAnimation(boolean hasConnect) {
        cancelAnimation();
        mHasConnect = hasConnect;

        if (null == wifiHasConnectAnimSet && mHasConnect) {
            wifiHasConnectAnimSet = new AnimatorSet();

            Interpolator cInterpolator = AnimationUtils.loadInterpolator(getContext(), android.R.interpolator.fast_out_slow_in);
            Interpolator bInterpolator = new AccelerateDecelerateInterpolator();
            Interpolator hInterpolator = new OvershootInterpolator();

            // circle scale anim
            ObjectAnimator mCircleAnim = ObjectAnimator.ofFloat(mBackCircle, ShapeHolder.RADIUS, 0, BACK_CIRCLE_WIDTH);
            initAnimator(mCircleAnim, cInterpolator, BACK_CIRCLE_ANIM_DURATION);
            mCircleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    invalidate();
                }
            });
            // wifiLine1 move & alpha
            ObjectAnimator wifiLine1AlphaAnim = ObjectAnimator.ofInt(this, WIFI_LINE1_ALPHA, WIFI_LINES_START_ALPHA, WIFI_LINES_END_ALPHA);
            initAnimator(wifiLine1AlphaAnim, bInterpolator, WIFI_LINE1_DELAY, WIFI_LINES_DURATION);
            ObjectAnimator wifiLine1TranslateYAnim = ObjectAnimator.ofFloat(this, WIFI_LINE1_TRANSLATE_Y, WIFI_LINE_START_TRANSLATE_Y, WIFI_LINE_END_TRANSLATE_Y);
            initAnimator(wifiLine1TranslateYAnim, hInterpolator, WIFI_LINE1_DELAY, WIFI_LINES_DURATION);
            // wifiLine2 move & alpha
            ObjectAnimator wifiLine2AlphaAnim = ObjectAnimator.ofInt(this, WIFI_LINE2_ALPHA, WIFI_LINES_START_ALPHA, WIFI_LINES_END_ALPHA);
            initAnimator(wifiLine2AlphaAnim, bInterpolator, WIFI_LINE2_DELAY, WIFI_LINES_DURATION);
            ObjectAnimator wifiLine2TranslateYAnim = ObjectAnimator.ofFloat(this, WIFI_LINE2_TRANSLATE_Y, WIFI_LINE_START_TRANSLATE_Y, WIFI_LINE_END_TRANSLATE_Y);
            initAnimator(wifiLine2TranslateYAnim, hInterpolator, WIFI_LINE2_DELAY, WIFI_LINES_DURATION);
            // wifiLine3 move & alpha
            ObjectAnimator wifiLine3AlphaAnim = ObjectAnimator.ofInt(this, WIFI_LINE3_ALPHA, WIFI_LINES_START_ALPHA, WIFI_LINES_END_ALPHA);
            initAnimator(wifiLine3AlphaAnim, bInterpolator, WIFI_LINE3_DELAY, WIFI_LINES_DURATION);
            ObjectAnimator wifiLine3TranslateYAnim = ObjectAnimator.ofFloat(this, WIFI_LINE3_TRANSLATE_Y, WIFI_LINE_START_TRANSLATE_Y, WIFI_LINE_END_TRANSLATE_Y);
            initAnimator(wifiLine3TranslateYAnim, hInterpolator, WIFI_LINE3_DELAY, WIFI_LINES_DURATION);

            wifiHasConnectAnimSet.playTogether(
                    mCircleAnim,
                    wifiLine1AlphaAnim, wifiLine1TranslateYAnim,
                    wifiLine2AlphaAnim, wifiLine2TranslateYAnim,
                    wifiLine3AlphaAnim, wifiLine3TranslateYAnim);
        }

        if (mHasConnect) {
            wifiHasConnectAnimSet.start();
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.animated_has_no_wifi_connect);
            setBackground(drawable);
            ((Animatable) drawable).start();
        }
    }

    public void cancelAnimation() {
        mBackCircle.setRadius(0);
        setWifiLine1Alpha(WIFI_LINES_START_ALPHA);
        setWifiLine2Alpha(WIFI_LINES_START_ALPHA);
        setWifiLine3Alpha(WIFI_LINES_START_ALPHA);
        setWifiLine1TranslateY(WIFI_LINE_START_TRANSLATE_Y);
        setWifiLine2TranslateY(WIFI_LINE_START_TRANSLATE_Y);
        setWifiLine3TranslateY(WIFI_LINE_START_TRANSLATE_Y);

        setCancelAnimSet(wifiHasConnectAnimSet);

        setBackground(null);

        invalidate();
    }

    /**
     * drawable 2 bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?
                Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public float getWifiLine1TranslateY() {
        return wifiLine1TranslateY;
    }

    public void setWifiLine1TranslateY(float wifiLine1TranslateY) {
        this.wifiLine1TranslateY = wifiLine1TranslateY;
    }

    public float getWifiLine2TranslateY() {
        return wifiLine2TranslateY;
    }

    public void setWifiLine2TranslateY(float wifiLine2TranslateY) {
        this.wifiLine2TranslateY = wifiLine2TranslateY;
    }

    public float getWifiLine3TranslateY() {
        return wifiLine3TranslateY;
    }

    public void setWifiLine3TranslateY(float wifiLine3TranslateY) {
        this.wifiLine3TranslateY = wifiLine3TranslateY;
    }

    public int getWifiLine1Alpha() {
        return wifiLine1Alpha;
    }

    public void setWifiLine1Alpha(int wifiLine1Alpha) {
        this.wifiLine1Alpha = wifiLine1Alpha;
        mWifiLine1Paint.setAlpha(wifiLine1Alpha);
    }

    public int getWifiLine2Alpha() {
        return wifiLine2Alpha;
    }

    public void setWifiLine2Alpha(int wifiLine2Alpha) {
        this.wifiLine2Alpha = wifiLine2Alpha;
        mWifiLine2Paint.setAlpha(wifiLine2Alpha);
    }

    public int getWifiLine3Alpha() {
        return wifiLine3Alpha;
    }

    public void setWifiLine3Alpha(int wifiLine3Alpha) {
        this.wifiLine3Alpha = wifiLine3Alpha;
        mWifiLine3Paint.setAlpha(wifiLine3Alpha);
    }
}
