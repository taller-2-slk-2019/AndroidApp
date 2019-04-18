package com.taller2.hypechatapp.ui.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.IUserClick;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.model.User;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewConversationUserViewHolder extends RecyclerView.ViewHolder {

    private TextView userName;
    private ImageView profile;
    private IUserClick listener;
    private User user;
    private Context context;

    public NewConversationUserViewHolder(@NonNull View itemView, IUserClick listener) {
        super(itemView);
        context = itemView.getContext();
        this.listener = listener;
        userName = itemView.findViewById(R.id.username);
        profile = itemView.findViewById(R.id.profile_image_view);

        ImageView button = itemView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userClick();
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
        userName.setText(user.getName());
        PicassoLoader.load(context, user.getPicture(), profile);
    }

    private void userClick(){
        listener.onUserClick(user);
    }
}
