package com.example.demoCollection.training.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;

public class EasyPermissions {

    public static void handlePermissions(@NonNull Activity activity, @NonNull String[] perms,
        int requestCode) {
        if (!hasPermissions(activity.getApplicationContext(), perms)) {
            EasyPermissions.requestPermissions(activity, perms, requestCode);
        }
    }

    public static boolean hasPermissions(@NonNull Context context, @NonNull String... perms) {
        if (Build.VERSION.SDK_INT < M) {
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) ==
                PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }

        return true;
    }

    public static void requestPermissions(@NonNull Activity activity, @NonNull String[] perms,
        int requestCode) {
        if (VERSION.SDK_INT < VERSION_CODES.M) {
            return;
        }

        List<String> permissionList = new ArrayList();

        for (String permission : perms) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED) {
                permissionList.add(permission);
            }
        }

        if (permissionList.size() > 0) {
            activity.requestPermissions(permissionList.toArray(new String[permissionList.size()]),
                requestCode);
        }
    }

}
