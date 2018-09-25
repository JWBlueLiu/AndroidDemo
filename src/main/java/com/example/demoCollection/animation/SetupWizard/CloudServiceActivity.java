package com.example.demoCollection.animation.SetupWizard;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.demoCollection.R;

public class CloudServiceActivity extends Activity {

    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int TRANSLATION_LENGTH = 600;
    private static final int CLOUD_SERVICE_ANIM_DURATION = 500;
    private static final int CLOUD_SERVICE_ANIM_DELAY = 70;
    private LinearLayout container;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private LinearLayout ll4;
    private Interpolator alphaInterpolator = new AccelerateDecelerateInterpolator();
    private Interpolator leftTranslationInterpolator = new AccelerateDecelerateInterpolator();  // LeEaseInQuartEaseOutCubicInterpolator
    private Interpolator rightTranslationInterpolator = new AccelerateDecelerateInterpolator(); // LeAccelerateDecelerateInterpolator
    private AnimatorSet cloudServiceLeftAnims;
    private AnimatorSet cloudServiceRightAnims;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_service);

        container = (LinearLayout) findViewById(R.id.container);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);
        ll4 = (LinearLayout) findViewById(R.id.ll4);

        initAnim();

        // 进入动画
        findViewById(R.id.btn_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCloudServiceAnim(LEFT);
            }
        });
        // 出去动画
        findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCloudServiceAnim(RIGHT);
            }
        });
    }

    /**
     * init animation
     */
    private void initAnim() {
        cloudServiceLeftAnims = new AnimatorSet();
        cloudServiceLeftAnims.playTogether(
                getCloudServiceAnim(LEFT, ll1, CLOUD_SERVICE_ANIM_DELAY * 0),
                getCloudServiceAnim(LEFT, ll2, CLOUD_SERVICE_ANIM_DELAY * 1),
                getCloudServiceAnim(LEFT, ll3, CLOUD_SERVICE_ANIM_DELAY * 2),
                getCloudServiceAnim(LEFT, ll4, CLOUD_SERVICE_ANIM_DELAY * 3)
        );
        cloudServiceLeftAnims.setDuration(CLOUD_SERVICE_ANIM_DURATION);

        cloudServiceRightAnims = new AnimatorSet();
        cloudServiceRightAnims.playTogether(
                getCloudServiceAnim(RIGHT, ll1, CLOUD_SERVICE_ANIM_DELAY * 0),
                getCloudServiceAnim(RIGHT, ll2, CLOUD_SERVICE_ANIM_DELAY * 1),
                getCloudServiceAnim(RIGHT, ll3, CLOUD_SERVICE_ANIM_DELAY * 2),
                getCloudServiceAnim(RIGHT, ll4, CLOUD_SERVICE_ANIM_DELAY * 3)
        );
        cloudServiceRightAnims.setDuration(CLOUD_SERVICE_ANIM_DURATION);
    }

    /**
     * start animation
     *
     * @param direction activity in or out
     */
    private void startCloudServiceAnim(int direction) {
        cancelAnim();

        switch (direction) {
            case LEFT:
                if (null != cloudServiceLeftAnims) {
                    cloudServiceLeftAnims.start();
                }
                break;
            case RIGHT:
                if (null != cloudServiceRightAnims) {
                    cloudServiceRightAnims.start();
                }
                break;
        }
    }

    private void cancelAnim() {
        if (null != cloudServiceLeftAnims && cloudServiceLeftAnims.isRunning()) {
            cloudServiceLeftAnims.cancel();
        }
        if (null != cloudServiceRightAnims && cloudServiceRightAnims.isRunning()) {
            cloudServiceRightAnims.cancel();
        }
    }

    private AnimatorSet getCloudServiceAnim(int direction, final LinearLayout ll, int startDelay) {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator translateAnim;
        ValueAnimator alphaAnim;

        float translateStartValue = 0;
        float translateEndValue = 0;
        float alphaStartValue = 1.0f;
        float alphaEndValue = 1.0f;

        switch (direction) {
            case LEFT:
                translateStartValue = TRANSLATION_LENGTH;
                translateEndValue = 0;

                alphaStartValue = 0.0f;
                alphaEndValue = 1.0f;
                break;
            case RIGHT:
                translateStartValue = 0;
                translateEndValue = TRANSLATION_LENGTH;

                alphaStartValue = 1.0f;
                alphaEndValue = 0.0f;
                break;
        }

        if (null != ll && ll.getChildCount() == 2) {
            final TextView tv = (TextView) ll.getChildAt(1);

            translateAnim = ValueAnimator.ofFloat(translateStartValue, translateEndValue);
            if (direction == LEFT) {
                translateAnim.setInterpolator(leftTranslationInterpolator);
            } else if (direction == RIGHT) {
                translateAnim.setInterpolator(rightTranslationInterpolator);
            }
            translateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float translateValue = (Float) animation.getAnimatedValue();

                    tv.scrollTo((int) translateValue, 0);
                    ll.scrollTo(-(int) translateValue, 0);
                }
            });

            alphaAnim = ValueAnimator.ofFloat(alphaStartValue, alphaEndValue);
            alphaAnim.setInterpolator(alphaInterpolator);
            alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ll.setAlpha((Float) animation.getAnimatedValue());
                }
            });

            animatorSet.playTogether(translateAnim, alphaAnim);
            animatorSet.setStartDelay(startDelay);
        }

        return animatorSet;
    }

}
