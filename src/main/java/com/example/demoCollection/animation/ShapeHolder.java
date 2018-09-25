package com.example.demoCollection.animation;

import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import com.example.demoCollection.animation.SetupWizard.BaseView;

public class ShapeHolder {

    public static final String X = "x";
    public static final String Y = "y";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String RADIUS = "radius";

    private float x = 0, y = 0;
    private ShapeDrawable shape;
    private Paint paint;
    private int color;
    private RadialGradient gradient;

    public ShapeHolder(ShapeDrawable s) {
        shape = s;

        setAlpha(BaseView.OPACITY);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint value) {
        paint = value;
    }

    public void setXY(float valueX, float valueY) {
        setX(valueX);
        setY(valueY);
    }

    public float getX() {
        return x;
    }

    public void setX(float value) {
        x = value;
    }

    public float getY() {
        return y;
    }

    public void setY(float value) {
        y = value;
    }

    public ShapeDrawable getShape() {
        return shape;
    }

    public void setShape(ShapeDrawable value) {
        shape = value;
    }

    public int getColor() {
        return shape.getPaint().getColor();
    }

    public void setColor(int value) {
        shape.getPaint().setColor(value);
        color = value;
    }

    public RadialGradient getGradient() {
        return gradient;
    }

    public void setGradient(RadialGradient value) {
        gradient = value;
    }

    public void setAlpha(float alpha) {
        shape.setAlpha((int) ((alpha * 255f) + .5f));
    }

    public float getAlpha() {
        return shape.getAlpha();
    }

    public float getWidth() {
        return shape.getShape().getWidth();
    }

    public void setWidth(float width) {
        Shape s = shape.getShape();
        s.resize(width, s.getHeight());
    }

    public float getHeight() {
        return shape.getShape().getHeight();
    }

    public void setHeight(float height) {
        Shape s = shape.getShape();
        s.resize(s.getWidth(), height);
    }

    public float getRadius() {
        Shape s = shape.getShape();
        return Math.min(s.getHeight(), s.getWidth());
    }

    public void setRadius(float radius) {
        Shape s = shape.getShape();
        s.resize(radius * 2, radius * 2);
    }

}
