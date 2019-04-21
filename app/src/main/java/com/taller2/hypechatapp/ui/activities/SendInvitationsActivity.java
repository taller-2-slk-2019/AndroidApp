package com.taller2.hypechatapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.SendInvitationsAdapter;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.UserInvitationRequest;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.OrganizationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SendInvitationsActivity extends AppCompatActivity {

    RecyclerView emailsRecyclerView;
    SendInvitationsAdapter sendInvitationsAdapter;
    List<String> emailsList=new ArrayList<>();
    private MaterialButton sendInvitationsButton;
    private OrganizationService organizationService;
    private ProgressBar loadingView;
    private UserManagerPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invitations);

        //Add first line to the list
        emailsList.add("");
        organizationService = new OrganizationService();
        preferences = new UserManagerPreferences(this);



        setUpUI();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_invitations);
        setSupportActionBar(toolbar);

        emailsRecyclerView = findViewById(R.id.invitations_email_list);
        sendInvitationsAdapter = new SendInvitationsAdapter(emailsList);
        emailsRecyclerView.setAdapter(sendInvitationsAdapter);

        emailsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        emailsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        sendInvitationsButton=findViewById(R.id.send_invitations_button);
        sendInvitationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInvitationRequest userInvitationRequest = buildUserInvitationsRequest();
                sendInvitations(userInvitationRequest);
            }
        });

        loadingView = findViewById(R.id.loading);

    }

    private void sendInvitations(UserInvitationRequest userInvitationRequest) {

        loadingView.setVisibility(View.VISIBLE);

        organizationService.inviteUsers(preferences.getSelectedOrganization(), userInvitationRequest, new Client<List<String>>() {
            @Override
            public void onResponseSuccess(List<String> rejectedInvitations) {
                loadingView.setVisibility(View.INVISIBLE);
                if (rejectedInvitations.size()>0){
                    //TODO Show the rejected invitations to the user
                } else {
                    Toast.makeText(getContext(), "Invitaciones Enviadas!!!", Toast.LENGTH_LONG).show();
                    finish();
                }

            }

            @Override
            public void onResponseError(String errorMessage) {
                loadingView.setVisibility(View.INVISIBLE);
                String textToShow;
                if(!TextUtils.isEmpty(errorMessage)){
                    textToShow=errorMessage;
                } else {
                    textToShow="No fue posible enviar las invitaciones a los usuarios indicados. Intente más tarde.";
                }
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public Context getContext() {
                return SendInvitationsActivity.this;
            }
        });
    }

    private UserInvitationRequest buildUserInvitationsRequest() {
        UserInvitationRequest userInvitationRequest = new UserInvitationRequest();
        List<String> userEmails=new ArrayList<>();
        userEmails.addAll(emailsList);
        userEmails.removeAll(Collections.singleton(""));
        userEmails.removeAll(Collections.singleton(null));

        userInvitationRequest.userEmails=userEmails;
        return userInvitationRequest;
    }
}
