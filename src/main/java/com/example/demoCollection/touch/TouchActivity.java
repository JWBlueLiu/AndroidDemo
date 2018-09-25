package com.example.demoCollection.touch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.example.demoCollection.R;

public class TouchActivity extends Activity {
    public static final String TAG = "TouchActivity";

    private TextView mTv;
    private RTButton button;
    private RTLayout rtLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        mTv = (TextView) findViewById(R.id.tv);
        button = (RTButton) findViewById(R.id.btn1);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "RTButton---onTouch---DOWN");
                        mTv.setText("RTButton---onTouch---DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, "RTButton---onTouch---MOVE");
                        mTv.setText("RTButton---onTouch---MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "RTButton---onTouch---UP");
                        mTv.setText("RTButton---onTouch---UP");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "RTButton clicked!");
                mTv.setText("RTButton clicked!");
            }
        });

        rtLayout = (RTLayout) this.findViewById(R.id.myLayout);
        rtLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "RTLayout---onTouch---DOWN");
                        mTv.setText("RTButton---onTouch---DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, "RTLayout---onTouch---MOVE");
                        mTv.setText("RTButton---onTouch---MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "RTLayout---onTouch---UP");
                        mTv.setText("RTButton---onTouch---UP");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        rtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "RTLayout clicked!");
                mTv.setText("RTLayout clicked!");
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "Activity---dispatchTouchEvent---DOWN");
                mTv.setText("Activity---dispatchTouchEvent---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "Activity---dispatchTouchEvent---MOVE");
                mTv.setText("Activity---dispatchTouchEvent---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "Activity---dispatchTouchEvent---UP");
                mTv.setText("Activity---dispatchTouchEvent---UP");
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
                Log.i(TAG, "Activity---onTouchEvent---DOWN");
                mTv.setText("Activity---onTouchEvent---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "Activity---onTouchEvent---MOVE");
                mTv.setText("Activity---onTouchEvent---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "Activity---onTouchEvent---UP");
                mTv.setText("Activity---onTouchEvent---UP");
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
