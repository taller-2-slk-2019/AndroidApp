package com.taller2.hypechatapp.ui.model;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.ChannelListActionListener;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListChannelViewHolder extends RecyclerView.ViewHolder {

    private TextView name, description;
    private ImageView privacyIcon;
    private ChannelListActionListener listener;
    private Channel channel;
    private Context context;
    private UserManagerPreferences prefs;
    private Button deleteButton;

    public ListChannelViewHolder(@NonNull View itemView, ChannelListActionListener listener) {
        super(itemView);
        context = itemView.getContext();
        prefs = new UserManagerPreferences(context);
        this.listener = listener;
        name = itemView.findViewById(R.id.channel_name);
        description = itemView.findViewById(R.id.channel_description);
        privacyIcon = itemView.findViewById(R.id.img_channel_privacy);
        deleteButton = itemView.findViewById(R.id.deleteChannelButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButtonClick();
            }
        });
        name.setOnClickListener(new ChannelClickListener());
        privacyIcon.setOnClickListener(new ChannelClickListener());
        description.setOnClickListener(new ChannelClickListener());
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
        name.setText(channel.getName());
        description.setText(channel.getDescription());
        privacyIcon.setImageResource(channel.getIsPublic() ? R.drawable.ic_hashtag : R.drawable.ic_private_channel);
    }

    private void deleteButtonClick() {
        listener.onChannelDeleted(channel);
    }

    private class ChannelClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            listener.onChannelSelected(channel);
        }
    }
}
