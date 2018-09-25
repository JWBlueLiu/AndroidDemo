package com.example.demoCollection.animation;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.widget.ImageButton;
import com.example.demoCollection.R;

public class RecorderButton extends ImageButton {

    public RecorderButton(Context context) {
        this(context, null);
    }

    public RecorderButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
//        android.R.style.Widget_Material_ImageButton
    }

    public RecorderButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void play2Pause() {
        setImageDrawable(null);
        setImageResource(R.drawable.animated_btn_play_2_pause);
        Animatable drawable = (Animatable) getBackground();
        drawable.start();
    }

}
