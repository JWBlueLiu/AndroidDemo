package com.example.demoCollection.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DecodeSampledBitmapUtil {

    /**
     * 从源文件中加载处样品bitmap
     *
     * @param resId     资源id
     * @param reqWidth  期望宽度
     * @param reqHeight 期望高度
     * @return bitmap
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources resource, int resId, int reqWidth, int reqHeight) {
        // 避免分配内存
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inJustDecodeBounds = true;
        // decode获取信息decodeOptions
        BitmapFactory.decodeResource(resource, resId, decodeOptions);
        // 计算样品size
        decodeOptions.inSampleSize = calculateInSampleSize(decodeOptions, reqWidth, reqHeight);
        // 取消避免分配内存
        decodeOptions.inJustDecodeBounds = false;
        // 加载样品bitmap
        return BitmapFactory.decodeResource(resource, resId, decodeOptions);
    }

    /**
     * 计算样品size
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 获取原始尺寸
        final int width = options.outWidth;
        final int height = options.outHeight;
        // 默认不缩放
        int inSampleSize = 1;
//        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 8);
        int resultWidth = width;
        int resultHeight = height;
        // 结果大于要求 & 结果内存大于要求 默认是ARGB_8888
        while ((resultWidth > reqWidth || resultHeight > reqHeight)) {
//                && ((resultWidth * resultHeight * 4) > maxMemory)) {
            inSampleSize *= 2;
            resultWidth = width / inSampleSize;
            resultHeight = height / inSampleSize;
        }
        return inSampleSize;
    }
}
