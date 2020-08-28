package com.example.dss.project.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.dss.R;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    LoadingDialog(Activity myActivity){
        activity = myActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_loading, null));
        builder.setCancelable(false);


        alertDialog = builder.create();
        alertDialog.show();
    }

    void dissmissDialog(){
        alertDialog.dismiss();
    }
}
