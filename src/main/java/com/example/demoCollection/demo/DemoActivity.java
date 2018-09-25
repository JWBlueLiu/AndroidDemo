package com.example.demoCollection.demo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.demoCollection.Msg;
import com.example.demoCollection.R;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.brotli.dec.BrotliInputStream;

public class DemoActivity extends Activity {

    private static final String TAG = "DemoActivity1";
    private View view;

    private static final String SCAN_TIPS = "scan_tips";
    private static final String BTN_LABEL = "btn_label";
    private static final String TO_PACKAGE = "to_package";
    private static final String TO_CLASS = "to_class";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_demo, null);
        setContentView(view);

        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.P)
            public void onClick(View v) {
                ////Intent intent = new Intent(DemoActivity.this, DemoActivity1.class);
                ////intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ////intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                ////startActivity(intent);
                //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                //    return;
                //}
                //// 您可以在自己的View中获取到不应该绘制的部分屏幕
                //DisplayCutout displayCutout = view.getRootWindowInsets().getDisplayCutout();
                //displayCutout.getBoundingRects();
                //displayCutout.getSafeInsetBottom();
                //displayCutout.getSafeInsetLeft();
                //displayCutout.getSafeInsetRight();
                //displayCutout.getSafeInsetTop();
                //// 也可以设置Window的属性
                //WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                //WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                //layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
                //layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
                //layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111);
            }
        });

        findViewById(R.id.world).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.hao123.com"));
                //startActivity(browserIntent);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient httpClient =
                                new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).build();

                            byte[] data = Arrays.toString(Msg.msgStrs).getBytes("UTF-8");
                            ByteArrayOutputStream arr = new ByteArrayOutputStream();
                            OutputStream zipper = new GZIPOutputStream(arr);
                            zipper.write(data);
                            zipper.close();
                            RequestBody requestBody =
                                RequestBody.create(MediaType.parse("text/plain; charset=UTF-8"), arr.toByteArray());

                            Request build = new Request.Builder()
                                //.get()
                                .post(requestBody).url("https://www.zhihu.com/question/32225726")
                                //.addHeader("accept-encoding", "br")
                                .addHeader("accept-encoding", "gzip")
                                .addHeader("content-encoding", "gzip")
                                .build();
                            Response response = httpClient.newCall(build).execute();
                            BrotliInputStream brotliInputStream = new BrotliInputStream(response.body().byteStream());
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(brotliInputStream));
                            StringBuilder stringBuilder = new StringBuilder();
                            String strLine;
                            while ((strLine = bufferedReader.readLine()) != null) {
                                stringBuilder.append(strLine);
                            }
                            long contentLength = stringBuilder.length();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    private static final int SCAN_REQUEST_CODE = 100;
    private static final String SCAN_RESULT = "result";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if ((intent.getFlags() | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !isTaskRoot()) {
                ActivityManager tasksManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                tasksManager.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SCAN_REQUEST_CODE:
                    data.getStringExtra(SCAN_RESULT);
                    // Do something
                    break;
                case 111:
                    Glide.with(getBaseContext()).load(data.getData()).into((ImageView) findViewById(R.id.iv));
                    break;
            }
        }
    }
}