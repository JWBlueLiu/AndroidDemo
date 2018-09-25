package com.example.demoCollection.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.example.demoCollection.R;

public class ViewPagerActivity extends Activity {

    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        vp = (ViewPager) findViewById(R.id.vp);
    }

}
