package com.example.demoCollection.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.example.demoCollection.R;
import com.example.demoCollection.widget.multiTask.AutoCenterHorizontalScrollView;
import com.example.demoCollection.widget.multiTask.AutoCenterHorizontalScrollView2;
import com.example.demoCollection.widget.multiTask.AutoCenterScrollView;
import com.example.demoCollection.widget.multiTask.MultiTaskView;
import com.example.demoCollection.widget.multiTask.MyButton;
import java.util.Random;

public class MultiTaskActivity extends Activity {
    private AutoCenterHorizontalScrollView2 achs2;
    private AutoCenterScrollView acsv;
    private MultiTaskView mtv;
    private AutoCenterHorizontalScrollView a1;
    private AutoCenterHorizontalScrollView a2;
    private ScrollView achs3;
    public int count = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_task);

        mtv = (MultiTaskView) findViewById(R.id.mtv);
        mtv.setMaxOverScroll(300);
        a1 = (AutoCenterHorizontalScrollView) findViewById(R.id.achsv1);
        a2 = (AutoCenterHorizontalScrollView) findViewById(R.id.achsv2);
        for (int i = 0; i < count; i++) {
            Button v = new Button(getApplicationContext());
            v.setBackgroundResource(R.drawable.ic_launcher);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplication(), "position ", 0).show();
                    LinearLayout ll = (LinearLayout) a1.getChildAt(0);
//                    ll.removeView(v);
                }
            });
            v.setText("to " + String.valueOf(i));
            v.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
            ((ViewGroup) a1.getChildAt(0)).addView(v);

            Button btn = new Button(getApplicationContext());
            btn.setBackgroundResource(R.drawable.ic_launcher);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplication(), "position ", 0).show();
                    LinearLayout ll = (LinearLayout) a2.getChildAt(0);
//                    ll.removeView(v);
                }
            });
            btn.setText("bo " + String.valueOf(i));
            btn.setTextSize(8);
            btn.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
            ((ViewGroup) a2.getChildAt(0)).addView(btn);
        }
        a1.setItemPadding(100);
        a2.setItemPadding(150);
        a1.setItemWidth(300);
        a2.setItemWidth(150);

        //achs2 = (AutoCenterHorizontalScrollView2) findViewById(R.id.achs2);
        //for (int i = 0; i < 20; i++) {
        //    Button v = new Button(getApplicationContext());
        //    v.setBackgroundResource(R.drawable.ic_launcher);
        //    v.setLayoutParams(new LinearLayout.LayoutParams(400, 400));
        //    v.setText(String.valueOf(i));
        //    v.setOnClickListener(new View.OnClickListener() {
        //        @Override
        //        public void onClick(View v) {
        //            Toast.makeText(getApplication(), "position ", 0).show();
        //        }
        //    });
        //    ((ViewGroup) achs2.getChildAt(0)).addView(v);
        //}

        acsv = (AutoCenterScrollView) findViewById(R.id.acsv);
        acsv.setItemWidth(400);
        acsv.setItemPadding(150);
        for (int i = 0; i < 20; i++) {
            MyButton v = new MyButton(getApplicationContext());
            v.setBackgroundResource(R.drawable.ic_launcher);
            v.setLayoutParams(new LinearLayout.LayoutParams(400, 400));
            v.setText(String.valueOf(i));
            v.setTextSize(30);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplication(), "position ", 0).show();
                }
            });
            ((ViewGroup) acsv.getChildAt(0)).addView(v);
        }
//        achs2.setItemPadding(100);
//        achs2.setItemWidth(300);

//        BounceScroller bs = (BounceScroller) findViewById(R.id.bs);
//        achs3 = (ScrollView) findViewById(R.id.achs3);
//        LinearLayout ll = new LinearLayout(getApplication());
//        ll.setOrientation(LinearLayout.VERTICAL);
//        for (int i = 0; i < 80; i++) {
//            Button v = new Button(getApplicationContext());
//            v.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
//            v.setBackgroundResource(R.drawable.ic_launcher);
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getApplication(), "position ", 0).show();
//                }
//            });
//            ll.addView(v);
//        }
//        achs3.addView(ll);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mtv.setCenterPage(new Random().nextInt(count));
    }

}
