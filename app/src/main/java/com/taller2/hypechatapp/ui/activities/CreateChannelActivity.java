package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.network.ChannelService;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.ChannelRequest;

import androidx.appcompat.app.AppCompatActivity;

public class CreateChannelActivity extends AppCompatActivity {

    private ChannelService channelService;
    private EditText channelName, description, welcome;
    private Switch channelPrivacy;
    private Button btnCreate, btnCancel;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel);

        channelService = new ChannelService();

        setUpView();
        addUIBehaviour();
    }

    private void setUpView() {
        channelName = findViewById(R.id.txt_name);
        description = findViewById(R.id.txt_description);
        welcome = findViewById(R.id.txt_welcome);
        channelPrivacy = findViewById(R.id.swt_privacy);
        channelPrivacy = findViewById(R.id.swt_privacy);
        btnCreate = findViewById(R.id.btn_create);
        btnCancel = findViewById(R.id.btn_cancel);
        loading = findViewById(R.id.loading);
    }

    private void addUIBehaviour() {
        channelPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setText(R.string.channel_privacy_open);
                } else {
                    buttonView.setText(R.string.channel_privacy_closed);
                }
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                ChannelRequest channelRequest = createRequest();
                channelService.createChannel(channelRequest, new Client<Void>() {
                    @Override
                    public void onResponseSuccess(Void responseBody) {
                        loading.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(CreateChannelActivity.this, ChannelChatActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onResponseError(String errorMessage) {
                        //TODO: add generic error message
                        loading.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public Context getContext() {
                        return null;
                    }
                });

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateChannelActivity.this, ChannelChatActivity.class);
                startActivity(intent);
            }
        });
    }

    private ChannelRequest createRequest() {
        ChannelRequest channelRequest = new ChannelRequest();
        channelRequest.name = channelName.getText().toString();
        channelRequest.description = description.getText().toString();
        channelRequest.visibility = channelPrivacy.isChecked() ? "Visible" : "Privado";
        channelRequest.welcome = welcome.getText().toString();
        channelRequest.organizationId = 1;
        channelRequest.creatorId = 1;

        return channelRequest;
    }
}
