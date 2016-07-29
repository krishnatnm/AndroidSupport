package com.tanmay.androidsupport.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.interfaces.OnCustomDialogButtonClickListener;

/**
 * Created by TaNMay on 28/07/16.
 */
public class CustomDialogs {

    Context context;
    LayoutInflater inflater;
    View layout;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    Dialog dialog;
    OnCustomDialogButtonClickListener listener;

    public CustomDialogs(Context context, OnCustomDialogButtonClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void roundedCornerDialog() {
//        inflater = LayoutInflater.from(context);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_rounded_corner);
//        dialog.setTitle("Rounded Corners Dialog");
//        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rounded_rectangle));
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.transparent));

        dialog.show();
//        layout = inflater.inflate(R.layout.dialog_rounded_corner, null);
//        alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setView(layout);
//        alertDialogBuilder.setCancelable(true);
//        alertDialog = alertDialogBuilder.create();
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button closeDialog = (Button) dialog.findViewById(R.id.drc_close);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
//        alertDialog.show();
//        dialog.show();
    }
}
