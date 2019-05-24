package com.taller2.hypechatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.ui.model.ListChannelViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelsListAdapter extends RecyclerView.Adapter<ListChannelViewHolder> {

    protected ChannelListActionListener listener;
    private List<Channel> channels = new ArrayList<>();

    public ChannelsListAdapter(ChannelListActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list_holder, parent, false);
        return new ListChannelViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChannelViewHolder holder, int position) {
        Channel channel = channels.get(position);
        holder.setChannel(channel);
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
        notifyDataSetChanged();
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
        notifyDataSetChanged();
    }
}
