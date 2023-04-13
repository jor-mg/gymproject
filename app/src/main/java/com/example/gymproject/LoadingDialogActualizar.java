package com.example.gymproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;


public class LoadingDialogActualizar {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialogActualizar(Activity myActivity){
        activity = myActivity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_actualizar, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

        dialog.getWindow().setLayout(890, 580);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#34000000")));

    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
