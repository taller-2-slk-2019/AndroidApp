package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.MessageListAdapter;
import com.taller2.hypechatapp.components.FilePicker;
import com.taller2.hypechatapp.components.ImagePicker;
import com.taller2.hypechatapp.firebase.FirebaseStorageService;
import com.taller2.hypechatapp.firebase.FirebaseStorageUploadInterface;
import com.taller2.hypechatapp.model.Message;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.SuccessResponse;
import com.taller2.hypechatapp.services.MessageService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ChatActivity extends MenuActivity implements SwipeRefreshLayout.OnRefreshListener, FirebaseStorageUploadInterface {

    private SwipeRefreshLayout messagesListContainer;
    private RecyclerView messagesList;
    private MessageListAdapter messagesAdapter;

    private TextView newMessageText;

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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic("channel_1"); //TODO hardcoded id and allow conversations
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void setUpMessagesListUI() {
        messagesListContainer = findViewById(R.id.chatMessagesListContainer);
        messagesListContainer.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        messagesList = findViewById(R.id.chatMessagesList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        messagesList.setLayoutManager(layoutManager);

        messagesAdapter = new MessageListAdapter();
        messagesList.setAdapter(messagesAdapter);

        messagesListContainer.setOnRefreshListener(this);
    }

    private void setUpNewMessageUI(){
        newMessageText = findViewById(R.id.newMessageText);

        ImageView sendMessageButton = findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextMessage();
            }
        });

        ImageView pickImageButton = findViewById(R.id.pickImageButton);
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.chooseImage(ChatActivity.this);
            }
        });

        ImageView pickFileButton = findViewById(R.id.pickFileButton);
        pickFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePicker.chooseFile(ChatActivity.this);
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
                    messagesAdapter.addOlderMessages(messages);
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

    private void sendMessage(Message message){
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

    private void sendTextMessage() {
        String messageText = newMessageText.getText().toString();
        if (messageText.equals("")){
            return;
        }

        Message message = new Message();
        message.data = messageText;
        message.type = Message.TYPE_TEXT;
        message.channelId = 1; //TODO harcoded channel id

        sendMessage(message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message message) {
        Log.d("Chat Activity", "Message received: " + Integer.toString(message.id));
        messagesAdapter.addLastMessage(message);
        messagesList.scrollToPosition(messagesAdapter.getItemCount() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ImagePicker.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            Uri imagePath = ImagePicker.getFilePath(data);
            new FirebaseStorageService().uploadLocalImage(this, imagePath);
        } else if(requestCode == FilePicker.PICK_FILE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            Uri filePath = FilePicker.getFilePath(data);
            new FirebaseStorageService().uploadLocalFile(this, filePath);
        }
    }

    @Override
    public void onFileUploaded(String downloadUrl, String type) {
        Message message = new Message();
        message.data = downloadUrl;
        message.type = type.equals(FirebaseStorageService.TYPE_IMAGE) ? Message.TYPE_IMAGE : Message.TYPE_FILE;
        message.channelId = 1; //TODO harcoded channel id

        sendMessage(message);
    }

    @Override
    public void onFileUploadError(Exception exception) {
        String textToShow = "No se pudo enviar el archivo";
        Toast.makeText(this, textToShow, Toast.LENGTH_LONG).show();
    }
}