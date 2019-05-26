package com.taller2.hypechatapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Organization;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private boolean organizationAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        EventBus.getDefault().register(this);
        setUpUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (organizationAdded) {
            // if an organization was added outside this activity, go to chat
            Intent intent = new Intent(this, ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void setUpUI() {
        Button createOrganizationButton = findViewById(R.id.btn_create_organization);
        createOrganizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrganization();
            }
        });

        Button showInvitationsButton = findViewById(R.id.btn_show_invitations);
        showInvitationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInvitations();
            }
        });

        Button logOutButton = findViewById(R.id.btn_logout);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    private void createOrganization() {
        Intent intent = new Intent(this, CreateOrganizationActivity.class);
        startActivity(intent);
    }

    private void showInvitations() {
        Intent intent = new Intent(this, ReceivedInvitationsActivity.class);
        startActivity(intent);
    }

    private void logOut() {
        FirebaseAuthService.logOut(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrganizationEvent(Organization organization) {
        organizationAdded = true;
    }
}