package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.DialogConfirm;
import com.taller2.hypechatapp.components.DialogService;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.roles.RoleFactory;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.ChannelService;

import androidx.appcompat.widget.Toolbar;

public class ChannelProfileActivity extends BaseActivity {

    public static final String CHANNEL_ID_KEY = "CHANNEL_ID_KEY";

    private ChannelService channelService;
    private TextInputEditText name, description, welcome, type;
    private UserManagerPreferences preferences;
    private int channelId = 0;
    private boolean currentChannelShown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_channel_profile);

        preferences = new UserManagerPreferences(this);
        channelService = new ChannelService();

        if (getIntent().getExtras() != null) {
            channelId = getIntent().getExtras().getInt(CHANNEL_ID_KEY, 0);
            currentChannelShown = false;
        } else {
            channelId = preferences.getSelectedChannel();
        }

        setUpView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeChannel();
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
    }

    private void initializeChannel() {
        showLoading();
        channelService.getChannelInfo(channelId, new Client<Channel>() {
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
        abandonChannelButton.setVisibility(currentChannelShown ? View.VISIBLE : View.GONE);

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
        Intent intent = new Intent(this, ChannelUsersListActivity.class);
        intent.putExtra(ChannelUsersListActivity.CHANNEL_ID_KEY, channelId);
        startActivity(intent);
    }

    private void abandonChannel() {
        DialogService.showConfirmDialog(this, "Seguro que desea abandonar el canal?", new DialogConfirm() {
            @Override
            public void onConfirm() {
                showLoading();
                channelService.abandonChannel(preferences.getSelectedChannel(), new Client<Void>() {
                    @Override
                    public void onResponseSuccess(Void responseBody) {
                        hideLoading();
                        Toast.makeText(getContext(), "Has abandonado el canal: " + name.getText(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onResponseError(boolean connectionError, String errorMessage) {
                        hideLoading();
                        Toast.makeText(getContext(), "No se pudo abandonar el canal", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public Context getContext() {
                        return ChannelProfileActivity.this;
                    }
                });
            }
        });
    }

    private void editChannel() {
        Intent intent = new Intent(this, CreateChannelActivity.class);
        intent.putExtra(CreateChannelActivity.CHANNEL_ID_KEY, channelId);
        startActivity(intent);
    }
}
