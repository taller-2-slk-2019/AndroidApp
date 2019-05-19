package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.ChannelListActionListener;
import com.taller2.hypechatapp.adapters.ChannelsListAdapter;
import com.taller2.hypechatapp.components.DialogConfirm;
import com.taller2.hypechatapp.components.DialogService;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.ChannelService;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelsListActivity extends BaseActivity implements ChannelListActionListener {

    private ChannelsListAdapter channelsAdapter;
    private ChannelService channelService;
    private UserManagerPreferences preferences;
    private TextView noChannelsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_channels_list);

        channelService = new ChannelService();
        preferences = new UserManagerPreferences(this);

        setUpView();
    }

    private void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loading = findViewById(R.id.loading);
        noChannelsText = findViewById(R.id.noChannelsText);

        RecyclerView rvUsers = findViewById(R.id.channels_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvUsers.setLayoutManager(layoutManager);

        channelsAdapter = new ChannelsListAdapter(this);
        rvUsers.setAdapter(channelsAdapter);

        getChannels();
    }

    private void getChannels() {
        showLoading();

        channelService.getAllOrganizationChannels(preferences.getSelectedOrganization(), new Client<List<Channel>>() {
            @Override
            public void onResponseSuccess(List<Channel> channels) {
                channelsAdapter.setChannels(channels);
                checkChannelsCount();
                hideLoading();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                hideLoading();
                String textToShow = "No se pudo obtener los canales de la organización";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return ChannelsListActivity.this;
            }
        });
    }

    private void checkChannelsCount() {
        noChannelsText.setVisibility(channelsAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onChannelDeleted(final Channel channel) {
        DialogService.showConfirmDialog(this, "Seguro que desea eliminar el canal?", new DialogConfirm() {
            @Override
            public void onConfirm() {
                /*showLoading();
                channeService.removeUser(preferences.getSelectedOrganization(),
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
                                return ChannelsListActivity.this;
                            }
                        });*/
            }
        });
    }

    @Override
    public void onChannelSelected(Channel channel) {
        /*Intent intent = new Intent(this, UserProfileActivity.class);
        if (!FirebaseAuthService.isCurrentUser(user)) {
            intent.putExtra(UserProfileActivity.USER_ID_KEY, user.getId());
        }
        startActivity(intent);*/
    }
}
