package com.example.demoCollection.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.example.demoCollection.R;

public class DateGalleryActivity extends Activity {

    private YearGallery mGalleryYear;
    private MonthGallery mGalleryMonth;

    private int YEAR_START = 1992;
    private int YEAR_END = 2015;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_gallery);

        mGalleryYear = (YearGallery) findViewById(R.id.gallery_year);
        mGalleryMonth = (MonthGallery) findViewById(R.id.gallery_month);

        mGalleryYear.setStartEndYear(YEAR_START, YEAR_END);
        mGalleryMonth.setMonthes(YEAR_END - YEAR_START + 1);

        findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGalleryMonth.setSelection(mGalleryMonth.getSelectedItemPosition() - 1, false);
            }
        });
        findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGalleryMonth.setSelection(mGalleryMonth.getSelectedItemPosition() + 1);
            }
        });
    }

}
