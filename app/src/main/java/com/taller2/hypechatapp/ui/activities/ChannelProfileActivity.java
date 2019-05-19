package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.roles.RoleFactory;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.ChannelService;
import com.taller2.hypechatapp.ui.activities.utils.ScreenDisablerHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChannelProfileActivity extends AppCompatActivity {

    private ChannelService channelService;
    private TextInputEditText name, description, welcome, type;
    private ProgressBar loading;
    private UserManagerPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_channel_profile);

        preferences = new UserManagerPreferences(this);
        channelService = new ChannelService();

        setUpView();
    }

    private void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.channel_name);
        description = findViewById(R.id.channel_description);
        welcome = findViewById(R.id.channel_welcome);
        type = findViewById(R.id.channel_type);
        loading = findViewById(R.id.loading);

        setUpButtons();
        initializeChannel();
    }

    private void showLoading() {
        loading.setVisibility(View.VISIBLE);
        ScreenDisablerHelper.disableScreenTouch(getWindow());
    }

    private void hideLoading() {
        loading.setVisibility(View.GONE);
        ScreenDisablerHelper.enableScreenTouch(getWindow());
    }

    private void initializeChannel() {
        showLoading();
        channelService.getChannelInfo(preferences.getSelectedChannel(), new Client<Channel>() {
            @Override
            public void onResponseSuccess(Channel responseBody) {
                setChannel(responseBody);
                hideLoading();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                hideLoading();
                Toast.makeText(getContext(), "No se pudo obtener la información del canal", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public Context getContext() {
                return ChannelProfileActivity.this;
            }
        });
    }

    private void setChannel(Channel channel) {
        name.setText(channel.getName());
        description.setText(channel.getDescription());
        welcome.setText(channel.getWelcome());
        type.setText(channel.getIsPublic() ? "Público" : "Privado");
    }

    private void setUpButtons() {
        Button channelUsersList = findViewById(R.id.showUsersButton);
        channelUsersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChannelUsers();
            }
        });

        Button abandonChannelButton = findViewById(R.id.abandonChannelButton);
        abandonChannelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abandonChannel();
            }
        });

        FloatingActionButton editButton = findViewById(R.id.channelEditButton);
        if (!RoleFactory.getRole(preferences.getOrganizationRole()).hasChannelsPermissions()) {
            editButton.hide();
        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editChannel();
            }
        });
    }

    private void showChannelUsers() {
        //TODO do something
    }

    private void abandonChannel() {
        //TODO do something
    }

    private void editChannel() {
        //TODO do something
    }
}
