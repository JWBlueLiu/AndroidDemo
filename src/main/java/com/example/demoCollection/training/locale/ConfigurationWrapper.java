package com.example.demoCollection.training.locale;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;
import java.util.Locale;

/**
 * Author: JW.Liu
 * Created on 2018/5/22 16:11
 */
public class ConfigurationWrapper extends ContextWrapper {

    private ConfigurationWrapper(Context base) {
        super(base);
    }

    @SuppressWarnings("deprecation")
    public static ContextWrapper wrap(Context context, Locale locale) {
        Configuration sycConfig = context.getResources().getConfiguration();
        Locale sysLocale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = sycConfig.getLocales().get(0);
        } else {
            sysLocale = sycConfig.locale;
        }

        if (null != locale && !locale.equals(sysLocale)) {
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sycConfig.setLocale(locale);
            } else {
                sycConfig.locale = locale;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context = context.createConfigurationContext(sycConfig);
            } else {
                context.getResources().updateConfiguration(sycConfig, context.getResources().getDisplayMetrics());
            }
        }

        return new ConfigurationWrapper(context);
    }

}