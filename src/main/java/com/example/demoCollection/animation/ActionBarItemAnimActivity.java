package com.example.demoCollection.animation;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.example.demoCollection.R;
import com.example.demoCollection.common.logger.Log;

public class ActionBarItemAnimActivity extends AppCompatActivity {
    public String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_action_bar_item_anim);
        initActionBar();
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
    }

    /**
     * 加载ActionBar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar_item_anim, menu);
        ActionBarItemAnimHelper.setDefaultItemAnim(
                ActionBarItemAnimActivity.this,
                getActionBar(),
                R.id.action_search,
                R.id.action_settings1,
                R.id.action_settings2
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(TAG, "android.R.id.home");
//                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
