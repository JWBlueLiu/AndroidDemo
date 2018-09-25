package com.example.demoCollection.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.demoCollection.R;

public class AnswerFragment extends Activity {

    private static final String TAG = "AnswerFragment";
    private AnswerFragmentAnimHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_answer);

        View container = findViewById(R.id.container);
        View blackCover = findViewById(R.id.blackCover);
        TextView slideDownText = (TextView) findViewById(R.id.slide_down_text);
        TextView slideUpText = (TextView) findViewById(R.id.slide_up_text);

        helper = new AnswerFragmentAnimHelper(
                getApplication(),
                container, blackCover,
                findViewById(R.id.answerFr_shadow), findViewById(R.id.answerFr_bottom),
                slideDownText, slideUpText,
                null);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start anim
                helper.startEnterFloatAnim();
            }
        });
        findViewById(R.id.btn_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.startExitAnim(AnswerFragmentAnimHelper.PhoneOperating.ANSWER);
                //helper.exitAnim.addListener(new AnimatorListenerAdapter() {
                //    @Override
                //    public void onAnimationEnd(Animator animation) {
                //        super.onAnimationEnd(animation);
                //        helper.startExitAnim(AnswerFragmentAnimHelper.PhoneOperating.ANSWER);
                //    }
                //});
            }
        });
        findViewById(R.id.btn_refuse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.startExitAnim(AnswerFragmentAnimHelper.PhoneOperating.DECLINE);
            }
        });
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cancelAnim();
            }
        });
    }

}
