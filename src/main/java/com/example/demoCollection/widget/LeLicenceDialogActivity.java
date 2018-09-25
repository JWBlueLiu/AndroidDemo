package com.example.demoCollection.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.demoCollection.R;

public class LeLicenceDialogActivity extends Activity {

    private static final String APP_NAME1 = "音乐服务";
    private static final String APP_NAME2 = "地图服务";

    private static final String AGREE = "同意";
    private static final String CANCEL = "取消";
    private static final String TOUCH_OUT_CANCEL = "点击外部取消";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_licence_dialog);
        // 风格 1
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LeLicenceDialog(LeLicenceDialogActivity.this, APP_NAME1, LeLicenceDialog.TYPE_POSITION_NET).show()
                        .setOnAgreeListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // do some thing
                                Toast.makeText(getApplication(), AGREE, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnCancelListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // do some thing
                                Toast.makeText(getApplication(), CANCEL, Toast.LENGTH_LONG).show();
                            }
                        })
                        .setOnTouchOutCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                Toast.makeText(getApplication(), TOUCH_OUT_CANCEL, Toast.LENGTH_LONG).show();
                                // TODO 退出 APP
                            }
                        });
            }
        });

        // 风格 2
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LeLicenceDialog(LeLicenceDialogActivity.this, APP_NAME2, LeLicenceDialog.TYPE_NET)
                        .setLeLicenceDialogClickListener(new LeLicenceDialog.LeLicenceDialogClickListener() {
                            @Override
                            public void onClickListener(LeLicenceDialog.KEY key) {
                                switch (key) {
                                    case BTN_AGREE:
                                        // do some thing
                                        Toast.makeText(getApplication(), AGREE, Toast.LENGTH_SHORT).show();
                                        break;
                                    case BTN_CANCEL:
                                        // do some thing
                                        Toast.makeText(getApplication(), CANCEL, Toast.LENGTH_SHORT).show();
                                        break;
                                    case OUTSIDE:
                                        // do some thing
                                        Toast.makeText(getApplication(), TOUCH_OUT_CANCEL, Toast.LENGTH_SHORT).show();
                                        // TODO 退出 APP
                                        break;
                                }
                            }
                        }).show();
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LeLicenceDialog(LeLicenceDialogActivity.this, APP_NAME2, LeLicenceDialog.TYPE_USAGE_LOCATION_NET).show();
            }
        });

        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LeLicenceDialog(LeLicenceDialogActivity.this, APP_NAME2, LeLicenceDialog.TYPE_PRIVACY_LOCATION_NET).show();
            }
        });

        findViewById(R.id.btn5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LeLicenceDialog(LeLicenceDialogActivity.this, APP_NAME2, LeLicenceDialog.TYPE_USAGE_NET).show();
            }
        });

        findViewById(R.id.btn6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LeLicenceDialog(LeLicenceDialogActivity.this, APP_NAME2, LeLicenceDialog.TYPE_PRIVACY_NET).show();
            }
        });

    }
}
