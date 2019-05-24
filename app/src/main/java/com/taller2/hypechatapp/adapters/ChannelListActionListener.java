package com.taller2.hypechatapp.adapters;

import com.taller2.hypechatapp.model.Channel;

public interface ChannelListActionListener {
    void onChannelSelected(Channel channel);

    void onChannelDeleted(Channel channel);
}
