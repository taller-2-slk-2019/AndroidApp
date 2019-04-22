package com.taller2.hypechatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.ui.model.MenuChannelItemViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuChannelsAdapter extends RecyclerView.Adapter<MenuChannelItemViewHolder> {

    private final IMenuItemsClick listener;
    private List<Channel> data = new ArrayList<>();

    public MenuChannelsAdapter(IMenuItemsClick listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public MenuChannelItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_navigation, parent, false);
        return new MenuChannelItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuChannelItemViewHolder holder, int position) {
        Channel channel = data.get(position);
        holder.setChannel(channel);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setChannels(List<Channel> channels){
        data = channels;
        notifyDataSetChanged();
    }

    public List<Channel> getChannels() {
        return data;
    }
}
