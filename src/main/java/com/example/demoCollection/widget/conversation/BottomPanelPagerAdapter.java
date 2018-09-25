package com.example.demoCollection.widget.conversation;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class BottomPanelPagerAdapter extends PagerAdapter {
    private String TAG = getClass().getSimpleName();

    private List<View> mBottomPanelList;

    public BottomPanelPagerAdapter(List<View> bottomPanelList) {
        mBottomPanelList = bottomPanelList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mBottomPanelList.get(position), 0);
        return mBottomPanelList.get(position);
    }

    @Override
    public int getCount() {
        return mBottomPanelList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mBottomPanelList.get(position));
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
