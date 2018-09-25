package com.example.demoCollection.graphics;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import com.example.demoCollection.R;

public class LoadBitmapActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_bitmap);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;

        ImageView iv1 = (ImageView) findViewById(R.id.iv1);
        ImageView iv2 = (ImageView) findViewById(R.id.iv2);
        iv1.setImageBitmap(DecodeSampledBitmapUtil.decodeSampledBitmapFromResource(
                getResources(), R.drawable.a_75, w_screen, h_screen));
        iv2.setImageBitmap(DecodeSampledBitmapUtil.decodeSampledBitmapFromResource(
                getResources(), R.drawable.a_75, w_screen, h_screen));

        iv1.setScaleType(ImageView.ScaleType.MATRIX);
        Matrix matrix1 = new Matrix();
        matrix1.postScale(2F, 2F);
        iv1.setImageMatrix(matrix1);

        iv2.setScaleType(ImageView.ScaleType.MATRIX);
        Matrix matrix2 = new Matrix();
        matrix2.postScale(2F, 2F);
        matrix2.postTranslate(100, 100);
        iv2.setImageMatrix(matrix2);
    }
}
