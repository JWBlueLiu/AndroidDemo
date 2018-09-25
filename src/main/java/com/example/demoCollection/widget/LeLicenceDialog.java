package com.example.demoCollection.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.demoCollection.R;
import com.example.demoCollection.widget.leCheckBox.LeCheckBox;

public class LeLicenceDialog {
    /**
     * 使用协议 隐私协议 位置 联网
     *
     * @deprecated Use {@link #TYPE_USAGE_PRIVACY_LOCATION_NET} instead
     */
    public static final int TYPE_POSITION_NET = 1;
    /**
     * 使用协议 隐私协议 联网
     *
     * @deprecated Use {@link #TYPE_USAGE_PRIVACY_NET} instead
     */
    public static final int TYPE_NET = 2;

    /**
     * 使用协议 隐私协议 位置 联网
     */
    public static final int TYPE_USAGE_PRIVACY_LOCATION_NET = 3;
    /**
     * 使用协议 隐私协议 联网
     */
    public static final int TYPE_USAGE_PRIVACY_NET = 4;
    /**
     * 使用协议 位置 联网
     */
    public static final int TYPE_USAGE_LOCATION_NET = 5;
    /**
     * 隐私协议 位置 联网
     */
    public static final int TYPE_PRIVACY_LOCATION_NET = 6;
    /**
     * 使用协议 联网
     */
    public static final int TYPE_USAGE_NET = 7;
    /**
     * 隐私协议 联网
     */
    public static final int TYPE_PRIVACY_NET = 8;

    public enum KEY {
        BTN_AGREE, BTN_CANCEL, OUTSIDE
    }

    private Context mContext;
    private Dialog mAlertDialog;
    private Button btnCancel;
    private Button btnAgree;

    private boolean checked = true;

    /**
     * @param context 上下文
     * @param name    应用名称
     * @param type    对话框类型
     */
    public LeLicenceDialog(Context context, String name, int type) {
        mContext = context;

        initDialog(name, type);
    }

    private void initDialog(String name, int type) {
        mAlertDialog = new Dialog(mContext, R.style.leLicenceDialogTheme);
        mAlertDialog.getWindow().setGravity(Gravity.BOTTOM);
        mAlertDialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.le_licence_dialog_content, null);

        // init view
        TextView tvLicenceContent = (TextView) view.findViewById(R.id.tv_licence_content);
        String agreementStr = "";
        String pemissionStr = "";
        switch (type) {
            case TYPE_POSITION_NET:
            case TYPE_USAGE_PRIVACY_LOCATION_NET:
                agreementStr = mContext.getString(R.string.le_licence_dialog_usage_privacy);
                pemissionStr = mContext.getString(R.string.le_licence_dialog_permission_location_net);
                break;
            case TYPE_NET:
            case TYPE_USAGE_PRIVACY_NET:
                agreementStr = mContext.getString(R.string.le_licence_dialog_usage_privacy);
                pemissionStr = mContext.getString(R.string.le_licence_dialog_permission_net);
                break;
            case TYPE_USAGE_LOCATION_NET:
                agreementStr = mContext.getString(R.string.le_licence_dialog_usage);
                pemissionStr = mContext.getString(R.string.le_licence_dialog_permission_location_net);
                break;
            case TYPE_PRIVACY_LOCATION_NET:
                agreementStr = mContext.getString(R.string.le_licence_dialog_privacy);
                pemissionStr = mContext.getString(R.string.le_licence_dialog_permission_location_net);
                break;
            case TYPE_USAGE_NET:
                agreementStr = mContext.getString(R.string.le_licence_dialog_usage);
                pemissionStr = mContext.getString(R.string.le_licence_dialog_permission_net);
                break;
            case TYPE_PRIVACY_NET:
                agreementStr = mContext.getString(R.string.le_licence_dialog_privacy);
                pemissionStr = mContext.getString(R.string.le_licence_dialog_permission_net);
                break;
        }
        // url link
        tvLicenceContent.setText(Html.fromHtml(
                String.format(mContext.getString(R.string.le_licence_dialog_content),
                        name, agreementStr, pemissionStr)
        ));
        tvLicenceContent.setMovementMethod(LinkMovementMethod.getInstance());
        //
        final LeCheckBox cbPrompt = (LeCheckBox) view.findViewById(R.id.cb_prompt);
        cbPrompt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
            }
        });
        LinearLayout cb_prompt_wrapper = (LinearLayout) view.findViewById(R.id.cb_prompt_wrapper);
        cb_prompt_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbPrompt.isChecked()) {
                    cbPrompt.setChecked(false, true);
                } else {
                    cbPrompt.setChecked(true, true);
                }
            }
        });
        btnAgree = (Button) view.findViewById(R.id.btn_agree);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        setBtnOnClickListener(btnAgree, new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        setBtnOnClickListener(btnCancel, new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        mAlertDialog.setContentView(view);
    }

    /**
     * 显示对话框
     *
     * @return LeLicenceDialog
     */
    public LeLicenceDialog show() {
        if (null != mAlertDialog && !mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
        return this;
    }

    /**
     * dismiss对话框
     *
     * @return LeLicenceDialog
     */
    public LeLicenceDialog dismiss() {
        if (null != mAlertDialog && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        return this;
    }

    /**
     * 设置"取消按钮"监听,点击后对话框自动消失
     *
     * @param onCancelListener View.OnClickListener
     * @return LeLicenceDialog
     */
    public LeLicenceDialog setOnCancelListener(View.OnClickListener onCancelListener) {
        setBtnOnClickListener(btnCancel, onCancelListener);
        return this;
    }

    /**
     * 设置"同意按钮"监听,点击后对话框自动消失
     *
     * @param onAgreeListener View.OnClickListener
     * @return LeLicenceDialog
     */
    public LeLicenceDialog setOnAgreeListener(View.OnClickListener onAgreeListener) {
        setBtnOnClickListener(btnAgree, onAgreeListener);
        return this;
    }

    private void setBtnOnClickListener(Button btn, final View.OnClickListener onClickListener) {
        if (null != onClickListener) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(v);
                    dismiss();
                }
            });
        }
    }

    /**
     * 设置点击外部(黑色阴影区域)消失监听
     *
     * @param listener DialogInterface.OnCancelListener
     * @return LeLicenceDialog
     */
    public LeLicenceDialog setOnTouchOutCancelListener(DialogInterface.OnCancelListener listener) {
        if (null != mAlertDialog) {
            mAlertDialog.setOnCancelListener(listener);
        }
        return this;
    }

    /**
     * 一个接口设置"同意按钮"、"取消按钮"、"外部"点击监听
     *
     * @param listener LeLicenceDialogClickListener
     * @return LeLicenceDialog
     */
    public LeLicenceDialog setLeLicenceDialogClickListener(final LeLicenceDialogClickListener listener) {
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickListener(KEY.BTN_AGREE);
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickListener(KEY.BTN_CANCEL);
                dismiss();
            }
        });
        setOnTouchOutCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                listener.onClickListener(KEY.OUTSIDE);
            }
        });
        return this;
    }

    /**
     * CheckBox是否被勾选
     *
     * @return boolean
     */
    public boolean isChecked() {
        return checked;
    }

    public interface LeLicenceDialogClickListener {
        public void onClickListener(KEY key);
    }
}
