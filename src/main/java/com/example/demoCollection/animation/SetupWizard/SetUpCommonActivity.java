package com.example.demoCollection.animation.SetupWizard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.demoCollection.R;
import java.util.Arrays;
import java.util.List;

public class SetUpCommonActivity extends Activity {
    LinearLayout ll;
    private LinearLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_wizard);

        rl = (LinearLayout) findViewById(R.id.rl);
        ll = (LinearLayout) findViewById(R.id.container);

        final List<Integer> togetherList = Arrays.asList(new Integer[]{R.id.btn_back, R.id.title1});
        final List<Integer> expectList = Arrays.asList(new Integer[]{R.id.btns, R.id.btn_lz});

        // anim enter
        findViewById(R.id.btn_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterExitAnimationUtil.setLayoutAnim(getApplication(), ll, EnterExitAnimationUtil.ENTER, 0.35f);
                EnterExitAnimationUtil.startAnim(getApplication(), rl, EnterExitAnimationUtil.ENTER, togetherList, expectList);
            }
        });

        // anim exit
        findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterExitAnimationUtil.setLayoutAnim(getApplication(), ll, EnterExitAnimationUtil.EXIT, 0.35f);
                EnterExitAnimationUtil.startAnim(getApplication(), rl, EnterExitAnimationUtil.EXIT, togetherList, expectList);
            }
        });

        // wifi enter
        findViewById(R.id.btn_wifi_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterExitAnimationUtil.setLayoutAnim(getApplication(), ll, EnterExitAnimationUtil.WIFI_ENTER, 0.07f);
                ll.startLayoutAnimation();
            }
        });

        // add wifi
        findViewById(R.id.btn_wifi_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = new Button(getApplication());
                btn.setText("WIFI");
                ll.addView(btn);
            }
        });

        // wifi exit
        findViewById(R.id.btn_wifi_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterExitAnimationUtil.setLayoutAnim(getApplication(), ll, EnterExitAnimationUtil.WIFI_EXIT, 0.14f);
                ll.startLayoutAnimation();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        EnterExitAnimationUtil.setLayoutTransitionAnim(ll, EnterExitAnimationUtil.WIFI_ADD);
    }
}
