package com.taller2.hypechatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.ui.model.ListUserViewHolder;
import com.taller2.hypechatapp.ui.model.OrganizationListUserViewHolder;

import androidx.annotation.NonNull;

public class OrganizationUsersListAdapter extends UsersListAdapter {


    public OrganizationUsersListAdapter(UserListActionListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ListUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_holder, parent, false);
        return new OrganizationListUserViewHolder(view, listener);
    }
}
