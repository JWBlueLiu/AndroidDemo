package com.example.demoCollection.widget.stikky.animator;

import com.example.demoCollection.widget.stikky.HeaderAnimator;

public class HeaderStikkyAnimator extends HeaderAnimator {

    private float mBoundedTranslatedRatio;
    private float mTransactionRatio;
    protected AnimatorBuilder mAnimatorBuilder;

    private boolean hasAnimatorBundles = false;

    @Override
    protected void onAnimatorReady() {
        mAnimatorBuilder = getAnimatorBuilder();
        hasAnimatorBundles = (mAnimatorBuilder != null) && (mAnimatorBuilder.hasAnimatorBundles());
    }

    @Override
    protected void onAnimatorAttached() {
    }

    public AnimatorBuilder getAnimatorBuilder() {
        return null;
    }

    @Override
    public void onScroll(int scrolledY) {
        getHeader().setTranslationY(Math.max(scrolledY, getMaxTransaction()));
        mTransactionRatio = calculateTransactionRatio(scrolledY);

        mBoundedTranslatedRatio = clamp(getTranslationRatio(), 0f, 1f);
        if (hasAnimatorBundles) {
            mAnimatorBuilder.animateOnScroll(mBoundedTranslatedRatio, getHeader().getTranslationY());
        }
    }

    public float getTranslationRatio() {
        return mTransactionRatio;
    }

    private float calculateTransactionRatio(int scrolledY) {
        return (float) scrolledY / (float) getMaxTransaction();
    }
}
