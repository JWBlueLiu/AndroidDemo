package com.example.demoCollection.demo;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by l on 15-2-28.
 */
public class SubButton extends LinearLayout {
    public SubButton(Context context) {
        this(context, null);
    }

    public SubButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    ViewDragHelper vdh;

    private void init() {
        vdh = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View view, int pointerId) {
//          return mCanDragView == view;
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
//                System.out.println("left = " + left + ", dx = " + dx);
//
//                // 两个if主要是为了让viewViewGroup里
//                if (getPaddingLeft() > left) {
//                    return getPaddingLeft();
//                }
//
//                if (getWidth() - child.getWidth() < left) {
//                    return getWidth() - child.getWidth();
//                }

                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
//                // 两个if主要是为了让viewViewGroup里
//                if (getPaddingTop() > top) {
//                    return getPaddingTop();
//                }
//
//                if (getHeight() - child.getHeight() < top) {
//                    return getHeight() - child.getHeight();
//                }

                return top;
            }
//

            /**
             * 当拖拽到状态改变时回调
             * @params 新的状态
             */
            @Override
            public void onViewDragStateChanged(int state) {
                switch (state) {
                    case ViewDragHelper.STATE_DRAGGING:  // 正在被拖动
                        break;
                    case ViewDragHelper.STATE_IDLE:  // view没有被拖拽或者 正在进行fling/snap
                        break;
                    case ViewDragHelper.STATE_SETTLING: // fling完毕后被放置到一个位置
                        break;
                }
                super.onViewDragStateChanged(state);
            }

        });
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                vdh.abort(); // 相当于调用 processTouchEvent收到ACTION_CANCEL
                break;
            default:
                vdh.cancel();
                break;
        }

        /**
         * 检查是否可以拦截touch事件
         * 如果onInterceptTouchEvent可以return true 则这里return true
         */
        return vdh.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 处理拦截到的事件
         * 这个方法会在返回前分发事件
         */
        vdh.processTouchEvent(event);
        return true;
    }
}
