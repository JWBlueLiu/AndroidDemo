package com.example.demoCollection.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import com.example.demoCollection.R;

/**
 * Created by L on 15/7/13.
 */
public class LoginDialogFragment extends DialogFragment {

    public LoginDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.dialog_fragment_edit_name);
        return builder.create();
    }

}