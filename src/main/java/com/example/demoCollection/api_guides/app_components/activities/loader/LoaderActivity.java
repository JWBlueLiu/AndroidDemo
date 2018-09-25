package com.example.demoCollection.api_guides.app_components.activities.loader;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LoaderActivity extends ListActivity {

    public static final String TAG = "LoaderActivity";

    private String[] LIST = new String[]{"LoaderCursor", "Custom", "LoaderRetained", "LoaderThrottle"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                LIST
        ));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = LoaderCursorFragment.newInstance();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
        if (null != fragment) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content, fragment);
            fragmentTransaction.addToBackStack(TAG);
            fragmentTransaction.commit();
        }
    }

}
