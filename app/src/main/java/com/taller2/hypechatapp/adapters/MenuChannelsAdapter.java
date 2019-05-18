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
    private List<Channel> data = new ArrayList<>();
    private List<Channel> filteredData=new ArrayList<>();

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
        Channel channel = filteredData.get(position);
        holder.setChannel(channel);
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    public void setChannels(List<Channel> channels){
        data = channels;
        filteredData = channels;
        notifyDataSetChanged();
    }

    public List<Channel> getChannels() {
        return data;
    }

    public void add(Channel channel) {
        data.add(channel);
        filteredData.add(channel);
        this.notifyDataSetChanged();
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

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
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
