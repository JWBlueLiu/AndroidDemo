package com.example.demoCollection.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.demoCollection.R;
import com.example.demoCollection.common.activities.AbsBaseActivity;

/**
 * Created by L on 15/7/13.
 */
public class DialogFragmentActivity extends AbsBaseActivity implements
        View.OnClickListener, EditNameDialogFragment.EditNameDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dialog_fragment);

        findViewById(R.id.btn_on_create_view).setOnClickListener(this);
        findViewById(R.id.btn_on_create_dialog).setOnClickListener(this);
    }

    /**
     * EditNameDialogFragment better than LoginDialogFragment
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_on_create_view:   // show EditNameDialogFragment
                EditNameDialogFragment editNameDialog = new EditNameDialogFragment();
                editNameDialog.show(getSupportFragmentManager(), "EditNameDialog");
                break;
            case R.id.btn_on_create_dialog:
                LoginDialogFragment loginDialogFragment = new LoginDialogFragment();
                loginDialogFragment.show(getSupportFragmentManager(), "LoginDialogFragment");
                break;
        }
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        // 编辑完成
        Toast.makeText(
                getApplicationContext(),
                inputText,
                0
        ).show();
    }
}
