package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.OrganizationService;
import com.taller2.hypechatapp.ui.activities.utils.ScreenDisablerHelper;

import androidx.appcompat.app.AppCompatActivity;

public class OrganizationProfileActivity extends AppCompatActivity {
    private OrganizationService organizationService;
    private TextView name, description, welcomeMessage;
    private ProgressBar loading;
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

        FloatingActionButton editButton = findViewById(R.id.organizationProfileEdit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOrganization();
            }
        });

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

    private void showLoading() {
        loading.setVisibility(View.VISIBLE);
        ScreenDisablerHelper.disableScreenTouch(getWindow());
    }

    private void hideLoading() {
        loading.setVisibility(View.GONE);
        ScreenDisablerHelper.enableScreenTouch(getWindow());
    }

    private void editOrganization() {
        //TODO do something
    }

    private void initializeButtons() {
        // Send invitations
        Button sendInvitationsButton = findViewById(R.id.sendInvitationsButton);
        //TODO show or hide button depending on role
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
        //TODO show or hide button depending on role
        channelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChannels();
            }
        });
    }

    private void sendInvitations() {
        Intent intent = new Intent(this, SendInvitationsActivity.class);
        startActivity(intent);
    }

    private void showUsersList() {
        // TODO do something
    }

    private void showUsersMap() {
        // TODO do something
    }

    private void showChannels() {
        // TODO do something
    }

}
