package com.example.demoCollection.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Gallery;

public class DateGallery extends Gallery {
    public DateGallery(Context context) {
        this(context, null);
    }

    public DateGallery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics()));
    }

    public static int sp2px(Context context, float dpValue) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dpValue, context.getResources().getDisplayMetrics()));
    }

    public DateGallerySelectedListener mSelectedListener;

    public void addDateGallerySelectedListener(DateGallerySelectedListener dateGallerySelectedListener) {
        mSelectedListener = dateGallerySelectedListener;
    }
}
