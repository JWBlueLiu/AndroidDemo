package com.example.demoCollection.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;

/**
 * Extended image view to show the content description in a Toast view when
 * the user long presses.
 * <p>
 * Note: `android:contentDescription` must be set in order for the toast to
 * work
 *
 * @author Callum Taylor <http://callumtaylor.net>
 */
public class HintedImageButton extends ImageButton implements OnLongClickListener {
    private OnLongClickListener mOnLongClickListener;

    public HintedImageButton(Context context) {
        super(context);

        setOnLongClickListener(this);
    }

    public HintedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnLongClickListener(this);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        if (l == this) {
            super.setOnLongClickListener(l);
            return;
        }

        mOnLongClickListener = l;
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnLongClickListener != null) {
            if (!mOnLongClickListener.onLongClick(v)) {
                handleLongClick();
                return true;
            }
            popupWindow.dismiss();
        } else {
            handleLongClick();
            return true;
        }
        return false;
    }

    PopupWindow popupWindow;
    public void handleLongClick() {
        if (null == popupWindow) {
            popupWindow = new PopupWindow(getContext());
        }
        Button btn = new Button(getContext());
        btn.setText("22222");
        popupWindow.setContentView(btn);
        popupWindow.showAsDropDown(this);
    }
}