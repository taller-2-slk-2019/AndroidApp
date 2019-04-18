package com.taller2.hypechatapp.ui.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Channel;

import androidx.recyclerview.widget.RecyclerView;

public class MenuChannelItemViewHolder extends RecyclerView.ViewHolder {

    private TextView displayName;
    private ImageView iconPrivacy;

    public MenuChannelItemViewHolder(View itemView) {
        super(itemView);
        displayName = itemView.findViewById(R.id.channel_name);
        iconPrivacy= itemView.findViewById(R.id.img_channel_privacy);
    }

    public void setChannel(Channel channel) {
        displayName.setText(channel.getName());
        iconPrivacy.setImageResource(channel.getIsPublic() ? R.drawable.ic_hashtag : R.drawable.ic_private_channel);
    }
}
