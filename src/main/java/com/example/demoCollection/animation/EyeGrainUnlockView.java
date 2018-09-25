package com.example.demoCollection.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.example.demoCollection.R;
import java.util.ArrayList;
import java.util.List;

//import com.letv.leui.animation.LeAccelerateDecelerateDampingInterpolator;

public class EyeGrainUnlockView extends View {

    private static final String TAG = "EyeGrainUnlockView1";

    private static final String HALF_EYE_SOCKET_HEIGHT_STR = "halfEyesSocketHeight";
    private static final String EYES_SCALE = "eyesScale";
    private static final String LOCK_SCALE = "lockScale";
    private static final int SCAN_EYE_CLOSE_ANIM_DELAY = 400;
    private static final int SCAN_EYE_CLOSE_ANIM_DURATION = 300;
    private static final int SCAN_EYE_OPEN_ANIM_DURATION = 300;
    private static final int SCAN_FAIL_FLOAT_ANIM_DURATION = 600;
    private static final int SCAN_OK_EYES_SCALE_ANIM_DURATION = 300;
    private static final int SCAN_OK_EYES_SOCKET_SCALE_ANIM_DURATION = 400;
    private static final int SCAN_OK_LOCK_SOCKET_SCALE_ANIM_DELAY = 200;
    private static final int SCAN_OK_LOCK_SOCKET_SCALE_ANIM_DURATION = 200;
    private static final int SCAN_OK_RAY_ANIM_DELAY = 200;
    private static final int SCAN_OK_RAY_ANIM_DURATION1 = 132;
    private static final int SCAN_OK_RAY_ANIM_DURATION2 = 68;
    private static final int EYES_RESUME_ANIM_DURATION = 300;
    private static final float SCLAE_MAX = 0.7f;
    private static final float SCLAE_MIN = 0.0f;
    private static float MAX_OFFSET;
    private static float RAY_LENGTH;

    private AnimatorSet scanAnimSet;
    private AnimatorSet scanFailAnimSet;
    private AnimatorSet scanOkAnimSet;
    private AnimatorSet eyesResumeAnimSet;
    private ObjectAnimator scanEyeCloseAnim;
    private ObjectAnimator scanEyeOpenAnim;
    private boolean startScanOkEyeAnim;
    private boolean startScanOkLockAnim;

    private Paint eyesSocketPaint;
    private Paint eyesPaint;
    private Paint lockPaint;
    private Bitmap eyes;
    private Bitmap lock;

    private RectF rf;

    private float HALF_EYE_SOCKET_HEIGHT;
    private int screenWidth;
    private int screenHeight;
    private int width;
    private int height;
    private int halfWidth;
    private int halfHeight;
    private float centerX;
    private float centerY;
    private float eyesSocketWidth;
    private float eyesSocketHeight;
    private float halfEyesSocketWidth;
    private float halfEyesSocketHeight;
    private float eyesScale;
    private float lockScale;

    private Ray[] rays;

    private boolean cancelEndEnable = true;

    public EyeGrainUnlockView(Context context) {
        this(context, null);
    }

    public EyeGrainUnlockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EyeGrainUnlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        screenWidth =
            (int) (Math.min(wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getHeight()) * 1.5);
        screenHeight =
            (int) (Math.max(wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getHeight()) * 1.5);

        // 屏幕适配
        width = Math.round(screenWidth / 9.17f);
        height = width;
        halfWidth = Math.round(width / 2.0f);
        halfHeight = halfWidth;
        centerX = halfWidth;
        centerY = halfHeight;
        eyesSocketWidth = Math.round(screenWidth / 14f);
        eyesSocketHeight = Math.round(screenHeight / 36.6f);
        halfEyesSocketWidth = Math.round(eyesSocketWidth / 2.0f);
        halfEyesSocketHeight = Math.round(eyesSocketHeight / 2.0f);
        HALF_EYE_SOCKET_HEIGHT = halfEyesSocketHeight;
        MAX_OFFSET = eyesSocketWidth * 0.5f;

        // 光线
        float rayWidth = Math.round(screenWidth / 480.0f);
        RAY_LENGTH = Math.round(screenWidth / 84.7f);
        Ray.RAY_LENGTH = RAY_LENGTH;
        float rayLength = 0;
        float f = Math.round((halfHeight - RAY_LENGTH) * 0.7);

        float f2 = RAY_LENGTH + 3;
        Ray ray1 = new Ray(width - f2, centerY, rayWidth, rayLength, 0, Color.WHITE);
        Ray ray2 = new Ray(centerX + f, centerY - f, rayWidth, rayLength, (float) (Math.PI / 4), Color.WHITE);
        Ray ray3 = new Ray(centerX, f2, rayWidth, rayLength, (float) (Math.PI / 2), Color.WHITE);
        Ray ray4 = new Ray(centerX - f, centerY - f, rayWidth, rayLength, (float) (Math.PI / 4 * 3), Color.WHITE);
        Ray ray5 = new Ray(f2, centerY, rayWidth, rayLength, (float) (Math.PI), Color.WHITE);
        Ray ray6 = new Ray(centerX - f, centerY + f, rayWidth, rayLength, (float) (Math.PI * 5 / 4), Color.WHITE);
        Ray ray7 = new Ray(centerX, height - f2, rayWidth, rayLength, (float) (Math.PI * 3 / 2), Color.WHITE);
        Ray ray8 = new Ray(centerX + f, centerY + f, rayWidth, rayLength, (float) (Math.PI * 7 / 4), Color.WHITE);
        rays = new Ray[]{ray1, ray2, ray3, ray4, ray5, ray6, ray7, ray8};

        // 眼珠
        eyes = drawableToBitmap(getResources().getDrawable(R.drawable.eyes));
        eyesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        eyesPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        // 眼眶
        eyesSocketPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        eyesSocketPaint.setColor(Color.WHITE);
        eyesSocketPaint.setAlpha((int) (255 * 0.4f));
        eyesSocketPaint.setStyle(Paint.Style.FILL);

        // 眼珠
        lock = drawableToBitmap(getResources().getDrawable(R.drawable.lock));
        lockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        rf = new RectF();
    }

    /**
     * 开始扫描
     */
    public void startScanAnim() {
        cancelAnim();

        if (null == scanAnimSet) {
            scanAnimSet = new AnimatorSet();

            Interpolator cInterpolator = new AccelerateInterpolator();
            Interpolator dInterpolator = new DecelerateInterpolator();

            int closeHeight = Math.round(HALF_EYE_SOCKET_HEIGHT / 8.0f);
            // close
            scanEyeCloseAnim = ObjectAnimator.ofFloat(this, HALF_EYE_SOCKET_HEIGHT_STR, closeHeight);
            initAnimator(scanEyeCloseAnim, cInterpolator, SCAN_EYE_CLOSE_ANIM_DURATION);

            // open
            scanEyeOpenAnim = ObjectAnimator.ofFloat(this, HALF_EYE_SOCKET_HEIGHT_STR, closeHeight, HALF_EYE_SOCKET_HEIGHT);
            initAnimator(scanEyeOpenAnim, dInterpolator, SCAN_EYE_OPEN_ANIM_DURATION);

            scanEyeCloseAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (getCancelEndIsAble()) {
                        scanEyeOpenAnim.setRepeatCount(2);
                        scanEyeOpenAnim.setRepeatMode(ValueAnimator.REVERSE);
                        scanEyeOpenAnim.start();
                    }
                }
            });

            scanEyeOpenAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (getCancelEndIsAble()) {
                        scanEyeCloseAnim.setStartDelay(SCAN_EYE_CLOSE_ANIM_DELAY);
                        scanEyeCloseAnim.start();
                    }
                }
            });

            scanAnimSet.playSequentially(getResumeEyesAnimSet(), scanEyeCloseAnim);
        }
        // reset eye resume duration
        setResumeEyesAnimDuration();
        scanAnimSet.start();
    }

    /**
     * 扫描失败
     */
    public void startScanFailAnim() {
        cancelAnim();

        if (null == scanFailAnimSet) {
            scanFailAnimSet = new AnimatorSet();

            ObjectAnimator scanFailFloatAnim = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0, MAX_OFFSET);
            initAnimator(scanFailFloatAnim, new CycleInterpolator(3), SCAN_FAIL_FLOAT_ANIM_DURATION);

            scanFailAnimSet.playSequentially(getResumeEyesAnimSet(), scanFailFloatAnim);
        }
        // reset eye resume duration
        setResumeEyesAnimDuration();
        scanFailAnimSet.start();
    }

    /**
     * 扫描完成
     */
    public void startScanOkAnim() {
        cancelAnim();

        if (null == scanOkAnimSet) {
            scanOkAnimSet = new AnimatorSet();

            Interpolator interpolator = new AccelerateDecelerateInterpolator();
//            Interpolator it = LeAccelerateDecelerateDampingInterpolator.getDefaultInterpolator();

            // 眼眶放大
            ObjectAnimator eyesSocketScaleAnim = ObjectAnimator.ofFloat(this, HALF_EYE_SOCKET_HEIGHT_STR, halfEyesSocketWidth);
            eyesSocketScaleAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    startScanOkEyeAnim = true;
                }
            });
            initAnimator(eyesSocketScaleAnim, null, SCAN_OK_EYES_SOCKET_SCALE_ANIM_DURATION);

            // 眼珠缩小
            ObjectAnimator eyesScaleAnim = ObjectAnimator.ofFloat(this, EYES_SCALE, SCLAE_MAX, SCLAE_MIN);
            initAnimator(eyesScaleAnim, null, SCAN_OK_EYES_SCALE_ANIM_DURATION);

            // 锁放大
            ObjectAnimator lockScaleAnim = ObjectAnimator.ofFloat(this, LOCK_SCALE, SCLAE_MIN, SCLAE_MAX);
            lockScaleAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    startScanOkLockAnim = true;
                }
            });
            initAnimator(lockScaleAnim, null, SCAN_OK_LOCK_SOCKET_SCALE_ANIM_DURATION);
            lockScaleAnim.setStartDelay(SCAN_OK_LOCK_SOCKET_SCALE_ANIM_DELAY);

            // 光线变化
            final AnimatorSet scanOkRayAnimSet = new AnimatorSet();
            List<Animator> scanOkScaleAnimList = new ArrayList<Animator>();
            for (int i = 0; i < rays.length; i++) {
                Ray ray = rays[i];
                ObjectAnimator rayScaleAnim = ObjectAnimator.ofFloat(ray, Ray.LENGTH, 0, 13);
                rayScaleAnim.setDuration(SCAN_OK_RAY_ANIM_DURATION1);
                rayScaleAnim.setInterpolator(interpolator);
                if (i == 0) {
                    rayScaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            invalidate();
                        }
                    });
                    rayScaleAnim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            Ray.change = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (getCancelEndIsAble()) {
                                Ray.change = false;
                            }
                        }
                    });
                }
                scanOkScaleAnimList.add(rayScaleAnim);
            }
            for (int i = 0; i < rays.length; i++) {
                Ray ray = rays[i];
                ObjectAnimator rayScaleAnim = ObjectAnimator.ofFloat(ray, Ray.LENGTH, 0);
                rayScaleAnim.setStartDelay(SCAN_OK_RAY_ANIM_DURATION1);
                rayScaleAnim.setDuration(SCAN_OK_RAY_ANIM_DURATION2);
                rayScaleAnim.setInterpolator(interpolator);
                if (i == 0) {
                    rayScaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            invalidate();
                        }
                    });
                }
                scanOkScaleAnimList.add(rayScaleAnim);
            }
            scanOkRayAnimSet.playTogether(scanOkScaleAnimList);

            eyesSocketScaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (animation.getCurrentPlayTime() >= SCAN_OK_RAY_ANIM_DELAY) {
                        if (null != scanOkRayAnimSet && !scanOkRayAnimSet.isRunning()) {
                            scanOkRayAnimSet.start();
                        }
                    }
                }
            });

            scanOkAnimSet.playTogether(getResumeEyesAnimSet(), eyesSocketScaleAnim, eyesScaleAnim, lockScaleAnim);
        }
        // reset eye resume duration
        setResumeEyesAnimDuration();
        scanOkAnimSet.start();
    }

    /**
     * 眼睛复位
     */
    public void resetAnim() {
        cancelAnim();
        eyesResumeAnimSet = getResumeEyesAnimSet();
        setResumeEyesAnimDuration();
        eyesResumeAnimSet.start();
    }

    /**
     * 眼睛形状恢复动画
     */
    private ObjectAnimator eyesSizeResumeAnim;
    /**
     * 眼睛位置恢复动画
     */
    private ObjectAnimator eyesPosResumeAnim;

    /**
     * 眼睛恢复正常
     *
     * @return
     */
    private AnimatorSet getResumeEyesAnimSet() {
        if (null == eyesResumeAnimSet) {
            eyesResumeAnimSet = new AnimatorSet();
            eyesSizeResumeAnim = ObjectAnimator.ofFloat(this, HALF_EYE_SOCKET_HEIGHT_STR, HALF_EYE_SOCKET_HEIGHT);
            eyesPosResumeAnim = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0);

            eyesResumeAnimSet.playTogether(eyesSizeResumeAnim, eyesPosResumeAnim);
        }
        return eyesResumeAnimSet;
    }

    /**
     * 设置恢复时间
     */
    private void setResumeEyesAnimDuration() {
        int eyesSizeResumeDuration = (int) (Math.abs(SCLAE_MAX - getHalfEyesSocketHeight() / HALF_EYE_SOCKET_HEIGHT) * EYES_RESUME_ANIM_DURATION);
        if (null != eyesSizeResumeAnim) {
            eyesSizeResumeAnim.setDuration(eyesSizeResumeDuration);
        }
        int eyesPosResumeDuration = (int) (Math.abs(getTranslationX()) / MAX_OFFSET * EYES_RESUME_ANIM_DURATION);
        if (null != eyesPosResumeAnim) {
            eyesPosResumeAnim.setDuration(eyesPosResumeDuration);
            eyesPosResumeAnim.setFloatValues(getTranslationX(), 0);
        }
    }

    private void cancelAnim() {
        setCancelAnimSet(scanEyeCloseAnim);
        setCancelAnimSet(scanEyeOpenAnim);
        setCancelAnimSet(scanAnimSet);
        setCancelAnimSet(scanFailAnimSet);
        setCancelAnimSet(scanOkAnimSet);

        startScanOkEyeAnim = false;
        startScanOkLockAnim = false;
        eyesScale = SCLAE_MAX;
        lockScale = SCLAE_MIN;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = width;
        params.height = height;
        setLayoutParams(params);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //int sc = canvas.saveLayer(0, 0, width, height, null,
        //        Canvas.MATRIX_SAVE_FLAG |
        //                Canvas.CLIP_SAVE_FLAG |
        //                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
        //                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
        //                Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        // 眼眶
        float left = centerX - halfEyesSocketWidth;
        float right = centerX + halfEyesSocketWidth;
        float top = centerY - halfEyesSocketHeight;
        float bottom = centerX + halfEyesSocketHeight;
        rf.set(left, top, right, bottom);
        canvas.drawArc(rf, 0, 360, true, eyesSocketPaint);

        // 眼珠
        if (startScanOkEyeAnim) {
            canvas.save();
            canvas.scale(eyesScale, eyesScale, centerX, centerY);
            canvas.drawBitmap(eyes, 0, 0, eyesPaint);
            canvas.restore();

            if (startScanOkLockAnim) {
                canvas.save();
                canvas.scale(lockScale, lockScale, centerX, centerY);
                canvas.drawBitmap(lock, 0, 0, lockPaint);
                canvas.restore();
                // 光线
                for (Ray ray : rays) {
                    ray.draw(canvas, null);
                }
            }
        } else {
            canvas.drawBitmap(eyes, 0, 0, eyesPaint);
        }

        //canvas.restoreToCount(sc);
    }

    public Animator initAnimator(Animator animator, Interpolator interpolator, int duration) {
        if (animator instanceof ObjectAnimator) {
            ((ObjectAnimator) animator).setAutoCancel(true);
        }
        if (null != interpolator) {
            animator.setInterpolator(interpolator);
        }
        animator.setDuration(duration);
        return animator;
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

    public float getHalfEyesSocketHeight() {
        return halfEyesSocketHeight;
    }

    public void setHalfEyesSocketHeight(float halfEyesSocketHeight) {
        this.halfEyesSocketHeight = halfEyesSocketHeight;
        invalidate();
    }

    public float getEyesScale() {
        return eyesScale;
    }

    public void setEyesScale(float eyesScale) {
        this.eyesScale = eyesScale;
        invalidate();
    }

    public float getLockScale() {
        return lockScale;
    }

    public void setLockScale(float lockScale) {
        this.lockScale = lockScale;
    }

    private void setCancelAnimSet(Animator animator) {
        if (null != animator) {
            setCancelEndIsAble(false);
            animator.cancel();
            setCancelEndIsAble(true);
        }
    }

    public void setCancelEndIsAble(boolean enable) {
        cancelEndEnable = enable;
    }

    public boolean getCancelEndIsAble() {
        return cancelEndEnable;
    }

}
