package com.example.demoCollection.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import com.example.demoCollection.R;

public class AnswerFragmentAnimHelper {

    private static final float TRANSPARENCY = 0.0f;
    private static final float OPACITY = 1.0f;

    private Context mContext;
    private View containerView;
    private View blackCoverView;
    private View topView;
    private View bottomView;
    private View topTextView;
    private View bottomTextView;
    private View msgIconView;

    private AnimatorSet enterFloatAnimSet;
    private AnimatorSet floatAnimSet;
    private ObjectAnimator answerFragmentFloatAnim;
    private boolean floatAlphaFlag = true;

    public enum PhoneOperating {
        ANSWER, DECLINE;
    }

    public AnswerFragmentAnimHelper(Context context, View container, View blackCover, View top, View bottom, View topText, View bottomText, View msgIcon) {
        mContext = context;
        containerView = container;
        blackCoverView = blackCover;
        topView = top;
        bottomView = bottom;
        topTextView = topText;
        bottomTextView = bottomText;
        msgIconView = msgIcon;

        setTopBottomOnTouchListener();

        exitAnim = new AnimatorSet();
    }

    /**
     * start enter float anim
     */
    public void startEnterFloatAnim() {
        cancelAnim();

        enterFloatAnimSet = new AnimatorSet();
        enterFloatAnimSet.playSequentially(
                getEnterAnimSet(),
                getFloatAnimSet());
        enterFloatAnimSet.start();
    }

    public AnimatorSet exitAnim;

    /**
     * start exit anim
     *
     * @param phoneOperating
     */
    public void startExitAnim(PhoneOperating phoneOperating) {
        // pause current
        if (null != enterFloatAnimSet) {
            enterFloatAnimSet.pause();
        }

        // slide top text alpha anim
        Animator topTextExitAnim = AnimatorInflater.loadAnimator(mContext, R.animator.slide_text_exit_alpha);
        topTextExitAnim.setTarget(topTextView);
        // slide bottom text alpha anim
        Animator bottomTextExitAnim = AnimatorInflater.loadAnimator(mContext, R.animator.slide_text_exit_alpha);
        bottomTextExitAnim.setTarget(bottomTextView);
        // black cover alpha anim
        Animator blackCoverExitAnim = AnimatorInflater.loadAnimator(mContext, R.animator.black_cover_exit_alpha);
        blackCoverExitAnim.setTarget(blackCoverView);

        if (phoneOperating == PhoneOperating.ANSWER) {   // answer phone anim
            // answer fragment translation y anim
            Animator answerFragmentExitAnim = AnimatorInflater.loadAnimator(mContext, R.animator.fragment_answer_answer_exit_translation_y);
            answerFragmentExitAnim.setTarget(containerView);
            // msg icon alpha anim
            Animator msgIconExitAnim = AnimatorInflater.loadAnimator(mContext, R.animator.slide_text_exit_alpha);
            msgIconExitAnim.setTarget(msgIconView);

            exitAnim.playTogether(answerFragmentExitAnim, blackCoverExitAnim, topTextExitAnim, bottomTextExitAnim, msgIconExitAnim);
        } else if (phoneOperating == PhoneOperating.DECLINE) {    // refuse phone anim
            // answer fragment translation y anim
            Animator answerFragmentExitAnim = AnimatorInflater.loadAnimator(mContext, R.animator.fragment_answer_decline_exit_translation_y);
            answerFragmentExitAnim.setTarget(containerView);

            exitAnim.playTogether(answerFragmentExitAnim, blackCoverExitAnim, topTextExitAnim, bottomTextExitAnim);
        }
        exitAnim.start();
    }

    /**
     * cancel anim
     */
    public void cancelAnim() {
        if (null != enterFloatAnimSet) {
            enterFloatAnimSet.cancel();
        }
        if (null != floatAnimSet) {
            floatAnimSet.cancel();
        }
        topTextView.setAlpha(TRANSPARENCY);
        bottomTextView.setAlpha(TRANSPARENCY);
    }

    private AnimatorSet getEnterAnimSet() {
        // answer fragment enter anim
        Animator answerFragmentEnterAnim = AnimatorInflater.loadAnimator(mContext, R.animator.fragment_answer_enter_translation_y);
        answerFragmentEnterAnim.setTarget(null);
        // black cover enter anim
        Animator blackCoverEnterAnim = AnimatorInflater.loadAnimator(mContext, R.animator.black_cover_enter_alpha);
        blackCoverEnterAnim.setTarget(blackCoverView);
        // slide down text enter anim
        Animator slideDownTextEnterAnim = AnimatorInflater.loadAnimator(mContext, R.animator.slide_text_enter_alpha);
        slideDownTextEnterAnim.setTarget(topTextView);

        AnimatorSet enterAnimSet = new AnimatorSet();
        enterAnimSet.playTogether(answerFragmentEnterAnim, blackCoverEnterAnim, slideDownTextEnterAnim);
        return enterAnimSet;
    }

    public AnimatorSet getFloatAnimSet() {
        if (null == answerFragmentFloatAnim) {
            // answer fragment float anim
            answerFragmentFloatAnim = (ObjectAnimator) AnimatorInflater.loadAnimator(mContext, R.animator.fragment_answer_float);
            answerFragmentFloatAnim.setTarget(containerView);
            // slide text float 1.0 to 0.0 anim
            final Animator slideTextFloatAnim1 = AnimatorInflater.loadAnimator(mContext, R.animator.slide_text_float_alpha1);
            slideTextFloatAnim1.setTarget(topTextView);
            // slide text float 0.0 to 1.0 anim
            final Animator slideTextFloatAnim0 = AnimatorInflater.loadAnimator(mContext, R.animator.slide_text_float_alpha0);
            slideTextFloatAnim0.setTarget(bottomTextView);

            // slide text float anim
            answerFragmentFloatAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                    slideTextFloatAnim1.end();
                    slideTextFloatAnim0.end();

                    if (floatAlphaFlag) {
                        slideTextFloatAnim1.setTarget(bottomTextView);
                        slideTextFloatAnim0.setTarget(topTextView);
                    } else {
                        slideTextFloatAnim1.setTarget(topTextView);
                        slideTextFloatAnim0.setTarget(bottomTextView);

                    }
                    floatAlphaFlag = !floatAlphaFlag;
                    slideTextFloatAnim1.start();
                    slideTextFloatAnim0.start();
                }
            });

            floatAnimSet = new AnimatorSet();
            floatAnimSet.playTogether(answerFragmentFloatAnim, slideTextFloatAnim1, slideTextFloatAnim0);
        }

        return floatAnimSet;
    }

    private void setTopBottomOnTouchListener() {
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // pause anim
                        floatAnimPause();
                        // set alpha
                        int id = v.getId();
                        if (id == R.id.answerFr_shadow) {
                            topTextView.setAlpha(OPACITY);
                            bottomTextView.setAlpha(TRANSPARENCY);
                        } else if (id == R.id.answerFr_bottom) {
                            bottomTextView.setAlpha(OPACITY);
                            topTextView.setAlpha(TRANSPARENCY);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        floatAnimResume();
                        return true;
                }
                return false;
            }
        };
        topView.setOnTouchListener(onTouchListener);
        bottomView.setOnTouchListener(onTouchListener);
    }

    private void floatAnimPause() {
        if (null != floatAnimSet) {
            floatAnimSet.pause();
        }
    }

    private void floatAnimResume() {
        if (null != floatAnimSet) {
            floatAnimSet.resume();
        }
    }
}
