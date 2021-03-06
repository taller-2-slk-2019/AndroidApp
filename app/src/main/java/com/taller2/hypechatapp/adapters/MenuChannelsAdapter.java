package com.taller2.hypechatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.ui.model.MenuChannelItemViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuChannelsAdapter extends RecyclerView.Adapter<MenuChannelItemViewHolder> implements Filterable {

    private final IMenuItemsClick listener;
    private boolean withChannelDescription;
    private List<Channel> data = new ArrayList<>();
    private List<Channel> filteredData = new ArrayList<>();

    public MenuChannelsAdapter(IMenuItemsClick listener, boolean withChannelDescription) {
        this.listener = listener;
        this.withChannelDescription = withChannelDescription;
    }


    @NonNull
    @Override
    public MenuChannelItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_navigation, parent, false);
        return new MenuChannelItemViewHolder(view, listener, withChannelDescription);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuChannelItemViewHolder holder, int position) {
        Channel channel = filteredData.get(position);
        holder.setChannel(channel);
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    public void setChannels(List<Channel> channels) {
        data = channels;
        filteredData = channels;
        notifyDataSetChanged();
    }

    public List<Channel> getChannels() {
        return data;
    }

    public void add(Channel channel) {
        addOrReplaceChannel(data, channel);
        addOrReplaceChannel(filteredData, channel);
        this.notifyDataSetChanged();
    }

    private void addOrReplaceChannel(List<Channel> list, Channel channel) {
        int index = getChannelIndex(list, channel);
        if (index >= 0) {
            list.remove(index);
            list.add(index, channel);
        } else {
            list.add(channel);
        }
    }

    private int getChannelIndex(List<Channel> list, Channel channel) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(channel.getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredData = data;
                } else {
                    List<Channel> filteredList = new ArrayList<>();
                    for (Channel channel : data) {

                        if (channel.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(channel);
                        }
                    }

                    filteredData = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredData = (ArrayList<Channel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
