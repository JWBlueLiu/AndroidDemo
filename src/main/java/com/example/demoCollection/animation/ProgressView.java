package com.example.demoCollection.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.example.demoCollection.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ProgressView extends View {

    private static final String TAG = "ProgressView";

    private static final String PROGRESS = "progress";
    private static final String MOVE_PROGRESS = "moveProgress";

    /**
     * 间隔延迟时间
     */
    private static final int PROGRESS_ANIM_DELAY = 300;
    /**
     * 结束时间
     */
    private static final int FINISH_ANIM_DURATION = 5000;
    /**
     * 光波时间
     */
    private static final int HALO_ANIM_DURATION = 1500;

    private AnimatorSet progressViewAnimSet;
    private AnimatorSet progressViewFinishAnimSet;
    private ObjectAnimator haloAnim;
    private Interpolator cInterpolator;

    private Shape shape;
    private int width;
    private int height;
    private int mColor = Color.parseColor("#00BD9C");
    private Paint mProgressPaint;
    private float progress = 0;
    private float moveProgress = 0;

    // 13% width
    private int haloLength;

    private Random r;
    private Drawable halo;

    private boolean cancelEndEnable = true;

    private OnProgressChangeListener mProgresChangeListener;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setDither(true);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setColor(mColor);

        cInterpolator = AnimationUtils.loadInterpolator(
                getContext(),
                R.interpolator.le_c_interpolator);
        r = new Random();

        shape = new RoundRectShape(null, new RectF(), null);

        halo = getResources().getDrawable(R.drawable.halo);
    }

    /**
     * 开始动画
     */
    public void startAnim() {
        reset();

        List<Integer> list = getRandomIntArr();

        int num1 = list.get(0);
        int num2 = list.get(1);
        int num3 = list.get(2);
        int num4 = list.get(3);
        // 使progress幅度更大
        float progress1 = (r.nextInt(num1 - 15) + 15) / 100.0f * width;
        float progress2 = (num1 + r.nextInt(num2 - 10) + 10) / 100.0f * width;
        float progress3 = (num1 + num2 + r.nextInt(num3)) / 100.0f * width;
        float progress4 = (num1 + num2 + num3 + r.nextInt(num4)) / 100.0f * width;

        AnimatorSet progressAnimSet = new AnimatorSet();
        ObjectAnimator progressAnim1 = ObjectAnimator.ofFloat(this, PROGRESS, 0, progress1);
        // 1到1.5秒随机
        initAnimator(progressAnim1, cInterpolator, 0, 2000);
        ObjectAnimator progressAnim2 = ObjectAnimator.ofFloat(this, PROGRESS, progress1, progress2);
        initAnimator(progressAnim2, cInterpolator, PROGRESS_ANIM_DELAY, getRandomDuration());
        Interpolator dInterpolator = AnimationUtils.loadInterpolator(
                getContext(),
                R.interpolator.le_d_interpolator);
        ObjectAnimator progressAnim3 = ObjectAnimator.ofFloat(this, PROGRESS, progress2, progress3);
        initAnimator(progressAnim3, dInterpolator, PROGRESS_ANIM_DELAY, getRandomDuration());
        ObjectAnimator progressAnim4 = ObjectAnimator.ofFloat(this, PROGRESS, progress3, progress4);
        initAnimator(progressAnim4, cInterpolator, PROGRESS_ANIM_DELAY, getRandomDuration());

        progressAnimSet.playSequentially(progressAnim1, progressAnim2, progressAnim3, progressAnim4);

        progressViewAnimSet = new AnimatorSet();
        progressViewAnimSet.playTogether(progressAnimSet, haloAnim);

        progressViewAnimSet.start();
    }

    /**
     * 结束动画
     *
     * @param listenerAdapter
     */
    public void startFinishAnim(AnimatorListenerAdapter listenerAdapter) {
        setCancelAnim(progressViewAnimSet);
        setMoveProgress(0);

        progressViewFinishAnimSet = new AnimatorSet();
        ObjectAnimator finishAnim = ObjectAnimator.ofFloat(this, PROGRESS, width);
        initAnimator(finishAnim, cInterpolator, Math.round((1.00f - getProgress() / width) * FINISH_ANIM_DURATION));
        if (null != listenerAdapter) {
            finishAnim.addListener(listenerAdapter);
        }
        progressViewFinishAnimSet.playTogether(finishAnim, haloAnim);

        progressViewFinishAnimSet.start();
    }

    /**
     * 取消动画
     */
    public void cancelAnim() {
        setCancelAnim(progressViewAnimSet);
        setCancelAnim(progressViewFinishAnimSet);
    }

    /**
     * 重置
     */
    public void reset() {
        cancelAnim();

        setProgress(0);
        setMoveProgress(0);
    }

    private void setCancelAnim(Animator animator) {
        if (null != animator && animator.isRunning()) {
            setCancelEndIsAble(false);
            animator.cancel();
            setCancelEndIsAble(true);
        }
    }

    /**
     * 获取随机数组
     *
     * @return
     */
    private List<Integer> getRandomIntArr() {
        Integer[] srcArr = new Integer[]{20, 20, 25, 25};
        List<Integer> srcList = new ArrayList<Integer>(Arrays.asList(srcArr));
        int length = srcArr.length;
        List<Integer> dstList = new ArrayList<Integer>();
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            int n = r.nextInt(length - i);
            dstList.add(srcList.get(n));
            srcList.remove(n);
        }
        return dstList;
    }

    public void setProgressColor(int color) {
        mColor = color;
        mProgressPaint.setColor(mColor);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();

        haloLength = Math.round(width / 13.0f);

        haloAnim = ObjectAnimator.ofFloat(this, MOVE_PROGRESS, 0, width);
        initAnimator(haloAnim, cInterpolator, HALO_ANIM_DURATION);
        haloAnim.setRepeatMode(ValueAnimator.RESTART);
        haloAnim.setRepeatCount(ValueAnimator.INFINITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        shape.draw(canvas, mProgressPaint);
        if (getMoveProgress() <= getProgress()) {
            halo.draw(canvas);
        }
    }

    /**
     * 监听进度状态变化
     *
     * @param progresChangeListener
     */
    public void setOnProgressChangeListener(OnProgressChangeListener progresChangeListener) {
        mProgresChangeListener = progresChangeListener;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        shape.resize(Math.round(progress), height);
        if (null != mProgresChangeListener) {
            int p = Math.round(progress / width * 100);
            if (p <= 100) {
                mProgresChangeListener.onProgressChange(p);
            }
        }
        invalidate();
    }

    public float getMoveProgress() {
        return moveProgress;
    }

    public void setMoveProgress(float moveProgress) {
        this.moveProgress = moveProgress;
        halo.setBounds(Math.round(moveProgress - haloLength), 0, Math.round(moveProgress), getHeight());
        invalidate();
    }

    public void setCancelEndIsAble(boolean enable) {
        cancelEndEnable = enable;
    }

    public boolean getCancelEndIsAble() {
        return cancelEndEnable;
    }

    public Animator initAnimator(Animator animator, Interpolator interpolator, int duration) {
        return initAnimator(animator, interpolator, 0, duration);
    }

    public Animator initAnimator(Animator animator, Interpolator interpolator, int delay, int duration) {
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

    private int mFrom = 2500;
    private int mTo = 3000;

    /**
     * 设置进度随机时间
     *
     * @param from
     * @param to
     */
    public void setRandomDuration(int from, int to) {
        this.mFrom = from;
        this.mTo = to;
    }

    private int getRandomDuration() {
        return (r.nextInt(mTo - mFrom) + mFrom);
    }

    public interface OnProgressChangeListener {
        public void onProgressChange(int progress);
    }

}
