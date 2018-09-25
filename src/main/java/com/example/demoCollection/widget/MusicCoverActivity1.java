package com.example.demoCollection.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.example.demoCollection.R;

public class MusicCoverActivity1 extends Activity implements View.OnClickListener {

    private static final int DISAPPEAR_TIME = 250;
    private static final int APPEAR_TIME = 500;
    private static final int[] PHOTOS_RESOURCES = new int[]{
            R.drawable.aa,
            R.drawable.aa,
            R.drawable.aa,
            R.drawable.aa,
            R.drawable.aa,
            R.drawable.aa,
            R.drawable.aa,
            R.drawable.aa,
            R.drawable.aa,
            R.drawable.aa,
            R.drawable.aa
    };
    private Rotate3dView1 imgBottom;
    private Rotate3dView1 imgTop;
    // current song position
    private int currentPosition = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_cover_activity1);

        imgBottom = (Rotate3dView1) findViewById(R.id.iv1);
        imgTop = (Rotate3dView1) findViewById(R.id.iv2);

        findViewById(R.id.btn_pre).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);

        // both set current image
        imgTop.setImageResource(PHOTOS_RESOURCES[currentPosition]);
        imgBottom.setImageResource(PHOTOS_RESOURCES[currentPosition]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pre:
                // whether has img
                if (currentPosition > 0) {
                    imgBottom.setImageMatrix(null);
                    imgTop.setImageMatrix(null);

                    imgBottom.setImageResource(PHOTOS_RESOURCES[currentPosition]);
                    currentPosition -= 1;
                    imgTop.setImageResource(PHOTOS_RESOURCES[currentPosition]);

                    // disappear from 0 to 0.2 PI
                    imgBottom.initAnim(0, 0.2 * Math.PI, DISAPPEAR_TIME).start();
                    // appear from 1.6 to 2 PI
                    imgTop.initAnim(1.6 * Math.PI, 2 * Math.PI, APPEAR_TIME).start();
                }
                break;
            case R.id.btn_next:
                // whether has img
                if (currentPosition < (PHOTOS_RESOURCES.length - 1)) {
                    imgBottom.setImageMatrix(null);
                    imgTop.setImageMatrix(null);

                    imgTop.setImageResource(PHOTOS_RESOURCES[currentPosition]);
                    currentPosition += 1;
                    imgBottom.setImageResource(PHOTOS_RESOURCES[currentPosition]);

                    // disappear from 2 PI to 1.5 PI
                    imgTop.initAnim(2 * Math.PI, 1.5 * Math.PI, DISAPPEAR_TIME).start();
                    // appear from 0.2 PI to 0 PI
                    imgBottom.initAnim(0.2 * Math.PI, 0, APPEAR_TIME).start();
                }
                break;
        }
    }

}
