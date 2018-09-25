package com.example.demoCollection.third_party;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.demoCollection.R;
import com.example.demoCollection.common.logger.Log;
import java.io.File;

public class GlideActivity extends Activity {

    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);

        ImageView iv = (ImageView) findViewById(R.id.iv);
        Glide.with(this)
                .load("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png")
                .thumbnail(.00001F)
                .into(iv);

        File photoCacheDir = Glide.getPhotoCacheDir(getApplicationContext());
        Log.i(TAG, "photoCacheDir " + photoCacheDir.getAbsolutePath());
    }

}
