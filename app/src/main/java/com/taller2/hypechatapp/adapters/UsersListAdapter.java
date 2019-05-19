package com.taller2.hypechatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.ui.model.ListUserViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UsersListAdapter extends RecyclerView.Adapter<ListUserViewHolder> {

    private UserListActionListener listener;
    private List<User> users = new ArrayList<>();

    public UsersListAdapter(UserListActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_organization_list, parent, false);
        return new ListUserViewHolder(view, listener);
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
