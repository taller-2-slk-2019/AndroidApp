package com.taller2.hypechatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.NavigationDrawerItemType;
import com.taller2.hypechatapp.model.NavigationDrawerShowable;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.ui.model.NavigationActionItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.BaseItemViewHolder> {
    private static final int TYPE_CHANNEL = 0;
    private static final int TYPE_ACTION = 1;
    private static final int TYPE_ORGANIZATION = 2;

    private List<NavigationDrawerShowable> data;
    private INavigation listener;

    public NavigationAdapter(INavigation listener, List<NavigationDrawerShowable> data) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        NavigationDrawerShowable element = data.get(position);
        if (element instanceof Channel) {
            return TYPE_CHANNEL;
        } else if (element instanceof NavigationActionItem) {
            return TYPE_ACTION;
        } else if (element instanceof Organization) {
            return TYPE_ORGANIZATION;
        }

        throw new IllegalArgumentException("Invalid position " + position + "class: " + element.getClass());
    }

    @Override
    public BaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        switch (viewType) {
            case TYPE_CHANNEL: {
                View view = LayoutInflater.from(context).inflate(R.layout.item_channel_navigation, parent, false);
                return new ChannelViewHolder(view, context, data, listener);

            }
            case TYPE_ACTION: {
                View view = LayoutInflater.from(context).inflate(R.layout.item_action_navigation, parent, false);
                return new ActionItemViewHolder(view, data, listener);
            }

            case TYPE_ORGANIZATION: {
                View view = LayoutInflater.from(context).inflate(R.layout.item_organization_navigation, parent, false);
                return new OrganizationItemViewHolder(view, data);
            }

            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseItemViewHolder holder, int position) {
        NavigationDrawerShowable element = data.get(position);
        holder.bind(element);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refreshAdapter(List newData) {
        if (data == null) {
            data = new ArrayList<>();
        }

        data.clear();
        List dataOrderedByType = sortNavigationDrawersItems(newData);
        data.addAll(dataOrderedByType);
        notifyDataSetChanged();
    }

    private List sortNavigationDrawersItems(List<NavigationDrawerShowable> newData) {
        Map<String, List<NavigationDrawerShowable>> dataOrderedByType = new HashMap<>();
        for (NavigationDrawerShowable itemShowable: newData) {
            if (!dataOrderedByType.containsKey(itemShowable.getType().toString())) {
                List<NavigationDrawerShowable> list = new ArrayList<>();
                list.add(itemShowable);

                dataOrderedByType.put(itemShowable.getType().toString(), list);
            } else {
                dataOrderedByType.get(itemShowable.getType().toString()).add(itemShowable);
            }

        }

        List<NavigationDrawerShowable> itemsOrdered = new ArrayList<>();
        itemsOrdered.add(new NavigationActionItem("CANALES", R.drawable.ic_plus, NavigationActionItem.ActionType.CREATE_CHANNEL));
        List<NavigationDrawerShowable> channelList = dataOrderedByType.get(NavigationDrawerItemType.CHANNELS.toString());
        if (channelList != null) itemsOrdered.addAll(channelList);

        itemsOrdered.add(new NavigationActionItem("MENSAJES", R.drawable.ic_plus, NavigationActionItem.ActionType.CREATE_DIRECT_MESSAGE));
        List<NavigationDrawerShowable> messages = dataOrderedByType.get(NavigationDrawerItemType.MESSAGES.toString());
        if (messages != null) itemsOrdered.addAll(messages);

        return itemsOrdered;

    }

    public static abstract class BaseItemViewHolder<T> extends RecyclerView.ViewHolder {

        public BaseItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bind(T type);
    }

    public static class ActionItemViewHolder extends BaseItemViewHolder<NavigationActionItem> implements View.OnClickListener {
        TextView displayName;
        ImageView actionIcon;
        INavigation listener;
        List<NavigationDrawerShowable> data;

        ActionItemViewHolder(View itemView, List<NavigationDrawerShowable> data, INavigation listener) {
            super(itemView);
            this.listener = listener;
            this.data = data;
            displayName = itemView.findViewById(R.id.item_title);
            actionIcon = itemView.findViewById(R.id.item_img);
        }

        @Override
        public void bind(NavigationActionItem navigationData) {
            displayName.setText(navigationData.getTitle());
            actionIcon.setImageResource(R.drawable.ic_plus_channel);
            actionIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            NavigationActionItem navigationData = (NavigationActionItem) data.get(getAdapterPosition());
            listener.onIconClick(navigationData.getActionType().toString());
        }
    }

    public static class OrganizationItemViewHolder extends BaseItemViewHolder<Organization> {
        TextView organizationName;
        List<NavigationDrawerShowable> data;

        OrganizationItemViewHolder(View itemView, List<NavigationDrawerShowable> data) {
            super(itemView);
            this.data = data;
            organizationName = itemView.findViewById(R.id.organization_name);
        }

        @Override
        public void bind(Organization organization) {

            organizationName.setText(organization.getName());
        }
    }

    public static class ChannelViewHolder extends BaseItemViewHolder<Channel> implements View.OnClickListener {
        TextView displayName;
        ImageView iconPrivacy;
        List<NavigationDrawerShowable> data;
        INavigation navigationListener;
        Context context;

        ChannelViewHolder(View itemView, Context context, List<NavigationDrawerShowable> data, INavigation navigationListener) {
            super(itemView);
            this.data = data;
            this.navigationListener = navigationListener;
            this.context = context;
            displayName = itemView.findViewById(R.id.channel_name);
            iconPrivacy= itemView.findViewById(R.id.img_channel_privacy);
        }

        @Override
        public void bind(Channel channel) {
            displayName.setText(channel.getName());
            iconPrivacy.setImageResource(channel.getIsPublic() ? R.drawable.ic_hashtag : R.drawable.ic_private_channel);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position < 0) {
                return;
            }

            Channel channel = (Channel) data.get(position);
            Toast.makeText(context, "Canal | id: " + channel.getId(), Toast.LENGTH_SHORT).show();
        }
    }
}