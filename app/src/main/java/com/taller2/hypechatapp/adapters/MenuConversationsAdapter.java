package com.taller2.hypechatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Conversation;
import com.taller2.hypechatapp.ui.model.MenuConversationItemViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuConversationsAdapter extends RecyclerView.Adapter<MenuConversationItemViewHolder> {

    private final IMenuItemsClick listener;
    private List<Conversation> data = new ArrayList<>();

    public MenuConversationsAdapter(IMenuItemsClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuConversationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_navigation, parent, false);
        return new MenuConversationItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuConversationItemViewHolder holder, int position) {
        Conversation conversation = data.get(position);
        holder.setConversation(conversation);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setConversations(List<Conversation> conversations) {
        data = conversations;
        notifyDataSetChanged();
    }
}
