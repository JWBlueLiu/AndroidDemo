package com.example.demoCollection.demo;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Random;

/**
 * Created by JWBlue.Liu on 15/11/28.
 */
public class Fragment2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = new View(getActivity().getBaseContext());
        v.setBackgroundColor(Color.rgb(new Random().nextInt(254) + 1, new Random().nextInt(254) + 1, new Random().nextInt(254) + 1));
        return v;
    }
}
