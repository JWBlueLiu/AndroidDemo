package com.example.demoCollection.widget.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.example.demoCollection.R;

/**
 * Created by JW.Liu on 2017/4/25.
 */
public class ShapeSelectorView extends View {

    private int shapeWidth = 300;
    private int shapeHeight = 300;
    private int textXOffset = 0;
    private int textYOffset = 50;
    private Paint paintShape;

    private int shapeColor;
    private boolean displayShapeName;

    public ShapeSelectorView(Context context) {
        this(context, null);
    }

    public ShapeSelectorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Obtain a typed array of attributes
        TypedArray a = getContext().getTheme()
            .obtainStyledAttributes(attrs, R.styleable.ShapeSelectorView, defStyleAttr, 0);
        // Extract custom attributes into member variables
        try {
            shapeColor = a.getColor(R.styleable.ShapeSelectorView_shapeColor, Color.BLACK);
            displayShapeName = a.getBoolean(R.styleable.ShapeSelectorView_displayShapeName, true);
        } finally {
            // TypedArray objects are shared and must be recycled.
            a.recycle();
        }

        setupPaint();
    }

    private void setupPaint() {
        paintShape = new Paint();
        paintShape.setStyle(Paint.Style.FILL);
        paintShape.setColor(shapeColor);
        paintShape.setTextSize(50);
        paintShape.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, shapeWidth, shapeHeight, paintShape);
        if (displayShapeName) {
            canvas.drawText("Square", textXOffset, shapeHeight + textYOffset, paintShape);
        }
    }

    public boolean isDisplayShapeName() {
        return displayShapeName;
    }

    public void setDisplayingShapeName(boolean state) {
        this.displayShapeName = state;
        invalidate();
        requestLayout();
    }

    public int getShapeColor() {
        return shapeColor;
    }

    public void setShapeColor(int color) {
        this.shapeColor = color;
        invalidate();
        requestLayout();
    }
}