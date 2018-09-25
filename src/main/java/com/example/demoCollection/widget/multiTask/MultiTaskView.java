package com.example.demoCollection.widget.multiTask;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.OverScroller;

public class MultiTaskView extends LinearLayout {
    private final String TAG = getClass().getSimpleName();
    private boolean DEBUG = true;

    public static final float DEFAULT_SCALE_SIZE = 1f;
    private static final int ON_RESTORE_DURATION = 700;

    private OverScroller mScroller;
    private float MAX_OVER_SCROLL_DISTANCE = 200;
    private boolean isOverScroll = false;

    private int mCurrentCenterIndex;

    public enum UseType {
        ABORT_ANIMATION, ON_SCROLL, ON_FLING, ACTION_UP_OR_CANCEL
    }

    public enum ScrollDirection {
        LEFT, RIGHT
    }

    public enum AutoCenterType {
        ON_DOWN_CENTER, ON_UP_CENTER
    }

    private GestureDetector mDetector;
    private int mTouchSlop;

    private AutoCenterHorizontalScrollView achsv1;
    private AutoCenterHorizontalScrollView achsv2;
    private int mDistanceX;
    private float mVelocityX;
    private float mVelocityY;

    public MultiTaskView(Context context) {
        this(context, null);
    }

    public MultiTaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiTaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        String INIT = TAG + " init";
        if (DEBUG) Log.i(TAG, INIT);

        setClipChildren(false);
        // 横排显示
        setOrientation(VERTICAL);
        // GestureDetector
        mDetector = new GestureDetector(getContext(), mSimpleOnGestureListener);
        mDetector.setOnDoubleTapListener(null);
        mDetector.setIsLongpressEnabled(false);
        // ViewConfiguration
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        // OverScroller
        mScroller = new OverScroller(getContext(), new DecelerateInterpolator());
    }

    @Override
    protected void onFinishInflate() {
        if (DEBUG) Log.i(TAG, "onFinishInflate");
        super.onFinishInflate();

        achsv1 = (AutoCenterHorizontalScrollView) getChildAt(0);
        achsv2 = (AutoCenterHorizontalScrollView) getChildAt(1);
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            String ON_DOWN = TAG + " onDown";
            if (DEBUG) Log.i(TAG, ON_DOWN);
            useScrollView(UseType.ABORT_ANIMATION);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            String ON_SCROLL = TAG + " onScroll ";
//            if (DEBUG) Log.i(TAG, ON_SCROLL);
            if (Math.abs(distanceX) > Math.abs(distanceY)) {    // 横向scroll
                mDistanceX = Math.round(distanceX);
                // 滑动方向
                ScrollDirection scrollDirection = mDistanceX < 0 ? ScrollDirection.RIGHT : ScrollDirection.LEFT;
                setScrollDirection(scrollDirection);

                int childScrollX = achsv1.getScrollX();
                // 回弹系数
                float scrollFactor;

                if (((childScrollX > 0 && childScrollX < achsv1.getMaxScrollX()) ||
                        (childScrollX == 0 && mDistanceX > 0) ||
                        (childScrollX == achsv1.getMaxScrollX() && mDistanceX < 0)) &&
                        (achsv1.getItemCount() != 1)) { // 正常滑动
                    if (DEBUG) Log.i(TAG, ON_SCROLL + " 正常滑动");
                    useScrollView(UseType.ON_SCROLL);
                } else if (mDistanceX > 0) {   // 向左移动
                    ON_SCROLL = ON_SCROLL + " 向左移动";
                    if (DEBUG) Log.i(TAG, ON_SCROLL);
                    isOverScroll = true;
                    if (getScrollX() >= 0 && childScrollX == achsv1.getMaxScrollX()) {   // 回弹
                        if (DEBUG) Log.i(TAG, ON_SCROLL + " 回弹");
                        scrollFactor = 1f - getScrollX() / MAX_OVER_SCROLL_DISTANCE;
                        scrollFactor = scrollFactor < 0 ? 0 : scrollFactor;
                        scrollBy(Math.round(mDistanceX * scrollFactor), 0);
                    } else if (getScrollX() < MAX_OVER_SCROLL_DISTANCE) {   // 手动复位
                        if (DEBUG) Log.i(TAG, ON_SCROLL + " 手动复位");
                        scrollBy(Math.round(mDistanceX), 0);
                    }
                } else {   // 向右移动
                    ON_SCROLL = ON_SCROLL + " 向右移动";
//                    if (DEBUG) Log.i(TAG, ON_SCROLL);
                    isOverScroll = true;
                    if ((getScrollX() <= 0 && childScrollX == 0)) {   // 回弹
//                        if (DEBUG) Log.i(TAG, ON_SCROLL + " 回弹");
                        scrollFactor = 1f - getScrollX() / -MAX_OVER_SCROLL_DISTANCE;
                        scrollFactor = scrollFactor < 0 ? 0 : scrollFactor;
                        scrollBy(Math.round(mDistanceX * scrollFactor), 0);
                    } else if (getScrollX() > -MAX_OVER_SCROLL_DISTANCE) {   // 手动复位
                        if (DEBUG) Log.i(TAG, ON_SCROLL + " 手动复位");
                        scrollBy(Math.round(mDistanceX), 0);
                    }
                }
            } else {    // 竖直Scroll
                // TODO
                if (DEBUG) Log.i(TAG, ON_SCROLL + " 竖直scroll");
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            String ON_FLING = TAG + " onFling ";
            if (DEBUG) Log.i(TAG, ON_FLING + " velocityX " + velocityX + " velocityY " + velocityY);
            mVelocityX = velocityX;
            mVelocityY = velocityY;
            if (Math.abs(velocityX) > Math.abs(velocityY) && !isOverScroll) {
                useScrollView(UseType.ON_FLING);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    private float mInitialX;
//    private float mInitialY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        String ON_INTERCEPT_TOUCH_EVENT = TAG + " onInterceptTouchEvent ";
//        if (DEBUG) Log.i(TAG, ON_INTERCEPT_TOUCH_EVENT + MotionEvent.actionToString(ev.getAction()));
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialX = ev.getX();
                mDetector.onTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = Math.round(ev.getX() - mInitialX);
                if (Math.abs(distanceX) > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String ON_TOUCH_EVENT = TAG + " onTouchEvent ";
//        if (DEBUG) Log.i(TAG, ON_TOUCH_EVENT + MotionEvent.actionToString(event.getAction()));
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isOverScroll) { // 是否需要回弹
                    if (DEBUG) Log.i(TAG, ON_TOUCH_EVENT + " -getScrollX() " + -getScrollX());
                    mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
                    invalidate();
                } else {
                    useScrollView(UseType.ACTION_UP_OR_CANCEL);
                }
                break;
        }
        return mDetector.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        // 设置移动比例
        achsv1.setScaleSize(DEFAULT_SCALE_SIZE);
        achsv2.setScaleSize(achsv2.getItemDistance() / (float) achsv1.getItemDistance());

//        useScrollView(UseType.ABORT_ANIMATION);
//        setCenterPage(mCurrentCenterIndex);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int x = mScroller.getCurrX();

            if (oldX != x) {
                scrollTo(x, 0);
            }
            invalidate();
        }
    }

    private void useScrollView(UseType useType) {
        String USE_SCROLL_VIEW = TAG + " useScrollView";
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            AutoCenterHorizontalScrollView achsv = (AutoCenterHorizontalScrollView) getChildAt(i);
            switch (useType) {
                case ABORT_ANIMATION:   // 打断
                    if (DEBUG) Log.i(TAG, USE_SCROLL_VIEW + " ABORT_ANIMATION ");
                    isOverScroll = false;
                    achsv.abortAnim();
                    // 打断后 up事件无效 调用居中
                    achsv.autoCenter(AutoCenterType.ON_DOWN_CENTER, 500);
                    setScrollViewCenterIndex(achsv);
                    break;
                case ON_SCROLL: // 滑动
                    isOverScroll = false;
                    if (getScrollX() != 0) {
                        setScrollX(0);
                    }
                    achsv.scrollBy(Math.round(mDistanceX * achsv.getScaleSize()), 0);
                    break;
                case ON_FLING:  // fling
                    if (DEBUG) Log.i(TAG, USE_SCROLL_VIEW + " ON_FLING ");
                    isOverScroll = false;
                    achsv.onFling(mVelocityX, mVelocityY);
                    setScrollViewCenterIndex(achsv);
                    break;
                case ACTION_UP_OR_CANCEL:   // up or cancel
                    if (DEBUG)
                        Log.i(TAG, USE_SCROLL_VIEW + " ACTION_UP_OR_CANCEL " + achsv1.getScrollX() + " \n\n");
                    achsv.autoCenter(AutoCenterType.ON_UP_CENTER, ON_RESTORE_DURATION);
                    setScrollViewCenterIndex(achsv);
                    break;
            }
        }
    }

    /**
     * 设置当前居中位置
     *
     * @param achsv
     */
    private void setScrollViewCenterIndex(AutoCenterHorizontalScrollView achsv) {
        String SET_SCROLL_VIEW_CENTER_INDEX = TAG + " setScrollViewCenterIndex ";
        if (DEBUG) Log.i(TAG, SET_SCROLL_VIEW_CENTER_INDEX);
        if (achsv.getScaleSize() == DEFAULT_SCALE_SIZE) {
            mCurrentCenterIndex = achsv.getCenterIndex();
            if (DEBUG) Log.e(TAG, SET_SCROLL_VIEW_CENTER_INDEX + mCurrentCenterIndex);
            achsv2.setCenterIndex(mCurrentCenterIndex);
        }
    }

    public void setCenterPage(int index) {
        if (DEBUG) Log.i(TAG, "setCenterPage " + index);
        achsv1.setCenterIndex(index);
        achsv2.setCenterIndex(index);
        achsv1.scrollTo(achsv1.getItemDistance() * index, 0);
        achsv2.scrollTo(achsv2.getItemDistance() * index, 0);
    }

    private void setScrollDirection(ScrollDirection scrollDirection) {
        achsv1.setScrollDirection(scrollDirection);
        achsv2.setScrollDirection(scrollDirection);
    }

    public void setMaxOverScroll(int distance) {
        MAX_OVER_SCROLL_DISTANCE = distance;
    }
}
