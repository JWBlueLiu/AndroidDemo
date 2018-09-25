package com.example.demoCollection.api_guides.app_components.Intent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.demoCollection.R;

/**
 * Created by JW.Liu on 2017/1/3.
 */

public class UriActivity extends Activity {

    private static final String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_uri);

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (null != data) {
            String url = data.getQueryParameter(URL);
            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = new Uri.Builder()
                        .scheme("eui")  // scheme
                        .authority("com.example.demo")    // host
                        .path("uri")   // path
                        // 参数携带
                        .appendQueryParameter(URL, "http://www.baidu.com")
//                        .appendQueryParameter("type", "1")
                        .build();
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

}