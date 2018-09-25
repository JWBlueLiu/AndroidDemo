package com.example.demoCollection.widget.stikky;

import android.content.Context;
import android.view.View;
import android.widget.ScrollView;
import com.example.demoCollection.widget.stikky.animator.HeaderStikkyAnimator;

public abstract class StikkyHeaderBuilder {

    protected final Context mContext;

    protected View mHeader;
    protected int mMinHeight;
    protected HeaderAnimator mAnimator;

    protected StikkyHeaderBuilder(final Context context) {
        mContext = context;
        mMinHeight = 0;
    }

    public static ScrollViewBuilder stickTo(final ScrollView scrollView) {
        return new ScrollViewBuilder(scrollView);
    }

    public StikkyHeaderBuilder setHeader(final View header) {
        mHeader = header;
        return this;
    }

    public StikkyHeaderBuilder minHeightHeaderPixel(final int minHeight) {
        mMinHeight = minHeight;
        return this;
    }

    public StikkyHeaderBuilder animator(final HeaderAnimator animator) {
        mAnimator = animator;
        return this;
    }

    public abstract StikkyHeader build();

    public static class ScrollViewBuilder extends StikkyHeaderBuilder {

        private final ScrollView mScrollView;

        protected ScrollViewBuilder(final ScrollView scrollView) {
            super(scrollView.getContext());
            this.mScrollView = scrollView;
        }

        @Override
        public StikkyHeaderScrollView build() {
            //if the animator has not been set, the default one is used
            if (mAnimator == null) {
                mAnimator = new HeaderStikkyAnimator();
            }
            return new StikkyHeaderScrollView(mContext, mScrollView, mHeader, mMinHeight, mAnimator);
        }
    }

}
