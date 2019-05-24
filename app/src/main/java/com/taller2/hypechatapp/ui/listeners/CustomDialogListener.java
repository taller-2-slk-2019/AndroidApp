package com.taller2.hypechatapp.ui.listeners;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.taller2.hypechatapp.R;

import androidx.annotation.NonNull;


public class CustomDialogListener implements View.OnClickListener {
    private final Dialog dialog;
    private EditText input;

    public CustomDialogListener(Dialog dialog, EditText input) {
        this.dialog = dialog;
        this.input = input;
    }

    @Override
    public void onClick(final View v) {
        final Context ctx = v.getContext();
        boolean isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(input.getText()).matches();
        if (isValidEmail) {
            final String email = input.getText().toString();

            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Log.d(this.getClass().getName(), "Email sent.");
                                dialog.dismiss();
                                Toast.makeText(ctx, ctx.getString(R.string.email_sent_to) + email, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ctx, ctx.getString(R.string.invalid_email_recovery), Toast.LENGTH_LONG).show();
                            }


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(this.getClass().getName(), e.getMessage());
                        }
                    });

        } else {
            input.setError(ctx.getString(R.string.wrong_format));
        }

    }

}

