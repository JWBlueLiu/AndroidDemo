package com.example.demoCollection.designPatern.singleResponsibility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.example.demoCollection.common.logger.Log;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by JWBlue.Liu on 16/1/4.
 */
public class ImageLoader {
    public String TAG = getClass().getSimpleName();

    // 图片缓存
    ImageCache mImageCache = new ImageCache();
    // 线程池
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void displayImage(final String url, final ImageView imageView) {
        imageView.setTag(url);
        Bitmap bitmap = mImageCache.getBitmap(url);
        if (null == bitmap) {
            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = downloadImage(url);
                    if (null == bitmap) {
                        return;
                    }
                    mImageCache.put(url, bitmap);
                    if (url.equals(imageView.getTag())) {
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "run imageView " + imageView.toString() + " imageView.tag" + imageView.getTag());
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }
                }
            });
        } else {
            Log.i(TAG, "else imageView " + imageView.toString() + " imageView.tag" + imageView.getTag());
            imageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(2000);
            bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
