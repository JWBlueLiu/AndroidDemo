package com.example.demoCollection.api_guides.media_and_camera;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;
import com.example.demoCollection.R;

/**
 * Created by JW.Liu on 2017/1/3.
 */

public class VideoPlayActivity extends Activity {

    private VideoView mVideoView;
    private WebView mWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_video_play);

        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mWv = (WebView) findViewById(R.id.wv);

        String url = "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4";
        url = "http://play.g3proxy.lecloud.com/vod/v2/MjI5LzQxLzgwL2xldHYtdXRzLzE0L3Zlcl8wMF8yMi0xMDgwMDcxOTE1LWF2Yy0xOTk3NzctYWFjLTQ4MDAwLTUwMjgwLTE2NDg5NzYtOTM0Y2VkMGUxY2I5ZDBjOGMyMTMwNWQ3MWMxNzBmOWMtMTQ4MzU4NjY3NzQyOS5tcDQ=?b=262&mmsid=62431858&tm=1483609473&key=9ebf7628076eed486edbf517a3ab5dd8&platid=6&splatid=602&orifrom=aHR0cDovLzExNS4xODIuNjMuODY6ODA4MC9mdGwvYXBrL2RhdGEvY29tbW9uL3NlY3VyaXR5L3BsYXl1cmwvZ2V0dXJsL2J5dmlk&orivid=27437077&clientIp01=115.28.209.129&clientIp02=115.28.209.129&playid=0&tss=tvts&vtype=21&cvid=303038769718&payff=0&pip=5220a8fda7c243d6d24bca824d571d15";
        mVideoView.setZOrderOnTop(true);//this line solve the problem
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.setZOrderOnTop(false);
            }
        });
        mVideoView.setVideoPath(url);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();

        WebSettings settings = mWv.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mWv.loadUrl(url);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mVideoView.resume();
//    }

}
