package com.example.demoCollection.widget;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.widget.ImageView;
import com.example.demoCollection.R;

public class MusicCoverAnimHelper {

    private float SCREEN_WIDTH;

    private Context mContext;
    private ImageView mTopImageView;
    private ImageView mBottomImageView;

    private AnimatorSet topPreAnimSet;
    private AnimatorSet bottomPreAnimSet;
    private AnimatorSet topNextAnimSet;
    private AnimatorSet bottomNextAnimSet;
    private ObjectAnimator topPreTranslationAnim;
    private ObjectAnimator bottomPreTranslationAnim;
    private ObjectAnimator topNextTranslationAnim;
    private ObjectAnimator bottomNextTranslationAnim;

    public MusicCoverAnimHelper(Context context, ImageView top, ImageView bottom) {
        mContext = context;

        mTopImageView = top;
        mBottomImageView = bottom;

        init();
    }

    private void init() {
        topPreAnimSet = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.music_cover_top_pre_anim);
        bottomPreAnimSet = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.music_cover_bottom_pre_anim);
        topPreAnimSet.setTarget(mTopImageView);
        bottomPreAnimSet.setTarget(mBottomImageView);
        topPreTranslationAnim = (ObjectAnimator) topPreAnimSet.getChildAnimations().get(0);
        bottomPreTranslationAnim = (ObjectAnimator) bottomPreAnimSet.getChildAnimations().get(0);

        topNextAnimSet = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.music_cover_top_next_anim);
        bottomNextAnimSet = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.music_cover_bottom_next_anim);
        topNextAnimSet.setTarget(mTopImageView);
        bottomNextAnimSet.setTarget(mBottomImageView);
        topNextTranslationAnim = (ObjectAnimator) topNextAnimSet.getChildAnimations().get(0);
        bottomNextTranslationAnim = (ObjectAnimator) bottomNextAnimSet.getChildAnimations().get(0);
    }

    public void startPreAnim() {
        topPreAnimSet.cancel();
        bottomPreAnimSet.cancel();

        SCREEN_WIDTH = mTopImageView.getWidth();

        topPreTranslationAnim.setFloatValues(0, SCREEN_WIDTH);
        bottomPreTranslationAnim.setFloatValues(-SCREEN_WIDTH, 0);

        topPreAnimSet.start();
        bottomPreAnimSet.start();
    }

    public void startNextAnim() {
        topNextAnimSet.cancel();
        bottomNextAnimSet.cancel();

        SCREEN_WIDTH = mTopImageView.getWidth();

        topNextTranslationAnim.setFloatValues(0, -SCREEN_WIDTH);
        bottomNextTranslationAnim.setFloatValues(SCREEN_WIDTH, 0);

        topNextAnimSet.start();
        bottomNextAnimSet.start();
    }

    public void cancelAnim() {
        if (null != topPreAnimSet) {
            topPreAnimSet.cancel();
        }
        if (null != bottomPreAnimSet) {
            bottomPreAnimSet.cancel();
        }
        if (null != topNextAnimSet) {
            topNextAnimSet.cancel();
        }
        if (null != bottomNextAnimSet) {
            bottomNextAnimSet.cancel();
        }
    }

}
