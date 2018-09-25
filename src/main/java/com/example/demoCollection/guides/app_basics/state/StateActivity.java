package com.example.demoCollection.guides.app_basics.state;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.demoCollection.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: JW.Liu
 * Created on 2018/6/11 17:58
 */
public class StateActivity extends AppCompatActivity {

    private StateFragment stateFragment;
    private int test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_state);
        Log.d("StateActivity", "onCreate");

        stateFragment = (StateFragment) getSupportFragmentManager().findFragmentByTag("data");
        if (null == stateFragment) {
            stateFragment = new StateFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.rl_root, stateFragment, "data").commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        List<String> dataList = new ArrayList();
        dataList.add("111");
        dataList.add("222");
        dataList.add("333");
        stateFragment.setData(dataList);
    }

}