package com.example.demoCollection.widget.custom_view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by JW.Liu on 2017/4/25.
 */

public class CustomViewActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ShapeSelectorView(getBaseContext()));
    }
}