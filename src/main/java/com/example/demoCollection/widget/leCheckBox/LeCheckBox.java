package com.example.demoCollection.widget.leCheckBox;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.demoCollection.R;

@SuppressLint("AppCompatCustomView")
public class LeCheckBox extends CheckBox implements LeCheckable, ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private static final int DISABLE_ALPHA = (int) (255 * 0.3);
    private final int mMeasureSize;
    private final Animator mShowAnimatior;
    private final Animator mHiddenAnimatior;
    private final int mMaxCircleRadius;
    private final LeArrowShape mArrawShape;
    RectF mSaveLayerRectF = new RectF();
    private int mBoxBorderColor;
    private int mBoxTrackColor;
    private AnimatorSet mZoomOutAnimator;
    private AnimatorSet mZoomInAnimator;
    private ObjectAnimator mArrowShownAnimator;
    private ObjectAnimator mArrowHiddenAnimator;
    private int mBoxTrackColorOn;
    private int mArrowColor;
    private int mArrowColorWithoutBorder;
    private boolean mIsBoxTextOnRight;
    private boolean mWithoutBoxBorder;
    private int mBoxSize;
    private int mDynimacRadius = 0;
    private float mArrowInterpolatedTime = 0;
    private Rect mInvalidateRect = new Rect();
    private Path mCirclePath = new Path();
    private int mCircleBoxRadius;
    private ArgbEvaluator mArgbEvaluator;
    private TextView mAnimateTextView;
    private ColorStateList mTextColorOnChecked;
    private int mAnimateTextColorOnChecked;
    private int mAnimateTextColorOrigin;
    private int mAnimateTextViewColor;
    private int mBoxTop;
    private int mBoxBottom;
    private int mBoxLeft;
    private int mBoxRight;
    private int mBoxInnerPadding;
    private boolean mIsAnimating;
    private int mAlpha = 255;

    public LeCheckBox(Context context) {
        this(context, null);
    }

    public LeCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.checkedTextViewStyle);
    }

    public LeCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final Resources res = context.getResources();

        mIsBoxTextOnRight = true;
        mWithoutBoxBorder = false;
        mBoxTrackColorOn = res.getColor(R.color.le_color_default_checkbox_track_on);
        mBoxBorderColor = res.getColor(R.color.le_color_default_checkbox_track_border);
        mBoxTrackColor = res.getColor(R.color.le_color_default_checkbox_track);
        mArrowColor = res.getColor(R.color.le_color_default_checkbox_arrow);
        mArrowColorWithoutBorder = res.getColor(R.color.le_color_default_checkbox_track_on);
        mBoxSize = res.getDimensionPixelSize(R.dimen.le_default_box_size_with_border);

        // if(theme.resolveAttribute(com.android.internal.R.attr.colorControlNormal,
        // outValue, true)) {
        // mBoxTrackColor = outValue.data;
        // }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.leCheckbox, defStyle, 0);
        if (a != null) {
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);

                switch (attr) {
                    case R.styleable.leCheckbox_leBoxWithoutBorder:
                        mWithoutBoxBorder = a.getBoolean(attr, mWithoutBoxBorder);
                        if (mWithoutBoxBorder) {
                            mBoxSize = res.getDimensionPixelSize(R.dimen.le_default_box_size);
                        }
                        break;
                    case R.styleable.leCheckbox_leBoxInnerPadding:
                        mBoxInnerPadding = a.getDimensionPixelSize(attr, mBoxInnerPadding);
                        break;
                    case R.styleable.leCheckbox_leTextOnColor:
                        mTextColorOnChecked = a.getColorStateList(attr);
                        break;
                    case R.styleable.leCheckbox_leBoxOnColor:
                        mBoxTrackColorOn = a.getColor(attr, mBoxTrackColorOn);
                        break;
                    case R.styleable.leCheckbox_leBoxArrowColor:
                        mArrowColor = a.getColor(attr, mArrowColor);
                        break;
                    case R.styleable.leCheckbox_leBoxArrowColorWithoutBorder:
                        mArrowColorWithoutBorder = a.getColor(attr, mArrowColorWithoutBorder);
                        break;
                    case R.styleable.leCheckbox_leBoxSize:
                        mBoxSize = a.getDimensionPixelSize(attr, mBoxSize);
                        break;
                    case R.styleable.leCheckbox_leBoxIsTextOnRight:
                        mIsBoxTextOnRight = a.getBoolean(attr, mIsBoxTextOnRight);
                        break;
                }
            }
        }
        a.recycle();

        boolean clickable = true;
        // a = context
        // .obtainStyledAttributes(attrs, R.styleable.View, defStyle, 0);
        // clickable = a.getBoolean(R.styleable.View_clickable, clickable);
        // a.recycle();
        //
        setClickable(clickable);

        mMaxCircleRadius = mBoxSize / 2;
        if (mWithoutBoxBorder) {
            final int arrowDuration = 300;
            mMeasureSize = mBoxSize;

            mArrowShownAnimator = ObjectAnimator.ofFloat(this, "ArrowInterpolatedTime", 0, 1);
            mArrowShownAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mArrowShownAnimator.setDuration(arrowDuration);
            mArrowShownAnimator.addListener(this);
            mArrowShownAnimator.addUpdateListener(this);

            mArrowHiddenAnimator = ObjectAnimator.ofFloat(this, "ArrowInterpolatedTime", 1, 0);
            mArrowHiddenAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mArrowHiddenAnimator.setDuration(arrowDuration);
            mArrowHiddenAnimator.addListener(this);
            mArrowHiddenAnimator.addUpdateListener(this);
        } else {
            final float enLargeRate = 1.2f;
            final int arrowDuration = 100;
            mMeasureSize = (int) (mBoxSize * enLargeRate);

            mZoomOutAnimator = new AnimatorSet();
            ObjectAnimator animatorOutEnlarge = ObjectAnimator.ofInt(this, "DynimacRadius", 0, mMaxCircleRadius);
            ObjectAnimator animatorArrowShown = ObjectAnimator.ofFloat(this, "ArrowInterpolatedTime", 0, 1);

            animatorOutEnlarge.setInterpolator(new OvershootInterpolator());
            animatorArrowShown.setInterpolator(new AccelerateDecelerateInterpolator());

            animatorOutEnlarge.setDuration(200);
            animatorArrowShown.setDuration(arrowDuration);

            animatorOutEnlarge.addUpdateListener(this);
            animatorArrowShown.addUpdateListener(this);

            mZoomOutAnimator.play(animatorOutEnlarge).before(animatorArrowShown);
            mZoomOutAnimator.addListener(this);

            mZoomInAnimator = new AnimatorSet();
            ObjectAnimator animatorArrowHidden = ObjectAnimator.ofFloat(this, "ArrowInterpolatedTime", 1, 0);
            ObjectAnimator animatorInNarrow = ObjectAnimator.ofInt(this, "DynimacRadius", mMaxCircleRadius, 0);
            animatorArrowHidden.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorInNarrow.setInterpolator(new AnticipateOvershootInterpolator());

            animatorArrowHidden.setDuration(arrowDuration);
            animatorInNarrow.setDuration(200);

            animatorArrowHidden.addUpdateListener(this);
            animatorInNarrow.addUpdateListener(this);

            mZoomInAnimator.play(animatorArrowHidden).before(animatorInNarrow);
            mZoomInAnimator.addListener(this);
        }
        setMinHeight(mMeasureSize);

        if (mWithoutBoxBorder) {
            mShowAnimatior = mArrowShownAnimator;
            mHiddenAnimatior = mArrowHiddenAnimator;
        } else {
            mShowAnimatior = mZoomOutAnimator;
            mHiddenAnimatior = mZoomInAnimator;
        }
        final boolean checked = isChecked();
        mDynimacRadius = checked ? mMaxCircleRadius : 0;
        mArrowInterpolatedTime = checked ? 1 : 0;

        mCircleBoxRadius = mBoxSize / 2;

        mArrawShape = new LeArrowShape(mBoxSize, mWithoutBoxBorder, mWithoutBoxBorder);

        if (isEnabled()) {
            mAlpha = 255;
        } else {
            mAlpha = DISABLE_ALPHA;
        }

        if (mTextColorOnChecked != null) {
            attachAnimateToTextViewColor(this, mTextColorOnChecked.getDefaultColor());
        }
    }

    public void setTrackBoxColor(int colorOn, int colorOff) {
        if (mBoxTrackColor != colorOff) {
            mBoxTrackColor = colorOff;
        }

        if (mBoxTrackColorOn != colorOn) {
            mBoxTrackColorOn = colorOn;
        }
    }

    public void setBoxBorderColor(int color) {
        if (mBoxBorderColor != color) {
            mBoxBorderColor = color;
        }
    }

    public void setArrowColor(int color) {
        if (mArrowColor != color) {
            mArrowColor = color;
        }
    }

    public void setArrowColorWithoutBorder(int color) {
        if (mArrowColorWithoutBorder != color) {
            mArrowColorWithoutBorder = color;
        }
    }

    public void attachAnimateToTextViewColor(TextView textView, int colorOnCheck) {
        if (mArgbEvaluator == null) {
            mArgbEvaluator = new ArgbEvaluator();
        }

        mAnimateTextView = textView;
        mAnimateTextColorOrigin = textView.getCurrentTextColor();
        mAnimateTextColorOnChecked = colorOnCheck;
    }

    public void setDynimacRadius(int radius) {
        mDynimacRadius = radius;
    }

    public void setArrowInterpolatedTime(float interpolatedTime) {
        mArrowInterpolatedTime = interpolatedTime;

        if (mAnimateTextView != null) {
            mAnimateTextViewColor =
                (Integer) mArgbEvaluator.evaluate(interpolatedTime, mAnimateTextColorOrigin, mAnimateTextColorOnChecked);
        }
    }

    private boolean isBoxOnRight() {
        return !mIsBoxTextOnRight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (mMeasureSize > height) {
            height = mMeasureSize;
            heightMode = MeasureSpec.EXACTLY;
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, heightMode));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
        final int padidngLeft = super.getCompoundPaddingLeft();
        final int paddingRight = super.getCompoundPaddingRight();

        final int mMeasureBorderSize = (mMeasureSize - mBoxSize) / 2;

        int boxTop = mMeasureBorderSize + super.getCompoundPaddingTop();
        switch (verticalGravity) {
            case Gravity.BOTTOM:
                boxTop = getHeight() - mBoxSize - mMeasureBorderSize - super.getCompoundPaddingBottom();
                break;
            case Gravity.CENTER_VERTICAL:
                boxTop = (getHeight() - mBoxSize) / 2;
                break;
        }

        int boxBottom = boxTop + mBoxSize;
        int boxLeft =
            isBoxOnRight() ? getWidth() - paddingRight - mBoxSize - mMeasureBorderSize : mMeasureBorderSize + padidngLeft;
        int boxRight =
            isBoxOnRight() ? getWidth() - paddingRight - mMeasureBorderSize : mBoxSize + mMeasureBorderSize + paddingRight;

        mInvalidateRect.left = boxLeft - mMeasureBorderSize;
        mInvalidateRect.right = boxRight + mMeasureBorderSize;
        mInvalidateRect.top = boxTop - mMeasureBorderSize;
        mInvalidateRect.bottom = boxBottom + mMeasureBorderSize;

        mBoxTop = boxTop;
        mBoxBottom = boxBottom;
        mBoxLeft = boxLeft;
        mBoxRight = boxRight;

        mSaveLayerRectF.left = boxLeft;
        mSaveLayerRectF.top = boxTop;
        mSaveLayerRectF.right = boxRight;
        mSaveLayerRectF.bottom = boxBottom;
    }

    @Override
    public int getCompoundPaddingLeft() {
        int padding = super.getCompoundPaddingLeft();
        if (!isBoxOnRight()) {
            padding += mMeasureSize + mBoxInnerPadding;
        }
        return padding;
    }

    @Override
    public int getCompoundPaddingRight() {
        int padding = super.getCompoundPaddingRight();
        if (isBoxOnRight()) {
            padding += mMeasureSize + mBoxInnerPadding;
        }
        return padding;
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(LeCheckBox.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(LeCheckBox.class.getName());
    }

    @Override
    public void toggle() {
        final boolean checked = isChecked();
        setChecked(!checked, true);
    }

    @Override
    public void setChecked(boolean checked) {
        setChecked(checked, false);
    }

    @Override
    public void setChecked(boolean checked, boolean playAnimation) {
        final boolean oldChecked = isChecked();

        super.setChecked(checked);

        if (oldChecked == checked) {
            return;
        }

        if (mIsAnimating && mShowAnimatior != null && mShowAnimatior.isRunning()) {
            mShowAnimatior.cancel();
        } else if (mIsAnimating && mHiddenAnimatior != null && mHiddenAnimatior.isRunning()) {
            mHiddenAnimatior.cancel();
        }

        if (mShowAnimatior == null || mHiddenAnimatior == null || !playAnimation) {
            mDynimacRadius = checked ? mMaxCircleRadius : 0;
            mArrowInterpolatedTime = checked ? 1 : 0;
            invalidate();
            if (mAnimateTextView != null && mTextColorOnChecked != null) {
                mAnimateTextView.setTextColor(isChecked() ? mAnimateTextColorOnChecked : mAnimateTextColorOrigin);
            }
        } else if (checked && mShowAnimatior != null) {
            mShowAnimatior.start();
            mIsAnimating = true;
        } else if (!checked && mHiddenAnimatior != null) {
            mHiddenAnimatior.start();
            mIsAnimating = true;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            mAlpha = 255;
        } else {
            mAlpha = DISABLE_ALPHA;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mShowAnimatior != null && mHiddenAnimatior != null) {
            final boolean checked = isChecked();

            final int left = mBoxLeft;
            final int top = mBoxTop;
            final int boxRadius = mCircleBoxRadius;

            final Paint paint = getPaint();
            final boolean isEnabled = isEnabled();
            final int originColor = paint.getColor();
            final Paint.Style originStyle = paint.getStyle();

            if (!mWithoutBoxBorder) {
                if (!isEnabled) {
                    //canvas.saveLayerAlpha(mSaveLayerRectF, mAlpha,
                    //        Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
                    //                | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                    //                | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                    //                | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
                }
                final int pivotX = left + boxRadius;
                final int pivotY = top + boxRadius;

                paint.setColor(mBoxBorderColor);
                canvas.drawCircle(pivotX, pivotY, boxRadius, paint);

                paint.setColor(mBoxTrackColor);
                canvas.drawCircle(pivotX, pivotY, boxRadius - 1, paint);

                paint.setColor(mBoxTrackColorOn);
                canvas.drawCircle(pivotX, pivotY, mDynimacRadius, paint);

                if (!isEnabled) {
                    canvas.restore();
                }
            }

            if (!isEnabled && mWithoutBoxBorder) {
                //canvas.saveLayerAlpha(mSaveLayerRectF, mAlpha,
                //Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
                //        | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                //        | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                //        | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
            } else {
                canvas.save();
            }
            canvas.translate(left, top);
            if (!mWithoutBoxBorder) {
                mCirclePath.reset();
                mCirclePath.addCircle(boxRadius, boxRadius, boxRadius, Path.Direction.CW);
                canvas.clipPath(mCirclePath);
                paint.setColor(mArrowColor);
            } else {
                paint.setColor(mArrowColorWithoutBorder);
            }

            paint.setStyle(Paint.Style.FILL);

            mArrawShape.setIsShowUp(checked);
            mArrawShape.draw(canvas, paint, mArrowInterpolatedTime);

            canvas.restore();

            paint.setStyle(originStyle);
            paint.setColor(originColor);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (mWithoutBoxBorder && mAnimateTextView != null) {
            mAnimateTextView.setTextColor(mAnimateTextViewColor);
            if (mAnimateTextView == this) {
                return;
            }
        }
        invalidate(mInvalidateRect);
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (animation == mShowAnimatior) {
            mArrowInterpolatedTime = 1;
            mDynimacRadius = mMaxCircleRadius;
        } else if (animation == mHiddenAnimatior) {
            mArrowInterpolatedTime = 0;
            mDynimacRadius = 0;
        }
        mIsAnimating = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        if (animation == mShowAnimatior) {
            mArrowInterpolatedTime = 1;
            mDynimacRadius = mMaxCircleRadius;
        } else if (animation == mHiddenAnimatior) {
            mArrowInterpolatedTime = 0;
            mDynimacRadius = 0;
        }
        mIsAnimating = false;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
