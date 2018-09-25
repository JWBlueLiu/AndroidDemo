package com.example.demoCollection.animation;

import android.app.Activity;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.view.View;
import com.example.demoCollection.R;

/**
 * Created by JWBlue.Liu on 16/5/10.
 */
public class GallerySvgUploadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_svg_upload);

        View view = findViewById(R.id.upload);
        final AnimatedVectorDrawable upload = (AnimatedVectorDrawable) getDrawable(R.drawable.animated_upload);
        upload.reset();
        view.setBackground(upload);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.start();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.reset();
            }
        });
    }

}