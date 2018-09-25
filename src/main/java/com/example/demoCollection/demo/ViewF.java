package com.example.demoCollection.demo;

import android.app.Dialog;
import android.content.Context;

public class ViewF extends Dialog {
    public ViewF(Context context) {
        super(context);
    }

    public ViewF(Context context, int theme) {
        super(context, theme);
    }

    public ViewF(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


}
