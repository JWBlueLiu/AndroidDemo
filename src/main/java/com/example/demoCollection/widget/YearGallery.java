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
import java.util.Calendar;
import java.util.List;

public class YearGallery extends DateGallery {
    private final String TAG = getClass().getSimpleName();

    private static final int START_YEAR = 1970;
    private static final int END_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private static final int SELECTED_COLOR = Color.parseColor("#29C0B0");
    private static final int SELECTED_VISIBLE = View.VISIBLE;

    private static final int UNSELECTED_COLOR = Color.parseColor("#B4B4B5");
    private static final int UNSELECTED_VISIBLE = View.INVISIBLE;

    private YearAdapter mYearAdapter;

    private int selected;

    public YearGallery(Context context) {
        this(context, null);
    }

    public YearGallery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mYearAdapter = new YearAdapter(getContext(), getYearsList(START_YEAR, END_YEAR));
        setAdapter(mYearAdapter);
        setSpacing(dip2px(getContext(), 35));

        setCallbackDuringFling(false);
        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout ll;

                if (selected != position) {
                    ll = (LinearLayout) parent.getChildAt(selected - parent.getFirstVisiblePosition());
                    if (null != ll) {
                        setSelected(ll, UNSELECTED_COLOR, UNSELECTED_VISIBLE);
                    }
                    selected = position;
                }

                ll = (LinearLayout) view;
                setSelected(ll, SELECTED_COLOR, SELECTED_VISIBLE);

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

        setSelected((LinearLayout) getSelectedView(), SELECTED_COLOR, SELECTED_VISIBLE);
    }

    private void setSelected(LinearLayout ll, int color, int visible) {
        if (null != ll) {
            TextView tv = (TextView) ll.getChildAt(0);
            tv.setTextColor(color);

            View v = ll.getChildAt(1);
            v.setVisibility(visible);
        }
    }

    private List<String> getYearsList(int startYear, int endYear) {
        List<String> years = new ArrayList<String>();
        for (int i = startYear; i <= endYear; i++) {
            years.add(String.valueOf(i));
        }
        return years;
    }

    public void setStartEndYear(int startYear, int endYear) {
        if (startYear == 0) {
            startYear = START_YEAR;
        }
        if (endYear == 0) {
            endYear = END_YEAR;
        }
        mYearAdapter.updateStartEndYear(getYearsList(startYear, endYear));
    }

    class YearAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> mYears;

        YearAdapter(Context context, List<String> years) {
            mContext = context;
            mYears = years;
        }

        @Override
        public int getCount() {
            if (null != mYears) {
                return mYears.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mYears.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            LinearLayout ll = (LinearLayout) layoutInflater.inflate(R.layout.le_date_gallery_year_view, null);

            TextView tv = (TextView) ll.getChildAt(0);
            tv.setText(mYears.get(position));
            ll.setTag(position);
            tv.setTextColor(UNSELECTED_COLOR);

            View view = ll.getChildAt(1);
            view.setVisibility(UNSELECTED_VISIBLE);

            return ll;
        }

        public void updateStartEndYear(List<String> years) {
            if (null != years) {
                mYears.clear();
                ;
                mYears.addAll(years);
                notifyDataSetChanged();
            }
        }
    }
}
