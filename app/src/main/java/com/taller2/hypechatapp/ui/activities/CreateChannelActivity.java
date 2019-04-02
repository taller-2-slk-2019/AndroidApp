package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.ChannelRequest;
import com.taller2.hypechatapp.services.ChannelService;

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

                if (!isValidForm(channelName, description, welcome)) {
                    return;
                }

                loading.setVisibility(View.VISIBLE);
                ChannelRequest channelRequest = createRequest();
                channelService.createChannel(channelRequest, new Client<Channel>() {
                    @Override
                    public void onResponseSuccess(Channel responseBody) {
                        loading.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(CreateChannelActivity.this, ChannelChatActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onResponseError(String errorMessage) {
                        loading.setVisibility(View.INVISIBLE);
                        String textToShow;
                        if(!TextUtils.isEmpty(errorMessage)){
                            textToShow=errorMessage;
                        } else {
                            textToShow="No fue posible crear un canal. Intente más tarde.";
                        }
                        Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public Context getContext() {
                        return CreateChannelActivity.this;
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

    private boolean isValidForm(EditText channelName, EditText description, EditText welcome) {
        if (TextUtils.isEmpty(channelName.getText().toString())) {
            channelName.setError(getString(R.string.input_channel_name_error));
            return false;
        }

        if (TextUtils.isEmpty(description.getText().toString())) {
            description.setError(getString(R.string.input_channel_description_error));
            return false;
        }

        if (TextUtils.isEmpty(welcome.getText().toString())) {
            welcome.setError(getString(R.string.input_channel_welcome_error));
            return false;
        }

        return true;

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
