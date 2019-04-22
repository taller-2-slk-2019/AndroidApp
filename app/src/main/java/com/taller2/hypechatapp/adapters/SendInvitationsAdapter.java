package com.taller2.hypechatapp.adapters;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.ui.model.EmailInvitationViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SendInvitationsAdapter extends RecyclerView.Adapter<EmailInvitationViewHolder> {

    private List<String> emailList;

    public SendInvitationsAdapter(List<String> emailList) {
        this.emailList = emailList;
    }

    @NonNull
    @Override
    public EmailInvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_invitation, parent, false);

        EmailInvitationViewHolder vh = new EmailInvitationViewHolder(v, new MyCustomEditTextListener());

        return vh;
    }

    @Override
    public void onBindViewHolder(final @NonNull EmailInvitationViewHolder holder, final int position) {
        holder.editTextListener.updatePosition(holder.getAdapterPosition());
        holder.editTextListener.setClearButton(holder.clearButton);
        holder.emailEditText.setText(emailList.get(holder.getAdapterPosition()));
        holder.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getAdapterPosition()==0 && emailList.size()==1){
                    emailList.set(holder.getAdapterPosition(),"");
                    notifyItemChanged(holder.getAdapterPosition());
                } else {
                    emailList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    public class MyCustomEditTextListener implements TextWatcher {
        private int position;
        private ImageView clearButton;

        public void updatePosition(int position) {
            this.position = position;
        }

        public void setClearButton(ImageView button){
            clearButton=button;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            emailList.set(position,charSequence.toString());
            //Add a new text box for the next email
            if(!TextUtils.isEmpty(charSequence.toString())){
                clearButton.setVisibility(View.VISIBLE);
                if(getItemCount()-1 == position){
                    emailList.add(new String());
                    notifyItemInserted(position+1);
                }
            } else {
                clearButton.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //no op
        }
    }


}
