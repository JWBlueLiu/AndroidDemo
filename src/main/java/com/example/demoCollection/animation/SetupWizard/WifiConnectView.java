package com.example.demoCollection.animation.SetupWizard;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.example.demoCollection.R;

public class WifiConnectView extends ImageView {
    private Drawable drawable;

    public WifiConnectView(Context context) {
        this(context, null);
    }

    public WifiConnectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WifiConnectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startAnimation(boolean hasConnect) {
        if (hasConnect) {
            drawable = getResources().getDrawable(R.drawable.animated_has_wifi_connect);
        } else {
            drawable = getResources().getDrawable(R.drawable.animated_has_no_wifi_connect);
        }
        setImageDrawable(drawable);
        ((Animatable) drawable).start();
    }

}
