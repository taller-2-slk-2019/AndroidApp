package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.ChannelRequest;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.ChannelService;
import com.taller2.hypechatapp.ui.listeners.OnViewTouchListener;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.widget.Toolbar;

public class CreateChannelActivity extends BaseActivity {

    public static final String CHANNEL_ID_KEY = "CHANNEL_ID_KEY";

    private ChannelService channelService;
    private TextInputEditText channelName, description, welcome;
    private Switch channelPrivacy;
    private Button btnCreate;
    private UserManagerPreferences preferences;
    private int channelId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_channel);
        findViewById(R.id.layoutContainer).setOnTouchListener(new OnViewTouchListener());

        if (getIntent().getExtras() != null) {
            channelId = getIntent().getExtras().getInt(CHANNEL_ID_KEY, 0);
        }

        preferences = new UserManagerPreferences(this);
        channelService = new ChannelService();

        setUpView();
        addUIBehaviour();
        if (channelId > 0) {
            setChannel();
        }
    }

    private void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar_create_channel);
        if (channelId > 0) {
            toolbar.setTitle(getString(R.string.title_channel_edit));
        }
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
                    ((EditText) v).setHint(getString(R.string.hint_example_channel_name));
                } else {
                    ((EditText) v).setHint("");
                }
            }
        });

        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).setHint(getString(R.string.hint_about_channel));
                } else {
                    ((EditText) v).setHint("");
                }
            }
        });

        welcome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).setHint(getString(R.string.hint_regards_channel));
                } else {
                    ((EditText) v).setHint("");
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

        if (channelId > 0) {
            btnCreate.setText(R.string.channel_edit);
        }
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channelName.setError(null);
                if (!isValidForm(channelName, description, welcome)) {
                    return;
                }

                showLoading();
                ChannelRequest channelRequest = createRequest();
                if (channelId > 0) {
                    editChannel(channelRequest);
                } else {
                    createChannel(channelRequest);
                }
            }
        });
    }

    private void editChannel(ChannelRequest channelRequest) {
        channelService.editChannel(channelId, channelRequest, new Client<Void>() {
            @Override
            public void onResponseSuccess(Void responseBody) {
                hideLoading();
                Toast.makeText(getContext(), "Woow! Canal editado", Toast.LENGTH_LONG).show();
                EventBus.getDefault().post(getChannel());
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                hideLoading();
                showError(connectionError, true);
            }

            @Override
            public Context getContext() {
                return CreateChannelActivity.this;
            }
        });
    }

    private void createChannel(ChannelRequest channelRequest) {
        channelService.createChannel(channelRequest, new Client<Channel>() {
            @Override
            public void onResponseSuccess(Channel responseBody) {
                hideLoading();
                Toast.makeText(getContext(), "Woow! Canal creado", Toast.LENGTH_LONG).show();
                preferences.saveSelectedChannel(responseBody.getId());
                Intent intent = new Intent(CreateChannelActivity.this, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                hideLoading();
                showError(connectionError, false);
            }

            @Override
            public Context getContext() {
                return CreateChannelActivity.this;
            }
        });
    }

    private void setChannel() {
        showLoading();
        channelService.getChannelInfo(channelId, new Client<Channel>() {
            @Override
            public void onResponseSuccess(Channel channel) {
                channelName.setText(channel.getName());
                description.setText(channel.getDescription());
                welcome.setText(channel.getWelcome());
                channelPrivacy.setChecked(channel.getIsPublic());
                hideLoading();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                Toast.makeText(getContext(), "No se pudo obtener la información del canal", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public Context getContext() {
                return CreateChannelActivity.this;
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
        channelRequest.organizationId = preferences.getSelectedOrganization();

        return channelRequest;
    }

    private Channel getChannel() {
        Channel channel = new Channel();
        channel.setId(channelId);
        channel.setName(channelName.getText().toString());
        channel.setDescription(description.getText().toString());
        channel.setIsPublic(channelPrivacy.isChecked());
        channel.setWelcome(welcome.getText().toString());
        return channel;
    }

    private void showError(boolean connectionError, boolean edit) {
        if (connectionError) {
            String textToShow = "No fue posible %1$s el canal. Intente más tarde.";
            if (edit) {
                textToShow = String.format(textToShow, "editar");
            } else {
                textToShow = String.format(textToShow, "crear");
            }
            Toast.makeText(this, textToShow, Toast.LENGTH_LONG).show();
        } else {
            channelName.setError("El nombre del canal ya existe");
            channelName.requestFocus();
        }
    }
}
