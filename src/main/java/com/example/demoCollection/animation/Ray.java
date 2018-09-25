package com.example.demoCollection.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;

public class Ray extends Shape {
    public static final String LENGTH = "length";

    private float x;
    private float y;
    private float length;
    private float angle;
    private Paint mPaint;

    public static float RAY_LENGTH;
    public static boolean change = true;

    public Ray(float x, float y, float width, float length, float angle, int color) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.angle = angle;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(width);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (change) {
            canvas.drawLine(x, y, Math.round(x + length * Math.cos(angle)), Math.round(y - length * Math.sin(angle)), mPaint);
        } else {
            float endX = Math.round(x + RAY_LENGTH * Math.cos(angle));
            float endy = Math.round(y - RAY_LENGTH * Math.sin(angle));
            float startX = Math.round(endX - length * Math.cos(angle));
            float startY = Math.round(endy + length * Math.sin(angle));
            canvas.drawLine(startX, startY, endX, endy, mPaint);
        }
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }
}
