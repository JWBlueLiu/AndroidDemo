package com.example.demoCollection.widget.multiTask;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;

public class MyButton extends Button {

    private GestureDetector mDetector;
    private int mTouchSlop;

    public MyButton(Context context) {
        this(context, null);
    }

    public MyButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        // GestureDetector
        mDetector = new GestureDetector(getContext(), mSimpleOnGestureListener);
        mDetector.setOnDoubleTapListener(null);
        mDetector.setIsLongpressEnabled(false);
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollBy(0, Math.round(distanceY));
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                scrollTo(0, 0);
                break;
        }
        return mDetector.onTouchEvent(event);
    }
}
