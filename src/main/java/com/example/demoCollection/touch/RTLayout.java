package com.example.demoCollection.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class RTLayout extends LinearLayout {
    public RTLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TouchActivity.TAG, "RTLayout---dispatchTouchEvent---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TouchActivity.TAG, "RTLayout---dispatchTouchEvent---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TouchActivity.TAG, "RTLayout---dispatchTouchEvent---UP");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TouchActivity.TAG, "RTLayout---onInterceptTouchEvent---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TouchActivity.TAG, "RTLayout---onInterceptTouchEvent---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TouchActivity.TAG, "RTLayout---onInterceptTouchEvent---UP");
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TouchActivity.TAG, "RTLayout---onTouchEvent---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TouchActivity.TAG, "RTLayout---onTouchEvent---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TouchActivity.TAG, "RTLayout---onTouchEvent---UP");
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
