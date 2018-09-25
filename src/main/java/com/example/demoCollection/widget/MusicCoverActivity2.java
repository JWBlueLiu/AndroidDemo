package com.example.demoCollection.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.demoCollection.R;

public class MusicCoverActivity2 extends Activity {

    private static final int[] PHOTOS_RESOURCES = new int[]{
            R.drawable.aa,
            R.drawable.bb,
            R.drawable.cc,
            R.drawable.dd,
            R.drawable.ee,
            R.drawable.ff
    };

    private int currentPosition = 3;
    private ImageView mCurrentImageView;
    private ImageView mNextImageView;

    private MusicCoverAnimHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_cover_activity2);

        mCurrentImageView = (ImageView) findViewById(R.id.current);
        mNextImageView = (ImageView) findViewById(R.id.next);

        helper = new MusicCoverAnimHelper(getApplication(), mCurrentImageView, mNextImageView);

        mCurrentImageView.setImageResource(PHOTOS_RESOURCES[currentPosition]);

        findViewById(R.id.btn_pre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition > 0) {
                    mCurrentImageView.setImageResource(PHOTOS_RESOURCES[currentPosition]);
                    currentPosition -= 1;
                    mNextImageView.setImageResource(PHOTOS_RESOURCES[currentPosition]);
                    helper.startPreAnim();
                }
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition < (PHOTOS_RESOURCES.length - 1)) {
                    mCurrentImageView.setImageResource(PHOTOS_RESOURCES[currentPosition]);
                    currentPosition += 1;
                    mNextImageView.setImageResource(PHOTOS_RESOURCES[currentPosition]);
                    helper.startNextAnim();
                }
            }
        });
    }

}
