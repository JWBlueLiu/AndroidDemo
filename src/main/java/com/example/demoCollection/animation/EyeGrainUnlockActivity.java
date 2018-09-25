package com.example.demoCollection.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.example.demoCollection.R;

public class EyeGrainUnlockActivity extends Activity {

    private static final String TAG = "EyeGrainUnlockActivity";

    private EyeGrainUnlockView eyeGrainUnlockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_grain_unlock);

        eyeGrainUnlockView = (EyeGrainUnlockView) findViewById(R.id.eguv);

        // 开始扫描
        findViewById(R.id.start_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eyeGrainUnlockView.startScanAnim();
            }
        });
        // 扫描失败
        findViewById(R.id.scan_fail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eyeGrainUnlockView.startScanFailAnim();
            }
        });
        // 扫描成功
        findViewById(R.id.scan_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eyeGrainUnlockView.startScanOkAnim();
            }
        });
        // 眼睛复位
        findViewById(R.id.scan_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eyeGrainUnlockView.resetAnim();
            }
        });

    }

}
