package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.UserListActionListener;
import com.taller2.hypechatapp.adapters.UsersListAdapter;
import com.taller2.hypechatapp.components.DialogConfirm;
import com.taller2.hypechatapp.components.DialogService;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.OrganizationService;
import com.taller2.hypechatapp.services.UserService;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrganizationUsersListActivity extends BaseActivity implements UserListActionListener {

    private UsersListAdapter usersAdapter;
    private UserService userService;
    private OrganizationService organizationService;
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
                return OrganizationUsersListActivity.this;
            }
        });
    }

    @Override
    public void onUserDeleted(final User user) {
        DialogService.showConfirmDialog(this, "Seguro que desea eliminar al usuario de la organización?", new DialogConfirm() {
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
                                return OrganizationUsersListActivity.this;
                            }
                        });
            }
        });
    }

    @Override
    public void onUserRoleChanged(User user, String selectedRole) {
        showLoading();
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
                        return OrganizationUsersListActivity.this;
                    }
                });
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
