package com.taller2.hypechatapp.adapters;

import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.Conversation;

public interface IMenuItemsClick {

    void onChannelClick(Channel channel);

    void onConversationClick(Conversation conversation);
}
