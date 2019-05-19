package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.IUserClick;
import com.taller2.hypechatapp.adapters.NewConversationUsersAdapter;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Conversation;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.ConversationRequest;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.ConversationService;
import com.taller2.hypechatapp.services.UserService;
import com.taller2.hypechatapp.ui.activities.utils.ScreenDisablerHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CreateConversationActivity extends AppCompatActivity implements IUserClick {

    private NewConversationUsersAdapter usersAdapter;
    private UserService userService;
    private ConversationService conversationService;
    private ProgressBar loading;
    private UserManagerPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_conversation);

        conversationService = new ConversationService();
        userService = new UserService();
        preferences = new UserManagerPreferences(this);

        setUpView();
    }

    private void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar_create_conversation);
        setSupportActionBar(toolbar);

        loading = findViewById(R.id.loading);

        RecyclerView rvUsers = findViewById(R.id.create_conversation_users_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvUsers.setLayoutManager(layoutManager);

        usersAdapter = new NewConversationUsersAdapter(this);
        rvUsers.setAdapter(usersAdapter);

        getUsers();
    }

    private void getUsers(){
        loading.setVisibility(View.VISIBLE);

        userService.getUsersByOrganization(preferences.getSelectedOrganization(), new Client<List<User>>() {
            @Override
            public void onResponseSuccess(List<User> users) {
                List<User> filteredUsers = new ArrayList<>();
                for (User user: users){
                    if (!FirebaseAuthService.isCurrentUser(user)){
                        filteredUsers.add(user);
                    }
                }

                if (filteredUsers.isEmpty()){
                    findViewById(R.id.create_conversation_no_users).setVisibility(View.VISIBLE);
                } else {
                    usersAdapter.setUsers(filteredUsers);
                }

                loading.setVisibility(View.INVISIBLE);
                ScreenDisablerHelper.enableScreenTouch(getWindow());
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                loading.setVisibility(View.INVISIBLE);
                String textToShow = "No se pudo obtener los usuarios de la organización";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return CreateConversationActivity.this;
            }
        });
    }

    @Override
    public void onUserClick(final User user) {
        loading.setVisibility(View.VISIBLE);
        ScreenDisablerHelper.disableScreenTouch(getWindow());

        ConversationRequest conversation = new ConversationRequest();
        conversation.organizationId = preferences.getSelectedOrganization();
        conversation.userId = user.getId();

        conversationService.createConversation(conversation, new Client<Conversation>() {
            @Override
            public void onResponseSuccess(Conversation response) {
                loading.setVisibility(View.INVISIBLE);
                ScreenDisablerHelper.enableScreenTouch(getWindow());
                Toast.makeText(getContext(), "Woow! Conversación iniciada con " + user.getName(), Toast.LENGTH_LONG).show();
                preferences.saveSelectedConversation(response.id);
                Intent intent = new Intent(CreateConversationActivity.this, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                loading.setVisibility(View.INVISIBLE);
                ScreenDisablerHelper.enableScreenTouch(getWindow());
                String textToShow = "No fue posible iniciar una conversación. Intente más tarde.";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return CreateConversationActivity.this;
            }
        });
    }
}
