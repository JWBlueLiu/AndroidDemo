package com.example.demoCollection.designPatern.singleResponsibility;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.example.demoCollection.R;
import com.example.demoCollection.common.IMAGE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JWBlue.Liu on 16/1/4.
 */
public class SRPActivity extends ListActivity {
    private static final String URL = "URL";
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Map<String, String>> data = new ArrayList<>();
        for (String url : IMAGE.URLS) {
            Map<String, String> map = new HashMap<>();
            map.put(URL, url);

            data.add(map);
        }

        imageLoader = new ImageLoader();

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getBaseContext(),
                data,
                R.layout.simple_img_item,
                new String[]{URL, URL},
                new int[]{R.id.iv, R.id.tv});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                String url = (String) data;
                if (view instanceof ImageView) {
                    imageLoader.displayImage(url, ((ImageView) view));
                } else if (view instanceof TextView) {
                    ((TextView) view).setText(url);
                }

                return true;
            }
        });
        setListAdapter(simpleAdapter);
    }
}
