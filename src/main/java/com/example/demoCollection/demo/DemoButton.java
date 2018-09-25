package com.example.demoCollection.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import com.example.demoCollection.common.logger.Log;

public class DemoButton extends LinearLayout {
    private final String TAG = getClass().getSimpleName();

    public DemoButton(Context context) {
        this(context, null);
    }

    public DemoButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.i(TAG, TAG + "11111");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, TAG);
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, TAG);
                break;
        }
        return super.onTouchEvent(event);
    }
}
