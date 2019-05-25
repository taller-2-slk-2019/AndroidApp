package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.DialogConfirm;
import com.taller2.hypechatapp.components.DialogService;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.model.roles.Role;
import com.taller2.hypechatapp.model.roles.RoleFactory;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.OrganizationService;

public class OrganizationProfileActivity extends BaseActivity {
    private OrganizationService organizationService;
    private TextView name, description, welcomeMessage;
    private ImageView profilePicture;
    private UserManagerPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_profile);
        organizationService = new OrganizationService();
        prefs = new UserManagerPreferences(this);

        setUpUI();
    }

    private void setUpUI() {
        name = findViewById(R.id.organizationProfileName);
        description = findViewById(R.id.organizationProfileDescription);
        welcomeMessage = findViewById(R.id.organizationProfileWelcome);
        profilePicture = findViewById(R.id.organizationProfileImage);
        loading = findViewById(R.id.loading);
        initializeButtons();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateOrganizationProfile();
    }

    private void updateOrganizationProfile() {
        showLoading();
        organizationService.getOrganizationProfile(prefs.getSelectedOrganization(), new Client<Organization>() {
            @Override
            public void onResponseSuccess(Organization responseBody) {
                hideLoading();
                name.setText(responseBody.getName());
                description.setText(responseBody.getDescription());
                welcomeMessage.setText(responseBody.getWelcome());
                String url = responseBody.getPicture();
                PicassoLoader.load(getApplicationContext(), String.format("%s?type=large", url), R.drawable.default_user, profilePicture);
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                String textToShow = "No fue posible obtener el perfil de la organización. Intente más tarde.";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                hideLoading();
            }

            @Override
            public Context getContext() {
                return OrganizationProfileActivity.this;
            }
        });
    }

    private void editOrganization() {
        Intent intent = new Intent(this, EditOrganizationActivity.class);
        startActivity(intent);
    }

    private void initializeButtons() {
        Role role = RoleFactory.getRole(prefs.getOrganizationRole());

        // Edit organization
        FloatingActionButton editButton = findViewById(R.id.organizationProfileEdit);
        if (!role.hasOrganizationPermissions()) {
            editButton.hide();
        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOrganization();
            }
        });

        // Abandon organization
        Button abandonButton = findViewById(R.id.organizationAbandon);
        abandonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abandonOrganization();
            }
        });

        // Send invitations
        Button sendInvitationsButton = findViewById(R.id.sendInvitationsButton);
        sendInvitationsButton.setVisibility(role.hasUsersPermissions() ? View.VISIBLE : View.GONE);
        sendInvitationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInvitations();
            }
        });

        // Users list
        Button usersListButton = findViewById(R.id.showUsersButton);
        usersListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUsersList();
            }
        });

        // Users map
        Button usersMapButton = findViewById(R.id.showUsersMapButton);
        usersMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUsersMap();
            }
        });

        // Channels
        Button channelsButton = findViewById(R.id.showChannelsButton);
        channelsButton.setVisibility(role.hasChannelsPermissions() ? View.VISIBLE : View.GONE);
        channelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChannels();
            }
        });
    }

    private void abandonOrganization() {
        DialogService.showConfirmDialog(this, "Seguro que desea abandonar la organización?", new DialogConfirm() {
            @Override
            public void onConfirm() {
                showLoading();
                organizationService.abandonOrganization(prefs.getSelectedOrganization(), new Client<Void>() {
                    @Override
                    public void onResponseSuccess(Void responseBody) {
                        hideLoading();
                        Toast.makeText(getContext(), "Has abandonado la organización: " + name.getText(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onResponseError(boolean connectionError, String errorMessage) {
                        hideLoading();
                        Toast.makeText(getContext(), "No se pudo abandonar la organización", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public Context getContext() {
                        return OrganizationProfileActivity.this;
                    }
                });
            }
        });
    }

    private void sendInvitations() {
        Intent intent = new Intent(this, SendInvitationsActivity.class);
        startActivity(intent);
    }

    private void showUsersList() {
        Intent intent = new Intent(this, OrganizationUsersListActivity.class);
        startActivity(intent);
    }

    private void showUsersMap() {
        // TODO do something
    }

    private void showChannels() {
        Intent intent = new Intent(this, ChannelsListActivity.class);
        startActivity(intent);
    }

}
