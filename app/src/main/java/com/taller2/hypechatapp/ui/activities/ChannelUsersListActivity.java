package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.UserListActionListener;
import com.taller2.hypechatapp.adapters.UsersListAdapter;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.ChannelService;
import com.taller2.hypechatapp.services.OrganizationService;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelUsersListActivity extends BaseActivity implements UserListActionListener {

    public static final String CHANNEL_ID_KEY = "CHANNEL_ID_KEY";

    private UsersListAdapter usersAdapter;
    private ChannelService channelService;
    private OrganizationService organizationService;
    private UserManagerPreferences preferences;
    private int channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_users_list);

        if (getIntent().getExtras() != null) {
            channelId = getIntent().getExtras().getInt(CHANNEL_ID_KEY, 0);
        }

        organizationService = new OrganizationService();
        channelService = new ChannelService();
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

        channelService.getChannelUsers(channelId, new Client<List<User>>() {
            @Override
            public void onResponseSuccess(List<User> users) {
                usersAdapter.setUsers(users);
                hideLoading();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                hideLoading();
                String textToShow = "No se pudo obtener los usuarios del canal";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return ChannelUsersListActivity.this;
            }
        });
    }

    @Override
    public void onUserDeleted(final User user) {
        /*DialogService.showConfirmDialog(this, "Seguro que desea eliminar al usuario del canal?", new DialogConfirm() {
            @Override
            public void onConfirm() {
                showLoading();
                organizationService.removeUser(preferences.getSelectedOrganization(),
                        user.getId(), new Client<Void>() {
                            @Override
                            public void onResponseSuccess(Void responseBody) {
                                hideLoading();
                                usersAdapter.removeUser(user);
                                Toast.makeText(getContext(), "Usuario eliminado", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onResponseError(boolean connectionError, String errorMessage) {
                                hideLoading();
                                Toast.makeText(getContext(), "No se pudo eliminar el usuario", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public Context getContext() {
                                return ChannelUsersListActivity.this;
                            }
                        });
            }
        });*/
    }

    @Override
    public void onUserRoleChanged(User user, String selectedRole) {
        /*showLoading();
        organizationService.updateUserRole(preferences.getSelectedOrganization(),
                user.getId(), selectedRole, new Client<Void>() {
                    @Override
                    public void onResponseSuccess(Void responseBody) {
                        hideLoading();
                        Toast.makeText(getContext(), "Rol actualizado", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponseError(boolean connectionError, String errorMessage) {
                        hideLoading();
                        Toast.makeText(getContext(), "No se pudo actualizar el rol", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public Context getContext() {
                        return ChannelUsersListActivity.this;
                    }
                });*/
    }

    @Override
    public void onUserSelected(User user) {
        /*Intent intent = new Intent(this, UserProfileActivity.class);
        if (!FirebaseAuthService.isCurrentUser(user)) {
            intent.putExtra(UserProfileActivity.USER_ID_KEY, user.getId());
        }
        startActivity(intent);*/
    }
}
