package com.example.demoCollection.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import com.example.demoCollection.R;
import java.io.FileDescriptor;

public class DemoActivity1 extends Activity {

    private static final String TAG = "DemoActivity1";
    private static final int READ_REQUEST_CODE = 1;
    private Uri uri;
    ImageView iv1;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);

        // Pick
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoActivity1.this, DemoActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(intent);
            }
        });

        // Handle
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mProgressDialog && mProgressDialog.isShowing()) {
                    return;
                }

                // set origin
                Bitmap bitmapFromUri = getBitmapFromUri(uri);
                if (null != getBitmapFromUri(uri)) {
                    iv1.setImageBitmap(bitmapFromUri);
                }

                // handle
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        mProgressDialog = new ProgressDialog(DemoActivity1.this);
                        mProgressDialog.setMessage("处理中...");
                        mProgressDialog.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        SystemClock.sleep(3000);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        mProgressDialog.dismiss();
                    }
                }.execute();
            }
        });

        iv1 = (ImageView) findViewById(R.id.iv1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uri = data.getData();
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        Bitmap image = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    //private boolean mIsRestoredToTop;
    //@Override
    //protected void onNewIntent(Intent intent) {
    //    super.onNewIntent(intent);
    //    if ((intent.getFlags() | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) > 0) {
    //        mIsRestoredToTop = true;
    //    }
    //}
    //
    //@Override
    //public void finish() {
    //    super.finish();
    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !isTaskRoot() && mIsRestoredToTop) {
    //        // 4.4.2 platform issues for FLAG_ACTIVITY_REORDER_TO_FRONT,
    //        // reordered activity back press will go to home unexpectly,
    //        // Workaround: move reordered activity current task to front when it's finished.
    //        ActivityManager tasksManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    //        tasksManager.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
    //    }
    //}
}