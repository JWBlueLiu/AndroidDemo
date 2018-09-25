package com.example.demoCollection.widget;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.example.demoCollection.R;

/**
 * Created by L on 15/7/13.
 */
public class EditNameDialogFragment extends DialogFragment implements
        TextView.OnEditorActionListener, View.OnClickListener {

    private EditText et;

    public EditNameDialogFragment() {
    }

    // 给activity设置回调
    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 去除title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_fragment_edit_name, container);
        view.findViewById(R.id.id_sure_edit_name).setOnClickListener(this);
        et = (EditText) view.findViewById(R.id.id_txt_your_name);
        // 获取焦点弹出键盘
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        et.requestFocus();
        // 设置onEditorAction监听
        et.setOnEditorActionListener(this);
        return view;

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
                dismissDialog();
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_sure_edit_name:
                dismissDialog();
                break;
        }
    }

    /**
     * 对话框消失
     */
    public void dismissDialog() {
        DialogFragmentActivity dialogFragmentActivity = (DialogFragmentActivity) getActivity();
        dialogFragmentActivity.onFinishEditDialog(et.getText().toString());
        dismiss();
    }

}