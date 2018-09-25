package com.example.demoCollection.widget.multiTask;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AutoCenterHorizontalScrollView extends FrameLayout {
    public final String TAG_LOG = "MultiTaskView";
    public final String TAG = getClass().getSimpleName();
    private boolean DEBUG = true;

    // 反射变量 & 方法
    private static final String FIELD_SCROLLER_X = "mScrollerX";
    private static final String METHOD_GET_SPLINE_FLING_DISTANCE = "getSplineFlingDistance";
    private static final String METHOD_GET_SPLINE_FLING_DURATION = "getSplineFlingDuration";

    /**
     * 边缘间距
     */
    private int mItemPadding;
    private int mItemWidth;

    private LinearLayout mChildLinearLayout;

    private OverScroller mScroller;
    private Field fieldScrollerX;
    private Object mScrollerX;
    private Method methodGetSplineFlingDistance;
    private Method methodGetSplineFlingDuration;
    private float mScaleSize;

    private int mCenterIndex = 0;
    /**
     * onScroll方向
     */
    private MultiTaskView.ScrollDirection mScrollDirection;

    public AutoCenterHorizontalScrollView(Context context) {
        this(context, null);
    }

    public AutoCenterHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoCenterHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        String INIT = TAG + " init";
        Log.i(TAG_LOG, INIT);
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
                String ON_CHILD_VIEW_REMOVED = TAG + " onChildViewRemoved " + AutoCenterHorizontalScrollView.this.toString();
                // 判断last item的x是否在中间复位
                View lastChild = mChildLinearLayout.getChildAt(getItemCount() - 1);
                if (null != lastChild) {
                    int[] position = new int[2];
                    lastChild.getLocationOnScreen(position);
                    if (DEBUG) Log.i(TAG_LOG, ON_CHILD_VIEW_REMOVED +
                            " x " + position[0] +
                            " Math.round(getItemWidth() / 2f) " + Math.round(getItemWidth() / 2f) +
                            " Math.round(getWidth() / 2f) " + Math.round(getWidth() / 2f));
                    if ((position[0] + Math.round(getItemWidth() / 2f)) <= Math.round(getWidth() / 2f)) {
                        if (DEBUG) Log.i(TAG_LOG, ON_CHILD_VIEW_REMOVED + "startScroll");
                        int restoreDistance = Math.round(getWidth() / 2f) - position[0] - Math.round(getItemWidth() / 2f);
                        mScroller.startScroll(getScrollX(), 0, restoreDistance, 0);
                        invalidate();
                    }
                }
            }
        });
        addView(mChildLinearLayout);
        // OverScroller
        mScroller = new OverScroller(getContext(), new DecelerateInterpolator());
        try {
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
    }

    public void abortAnim() {
        String ABORT_ANIM = TAG + " abortAnim " + toString();
        if (!mScroller.isFinished()) {
            if (DEBUG) Log.i(TAG_LOG, ABORT_ANIM);
            mScroller.abortAnimation();
        }
    }

    public void onFling(float velocityX, float velocityY) {
        String ON_FLING = TAG + " onFling " + toString();
        if (DEBUG) Log.i(TAG_LOG, ON_FLING);
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            // 滑行距离
            int flingDistance = 0;
            // 滑行时间
            int flingDuration = 0;

            try {
                if (getScaleSize() == MultiTaskView.DEFAULT_SCALE_SIZE) {
                    flingDistance = (int) Math.round((Double) methodGetSplineFlingDistance.invoke(mScrollerX, Math.round(velocityX)));
                    flingDistance = Math.round(-Math.signum(velocityX) * flingDistance);
                    if (Math.abs(flingDistance) < getItemDistance()) {  // 滑动距离过短 自动回位
                        if (DEBUG) Log.i(TAG_LOG, ON_FLING + " return ");
                        return;
                    }
                    flingDistance = flingDistance + getOffsetDistance(flingDistance);
                    //  设置滑动中心点
                    setCenterIndex((getScrollX() + flingDistance) / getItemDistance());
                    if (DEBUG)
                        Log.e(TAG_LOG, ON_FLING + " getCenterIndex onFling " + getCenterIndex());
                } else {
                    flingDistance = getCenterIndex() * getItemDistance() - getScrollX();
                    if (Math.abs(flingDistance) < getItemDistance()) {  // 滑动距离过短 自动回位
                        if (DEBUG) Log.i(TAG_LOG, ON_FLING + " return ");
                        return;
                    }
                }
                flingDuration = (int) methodGetSplineFlingDuration.invoke(mScrollerX, Math.round(velocityX));

                if (DEBUG)
                    Log.i(TAG_LOG, ON_FLING + " flingDistance:" + flingDistance + " flingDuration:" + flingDuration);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            int dest = flingDistance + getScrollX();
//            if (dest < 0 || dest > getMaxScrollX()) {
//                mScroller.fling(
//                        getScrollX(), 0,
//                        Math.round(-velocityX), 0,
//                        0, getMaxScrollX(),
//                        0, 0,
//                        200, 0);
//            } else {
            mScroller.startScroll(getScrollX(), 0, flingDistance, 0, flingDuration);
//            }
            invalidate();
        }
    }

    /**
     * 自动居中
     */
    public void autoCenter(MultiTaskView.AutoCenterType autoCenterType, int duration) {
        String ON_ACTION_UP_CENTER = TAG + " autoCenter " + toString();
        // onScroll up事件
        if (DEBUG) Log.i(TAG_LOG, ON_ACTION_UP_CENTER);
        int scrollDistance;
        if (getScaleSize() == MultiTaskView.DEFAULT_SCALE_SIZE) {
            if (autoCenterType == MultiTaskView.AutoCenterType.ON_UP_CENTER) {
                scrollDistance = centerMove();
            } else {
                scrollDistance = getOffsetDistance(0);
            }
            setCenterIndex((getScrollX() + scrollDistance) / getItemDistance());
            Log.i(TAG_LOG, ON_ACTION_UP_CENTER + " getCenterIndex " + getCenterIndex());
        } else {
            scrollDistance = getCenterIndex() * getItemDistance() - getScrollX();
            Log.i(TAG_LOG, ON_ACTION_UP_CENTER + " getCenterIndex " + getCenterIndex());
        }
        if (scrollDistance != 0) {
            int scrollDuration = Math.round(Math.abs(scrollDistance) / (float) getItemDistance() * duration);
            if (DEBUG)
                Log.i(TAG_LOG, ON_ACTION_UP_CENTER + " onTouchEvent isOnHorizontalScrollUp scrollDistance:" + scrollDistance + "\n\n");
            mScroller.startScroll(getScrollX(), 0, scrollDistance, 0, scrollDuration);
            invalidate();
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
            x = clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), mChildLinearLayout.getWidth());
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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int edgePadding = Math.round((getWidth() - getItemWidth()) / 2f);
        setPadding(edgePadding, 0, edgePadding, 0);
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
        String GET_OFFSET_DISTANCE = TAG + " getOffsetDistance " + toString();
        int scrollOffset = (getScrollX() + scrollX) % getItemDistance();
        int halfItemDistance = Math.round(getItemDistance() / 2f);
        if (scrollX < 0) {
            return scrollOffset >= halfItemDistance ? -scrollOffset : (getItemDistance() - scrollOffset);
        } else {
            return scrollOffset >= halfItemDistance ? (getItemDistance() - scrollOffset) : -scrollOffset;
        }
    }

    public int centerMove() {
        String CENTER_MOVE = TAG + " centerMove " + toString();
        int scrollOffset = getScrollX() % getItemDistance();
        int minMoveDistance = Math.round(getItemDistance() / 9f);

        if (null != mScrollDirection) {
            if (mScrollDirection == MultiTaskView.ScrollDirection.LEFT) {
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

    public int getItemDistance() {
        return getItemWidth() + getItemPadding();
    }

    public float getScaleSize() {
        return mScaleSize;
    }

    public void setScaleSize(float scaleSize) {
        this.mScaleSize = scaleSize;
    }

    public int getCenterIndex() {
        return mCenterIndex;
    }

    public void setCenterIndex(int mCenterIndex) {
        String SET_CENTER_INDEX = TAG + " setCenterIndex ";
        Log.i(TAG_LOG, SET_CENTER_INDEX + mCenterIndex);
        this.mCenterIndex = mCenterIndex;
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

//    public int getChildWidth() {
//        return getMaxScrollX() + getItemWidth();
//    }

    public int getMaxScrollX() {
        return (getItemCount() - 1) * getItemDistance();
    }

    public void setScrollDirection(MultiTaskView.ScrollDirection scrollDirection) {
        mScrollDirection = scrollDirection;
    }

    @Override
    public String toString() {
        return String.valueOf(mScaleSize);
    }

}
