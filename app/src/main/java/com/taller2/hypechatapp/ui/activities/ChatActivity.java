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
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.MessageService;
import com.taller2.hypechatapp.services.OrganizationService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ChatActivity extends MenuActivity implements SwipeRefreshLayout.OnRefreshListener, FirebaseStorageUploadInterface {

    private SwipeRefreshLayout messagesListContainer;
    private View newMessageContainer;
    private View noChannelContainer;
    private RecyclerView messagesList;
    private MessageListAdapter messagesAdapter;

    private TextView newMessageText;

    private MessageService messageService;

    private int selectedChannel = 0;
    private int selectedConversation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        super.onCreate(savedInstanceState);

        messageService = new MessageService();

        setUpUI();
    }

    private boolean hasChatSelected(){
        return selectedChannel > 0 || selectedConversation > 0;
    }

    @Override
    protected void onChatSelected() {
        messagesAdapter.clear();
        unsubscribe();
        selectedChannel = userManagerPreferences.getSelectedChannel();
        selectedConversation = userManagerPreferences.getSelectedConversation();
        updateUI();
        if (hasChatSelected()){
            subscribe();
            messagesListContainer.setEnabled(true);
            onRefresh();
        }
    }

    @Override
    public void onDestroy(){
        unsubscribe();
        super.onDestroy();
    }

    private void subscribe(){
        FirebaseMessaging.getInstance().subscribeToTopic(getTopic());
    }

    private void unsubscribe(){
        if (hasChatSelected()){
            FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopic());
        }
    }

    private String getTopic(){
        if (selectedChannel > 0){
            return "channel_" + selectedChannel;
        }

        if (selectedConversation > 0){
            return "conversation_" + selectedConversation;
        }

        return "";
    }

    private void setUpUI(){
        messagesListContainer = findViewById(R.id.chatMessagesListContainer);
        newMessageContainer = findViewById(R.id.newMessageContainer);
        noChannelContainer = findViewById(R.id.noChannelContainer);
        setUpMessagesListUI();
        setUpNewMessageUI();
    }

    private void updateUI(){
        if (hasChatSelected()){
            messagesListContainer.setVisibility(View.VISIBLE);
            newMessageContainer.setVisibility(View.VISIBLE);
            noChannelContainer.setVisibility(View.INVISIBLE);
        } else {
            messagesListContainer.setVisibility(View.INVISIBLE);
            newMessageContainer.setVisibility(View.INVISIBLE);
            noChannelContainer.setVisibility(View.VISIBLE);
            setOrganizationWelcome();
        }
    }

    private void setUpMessagesListUI() {
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

        messageService.getChatMessages(selectedChannel, selectedConversation, offset, new Client<List<Message>>() {
            @Override
            public void onResponseSuccess(List<Message> messages) {
                messagesListContainer.setRefreshing(false);
                if (messages.size() > 0){
                    messagesAdapter.addOlderMessages(messages);
                    messagesList.scrollToPosition(messages.size() - 1);
                } else if (messagesAdapter.getItemCount() > 0) {
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
        message.channelId = selectedChannel;
        message.conversationId = selectedConversation;
        messageService.createMessage(message, new Client<Void>() {
            @Override
            public void onResponseSuccess(Void responseBody) {
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
            try {
                Uri filePath = FilePicker.getFilePath(data);
                new FirebaseStorageService().uploadLocalFile(this, filePath);
            } catch (Exception ex){
                ex.printStackTrace();
                onFileUploadError(ex);
            }
        }
    }

    private void setOrganizationWelcome(){
        final TextView welcome = findViewById(R.id.organizationWelcome);
        new OrganizationService().getOrganizationProfile(userManagerPreferences.getSelectedOrganization(), new Client<Organization>() {
            @Override
            public void onResponseSuccess(Organization responseBody) {
                welcome.setText(responseBody.getWelcome());
                toolbar.setTitle(responseBody.getName());
            }

            @Override
            public void onResponseError(String errorMessage) {
                welcome.setText(R.string.no_channel_msg);
            }

            @Override
            public Context getContext() {
                return ChatActivity.this;
            }
        });
    }

    @Override
    public void onFileUploaded(String downloadUrl, String type) {
        Message message = new Message();
        message.data = downloadUrl;
        message.type = type;

        sendMessage(message);
    }

    @Override
    public void onFileUploadError(Exception exception) {
        String textToShow = "No se pudo enviar el archivo";
        Toast.makeText(this, textToShow, Toast.LENGTH_LONG).show();
    }
}