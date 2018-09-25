package com.example.demoCollection.animation.SetupWizard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.example.demoCollection.R;
import java.util.ArrayList;
import java.util.List;

public class WifiConnectView1 extends View implements ValueAnimator.AnimatorUpdateListener {

    private List<BallShapeHolder> balls = new ArrayList<BallShapeHolder>();
    private AnimatorSet wifiConnectAnim;

    private float centerX;
    private float centerY;

    private static final int NORMAL = 0;
    private static final int CENTER = 1;
    private static final int CENTER_VERTICAL = 2;
    private static final int CENTER_HORIZONTAL = 3;

    // back ball
    private int mGravity = NORMAL;
    private int Back_BALL_ZOOM_OUT_DURATION = 1000;
    private float NORMAL_BACK_BALL_RADIUS = 127;
    private int mBackBallColor = Color.parseColor("#43AD69");

    // front ball
    private int FRONT_BALL_ZOOM_OUT_DURATION = 175;
    private int ROTATE_DURATION = 70;
    private int mFrontBallColor = Color.parseColor("#FFFFFF");
    /**
     * ball 1 to ball 3
     */
    private int FRONT_BALL_ZOOM_OUT_DELAY2 = 70;
    private float NORMAL_BALL1_RADIUS = 4;
    private float ARC_DEGREE = 90;
    private int DRAW_ARC_DURATION = 350;
    private int DRAW_ARC_DELAY = 35;
    /**
     * ball 4
     */
    private int FRONT_BALL_ZOOM_OUT_DELAY1 = 350;
    private float NORMAL_BALL4_RADIUS = 8;

    private boolean zoomEnd = false;
    private boolean rotateEnd = false;
    private float degree;

    private Paint mArcPaint;
    private boolean mHasConnect;
    private boolean mStarted;

    private int moveDistance;

    public WifiConnectView1(Context context) {
        this(context, null);
    }

    public WifiConnectView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WifiConnectView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // read attr
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WifiConnectView);
        mGravity = a.getInt(R.styleable.WifiConnectView_wifiGravity, 0);
        a.recycle();

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        mArcPaint.setStrokeJoin(Paint.Join.ROUND);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setColor(mFrontBallColor);
        mArcPaint.setStyle(Paint.Style.STROKE);

        moveDistance = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    public void startAnimation(boolean hasConnect) {
        cancelAnimation();
        mHasConnect = hasConnect;
        // delay start
        post(new Runnable() {
            @Override
            public void run() {
                createAnimation();
                wifiConnectAnim.start();
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        centerX = NORMAL_BACK_BALL_RADIUS;
        centerY = NORMAL_BACK_BALL_RADIUS;

        switch (mGravity) {
            case CENTER: {
                centerX = getWidth() / 2;
                centerY = getHeight() / 2;
            }
            break;
            case CENTER_VERTICAL: {
                centerY = getHeight() / 2;
            }
            break;
            case CENTER_HORIZONTAL: {
                centerX = getWidth() / 2;
            }
            break;
        }
    }

    private void createAnimation() {
        float thirdCircleHeight = NORMAL_BACK_BALL_RADIUS * 2 / 3;
        float interval = (thirdCircleHeight - NORMAL_BALL1_RADIUS * 2 * 4) / 3;

        addBall(centerX, centerY, mBackBallColor);
        addBall(centerX - NORMAL_BALL1_RADIUS / 2, centerY - NORMAL_BACK_BALL_RADIUS + thirdCircleHeight + NORMAL_BALL1_RADIUS, mFrontBallColor);
        addBall(centerX - NORMAL_BALL1_RADIUS / 2, centerY - NORMAL_BACK_BALL_RADIUS + thirdCircleHeight + NORMAL_BALL1_RADIUS * 3 + interval, mFrontBallColor);
        addBall(centerX - NORMAL_BALL1_RADIUS / 2, centerY - NORMAL_BACK_BALL_RADIUS + thirdCircleHeight + NORMAL_BALL1_RADIUS * 5 + interval * 2, mFrontBallColor);
        addBall(centerX - NORMAL_BALL4_RADIUS / 2, centerY - NORMAL_BACK_BALL_RADIUS + thirdCircleHeight * 2, mFrontBallColor);

        if (null == wifiConnectAnim) {
            BallShapeHolder ball;
            // zoom
            ObjectAnimator ballZoomOut;
            ObjectAnimator backBallZoomOut = null;
            ObjectAnimator ball1ZoomOut = null;
            ObjectAnimator ball2ZoomOut = null;
            ObjectAnimator ball3ZoomOut = null;
            ObjectAnimator ball4ZoomOut = null;
            for (int i = 0; i < balls.size(); i++) {
                ball = balls.get(i);
                switch (i) {
                    case 0:
                        PropertyValuesHolder pvhR = PropertyValuesHolder
                                .ofFloat("radius", 0, NORMAL_BACK_BALL_RADIUS);
                        PropertyValuesHolder pvTX = PropertyValuesHolder
                                .ofFloat("x", ball.getX(), ball.getX() - NORMAL_BACK_BALL_RADIUS);
                        PropertyValuesHolder pvTY = PropertyValuesHolder
                                .ofFloat("y", ball.getY(), ball.getY() - NORMAL_BACK_BALL_RADIUS);
                        backBallZoomOut = ObjectAnimator
                                .ofPropertyValuesHolder(ball, pvhR, pvTX, pvTY)
                                .setDuration(Back_BALL_ZOOM_OUT_DURATION);
                        backBallZoomOut.addUpdateListener(this);
                        backBallZoomOut.setInterpolator(AnimationUtils.loadInterpolator(getContext(), android.R.interpolator.accelerate_decelerate));
                        break;
                    case 1:
                    case 2:
                    case 3:
                        pvhR = PropertyValuesHolder.ofFloat("radius", 0, NORMAL_BALL1_RADIUS);
                        pvTX = PropertyValuesHolder
                                .ofFloat("x", ball.getX(), ball.getX() - NORMAL_BALL1_RADIUS);
                        pvTY = PropertyValuesHolder
                                .ofFloat("y", ball.getY(), ball.getY() - NORMAL_BALL1_RADIUS);
                        ballZoomOut = ObjectAnimator.ofPropertyValuesHolder(ball, pvhR, pvTX, pvTY)
                                .setDuration(FRONT_BALL_ZOOM_OUT_DURATION);
                        ballZoomOut.setStartDelay(
                                FRONT_BALL_ZOOM_OUT_DELAY1 + FRONT_BALL_ZOOM_OUT_DELAY2 * (4 - i));
                        ballZoomOut.addUpdateListener(this);
                        ballZoomOut.setInterpolator(AnimationUtils.loadInterpolator(getContext(), android.R.interpolator.accelerate_decelerate));

                        switch (i) {
                            case 1:
                                ball1ZoomOut = ballZoomOut;
                                ball1ZoomOut.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        zoomEnd = true;
                                    }
                                });
                                break;
                            case 2:
                                ball2ZoomOut = ballZoomOut;
                                break;
                            case 3:
                                ball3ZoomOut = ballZoomOut;
                                break;
                        }
                        break;
                    case 4:
                        pvhR = PropertyValuesHolder.ofFloat("radius", 0, NORMAL_BALL4_RADIUS);
                        pvTX = PropertyValuesHolder
                                .ofFloat("x", ball.getX(), ball.getX() - NORMAL_BALL4_RADIUS);
                        pvTY = PropertyValuesHolder
                                .ofFloat("y", ball.getY(), ball.getY() - NORMAL_BALL4_RADIUS);
                        ball4ZoomOut = ObjectAnimator.ofPropertyValuesHolder(ball, pvhR, pvTX, pvTY)
                                .setDuration(FRONT_BALL_ZOOM_OUT_DURATION);
                        ball4ZoomOut.setStartDelay(FRONT_BALL_ZOOM_OUT_DELAY1);
                        ball4ZoomOut.addUpdateListener(this);
                        ball4ZoomOut.setInterpolator(AnimationUtils.loadInterpolator(getContext(), android.R.interpolator.accelerate_decelerate));
                        break;
                }
            }

            // rotate
            ObjectAnimator ballRotate1 = ObjectAnimator.ofFloat(this, "degree", 0, 10);
            ballRotate1.setStartDelay(FRONT_BALL_ZOOM_OUT_DELAY1 + FRONT_BALL_ZOOM_OUT_DELAY2 * 3 +
                    FRONT_BALL_ZOOM_OUT_DURATION);
            ballRotate1.setDuration(ROTATE_DURATION);
            ballRotate1.setInterpolator(AnimationUtils.loadInterpolator(getContext(), android.R.interpolator.accelerate_decelerate));
            ballRotate1.addUpdateListener(this);

            ObjectAnimator ballRotate2 = ObjectAnimator.ofFloat(this, "degree", 10, -45);
            ballRotate2.setStartDelay(FRONT_BALL_ZOOM_OUT_DELAY1 + FRONT_BALL_ZOOM_OUT_DELAY2 * 3 +
                    FRONT_BALL_ZOOM_OUT_DURATION + ROTATE_DURATION);
            ballRotate2.setDuration(ROTATE_DURATION);
            ballRotate2.addUpdateListener(this);
            ballRotate2.setInterpolator(AnimationUtils.loadInterpolator(getContext(), android.R.interpolator.accelerate_decelerate));
            ballRotate2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rotateEnd = true;
                }
            });

            // draw arc
            List<ObjectAnimator> ballArcs = new ArrayList<ObjectAnimator>();
            for (int i = 1; i < 4; i++) {
                ObjectAnimator ballArc = ObjectAnimator.ofFloat(balls.get(i), "arc", 0, ARC_DEGREE);
                ballArc.setStartDelay(FRONT_BALL_ZOOM_OUT_DELAY1 + FRONT_BALL_ZOOM_OUT_DELAY2 * 3 +
                        FRONT_BALL_ZOOM_OUT_DURATION + ROTATE_DURATION * 2 +
                        DRAW_ARC_DELAY * (3 - i));
                ballArc.setDuration(DRAW_ARC_DURATION);
                ballArc.addUpdateListener(this);
                ballArc.setInterpolator(AnimationUtils.loadInterpolator(getContext(), android.R.interpolator.accelerate_decelerate));
                ballArcs.add(ballArc);
            }

            wifiConnectAnim = new AnimatorSet();

            if (mHasConnect) {
                wifiConnectAnim.playTogether(
                        backBallZoomOut,    // back ball zoom
                        ball1ZoomOut, ball2ZoomOut, ball3ZoomOut, ball4ZoomOut,
                        // ball 1 to ball 4 zoom
                        ballRotate1, ballRotate2,   // ball 1 to ball 3 rotate
                        ballArcs.get(0), ballArcs.get(1),
                        ballArcs.get(2));  // ball 1 to ball 3 draw arc
            } else {
                backBallZoomOut.setDuration(Back_BALL_ZOOM_OUT_DURATION);
                ball4ZoomOut.setDuration(Back_BALL_ZOOM_OUT_DURATION);
                ball4ZoomOut.setStartDelay(0);
                for (ObjectAnimator ballArc : ballArcs) {
                    ballArc.setDuration(0);
                    ballArc.setStartDelay(0);
                }

                wifiConnectAnim.playTogether(
                        backBallZoomOut,    // back ball zoom
                        ball4ZoomOut,
                        ballArcs.get(0), ballArcs.get(1), ballArcs.get(2));  // ball 1 to ball 3 draw arc
            }
        }
    }

    public void cancelAnimation() {
        if (null != wifiConnectAnim && wifiConnectAnim.isRunning()) {
            wifiConnectAnim.cancel();
        }
        wifiConnectAnim = null;
        balls.clear();
        // init
        degree = 0;
        zoomEnd = false;
        rotateEnd = false;

        invalidate();
    }

    private void addBall(float x, float y, int color) {
        OvalShape circle = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(circle);

        BallShapeHolder ball = new BallShapeHolder(shapeDrawable);
        ball.setX(x);
        ball.setY(y);
        ball.setColor(color);

        balls.add(ball);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            result = (int) NORMAL_BACK_BALL_RADIUS * 2;
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (balls.size() > 0) {
            BallShapeHolder ball;
            ball = balls.get(0);
            canvas.translate(ball.getX(), ball.getY());
            ball.getShapeDrawable().draw(canvas);
            canvas.translate(-ball.getX(), -ball.getY());

            canvas.save();
            if (zoomEnd) {
                canvas.rotate(degree, balls.get(4).getX(), balls.get(4).getY());
            }
            for (int i = 1; i <= 4; i++) {
                ball = balls.get(i);

                if (!mHasConnect) {
                    float thirdCircleHeight = balls.get(0).getRadius() * 2 / 3;
                    float interval = (thirdCircleHeight - balls.get(1).getRadius() * 2 * 4) / 3;

                    if (i == 4) {
                        ball.setY(centerY - balls.get(0).getRadius() + thirdCircleHeight * 2);
                    } else {
                        ball.setY(centerY - balls.get(0).getRadius() + thirdCircleHeight + balls.get(i).getRadius() * (2 * i - 1) + interval * (i - 1) * 0.9f);
                    }
                }

                canvas.translate(ball.getX(), ball.getY());
                ball.getShapeDrawable().draw(canvas);
                canvas.translate(-ball.getX(), -ball.getY());
            }
            canvas.restore();

            // draw arc
            for (int i = 1; i < 4; i++) {
                if (rotateEnd || !mHasConnect) {
                    ball = balls.get(i);

                    if (mHasConnect) {
                        ball.setRadius(0);
                    }

                    BallShapeHolder b = balls.get(4);

                    RectF rectF = new RectF();
                    float radius = b.getY() - ball.getY();

                    if (!mHasConnect) {
                        float rate = balls.get(0).getRadius() / NORMAL_BACK_BALL_RADIUS;
                        radius *= rate;
                    }

                    rectF.top = ball.getY();
                    rectF.bottom = ball.getY() + radius * 2;
                    rectF.left = b.getX() - radius + b.getRadius();
                    if (mHasConnect) {
                        rectF.left = b.getX() - radius + b.getRadius() - moveDistance;
                        rectF.right = b.getX() + radius + b.getRadius() * 2;
                    } else {
                        rectF.top = ball.getY() + b.getRadius();
                        rectF.right = b.getX() + radius + b.getRadius();
                    }

                    mArcPaint.setStrokeWidth(NORMAL_BALL1_RADIUS * 2 * (balls.get(0).getRadius() / NORMAL_BACK_BALL_RADIUS));
                    canvas.drawArc(rectF, 225, ball.getArc(), false, mArcPaint);
                }
            }
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        invalidate();
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public WifiConnectView1 setBackBallColor(int color) {
        this.mBackBallColor = color;
        return this;
    }

    public WifiConnectView1 setFrontBallColor(int color) {
        this.mFrontBallColor = color;
        return this;
    }

    public WifiConnectView1 setBackBall(int radius) {
        NORMAL_BACK_BALL_RADIUS = radius;
        return this;
    }

}
