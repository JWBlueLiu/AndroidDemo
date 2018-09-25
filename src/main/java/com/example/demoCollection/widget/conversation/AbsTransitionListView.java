package com.example.demoCollection.widget.conversation;

import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.widget.ListView;
import com.example.demoCollection.R;

public abstract class AbsTransitionListView extends ListView {

    public boolean mIsDeletingAnimRunning;
    public ActionMode mActionMode;

    public AbsTransitionListView(Context context) {
        this(context, null);
    }

    public AbsTransitionListView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public AbsTransitionListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 进入多选模式
    public void enterSelectMode(AbsTransitionAdapter adapter) {
        // 自定义函数，通知adapter，进入多选模式，即显示checkbox
        adapter.setSelectMode(true);
        // 启动 进入多选模式 的动画
        TransitionManager.beginDelayedTransition(this, createEnterTransitions());
        adapter.notifyDataSetInvalidated();
    }

    // 退出多选模式
    public void exitSelectMode(AbsTransitionAdapter adapter) {
        adapter.setSelectMode(false); // 自定义函数，通知adapter，退出多选模式，即不显示checkbox
        adapter.unSelectAll();

        // 启动 退出多选模式 的动画
        TransitionManager.beginDelayedTransition(this, createExitTransitions());
        adapter.notifyDataSetInvalidated();
    }

    public void deleteCheckedItems(AbsTransitionAdapter adapter) {
        if (adapter.isDeleteAll()) {
            setEmptyView(null);
        }

        // 启动 删除动画
        if (getLastVisiblePosition() == getCount() - 1) {
            TransitionManager.beginDelayedTransition(this, createDeleteItemsTopTransitions());
        } else {
            TransitionManager.beginDelayedTransition(this, createDeleteItemsBottomTransitions());
        }

        // 通知adapter，退出多选模式，即不显示checkbox
        adapter.setSelectMode(false); // 自定义函数，通知adapter，退出多选模式，即不显示checkbox

        // 在adapter 中删除选中的行，这里可能是其他函数，例如在数据库中删除某些行
        adapter.deleteCheckedItems();  // 自定义函数
        adapter.notifyDataSetChanged();
    }

    protected Transition createEnterTransitions() {
        // 初始化 进入多选模式 的动画
//        com.android.internal.R.transition.listview_item_add_checkbox
        return createTransitions(R.transition.listview_item_add_checkbox);
    }

    protected Transition createExitTransitions() {
        // 初始化 退出多选模式 的动画
//        com.android.internal.R.transition.listview_item_delete_checkbox
        return createTransitions(R.transition.listview_item_delete_checkbox);
    }

    protected Transition createAddItemsTransitions() {
        // 初始化 添加Item 的动画
//        Transition addItems = inflater.inflateTransition(com.android.internal.R.transition.listview_add_items);
        return createTransitions(R.transition.listview_add_items);
    }

    protected Transition createDeleteItemsBottomTransitions() {
//        Transition deleteItemsBottom = inflater.inflateTransition(com.android.internal.R.transition.listview_delete_items_without_checkbox_slideup);
        Transition deleteItemsBottom = createTransitions(R.transition.listview_delete_items_with_checkbox_slideup);
        deleteItemsBottom.addListener(deleteItemListener);
        setListViewItemIdToTransition((TransitionSet) deleteItemsBottom);
        return deleteItemsBottom;
    }

    protected Transition createDeleteItemsTopTransitions() {
//        Transition deleteItemsTop = inflater.inflateTransition(com.android.in ternal.R.transition.listview_delete_items_without_checkbox_slidedown);
        Transition deleteItemsTop = createTransitions(R.transition.listview_delete_items_with_checkbox_slidedown);
        deleteItemsTop.addListener(deleteItemListener);
        setListViewItemIdToTransition((TransitionSet) deleteItemsTop);
        return deleteItemsTop;
    }

    Transition.TransitionListener deleteItemListener = new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {
            mIsDeletingAnimRunning = true; // 正在做删除动画
            mActionMode.finish();  // 退出action mode
        }

        @Override
        public void onTransitionResume(Transition transition) {
        }

        @Override
        public void onTransitionPause(Transition transition) {
        }

        @Override
        public void onTransitionEnd(Transition transition) {
            onTransitionEndOrCancel();
        }

        @Override
        public void onTransitionCancel(Transition transition) {
            onTransitionEndOrCancel();
        }
    };

    protected abstract void setListViewItemIdToTransition(TransitionSet transitionSet);

    void onTransitionEndOrCancel() {
        mIsDeletingAnimRunning = false; // 删除动画已经做完
        invalidate(); // 动画完成，必须刷新一下，否则ListView分隔线可能显示异常
    }

    private Transition createTransitions(int transRes) {
        return TransitionInflater.from(getContext()).inflateTransition(transRes);
    }

}
