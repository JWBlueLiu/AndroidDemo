package com.example.demoCollection.animation.SetupWizard;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import com.example.demoCollection.R;

public class LoginIconActivity extends Activity {

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        view = findViewById(R.id.v);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setBackgroundResource(R.drawable.animated_login);
                Animatable animatable = (Animatable) view.getBackground();
                if (null != animatable) {
                    animatable.stop();
                    animatable.start();
                }
            }
        });

        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // icon scale exit
                EnterExitAnimationUtil.iconScaleExit(getApplication(), view);
            }
        });
    }

}
