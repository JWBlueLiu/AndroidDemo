package com.example.demoCollection.widget.conversation;

import android.widget.BaseAdapter;

public abstract class AbsTransitionAdapter extends BaseAdapter {
    protected boolean isSelectMode;

    public abstract boolean isDeleteAll();

    public abstract void unSelectAll();

    public abstract void toggleSelect(int position);

    public abstract void deleteCheckedItems();

    public void setSelectMode(boolean selectMode) {
        isSelectMode = selectMode;
    }

    public boolean isSelectMode() {
        return isSelectMode;
    }

    @Override
    public boolean hasStableIds() { // 必须返回true，否则过渡动画很怪异
        return true;
    }
}
