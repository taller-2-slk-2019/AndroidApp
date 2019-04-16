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

import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.ChannelRequest;
import com.taller2.hypechatapp.services.ChannelService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CreateChannelActivity extends AppCompatActivity {

    private ChannelService channelService;
    private TextInputEditText channelName, description, welcome;
    private Switch channelPrivacy;
    private Button btnCreate;
    private ProgressBar loading;
    private Integer organizationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        organizationId = getIntent().getIntExtra("ORGANIZATION_ID", -1);
        setContentView(R.layout.activity_create_channel);

        channelService = new ChannelService();

        setUpView();
        addUIBehaviour();
    }

    private void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar_create_channel);
        setSupportActionBar(toolbar);

        channelName = findViewById(R.id.edit_channel_name);
        description = findViewById(R.id.edit_channel_description);
        welcome = findViewById(R.id.edit_channel_welcome);
        channelPrivacy = findViewById(R.id.swt_privacy);
        btnCreate = findViewById(R.id.btn_create);
        loading = findViewById(R.id.loading);
    }

    private void addUIBehaviour() {
        channelName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText)v).setHint(getString(R.string.hint_example_channel_name));
                } else {
                    ((EditText)v).setHint("");
                }
            }
        });

        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText)v).setHint(getString(R.string.hint_about_channel));
                } else {
                    ((EditText)v).setHint("");
                }
            }
        });

        welcome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText)v).setHint(getString(R.string.hint_regards_channel));
                } else {
                    ((EditText)v).setHint("");
                }
            }
        });

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
                        Toast.makeText(getContext(), "Woow! Canal creado con el id: " + responseBody.getId(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreateChannelActivity.this, ChatActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onResponseError(String errorMessage) {
                        loading.setVisibility(View.INVISIBLE);
                        String textToShow;
                        if (!TextUtils.isEmpty(errorMessage)) {
                            textToShow = errorMessage;
                        } else {
                            textToShow = "No fue posible crear un canal. Intente más tarde.";
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
    }

    private boolean isValidForm(TextInputEditText channelName, EditText description, EditText welcome) {
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
        channelRequest.isPublic = channelPrivacy.isChecked();
        channelRequest.welcome = welcome.getText().toString();
        channelRequest.organizationId = this.organizationId;

        return channelRequest;
    }
}
