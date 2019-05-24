package com.taller2.hypechatapp.components;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

public class DialogService {

    public static void showConfirmDialog(Context context, String message, final DialogConfirm listener) {
        new AlertDialog.Builder(context)
                .setTitle("Atención")
                .setMessage(message)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ConfirmDialog", "Cancel");
                    }
                })
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ConfirmDialog", "Confirm");
                        listener.onConfirm();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
