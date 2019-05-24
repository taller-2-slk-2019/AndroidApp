package com.taller2.hypechatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.ui.model.NewConversationUserViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewConversationUsersAdapter extends RecyclerView.Adapter<NewConversationUserViewHolder> {

    private IUserClick listener;
    private List<User> users = new ArrayList<>();

    public NewConversationUsersAdapter(IUserClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewConversationUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_new_conversation, parent, false);
        return new NewConversationUserViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewConversationUserViewHolder holder, int position) {
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
}
