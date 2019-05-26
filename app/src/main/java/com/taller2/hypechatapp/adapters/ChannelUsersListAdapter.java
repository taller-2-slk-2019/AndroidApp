package com.taller2.hypechatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.ui.model.ChannelListUserViewHolder;
import com.taller2.hypechatapp.ui.model.ListUserViewHolder;

import androidx.annotation.NonNull;

public class ChannelUsersListAdapter extends UsersListAdapter {

    public ChannelUsersListAdapter(UserListActionListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ListUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_holder, parent, false);
        return new ChannelListUserViewHolder(view, listener);
    }
}
