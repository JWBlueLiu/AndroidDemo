package com.example.demoCollection.animation.SetupWizard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.example.demoCollection.R;

public class UsageTermsActivity extends Activity {

    private UsageTermsView utv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_terms);

        utv = (UsageTermsView) findViewById(R.id.utv);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterExitAnimationUtil.iconScaleResume(utv);
                utv.startAnim();
            }
        });

        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterExitAnimationUtil.iconScaleExit(getApplication(), utv);
            }
        });

    }
}
