package com.example.demoCollection.logic;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.example.demoCollection.R;
import com.example.demoCollection.common.logger.Log;

public class StickerProviderActivity extends Activity implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();
    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_provider);

        contentResolver = getContentResolver();

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Uri uri;
        ContentValues contentValues;
        switch (v.getId()) {
            case R.id.btn1:
                Log.d(TAG, "增");

                uri = Uri.parse(StickerProvider.PERSON_URI);

                contentValues = new ContentValues();
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < 1000; i++) {
                    contentValues.put("name", "刘俊伟" + i);
                    contentValues.put("male", i);
                    Uri insertUri = contentResolver.insert(uri, contentValues);
                    Log.d(TAG, "insertUri " + insertUri.getLastPathSegment());
                }
                Log.d(TAG, "insert time " + (System.currentTimeMillis() - startTime));
                break;
            case R.id.btn2:
                Log.d(TAG, "删");

                uri = Uri.parse(StickerProvider.PERSON_URI + "2");
                int deleteId = contentResolver.delete(uri, null, null);

                Log.d(TAG, "deleteId " + deleteId);

                break;
            case R.id.btn3:
                Log.d(TAG, "改");

                uri = Uri.parse(StickerProvider.PERSON_URI + "1");
                contentValues = new ContentValues();
                contentValues.put("name", "刘佳伟");
                contentValues.put("male", 1);
                int updateId = contentResolver.update(uri, contentValues, null, null);

                Log.d(TAG, "updateId " + updateId);

                break;
            case R.id.btn4:
                Log.d(TAG, "查");

                uri = Uri.parse(StickerProvider.PERSON_URI + "1");
                Cursor cursor = contentResolver.query(uri, null, null, null, null);
                if (null != cursor) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String male = cursor.getString(cursor.getColumnIndex("male"));
                        Log.d(TAG, "name " + name + " male" + male);
                    }
                } else {
                    Log.d(TAG, "error");
                }
                break;
        }
    }
}
