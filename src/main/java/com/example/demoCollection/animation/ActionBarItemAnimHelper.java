package com.example.demoCollection.animation;

import android.animation.AnimatorInflater;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.example.demoCollection.R;
import java.lang.reflect.Field;

public class ActionBarItemAnimHelper {

    private static final String FIELD_DECOR_TOOLBAR = "mDecorToolbar";
    private static final String FIELD_TOOLBAR = "mToolbar";
    private static final String FIELD_NAV_BUTTON_VIEW = "mNavButtonView";
    private static final String FIELD_TITLE_TEXT_VIEW = "mTitleTextView";
    private static final String TOOLBAR_WIDGET_WRAPPER = "ToolbarWidgetWrapper";
    private static final String TOOLBAR = "Toolbar";

    /**
     * 将5.0默认的波纹点击效果取消
     * 采用类似Iphone按钮点击效果
     * 包括back键
     *
     * @param act
     * @param ids
     */
    public static void setDefaultItemAnim(final Activity act, final Object actionBar, final int... ids) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // 遍历所有Item id设置动画
                for (int id : ids) {
                    setViewAnim(act.getApplicationContext(), act.findViewById(id));
                }
                // 设置back键动画
                try {
                    // DecorToolbar or Toolbar
                    Object mDecorToolbar = getFieldObj(actionBar, FIELD_DECOR_TOOLBAR);
                    // 通过mDecorToolbar获取Toolbar
                    Object toolBar = getFieldObj(mDecorToolbar, FIELD_TOOLBAR);
                    // 通过toolBar获取ActionBar返回键
                    View navButtonView = (View) getFieldObj(toolBar, FIELD_NAV_BUTTON_VIEW);
                    setViewAnim(act.getApplicationContext(), navButtonView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void setActionBarTitleStyle(final Object actionBar) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    Object mDecorToolbar = getFieldObj(actionBar, FIELD_DECOR_TOOLBAR);
                    Object toolBar = getFieldObj(mDecorToolbar, FIELD_TOOLBAR);
                    TextView titleTextView = (TextView) getFieldObj(toolBar, FIELD_TITLE_TEXT_VIEW);
                    titleTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 设置view的statelist动画
     *
     * @param context
     * @param view
     */
    private static void setViewAnim(Context context, View view) {
        if (null != view) {
            view.setBackground(null);
            view.setStateListAnimator(AnimatorInflater.loadStateListAnimator(
                    context,
                    R.anim.action_bar_item_state_list_anim
            ));
        }
    }

    /**
     * 通过反射获取成员变量
     *
     * @param obj
     * @param fieldName
     * @return
     */
    private static Object getFieldObj(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

}
