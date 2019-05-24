package com.taller2.hypechatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Message;
import com.taller2.hypechatapp.ui.model.MessageCodeViewHolder;
import com.taller2.hypechatapp.ui.model.MessageFileViewHolder;
import com.taller2.hypechatapp.ui.model.MessageImageViewHolder;
import com.taller2.hypechatapp.ui.model.MessageTextViewHolder;
import com.taller2.hypechatapp.ui.model.MessageViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageListAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private List<Message> messages = new ArrayList<>();

    private List<String> messageTypes = new ArrayList<String>(
            Arrays.asList(Message.TYPE_TEXT, Message.TYPE_IMAGE, Message.TYPE_FILE, Message.TYPE_CODE));

    @Override
    public int getItemViewType(int position) {
        Message msg = messages.get(position);
        return messageTypes.indexOf(msg.type);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message, parent, false);

        switch (messageTypes.get(viewType)) {
            case Message.TYPE_TEXT:
                return new MessageTextViewHolder(view);
            case Message.TYPE_IMAGE:
                return new MessageImageViewHolder(view);
            case Message.TYPE_FILE:
                return new MessageFileViewHolder(view);
            case Message.TYPE_CODE:
                return new MessageCodeViewHolder(view);
        }

        throw new IllegalArgumentException();
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.setMessage(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addOlderMessages(List<Message> messages) {
        Message first = null;
        if (getItemCount() > 0) {
            first = this.messages.get(0);
        }
        for (Message message : messages) {
            if (first == null || (compareMessages(message, first) && message.id < first.id)) {
                this.messages.add(0, message);
            }
        }
        notifyDataSetChanged();
    }

    public void addLastMessage(Message message) {
        if (getItemCount() > 0) {
            Message other = messages.get(0);
            if (!compareMessages(message, other)) {
                return;
            }
        }

        int index = messages.size();
        while (index > 0) {
            if (messages.get(index - 1).id < message.id) {
                break;
            }
            index--;
        }
        messages.add(index, message);
        notifyDataSetChanged();
    }

    private boolean compareMessages(Message message, Message other) {
        return other.channelId == message.channelId && other.conversationId == message.conversationId;
    }

    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }
}
