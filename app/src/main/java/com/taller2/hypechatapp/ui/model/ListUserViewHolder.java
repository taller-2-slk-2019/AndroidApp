package com.taller2.hypechatapp.ui.model;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.UserListActionListener;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ListUserViewHolder extends RecyclerView.ViewHolder {

    private TextView userName;
    private ImageView profile;
    protected UserListActionListener listener;
    protected User user;
    protected Context context;
    protected UserManagerPreferences prefs;
    protected Button actionButton;

    public ListUserViewHolder(@NonNull View itemView, UserListActionListener listener, Action action) {
        super(itemView);
        context = itemView.getContext();
        prefs = new UserManagerPreferences(context);
        this.listener = listener;
        userName = itemView.findViewById(R.id.username);
        profile = itemView.findViewById(R.id.profile_image_view);
        switch (action) {
            case ADD:
                actionButton = itemView.findViewById(R.id.addUserButton);
                break;
            case DELETE:
                actionButton = itemView.findViewById(R.id.deleteUserButton);
                break;
        }

        actionButton.setVisibility(View.VISIBLE);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButtonClick();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSelected();
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
        userName.setText(user.getName());
        PicassoLoader.load(context, user.getPicture(), profile);
        bindUser(user);
    }

    protected abstract void bindUser(User user);

    private void actionButtonClick() {
        listener.onUserAction(user);
    }

    private void userSelected() {
        listener.onUserSelected(user);
    }

    enum Action {
        DELETE, ADD
    }
}
