package com.example.demoCollection.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.example.demoCollection.R;
import com.example.demoCollection.widget.stikky.StikkyHeaderBuilder;

public class StickScrollViewActivity extends Activity {
    private static final String TAG = "StickScrollViewActivity";

    private ScrollView scrollView;
    private RelativeLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stikky_scroll_view);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        header = (RelativeLayout) findViewById(R.id.header);
        StikkyHeaderBuilder
                .stickTo(scrollView)
                .setHeader(findViewById(R.id.header))
//                .animator(new HeaderAnimator() {
//                    private float mTransactionRatio;
//
//                    @Override
//                    public void onScroll(int scrolledY) {
//                        getHeader().setTranslationY(Math.max(scrolledY, getMaxTransaction()));
//                        mTransactionRatio = calculateTransactionRatio(scrolledY);
//
//                        float distance = header.getHeight() - 250;
//                        float percent = 1 - Math.abs(scrolledY) / distance;
//                        findViewById(R.id.view).setScaleX(percent);
//                        findViewById(R.id.view).setScaleY(percent);
//                    }
//                    @Override
//                    protected void onAnimatorAttached() {}
//                    @Override
//                    protected void onAnimatorReady() {}
//                    public float getTranslationRatio() {
//                        return mTransactionRatio;
//                    }
//
//                    private float calculateTransactionRatio(int scrolledY) {
//                        return (float) scrolledY / (float) getMaxTransaction();
//                    }
//                })
//                .animator(new BaseStickyHeaderAnimator())
//                .animator(new HeaderStikkyAnimator() {
//                    @Override
//                    public AnimatorBuilder getAnimatorBuilder() {
//                        AnimatorBuilder animatorBuilder = AnimatorBuilder.create().applyScale(findViewById(R.id.view), 0, 0);
////                                .applyTranslation(mViewToAnimate, AnimatorBuilder.buildPointView(homeActionBar));
//
//                        return animatorBuilder;
//                    }
//                })
                .minHeightHeaderPixel(250)
                .build();
    }

}
