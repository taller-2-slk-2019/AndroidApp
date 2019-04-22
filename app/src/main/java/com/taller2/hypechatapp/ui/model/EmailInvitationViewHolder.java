package com.taller2.hypechatapp.ui.model;

import android.view.View;
import android.widget.ImageView;


import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.SendInvitationsAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmailInvitationViewHolder extends RecyclerView.ViewHolder {
    public TextInputEditText emailEditText;
    public ImageView clearButton;
    public SendInvitationsAdapter.MyCustomEditTextListener editTextListener;

    public EmailInvitationViewHolder(@NonNull View itemView, SendInvitationsAdapter.MyCustomEditTextListener myCustomEditTextListener) {
        super(itemView);

        emailEditText=itemView.findViewById(R.id.email_invitation_edit_text);
        clearButton=itemView.findViewById(R.id.email_invitation_clear_button);

        editTextListener=myCustomEditTextListener;

        emailEditText.addTextChangedListener(editTextListener);

    }
}
