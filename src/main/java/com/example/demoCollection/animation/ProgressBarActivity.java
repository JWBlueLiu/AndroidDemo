package com.example.demoCollection.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.demoCollection.R;

public class ProgressBarActivity extends Activity {

    private Button btnStart;
    private Button btnExit;
    private Button btnCancel;
    private ProgressView pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        btnStart = (Button) findViewById(R.id.btn_start);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        pb = (ProgressView) findViewById(R.id.pb);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.startAnim();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.startFinishAnim(null);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.reset();
            }
        });
    }

}
