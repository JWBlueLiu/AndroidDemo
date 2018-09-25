package com.example.demoCollection.widget;

import android.app.Activity;
import android.os.Bundle;
import com.example.demoCollection.R;

public class DraggableViewActivity extends Activity {

    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draggable_view);
    }

}
