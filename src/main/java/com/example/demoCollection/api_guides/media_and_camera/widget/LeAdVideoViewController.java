package com.example.demoCollection.api_guides.media_and_camera.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.example.demoCollection.R;

/**
 * Created by JW.Liu on 2017/1/6.
 */

public class LeAdVideoViewController extends FrameLayout {

    private String mVideoPath;

    public LeAdVideoViewController(Context context) {
        super(context);
    }
    public LeAdVideoViewController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public LeAdVideoViewController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LeAdVideoViewController(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.le_ad_video_view_controller, this);


    }

}