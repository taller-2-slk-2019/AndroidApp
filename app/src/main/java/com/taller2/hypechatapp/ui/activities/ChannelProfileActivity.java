package com.taller2.hypechatapp.ui.activities;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.ChannelService;

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
    }
}
