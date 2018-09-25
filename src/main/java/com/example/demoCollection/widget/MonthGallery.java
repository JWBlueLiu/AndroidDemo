package com.example.demoCollection.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.demoCollection.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MonthGallery extends DateGallery {
    private final String TAG = getClass().getSimpleName();

    private static int SELECTED_SIZE = 36;
    private static int UNSELECTED_SIZE = 24;

    private static int SELECTED_COLOR = Color.parseColor("#595757");
    private static int UNSELECTED_COLOR = Color.parseColor("#B4B4B5");

    private static final int START_MONTH = 1;
    private static final int END_MONTH = 12;

    private MonthAdapter mMonthAdapter;
    private int selected;

    public MonthGallery(Context context) {
        this(context, null);
    }

    public MonthGallery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mMonthAdapter = new MonthAdapter(getContext(), getMonthesList(START_MONTH, END_MONTH));
        setAdapter(mMonthAdapter);
        setSpacing(dip2px(getContext(), 83));

        setCallbackDuringFling(false);
        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (null != mSelectedListener && null != view) {
                    mSelectedListener.onItemSelected(view);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        setSelected((LinearLayout) getSelectedView(), SELECTED_SIZE, SELECTED_COLOR);
    }

    private void setSelected(final ViewGroup ll, final int size, final int color) {
        if (null != ll && ll.getWidth() != 0) {
            TextView tv = (TextView) ll.getChildAt(0);
            tv.setTextSize(size);
            tv.setTextColor(color);
        }
    }

    private List<String> getMonthesList(int startMonth, int endMonth) {
        String[] monthesArr = getResources().getStringArray(R.array.le_month_name);
        return new ArrayList<String>(Arrays.asList(monthesArr));
    }

    public void setMonthes(int yearNum) {
        List<String> monthes = getMonthesList(START_MONTH, END_MONTH);
        List<String> monthList = new ArrayList<String>();
        for (int i = 0; i < yearNum; i++) {
            monthList.addAll(monthes);
        }
        mMonthAdapter.updateStartEndMonth(monthList);
    }

    class MonthAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> mMonthes;
        private LayoutInflater layoutInflater;

        MonthAdapter(Context context, List<String> monthes) {
            mContext = context;
            mMonthes = monthes;
            layoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            if (null != mMonthes) {
                return mMonthes.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mMonthes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.le_date_gallery_month_view, null);
                holder = new ViewHolder();

                holder.tv = (TextView) convertView.findViewById(R.id.tv_month);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv.setText(mMonthes.get(position));

            if (!isSelected()) {
                setSelected((ViewGroup) convertView, UNSELECTED_SIZE, UNSELECTED_COLOR);
            } else {
                setSelected((ViewGroup) convertView, SELECTED_SIZE, SELECTED_COLOR);
            }

            return convertView;
        }

        public void updateStartEndMonth(List<String> monthes) {
            if (null != monthes) {
                mMonthes.clear();
                mMonthes.addAll(monthes);
                notifyDataSetChanged();
            }
        }

        public class ViewHolder {
            TextView tv;
        }
    }
}
