package com.example.demoCollection.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class Rotate3dView2 extends ImageView {

    private int SCREEN_WIDTH;

    private Context mContext;

    public enum ANIMATION_TYPE {
        LEFT_IN, LEFT_OUT,
        RIGHT_IN, RIGHT_OUT
    }

    public Rotate3dView2(Context context) {
        this(context, null);
    }

    public Rotate3dView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Rotate3dView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void startAnim(ANIMATION_TYPE type) {
        int width = getWidth();

        float translationStart = 0;
        float translationEnd = 0;

        switch (type) {
            case LEFT_IN:
                translationStart = width;
                translationEnd = 0;
                break;
            case LEFT_OUT:
                translationStart = 0;
                translationEnd = -width;
                break;
            case RIGHT_IN:
                translationStart = -width;
                translationEnd = 0;
                break;
            case RIGHT_OUT:
                translationStart = 0;
                translationEnd = width;
                break;
        }
        ObjectAnimator translationAnim = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, translationStart, translationEnd);
        translationAnim.setAutoCancel(true);
        translationAnim.setTarget(this);
    }

}
