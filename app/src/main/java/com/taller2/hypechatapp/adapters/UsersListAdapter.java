package com.taller2.hypechatapp.adapters;

import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.ui.model.ListUserViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class UsersListAdapter extends RecyclerView.Adapter<ListUserViewHolder> {

    protected UserListActionListener listener;
    private List<User> users = new ArrayList<>();

    public UsersListAdapter(UserListActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ListUserViewHolder holder, int position) {
        User user = users.get(position);
        holder.setUser(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void removeUser(User user) {
        users.remove(user);
        notifyDataSetChanged();
    }
}
