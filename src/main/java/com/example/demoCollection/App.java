package com.example.demoCollection;

import android.app.Application;
import com.facebook.stetho.Stetho;

/**
 * Author: JW.Liu
 * Created on 2018/9/18 21:50
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }

}