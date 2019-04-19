package com.taller2.hypechatapp.ui.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.IMenuItemsClick;
import com.taller2.hypechatapp.model.Channel;

import androidx.recyclerview.widget.RecyclerView;

public class MenuChannelItemViewHolder extends RecyclerView.ViewHolder {

    private final IMenuItemsClick listener;
    private TextView displayName;
    private ImageView iconPrivacy;
    private Channel channel;

    public MenuChannelItemViewHolder(View itemView, IMenuItemsClick listener) {
        super(itemView);
        displayName = itemView.findViewById(R.id.channel_name);
        iconPrivacy = itemView.findViewById(R.id.img_channel_privacy);
        this.listener = listener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHolderClick();
            }
        });
    }

    public void setChannel(Channel channel) {
        displayName.setText(channel.getName());
        iconPrivacy.setImageResource(channel.getIsPublic() ? R.drawable.ic_hashtag : R.drawable.ic_private_channel);
        this.channel = channel;
    }

    private void onHolderClick(){
        listener.onChannelClick(channel);
    }
}
