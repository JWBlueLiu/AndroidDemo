package com.example.demoCollection.demo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by JWBlue.Liu on 15/12/16.
 */
public class LeUAUtil {
    /**
     * 1. 终端版本号
     * 2. rom版本
     * 3. App
     * 4. App版本
     * 5. 终端自定义信息
     */
    private static final String UA = "Phone/%1$s (%2$s; %3$s %4$s; %5$s)";

    public static String getPhoneUA(Context context) {
        return getPhoneUA(context, "");
    }

    public static String getPhoneUA(Context context, String customInfo) {
        PackageInfo packageInfo;

        String appName = "";
        String appVersion = "";

        try {
            PackageManager packageManager = context.getPackageManager();
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);

            appName = packageManager.getApplicationLabel(applicationInfo).toString();
            appVersion = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        String terminalVersion = android.os.Build.MODEL;
        String romVersion = android.os.Build.DISPLAY;
        String info = customInfo;

        return String.format(UA, terminalVersion, romVersion, appName, appVersion, info);
    }
}