package com.taller2.hypechatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Message;
import com.taller2.hypechatapp.ui.model.MessageViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageListAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private List<Message> messages = new ArrayList<>();

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_text, parent, false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.setMessage(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessages(List<Message> messages) {
        for (Message message: messages) {
            this.messages.add(0, message);
        }
        this.notifyDataSetChanged();
    }
}
