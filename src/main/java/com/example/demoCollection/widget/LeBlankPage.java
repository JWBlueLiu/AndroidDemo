package com.example.demoCollection.widget;

/**
 * Created by L on 16/8/7.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.demoCollection.R;

public class LeBlankPage extends LinearLayout {
    private static final float DEFAULT_PORT_TOP_PERCENT = 0.28F;
    private static final int PRESS_ALPHA = (int) (255 * 0.7);
    private static final int DISABLED_ALPHA = (int) (255 * 0.3);
    private float SCREEN_WIDTH, SCREEN_HEIGHT, mVerticalHeight;

    private Context mContext;

    private Drawable mIcon;
    private CharSequence
            mDesc,
            mMsg,
            mPrimaryText, mSecondText, mThirdText;

    private View mIconView;
    private TextView mDesView, mMsgView;
    private Button mPrimaryBtn, mSecondBtn, mThirdBtn;

    private int mOrientation;
    private boolean mIsFullScreen = true;
    private int mLeBlankPageColorPrompt;
    private int mLeBlankPageColorButtonNormal;
    private int mLeBlankPageColorButtonPress;
    private int mLeBlankPageColorButtonDisable;

    public LeBlankPage(Context context) {
        this(context, null);
    }
    public LeBlankPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public LeBlankPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.LeBlankPage);

        mIcon = a.getDrawable(R.styleable.LeBlankPage_icon);
        mDesc = a.getString(R.styleable.LeBlankPage_description);
        mMsg = a.getString(R.styleable.LeBlankPage_message);
        mPrimaryText = a.getString(R.styleable.LeBlankPage_primaryText);
        mSecondText = a.getString(R.styleable.LeBlankPage_secondText);
        mThirdText = a.getString(R.styleable.LeBlankPage_thirdText);
        mLeBlankPageColorPrompt = a.getColor(
                R.styleable.LeBlankPage_leBlankPageColorButtonNormal,
                getResources().getColor(R.color.le_blank_page_prompt_color));
        mLeBlankPageColorButtonNormal = a.getColor(
                R.styleable.LeBlankPage_leBlankPageColorButtonNormal,
                getResources().getColor(R.color.le_blank_page_button_normal_color));
        mLeBlankPageColorButtonPress = a.getColor(
                R.styleable.LeBlankPage_leBlankPageColorButtonPress,
                getAlphaColor(mLeBlankPageColorButtonNormal, PRESS_ALPHA));
        mLeBlankPageColorButtonPress = a.getColor(
                R.styleable.LeBlankPage_leBlankPageColorButtonDisable,
                getAlphaColor(mLeBlankPageColorButtonNormal, DISABLED_ALPHA));

        a.recycle();

        // get vertical height
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display mDisplay = windowManager.getDefaultDisplay();
        SCREEN_WIDTH = Math.min(mDisplay.getWidth(), mDisplay.getHeight());
        SCREEN_HEIGHT = Math.max(mDisplay.getWidth(), mDisplay.getHeight());
        mOrientation = mContext.getResources().getConfiguration().orientation;
        syncVerticalHeight();

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.le_blank, this, true);

        mIconView = findViewById(R.id.icon);
        mDesView = (TextView) findViewById(R.id.desc);
        mMsgView = (TextView) findViewById(R.id.msg);
        mPrimaryBtn = (Button) findViewById(R.id.primaryBtn);
        mSecondBtn = (Button) findViewById(R.id.secondBtn);
        mThirdBtn = (Button) findViewById(R.id.thirdBtn);

        invalidateUI();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        float yOffset = caculateYOffset();
        if (yOffset != 0) {
            setY(getY() - yOffset);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mOrientation != newConfig.orientation) {
            mOrientation = newConfig.orientation;
            syncVerticalHeight();
        }
    }

    private void syncVerticalHeight() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mVerticalHeight = SCREEN_HEIGHT;
        } else {
            mVerticalHeight = SCREEN_WIDTH;
        }
    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//        return super.onSaveInstanceState();
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        super.onRestoreInstanceState(state);
//    }

    private float caculateYOffset() {
        int[] mScreenLocation = new int[2];
        getLocationOnScreen(mScreenLocation);
        int y = mScreenLocation[1];
        float verticalTopPercent = y / mVerticalHeight;
        if (verticalTopPercent == DEFAULT_PORT_TOP_PERCENT) {
            return 0;
        } else {
            return y - DEFAULT_PORT_TOP_PERCENT * mVerticalHeight;
        }
    }

    @Deprecated
    public void forceInvalidateUI() {}

    public void invalidateUI() {
        if (null == mIcon) {
            mIconView.setVisibility(View.GONE);
        } else {
            mIconView.setVisibility(View.VISIBLE);
            setIcon(mIcon);
        }

        handleTextShow(mDesView, mDesc);
        handleTextShow(mMsgView, mMsg);
        handleButtonShow(mPrimaryBtn, mPrimaryText);
        handleButtonShow(mSecondBtn, mSecondText);
        handleButtonShow(mThirdBtn, mThirdText);
    }

    private void handleTextShow(TextView view, CharSequence content) {
        if (TextUtils.isEmpty(content)) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText(content);
            view.setTextColor(mLeBlankPageColorPrompt);
        }
    }
    private void handleButtonShow(TextView view, CharSequence content) {
        if (TextUtils.isEmpty(content)) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText(content);
            view.setTextColor(mLeBlankPageColorButtonNormal);
        }
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
        mIcon.setTint(mLeBlankPageColorPrompt);
        mIconView.setBackground(mIcon);
    }
    // get view
    public View getIconView() {
        return mIconView;
    }
    public TextView getmMsgView() {
        return mMsgView;
    }
    public TextView getmDesView() {
        return mDesView;
    }
    public Button getmPrimaryBtn() {
        return mPrimaryBtn;
    }
    public Button getmSecondBtn() {
        return mSecondBtn;
    }
    public Button getmThirdBtn() {
        return mThirdBtn;
    }

    // get & set btn bg
    public void setButtonBg(Drawable buttonBg) {}
    public void setPrimaryBg(Drawable primaryBg) {}
    public void setSecondBg(Drawable secondBg) {}
    public void setThirdBg(Drawable thirdBg) {}

    // get & set text
    public void setMessage(CharSequence message) {
        this.mMsg = message;
    }
    public void setDescription(CharSequence description) {
        this.mDesc = description;
    }
    public void setPrimaryText(CharSequence mPrimaryText) {
        this.mPrimaryText = mPrimaryText;
    }
    public void setSecondText(CharSequence secondText) {
        this.mSecondText = secondText;
    }
    public void setThirdText(CharSequence thirdText) {
        this.mThirdText = thirdText;
    }

    public void setButtonTextColorList(ColorStateList buttonTextColorList) {}
    public void setPrimaryTextColorList(ColorStateList mPrimaryTextColorList) {}
    public void setSecondTextColorList(ColorStateList secondTextColorList) {}
    public void setThirdTextColorList(ColorStateList thirdTextColorList) {}

    // get & set text color
    public void setButtonTextColor(int buttonTextColor) {}
    public void setPrimaryTextColor(int mPrimaryTextColor) {}
    public void setSecondTextColor(int secondTextColor) {}
    public void setThirdTextColor(int thirdTextColor) {}
    /**
     * 设置button主色
     * @param tintColor
     */
    public void setTintColor(int tintColor) {
    }
    public void clearTint(){}
    // set listener
    public static abstract class OnClickAndLongClickListener implements
            View.OnClickListener, View.OnLongClickListener {
    }
    public void setPrimaryOnClickAndLongClickListener(OnClickAndLongClickListener listener) {
        mPrimaryBtn.setOnClickListener(listener);
        mPrimaryBtn.setOnLongClickListener(listener);
    }
    public void setSecondOnClickAndLongClickListener(OnClickAndLongClickListener listener) {
        mSecondBtn.setOnClickListener(listener);
        mSecondBtn.setOnLongClickListener(listener);
    }
    public void setThirdOnClickAndLongClickListener(OnClickAndLongClickListener listener) {
        mThirdBtn.setOnClickListener(listener);
        mThirdBtn.setOnLongClickListener(listener);
    }

    /**
     * 设置是否是全屏的空白页
     * 如果是全屏 自动计算距离顶部
     * 不是全屏   手动设置距离
     * @param isFullScreen 是否是全屏的空白页
     */
    public void isFullScreen(boolean isFullScreen) {
        mIsFullScreen = isFullScreen;
        forceInvalidateUI();
    }

    private int getAlphaColor(int original, int alpha) {
        return Color.argb(
                alpha,
                Color.red(original),
                Color.green(original),
                Color.blue(original));
    }

}