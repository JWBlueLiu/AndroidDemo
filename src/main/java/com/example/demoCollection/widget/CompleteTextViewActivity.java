package com.example.demoCollection.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import com.example.demoCollection.R;

public class CompleteTextViewActivity extends Activity {

    private String[] keywords = {
            "ab", "aaaaaaa", "aaaaaabbbbbb", "aaaaaccccccabbbbbb", "abdfff",
            "abc", "abdfdf", "abnsd", "abdfff"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complete_text_view);

        AutoCompleteTextView autoText = (AutoCompleteTextView) findViewById(R.id.actv);
        MultiAutoCompleteTextView autoMultiText = (MultiAutoCompleteTextView) findViewById(R.id.mactv);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.drop_down_item_line,
                keywords);
        autoText.setAdapter(adapter);
        autoMultiText.setAdapter(adapter);
        autoMultiText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

}
