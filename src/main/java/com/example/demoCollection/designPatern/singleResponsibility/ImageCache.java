package com.example.demoCollection.designPatern.singleResponsibility;

import android.graphics.Bitmap;
import android.support.v4.graphics.BitmapCompat;
import android.util.LruCache;

/**
 * Created by JWBlue.Liu on 16/1/13.
 */
public class ImageCache {
    private LruCache<String, Bitmap> mLruCache;

    public ImageCache() {
        initImageCache();
    }

    private void initImageCache() {
        // 1/4内存 byte
        mLruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 4)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return BitmapCompat.getAllocationByteCount(value);
            }
        };
    }

    public void put(String url, Bitmap bitmap) {
        mLruCache.put(url, bitmap);
    }

    public Bitmap getBitmap(String url) {
        return mLruCache.get(url);
    }
}
