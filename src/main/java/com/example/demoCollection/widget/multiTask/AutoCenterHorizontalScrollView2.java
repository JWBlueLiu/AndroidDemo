package com.example.demoCollection.widget.multiTask;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class AutoCenterHorizontalScrollView2 extends FrameLayout {
    private final String TAG = getClass().getSimpleName();
    private boolean DEBUG = true;

    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;
    private int LANDSCAPE_ITEM_SPACE;
    /**
     * 条目宽度
     */
    private int ITEM_WIDTH;
    /**
     * 条目间距
     */
    private int mItemPadding;
    /**
     * 边缘间距
     */
    private int mEdgePadding;
    // mItemPadding + ITEM_WIDTH
    private int mItemDistance;
    private static final int ON_RESTORE_DURATION = 400;

    private LinearLayout childLinearLayout;
    private ShapeDrawable shapeDrawable;

    private GestureDetector mDetector;
    private OverScroller mScroller;
    private Field fieldScrollerX;
    private Object mScrollerX;
    private Method methodGetSplineFlingDistance;
    private Method methodGetSplineFlingDuration;
    private boolean isOnHorizontalScrollUp = false;

    private int mTouchSlop;
    private int mMaxVelocity;
    private int mMinVelocity;

    public AutoCenterHorizontalScrollView2(Context context) {
        this(context, null);
    }

    public AutoCenterHorizontalScrollView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoCenterHorizontalScrollView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        // 获取屏幕宽高
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        SCREEN_WIDTH = Math.min(wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getHeight());
        SCREEN_HEIGHT = Math.max(wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getHeight());
        // 横屏间距20dp
        LANDSCAPE_ITEM_SPACE = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20,
                getResources().getDisplayMetrics());

        // 设置padding
        setClipToPadding(false);
        // 设置LinearLayout
        childLinearLayout = new LinearLayout(getContext());
        childLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        childLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
        addView(childLinearLayout);

        // 设置Item间距
        shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setColor(Color.TRANSPARENT);
        childLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        // GestureDetector
        mDetector = new GestureDetector(getContext(), mSimpleOnGestureListener);
        mDetector.setOnDoubleTapListener(null);
        mDetector.setIsLongpressEnabled(true);
        // OverScroller
        mScroller = new OverScroller(getContext(), new DecelerateInterpolator());
        try {
            // field mScrollerX
            fieldScrollerX = OverScroller.class.getDeclaredField("mScrollerX");
            fieldScrollerX.setAccessible(true);
            mScrollerX = fieldScrollerX.get(mScroller);
            // method getSplineFlingDistance
            methodGetSplineFlingDistance = mScrollerX.getClass().getDeclaredMethod(
                    "getSplineFlingDistance",
                    int.class);
            methodGetSplineFlingDistance.setAccessible(true);
            // method getSplineFlingDuration
            methodGetSplineFlingDuration = mScrollerX.getClass().getDeclaredMethod(
                    "getSplineFlingDuration",
                    int.class);
            methodGetSplineFlingDuration.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ViewConfiguration
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initLayout();
            }
        });
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            if (DEBUG) Log.i(TAG, "onDown");
            if (!mScroller.isFinished()) {
                if (DEBUG) Log.i(TAG, "onDown abortAnimation");
                mScroller.abortAnimation();
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (DEBUG) Log.i(TAG, "onScroll");
            if (Math.abs(distanceX) > Math.abs(distanceY)) {    // 横向scroll
                if (DEBUG) Log.i(TAG, "onScroll X");
                scrollBy(Math.round(distanceX), 0);
                isOnHorizontalScrollUp = true;
            } else {
                // TODO
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (DEBUG) Log.i(TAG, "onFling");
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                // 滑行距离
                int flingDistance = 0;
                // 滑行时间
                int flingDuration = 0;

                if (DEBUG) Log.i(TAG, "velocityX:" + velocityX + " mMaxVelocity:" + mMaxVelocity);
                velocityX = Math.abs(velocityX) > mMaxVelocity ?
                        (velocityX / Math.abs(velocityX) * mMaxVelocity) :
                        Math.round(velocityX);

                try {
                    flingDistance = (int) Math.round((Double) methodGetSplineFlingDistance.invoke(mScrollerX, Math.round(velocityX)));
                    flingDistance = (velocityX < 0 ? flingDistance : -flingDistance);
                    flingDistance = flingDistance + getOffsetDistance(flingDistance % mItemDistance);
                    flingDuration = (int) methodGetSplineFlingDuration.invoke(mScrollerX, Math.round(velocityX));

                    if (DEBUG)
                        Log.i(TAG, "ITEM_WIDTH:" + ITEM_WIDTH + " mItemPadding:" + mItemPadding + " flingDistance:" + flingDistance + " flingDuration:" + flingDuration);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int dest = flingDistance + getScrollX();
                if (dest < 0 || dest > getMaxScrollX()) {
                    mScroller.fling(
                            getScrollX(), 0,
                            Math.round(-velocityX), 0,
                            0, getMaxScrollX(),
                            0, 0,
                            200, 0);
                } else {
                    mScroller.startScroll(getScrollX(), 0, flingDistance, 0, flingDuration);
                }
                invalidate();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    public int getMaxScrollX() {
        return (childLinearLayout.getChildCount() - 1) * (mItemPadding);
    }

    private void initLayout() {
        // 获取第一个item
        ViewGroup vg = (ViewGroup) getChildAt(0);
        View v = vg.getChildAt(0);
        if (null != v) {
            // item 间距
            ITEM_WIDTH = v.getWidth();
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
                mItemPadding = LANDSCAPE_ITEM_SPACE;    // 20dp
                mEdgePadding = Math.round((SCREEN_HEIGHT - ITEM_WIDTH) / 2f);
                if (DEBUG) Log.i(TAG, "onLayout 横屏" + mEdgePadding);
            } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {    // 竖屏
                mItemPadding = Math.round(SCREEN_WIDTH / 24.0f);
                mEdgePadding = Math.round((SCREEN_WIDTH - ITEM_WIDTH) / 2f);
                if (DEBUG)
                    Log.i(TAG, "onLayout 竖屏" + mEdgePadding + " SCREEN_WIDTH " + SCREEN_WIDTH + " ITEM_WIDTH " + ITEM_WIDTH);
            }
            setItemSpacing(mItemPadding);
            mItemDistance = ITEM_WIDTH + mItemPadding;
            // 设置左右padding
            setPadding(mEdgePadding, 0, mEdgePadding, 0);
        }
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

    @Override
    public void scrollTo(int x, int y) {
        // we rely on the fact the View.scrollBy calls scrollTo.
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            x = clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), child.getWidth());
            if (x != getScrollX()) {
                super.scrollTo(x, 0);
            }
        }
    }

    private int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            return 0;
        }
        if ((my + n) > child) {
            return child - my;
        }
        return n;
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        int childWidthMeasureSpec;
        int childHeightMeasureSpec;

        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.leftMargin + lp.rightMargin, MeasureSpec.UNSPECIFIED);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    private float mInitialX;
    private float mInitialY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialX = ev.getX();
                mInitialY = ev.getY();

                mDetector.onTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = Math.round(ev.getX() - mInitialX);
                int distanceY = Math.round(ev.getY() - mInitialY);
                if (Math.abs(distanceX) > mTouchSlop || Math.abs(distanceY) > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                // onScroll up事件
                if (isOnHorizontalScrollUp) {
                    if (DEBUG) Log.i(TAG, "onTouchEvent isOnHorizontalScrollUp");
                    int scrollDistance = getOffsetDistance(0);
                    int scrollDuration = Math.round(Math.abs(scrollDistance) / (float) mItemDistance / 2 * ON_RESTORE_DURATION);
                    if (DEBUG)
                        Log.i(TAG, "onTouchEvent isOnHorizontalScrollUp scrollDistance:" + scrollDistance);
                    mScroller.startScroll(getScrollX(), 0, scrollDistance, 0, scrollDuration);
                    invalidate();
                }
                isOnHorizontalScrollUp = false;
                break;
        }
        return mDetector.onTouchEvent(event);
    }

    public void addChild(View view) {
        childLinearLayout.addView(view);
    }

    public void addViews(List<View> views) {
        if (null != views && views.size() > 0) {
            for (View v : views) {
                addChild(v);
            }
        }
    }

    private void setItemSpacing(int padding) {
        shapeDrawable.setIntrinsicWidth(padding);
        childLinearLayout.setDividerDrawable(shapeDrawable);
    }

    private int getOffsetDistance(int scrollX) {
        int scrollOffset = (getScrollX() + scrollX) % mItemDistance;
        int halfItemDistance = Math.round(mItemDistance / 2f);
        return scrollOffset > halfItemDistance ? (mItemDistance - scrollOffset) : -scrollOffset;
    }

}
