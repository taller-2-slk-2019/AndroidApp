package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.ChannelNewUsersListAdapter;
import com.taller2.hypechatapp.adapters.UserListActionListener;
import com.taller2.hypechatapp.adapters.UsersListAdapter;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.ChannelService;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelNewUsersListActivity extends BaseActivity implements UserListActionListener {

    public static final String CHANNEL_ID_KEY = "CHANNEL_ID_KEY";

    private UsersListAdapter usersAdapter;
    private ChannelService channelService;
    private TextView noUsersText;
    private int channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_users_list);

        if (getIntent().getExtras() != null) {
            channelId = getIntent().getExtras().getInt(CHANNEL_ID_KEY, 0);
        }

        channelService = new ChannelService();

        setUpView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUsers();
    }

    private void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loading = findViewById(R.id.loading);
        noUsersText = findViewById(R.id.noUsersText);

        RecyclerView rvUsers = findViewById(R.id.users_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvUsers.setLayoutManager(layoutManager);

        usersAdapter = new ChannelNewUsersListAdapter(this);
        rvUsers.setAdapter(usersAdapter);
    }

    private void checkUsersCount() {
        noUsersText.setVisibility(usersAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void getUsers() {
        showLoading();

        channelService.getChannelNewUsers(channelId, new Client<List<User>>() {
            @Override
            public void onResponseSuccess(List<User> users) {
                usersAdapter.setUsers(users);
                checkUsersCount();
                hideLoading();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                hideLoading();
                String textToShow = "No se pudo obtener los usuarios de la organización";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return ChannelNewUsersListActivity.this;
            }
        });
    }

    @Override
    public void onUserAction(final User user) {
        showLoading();
        channelService.addUserToChannel(channelId, user.getId(), new Client<Void>() {
            @Override
            public void onResponseSuccess(Void responseBody) {
                hideLoading();
                usersAdapter.removeUser(user);
                checkUsersCount();
                Toast.makeText(getContext(), "Usuario agregado al canal", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                hideLoading();
                Toast.makeText(getContext(), "No se pudo agregar al usuario", Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return ChannelNewUsersListActivity.this;
            }
        });
    }

    @Override
    public void onUserRoleChanged(User user, String selectedRole) {
        // Do nothing
    }

    @Override
    public void onUserSelected(User user) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        if (!FirebaseAuthService.isCurrentUser(user)) {
            intent.putExtra(UserProfileActivity.USER_ID_KEY, user.getId());
        }
        startActivity(intent);
    }
}
