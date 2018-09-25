package com.example.demoCollection.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

public class RTButton extends Button {
    public RTButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TouchActivity.TAG, "RTButton---dispatchTouchEvent---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TouchActivity.TAG, "RTButton---dispatchTouchEvent---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TouchActivity.TAG, "RTButton---dispatchTouchEvent---UP");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TouchActivity.TAG, "RTButton---onTouchEvent---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TouchActivity.TAG, "RTButton---onTouchEvent---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TouchActivity.TAG, "RTButton---onTouchEvent---UP");
                break;
            default:
                break;
        }
//        Log.i(TouchActivity.TAG, "RTButton---onTouchEvent " + super.onTouchEvent(event));
        return super.onTouchEvent(event);
    }
}
