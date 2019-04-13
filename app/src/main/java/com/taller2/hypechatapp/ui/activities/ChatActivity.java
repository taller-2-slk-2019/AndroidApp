package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.MessageListAdapter;
import com.taller2.hypechatapp.model.Message;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.MessageService;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ChatActivity extends MenuActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout messagesListContainer;
    private RecyclerView messagesList;
    private MessageListAdapter messagesAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private MessageService messageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        super.onCreate(savedInstanceState);

        messageService = new MessageService();

        setUpUI();
        onRefresh();//TODO change when selecting org and chanel is implemented
    }

    private void setUpUI() {
        messagesListContainer = findViewById(R.id.chatMessagesListContainer);
        messagesListContainer.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        messagesList = findViewById(R.id.chatMessagesList);

        layoutManager = new LinearLayoutManager(this);
        messagesList.setLayoutManager(layoutManager);

        messagesAdapter = new MessageListAdapter();
        messagesList.setAdapter(messagesAdapter);

        messagesListContainer.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(){
        int offset = messagesAdapter.getItemCount();
        messagesListContainer.setRefreshing(true);

        //TODO harcoded channel id
        //TODO refactor to allow conversation messages as well
        messageService.getChannelMessages(1, offset, new Client<List<Message>>() {
            @Override
            public void onResponseSuccess(List<Message> messages) {
                messagesListContainer.setRefreshing(false);
                if (messages.size() > 0){
                    messagesAdapter.addMessages(messages);
                    messagesList.scrollToPosition(messages.size() - 1);
                } else {
                    String textToShow = "No hay más mensajes";
                    Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                    messagesListContainer.setEnabled(false);
                }
            }

            @Override
            public void onResponseError(String errorMessage) {
                messagesListContainer.setRefreshing(false);
                String textToShow = "Ha ocurrido un error al cargar los mensajes";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return ChatActivity.this;
            }
        });
    }

}