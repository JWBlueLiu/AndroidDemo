package com.example.demoCollection.widget.multiTask;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AutoCenterScrollView extends FrameLayout {
    public final String TAG = getClass().getSimpleName();
    private boolean DEBUG = true;

    private static final int ON_RESTORE_DURATION = 1000;
    // 反射变量 & 方法
    private static final String FIELD_SCROLLER_X = "mScrollerX";
    private static final String METHOD_GET_SPLINE_FLING_DISTANCE = "getSplineFlingDistance";
    private static final String METHOD_GET_SPLINE_FLING_DURATION = "getSplineFlingDuration";
    private static float OVER_SCROLL_DISTANCE;

    private GestureDetector mDetector;
    private Field fieldScrollerX;
    private Object mScrollerX;
    private Method methodGetSplineFlingDistance;
    private Method methodGetSplineFlingDuration;
    private OverScroller mScroller;

    /**
     * 边缘间距
     */
    private int mItemPadding;
    private int mItemWidth;
    private LinearLayout mChildLinearLayout;

    /**
     * onScroll方向
     */
    private ScrollDirection mScrollDirection;
    private boolean isOverScroll;
    private float MAX_OVER_SCROLL_DISTANCE = 400;

    public enum ScrollDirection {
        LEFT, RIGHT
    }

    public AutoCenterScrollView(Context context) {
        this(context, null);
    }

    public AutoCenterScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoCenterScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        String INIT = TAG + " init";
        Log.i(TAG, INIT);

        // 设置padding
        setClipToPadding(false);
        setClipChildren(false);
        // 设置LinearLayout
        mChildLinearLayout = new LinearLayout(getContext());
        mChildLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mChildLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
        mChildLinearLayout.setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                String ON_CHILD_VIEW_REMOVED = TAG + " onChildViewRemoved ";

                // 判断last item的x是否在中间复位
                View lastChild = mChildLinearLayout.getChildAt(getItemCount() - 1);
                if (null != lastChild) {
                    int[] position = new int[2];
                    lastChild.getLocationOnScreen(position);
                    if (DEBUG) Log.i(TAG, ON_CHILD_VIEW_REMOVED +
                            " x " + position[0] +
                            " Math.round(getItemWidth() / 2f) " + Math.round(getItemWidth() / 2f) +
                            " Math.round(getWidth() / 2f) " + Math.round(getWidth() / 2f));
                    if ((position[0] + Math.round(getItemWidth() / 2f)) < Math.round(getWidth() / 2f)) {
                        if (DEBUG) Log.i(TAG, ON_CHILD_VIEW_REMOVED + "startScroll <");
                        int restoreDistance = Math.round(getWidth() / 2f) - position[0] - Math.round(getItemWidth() / 2f);
                        mScroller.startScroll(getScrollX(), 0, -restoreDistance, 0);
                        invalidate();
                    } else if ((position[0] + Math.round(getItemWidth() / 2f)) == Math.round(getWidth() / 2f)) {
                        if (DEBUG) Log.i(TAG, ON_CHILD_VIEW_REMOVED + "startScroll =");
                        mScroller.startScroll(getScrollX(), 0, -getItemDistance(), 0);
                        invalidate();
                    }
                }
            }
        });
        addView(mChildLinearLayout);
        try {
            // GestureDetector
            mDetector = new GestureDetector(getContext(), mSimpleOnGestureListener);
            mDetector.setOnDoubleTapListener(null);
            mDetector.setIsLongpressEnabled(false);
            // ViewConfiguration
            ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
            mTouchSlop = viewConfiguration.getScaledTouchSlop();
            //
            Field fieldTouchSlopSquare = GestureDetector.class.getDeclaredField("mTouchSlopSquare");
            fieldTouchSlopSquare.setAccessible(true);
            fieldTouchSlopSquare.setInt(mDetector, mTouchSlop);
            // OverScroller
            mScroller = new OverScroller(getContext(), new DecelerateInterpolator(2f));
            // field mScrollerX
            fieldScrollerX = OverScroller.class.getDeclaredField(FIELD_SCROLLER_X);
            fieldScrollerX.setAccessible(true);
            mScrollerX = fieldScrollerX.get(mScroller);
            // method getSplineFlingDistance
            methodGetSplineFlingDistance = mScrollerX.getClass().getDeclaredMethod(
                    METHOD_GET_SPLINE_FLING_DISTANCE,
                    int.class);
            methodGetSplineFlingDistance.setAccessible(true);
            // method getSplineFlingDuration
            methodGetSplineFlingDuration = mScrollerX.getClass().getDeclaredMethod(
                    METHOD_GET_SPLINE_FLING_DURATION,
                    int.class);
            methodGetSplineFlingDuration.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 回弹距离
        OVER_SCROLL_DISTANCE = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {  // 打断动画
            String ON_DOWN = TAG + " onDown";
            if (DEBUG) Log.i(TAG, ON_DOWN);

            if (!mScroller.isFinished()) {
                if (DEBUG) Log.i(TAG, " abortAnim ");
                mScroller.abortAnimation();
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            String ON_SCROLL = TAG + " onScroll ";

            float tan = Math.abs(distanceX) / Math.abs(distanceY);
            // tan10 = 0.176
            if (tan > 0.176) {    // 横向scroll
                Log.i(TAG, "distanceX " + distanceX + " getScrollX " + getScrollX());

                // 滑动方向
                int scrollX = getScrollX();
                // 回弹系数
                float scrollFactor;
                if (((scrollX > 0 && scrollX < getMaxScrollX()) ||
                        (scrollX == 0 && distanceX > 0) ||
                        (scrollX == getMaxScrollX() && distanceX < 0)) &&
                        (getItemCount() != 1)) { // 正常滑动
                    if (DEBUG) Log.i(TAG, ON_SCROLL + " 正常滑动");

                    isOverScroll = false;
                    mScrollDirection = (distanceX > 0) ? ScrollDirection.LEFT : ScrollDirection.RIGHT;

                    scrollBy(Math.round(distanceX), 0);
                } else if (distanceX > 0) {   // 向左移动
                    ON_SCROLL = ON_SCROLL + " 向左移动";
                    if (DEBUG) Log.i(TAG, ON_SCROLL);

                    isOverScroll = true;
                    if (scrollX >= getMaxScrollX()) {   // 回弹
                        if (DEBUG) Log.i(TAG, ON_SCROLL + " 回弹");
                        scrollFactor = 1f - (scrollX - getMaxScrollX()) / MAX_OVER_SCROLL_DISTANCE;
                        scrollFactor = scrollFactor < 0 ? 0 : scrollFactor;
                        scrollBy(Math.round(distanceX * scrollFactor), 0);
                    } else if ((scrollX - getMaxScrollX()) < MAX_OVER_SCROLL_DISTANCE) {   // 手动复位
                        if (DEBUG) Log.i(TAG, ON_SCROLL + " 手动复位");
                        scrollBy(Math.round(distanceX), 0);
                    }
                } else {   // 向右移动
                    ON_SCROLL = ON_SCROLL + " 向右移动";
                    if (DEBUG) Log.i(TAG, ON_SCROLL);

                    isOverScroll = true;
                    if (scrollX <= 0) {   // 回弹
                        if (DEBUG) Log.i(TAG, ON_SCROLL + " 回弹");
                        scrollFactor = 1f - scrollX / -MAX_OVER_SCROLL_DISTANCE;
                        scrollFactor = scrollFactor < 0 ? 0 : scrollFactor;
                        scrollBy(Math.round(distanceX * scrollFactor), 0);
                    } else if (scrollX > -MAX_OVER_SCROLL_DISTANCE) {   // 手动复位
                        if (DEBUG) Log.i(TAG, ON_SCROLL + " 手动复位");
                        scrollBy(Math.round(distanceX), 0);
                    }
                }
                return true;
            } else {    // 竖直Scroll
                if (DEBUG) Log.i(TAG, ON_SCROLL + " 竖直scroll");
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            String ON_FLING = TAG + " onFling ";
            if (DEBUG) Log.i(TAG, ON_FLING + " velocityX " + velocityX + " velocityY " + velocityY);

            float tan = Math.abs(velocityX) / Math.abs(velocityY);
            // tan10 = 0.176
            if (tan > 0.176 && !isOverScroll) {
                mScrollDirection = (velocityX < 0) ? ScrollDirection.LEFT : ScrollDirection.RIGHT;
                startFling(velocityX);
                return true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    private void startFling(float velocityX) {
        String ON_FLING = TAG + " onFling ";
        if (DEBUG) Log.i(TAG, ON_FLING);

        // 滑行距离
        int flingDistance = 0;
        // 滑行时间
        int flingDuration = 0;
        try {
            flingDistance = (int) Math.round((Double) methodGetSplineFlingDistance.invoke(mScrollerX, Math.round(velocityX)));
            flingDistance = Math.round(-Math.signum(velocityX) * flingDistance);
            if (Math.abs(flingDistance) < getItemDistance()) {  // 滑动距离过短 自动回位
                if (DEBUG) Log.i(TAG, ON_FLING + " return ");
                autoCenter();
                return;
            }
            flingDistance = flingDistance + getOffsetDistance(flingDistance);
            flingDuration = (int) methodGetSplineFlingDuration.invoke(mScrollerX, Math.round(velocityX));
            if (DEBUG)
                Log.i(TAG, ON_FLING + " flingDistance:" + flingDistance + " flingDuration:" + flingDuration);
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
                    Math.round(OVER_SCROLL_DISTANCE), 0);
        } else {
            mScroller.startScroll(getScrollX(), 0, flingDistance, 0, flingDuration);
        }
        invalidate();
    }

    private float mInitialX;
    private int mTouchSlop;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        String ON_INTERCEPT_TOUCH_EVENT = TAG + " onInterceptTouchEvent ";
        if (DEBUG)
            Log.i(TAG, ON_INTERCEPT_TOUCH_EVENT + MotionEvent.actionToString(ev.getAction()));

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (DEBUG) Log.i(TAG, ON_INTERCEPT_TOUCH_EVENT + " isCenter() " + isCenter());

                mInitialX = ev.getX();
                mDetector.onTouchEvent(ev);
                if (!isCenter()) {  // item不居中则打断点击事件
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = Math.round(ev.getX() - mInitialX);
                if (Math.abs(distanceX) > mTouchSlop) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String ON_TOUCH_EVENT = TAG + " onTouchEvent ";
        if (DEBUG) Log.i(TAG, ON_TOUCH_EVENT + MotionEvent.actionToString(event.getAction()));

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isOverScroll) { // 是否需要回弹
                    if (DEBUG)
                        Log.i(TAG, ON_TOUCH_EVENT + " isOverScroll -getScrollX() " + -getScrollX());

                    if (getScrollX() < 0 || getScrollX() > getMaxScrollX()) {
                        int scrollDistance = getScrollX() < 0 ? -getScrollX() : (getMaxScrollX() - getScrollX());
                        mScroller.startScroll(getScrollX(), 0, scrollDistance, 0);
                        invalidate();
                    }
                } else {
                    autoCenter();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = Math.round(event.getX() - mInitialX);
                if (Math.abs(distanceX) > mTouchSlop) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return mDetector.onTouchEvent(event);
    }

    /**
     * 自动居中
     */
    private void autoCenter() {
        String ON_ACTION_UP_CENTER = TAG + " autoCenter ";
        if (DEBUG) Log.i(TAG, ON_ACTION_UP_CENTER);

        int scrollDistance = centerMove();
        if (scrollDistance != 0) {
            int scrollDuration = Math.round(Math.abs(scrollDistance) / (float) getItemDistance() * ON_RESTORE_DURATION);
            if (DEBUG) Log.i(TAG, ON_ACTION_UP_CENTER + " scrollDistance:" + scrollDistance);
            mScroller.startScroll(getScrollX(), 0, scrollDistance, 0, scrollDuration);
            invalidate();
        }
    }

    private int centerMove() {
        String CENTER_MOVE = TAG + " centerMove ";

        int scrollOffset = getScrollX() % getItemDistance();
        int minMoveDistance = Math.round(getItemWidth() / 8f);

        if (null != mScrollDirection) {
            if (mScrollDirection == ScrollDirection.LEFT) {
                if (scrollOffset > minMoveDistance) {
                    return getItemDistance() - scrollOffset;
                } else {
                    return -scrollOffset;
                }
            } else {
                if (scrollOffset < (getItemDistance() - minMoveDistance)) {
                    return -scrollOffset;
                } else {
                    return getItemDistance() - scrollOffset;
                }
            }
        }
        return 0;
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
        if (getChildCount() > 0) {
            if (x != getScrollX()) {
                super.scrollTo(x, 0);
            }
        }
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
    protected void measureChildWithMargins(View child,
                                           int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.leftMargin + lp.rightMargin, MeasureSpec.UNSPECIFIED);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int edgePadding = Math.round((getWidth() - getItemWidth()) / 2f);
        setPadding(edgePadding, 0, edgePadding, 0);
    }

    /**
     * item 是否居中
     *
     * @return
     */
    private boolean isCenter() {
        return getScrollX() % getItemDistance() == 0 ? true : false;
    }

    public void setItemPadding(int padding) {
        Drawable drawable = mChildLinearLayout.getDividerDrawable();
        if (null != drawable && drawable.getIntrinsicWidth() == padding) {
            return;
        }
        // 设置Item间距
        mItemPadding = padding;
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setColor(Color.TRANSPARENT);
        shapeDrawable.setIntrinsicWidth(padding);
        mChildLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        mChildLinearLayout.setDividerDrawable(shapeDrawable);
    }

    public int getItemPadding() {
        return mItemPadding;
    }

    private int getOffsetDistance(int scrollX) {
        String GET_OFFSET_DISTANCE = TAG + " getOffsetDistance ";

        int scrollOffset = (getScrollX() + scrollX) % getItemDistance();
        int halfItemDistance = Math.round(getItemDistance() / 2f);
        if (scrollX < 0) {
            return scrollOffset >= halfItemDistance ? -scrollOffset : (getItemDistance() - scrollOffset);
        } else {
            return scrollOffset >= halfItemDistance ? (getItemDistance() - scrollOffset) : -scrollOffset;
        }
    }

    public int getItemDistance() {
        return getItemWidth() + getItemPadding();
    }

    public int getItemWidth() {
        return mItemWidth;
    }

    public void setItemWidth(int width) {
        mItemWidth = width;
    }

    public int getItemCount() {
        return mChildLinearLayout.getChildCount();
    }

    public int getMaxScrollX() {
        return (getItemCount() - 1) * getItemDistance();
    }

    public void setMaxOverScroll(int distance) {
        MAX_OVER_SCROLL_DISTANCE = distance;
    }

    public void setCenterPage(int index) {
        if (DEBUG) Log.i(TAG, "setCenterPage " + index);
        scrollTo(getItemDistance() * index, 0);
    }

}
