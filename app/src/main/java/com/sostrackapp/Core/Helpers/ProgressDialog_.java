package com.sostrackapp.Core.Helpers;

import android.content.Context;

import com.sostrackapp.R;

public class ProgressDialog_ {
    android.app.ProgressDialog dialog;

    public ProgressDialog_(Context context, String message){
        dialog = new android.app.ProgressDialog(context, R.style.ProgressDialogCustom);
        dialog.setIndeterminate(true);
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(false);

    }

    public void showProgress(){
        dialog.show();
    }

    public void dismissProgress(){
        dialog.dismiss();
    }
}
