package com.example.demoCollection.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.SeekBar;

public class LeSeekBar extends SeekBar {
    // default color
    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#66BCBCBC");
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private static final int DEFAULT_PROGRESS_COLOR = Color.parseColor("#518EF1");
    private int progressColor = DEFAULT_PROGRESS_COLOR;

    private ShapeDrawable backgroundDrawable;
    private ClipDrawable progressDrawable;

    public LeSeekBar(Context context) {
        this(context, null);
    }

    public LeSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public LeSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // background drawable
        backgroundDrawable = new ShapeDrawable(getDrawableShape());
        backgroundDrawable.getPaint().setColor(backgroundColor);
        // progress drawable
        progressDrawable = new ClipDrawable(new ColorDrawable(progressColor), Gravity.LEFT, ClipDrawable.HORIZONTAL);

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{backgroundDrawable, progressDrawable});
        setProgressDrawable(layerDrawable);

        // cancel press shadow
        setBackground(null);
        // without track
        setSplitTrack(false);
    }

    /**
     * get round corner shape
     *
     * @return
     */
    private Shape getDrawableShape() {
        final float[] roundedCorners = new float[]{5, 5, 5, 5, 5, 5, 5, 5};
        return new RoundRectShape(roundedCorners, null, null);
    }

    /**
     * set progress background color
     */
    public void setBackgroundColor(int color) {
        backgroundColor = color;
        backgroundDrawable.getPaint().setColor(backgroundColor);
    }

    /**
     * set progress color
     */
    public void setProgressColor(int color) {
        progressColor = color;
        progressDrawable.setTint(progressColor);
    }

}
