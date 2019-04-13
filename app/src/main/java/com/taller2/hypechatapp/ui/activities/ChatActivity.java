package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.MessageListAdapter;
import com.taller2.hypechatapp.model.Message;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.SuccessResponse;
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

    private TextView newMessageText;
    private ImageView sendMessageButton;

    private MessageService messageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        super.onCreate(savedInstanceState);

        messageService = new MessageService();

        setUpMessagesListUI();
        setUpNewMessageUI();
        onRefresh();//TODO change when selecting org and chanel is implemented

        FirebaseMessaging.getInstance().subscribeToTopic("channel_1"); //TODO hardcoded id and allow conversations
    }

    @Override
    protected void onDestroy(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic("channel_1"); //TODO hardcoded id and allow conversations
        super.onDestroy();
    }

    private void setUpMessagesListUI() {
        messagesListContainer = findViewById(R.id.chatMessagesListContainer);
        messagesListContainer.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        messagesList = findViewById(R.id.chatMessagesList);

        layoutManager = new LinearLayoutManager(this);
        messagesList.setLayoutManager(layoutManager);

        messagesAdapter = new MessageListAdapter();
        messagesList.setAdapter(messagesAdapter);

        messagesListContainer.setOnRefreshListener(this);
    }

    private void setUpNewMessageUI(){
        newMessageText = findViewById(R.id.newMessageText);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
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

    private void sendMessage() {
        String messageText = newMessageText.getText().toString();
        if (messageText.equals("")){
            return;
        }

        Message message = new Message();
        message.setData(messageText);
        message.setType(Message.TYPE_TEXT); //TODO harcoded type
        message.setChannel(1); //TODO harcoded channel id

        messageService.createMessage(message, new Client<SuccessResponse>() {
            @Override
            public void onResponseSuccess(SuccessResponse responseBody) {
                newMessageText.setText("");
            }

            @Override
            public void onResponseError(String errorMessage) {
                String textToShow = "No se pudo enviar el mensaje";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return ChatActivity.this;
            }
        });
    }

}