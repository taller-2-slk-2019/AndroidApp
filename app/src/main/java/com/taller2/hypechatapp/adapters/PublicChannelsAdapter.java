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

public class PublicChannelsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final int VIEW_TYPE_EMPTY=0;
    private static final int VIEW_TYPE_NOT_EMPTY=1;
    private final IMenuItemsClick listener;
    private List<Channel> channelsList;
    private List<Channel> filteredChannelList;


    @Override
    public int getItemViewType(int position) {
        if(channelsList.size()==0)
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_NOT_EMPTY;
    }

    public PublicChannelsAdapter(List<Channel> channelsList,IMenuItemsClick listener) {
        this.channelsList=channelsList;
        this.filteredChannelList=channelsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==VIEW_TYPE_EMPTY){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_channels_empty, parent, false);

            return new PublicChannelsAdapter.EmptyViewHolder(v);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_navigation, parent, false);
            return new MenuChannelItemViewHolder(view, listener);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if (viewType==VIEW_TYPE_NOT_EMPTY){
            MenuChannelItemViewHolder vh = (MenuChannelItemViewHolder) holder;
            Channel channel = filteredChannelList.get(position);
            vh.setChannel(channel);
        }


    }

    @Override
    public int getItemCount() {
        //Hack to show the empty view
        if(channelsList.size() == 0 ){
            return 1;
        }else {
            return filteredChannelList.size();
        }
    }

    public void setChannels(List<Channel> channels){
        channelsList = channels;
        notifyDataSetChanged();
    }

    public List<Channel> getChannels() {
        return channelsList;
    }

    public void add(Channel channel) {
        channelsList.add(channel);
        this.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredChannelList = channelsList;
                } else {
                    List<Channel> filteredList = new ArrayList<>();
                    for (Channel channel : channelsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (channel.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(channel);
                        }
                    }

                    filteredChannelList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredChannelList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredChannelList = (ArrayList<Channel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder{

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
