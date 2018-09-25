package com.example.demoCollection.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

public class DraggableView extends View implements
        View.OnLongClickListener, View.OnDragListener {

    public DraggableView(Context context) {
        this(context, null);
    }

    public DraggableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnLongClickListener(this);
        setOnDragListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        startDrag(null, new View.DragShadowBuilder(this), this, 0);
        setVisibility(View.INVISIBLE);
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                float X = event.getX();
                float Y = event.getY();

                Log.d("DraggableView", "X " + (int) X + "Y " + (int) Y);

                setX(X - getWidth() / 2);
                setY(Y - getHeight() / 2);
                setVisibility(View.VISIBLE);
            default:
                break;
        }
        return true;
    }
}