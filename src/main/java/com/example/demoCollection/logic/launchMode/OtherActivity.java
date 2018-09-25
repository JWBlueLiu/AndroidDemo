package com.example.demoCollection.logic.launchMode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class OtherActivity extends Activity {

    public String NAME = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(FirActivity.TAG, NAME + FirActivity.ON_CREATE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(FirActivity.TAG, NAME + FirActivity.ON_NEW_INTENT);
    }

}
