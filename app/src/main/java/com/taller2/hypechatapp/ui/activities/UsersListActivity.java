package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.UserListActionListener;
import com.taller2.hypechatapp.adapters.UsersListAdapter;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.OrganizationService;
import com.taller2.hypechatapp.services.UserService;
import com.taller2.hypechatapp.ui.activities.utils.ScreenDisablerHelper;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UsersListActivity extends AppCompatActivity implements UserListActionListener {

    private UsersListAdapter usersAdapter;
    private UserService userService;
    private OrganizationService organizationService;
    private ProgressBar loading;
    private UserManagerPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_users_list);

        organizationService = new OrganizationService();
        userService = new UserService();
        preferences = new UserManagerPreferences(this);

        setUpView();
    }

    private void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loading = findViewById(R.id.loading);

        RecyclerView rvUsers = findViewById(R.id.users_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvUsers.setLayoutManager(layoutManager);

        usersAdapter = new UsersListAdapter(this);
        rvUsers.setAdapter(usersAdapter);

        getUsers();
    }

    private void getUsers() {
        showLoading();

        userService.getUsersByOrganization(preferences.getSelectedOrganization(), new Client<List<User>>() {
            @Override
            public void onResponseSuccess(List<User> users) {
                usersAdapter.setUsers(users);
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
                return UsersListActivity.this;
            }
        });
    }

    private void showLoading() {
        loading.setVisibility(View.VISIBLE);
        ScreenDisablerHelper.disableScreenTouch(getWindow());
    }

    private void hideLoading() {
        loading.setVisibility(View.INVISIBLE);
        ScreenDisablerHelper.enableScreenTouch(getWindow());
    }

    @Override
    public void onUserDeleted(User user) {

    }

    @Override
    public void onUserRoleChanged(User user, String selectedRole) {

    }
}
