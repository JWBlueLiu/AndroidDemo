package com.example.demoCollection.training.permission;

import android.Manifest.permission;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.pm.PackageManager;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;

/**
 * Created by JW.Liu on 2017/4/17.
 */

public class PermissionActivity extends Activity {

    static final int PERMISSION_REQUEST_CODE = 1;
    private String[] perms = new String[]{
        permission.READ_CONTACTS,
        permission.READ_EXTERNAL_STORAGE};
    private AlertDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyPermissions.handlePermissions(this, perms, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        EasyPermissions.handlePermissions(this, perms, PERMISSION_REQUEST_CODE);
    }

    @RequiresApi(api = VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), permission)
                    == PackageManager.PERMISSION_DENIED) {
                    if (shouldShowRequestPermissionRationale(permission)) {
                        // TODO 引导权限作用
                    } else {
                        // 引导进入设置
                        if (null == mDialog) {
                            mDialog = new Builder(this).setMessage("权限设置").show();
                            return;
                        }
                        if (!mDialog.isShowing()) {
                            mDialog.show();
                            return;
                        }
                    }
                }
            }
            // TODO 权限ok
        }
    }

}