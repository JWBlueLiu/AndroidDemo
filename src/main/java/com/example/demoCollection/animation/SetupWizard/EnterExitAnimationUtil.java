package com.example.demoCollection.animation.SetupWizard;

import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import com.example.demoCollection.R;
import java.util.List;

public class EnterExitAnimationUtil {

    public static final int ENTER = 1;
    public static final int EXIT = 2;
    public static final int WIFI_ENTER = 3;
    public static final int WIFI_EXIT = 4;
    public static final int WIFI_ADD = 5;
    public static final int WIFI_REMOVE = 6;

    private static final int DELAY = 35;

    public static void startAnim(Context context, ViewGroup vg, int style, List<Integer> togetherIdList, List<Integer> exceptIdList) {
        if (null != vg) {
            int startDelay = 0;

            int animId = 0;
            // load anim
            switch (style) {
                case ENTER:
                    animId = R.anim.anim_setup_wizard_slide_enter;
                    break;
                case EXIT:
                    animId = R.anim.anim_setup_wizard_slide_exit;
                    break;
            }
            Animation animation = AnimationUtils.loadAnimation(context, animId);

            for (int i = 0; i < vg.getChildCount(); i++) {
                View view = vg.getChildAt(i);
                int id = view.getId();
                if (null == exceptIdList || exceptIdList.contains(id)) {
                    continue;
                } else {

                    // in togetherIdList
                    if (null != togetherIdList && togetherIdList.contains(id)) {
                        view.startAnimation(animation);
                        continue;
                    }

                    startDelay += DELAY;
                    animation.setStartOffset(startDelay);

                    // start anim
                    if (view instanceof ViewGroup && ((ViewGroup) view).getLayoutAnimation() != null) {
                        ((ViewGroup) view).getLayoutAnimation().getAnimation().setStartOffset(startDelay);
                        ((ViewGroup) view).startLayoutAnimation();
                    } else {
                        view.startAnimation(animation);
                    }
                }
            }
        }
    }

    /**
     * set layout anim, used in list
     *
     * @param context
     * @param vg
     * @param style
     */
    public static void setLayoutAnim(Context context, ViewGroup vg, int style, float delay) {
        if (null != vg) {
            int animId = 0;
            switch (style) {
                case ENTER:
                    animId = R.anim.layout_anim_language_choose_slide_enter;
                    break;
                case EXIT:
                    animId = R.anim.layout_anim_language_choose_slide_exit;
                    break;
                case WIFI_ENTER:
                    animId = R.anim.layout_anim_wifi_list_slide_enter;
                    break;
                case WIFI_EXIT:
                    animId = R.anim.layout_anim_wifi_list_slide_exit;
                    break;
            }
            LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, animId);
            layoutAnimationController.setDelay(delay);
            vg.setLayoutAnimation(layoutAnimationController);
        }
    }

    /**
     * set layout transition anim
     *
     * @param vg
     * @param style
     */
    public static void setLayoutTransitionAnim(ViewGroup vg, int style) {
        if (null != vg) {
            LayoutTransition lt = new LayoutTransition();

            ObjectAnimator translationXAnim;
            ObjectAnimator alphaAnim;

            switch (style) {
                case WIFI_ADD:
                    AnimatorSet wifiAddAnim = new AnimatorSet();

                    translationXAnim = ObjectAnimator.ofFloat(null, View.TRANSLATION_X, 100, 0);
                    translationXAnim.setInterpolator(new AccelerateDecelerateInterpolator());
                    alphaAnim = ObjectAnimator.ofFloat(null, View.ALPHA, 0.0f, 1.0f);
                    alphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());

                    wifiAddAnim.playTogether(translationXAnim);
                    wifiAddAnim.setDuration(500);

                    lt.setAnimator(LayoutTransition.APPEARING, translationXAnim);
                    break;
                case WIFI_REMOVE:
                    AnimatorSet wifiRemoveAnim = new AnimatorSet();

                    translationXAnim = ObjectAnimator.ofFloat(null, View.TRANSLATION_X, 0, -100);
                    translationXAnim.setInterpolator(new AccelerateDecelerateInterpolator());
                    alphaAnim = ObjectAnimator.ofFloat(null, View.ALPHA, 1.0f, 0.0f);
                    alphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());

                    wifiRemoveAnim.playTogether(translationXAnim);
                    wifiRemoveAnim.setDuration(500);

                    lt.setAnimator(LayoutTransition.DISAPPEARING, translationXAnim);
                    break;
            }

            vg.setLayoutTransition(lt);
        }
    }

    public static void iconScaleEnter(ImageView view, int resid) {
        iconScaleResume(view);
        view.setImageResource(resid);
        Animatable animatable = (Animatable) view.getDrawable();
        if (null != animatable) {
            animatable.stop();
            animatable.start();
        }
    }

    public static void iconScaleExit(Context context, final View view) {
        view.animate()
                .scaleX(0).scaleY(0)
                .setInterpolator(new AnticipateInterpolator())
                .setDuration(500)
                .start();
    }

    public static void iconScaleResume(View view) {
        view.setScaleX(1);
        view.setScaleY(1);
    }

}
