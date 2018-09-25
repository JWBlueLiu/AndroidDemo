package com.example.demoCollection.animation.SetupWizard;

import android.app.Activity;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.view.View;
import com.example.demoCollection.R;

public class WifiConnectViewActivity extends Activity {

    //    private WifiConnectView wcv;
    private WifiConnectView2 wcv;
    AnimatedVectorDrawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect_view);
        wcv = (WifiConnectView2) findViewById(R.id.wcv);
    }

    public void play1(View v) {
        wcv.startAnimation(true);
    }

    public void play2(View v) {
        wcv.startAnimation(false);
    }

    public void cancel(View v) {
        wcv.cancelAnimation();
    }

}
