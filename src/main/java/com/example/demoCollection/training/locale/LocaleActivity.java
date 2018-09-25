package com.example.demoCollection.training.locale;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.example.demoCollection.R;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author: JW.Liu
 * Created on 2018/6/5 16:18
 */
public class LocaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_locale);

        TextView icu = findViewById(R.id.tv_icu);
        long currentTimeMillis = System.currentTimeMillis();
        Locale locale = getResources().getConfiguration().locale;
        Date date = new Date(currentTimeMillis);

        String dateFullFormat = DateFormat.getDateInstance(DateFormat.FULL, locale).format(date);
        String dateLongFormat = DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);
        String dateMediumFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(date);
        String dateShortFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale).format(date);

        String timeFullFormat = DateFormat.getTimeInstance(DateFormat.FULL, locale).format(date);
        String timeLongFormat = DateFormat.getTimeInstance(DateFormat.LONG, locale).format(date);
        String timeMediumFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale).format(date);
        String timeShortFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(date);

        String dateFormat = String
            .format("dateFullFormat: %s, dateLongFormat: %s, dateMediumFormat: %s, dateShortFormat: %s",
                    dateFullFormat, dateLongFormat, dateMediumFormat, dateShortFormat);

        String timeFormat = String
            .format("timeFullFormat: %s, timeLongFormat: %s, timeMediumFormat: %s, timeShortFormat: %s",
                    timeFullFormat, timeLongFormat, timeMediumFormat, timeShortFormat);

        Log.i("LocaleActivity", "\n\n" + dateFormat + "\n" + timeFormat);

        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        String format = dateTimeInstance.format(new Date(currentTimeMillis));
        icu.setText(format);
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(ConfigurationWrapper.wrap(newBase, Locale.SIMPLIFIED_CHINESE));
//    }

}