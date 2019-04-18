package com.taller2.hypechatapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.InvitationsAdapter;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.UserInvitationRequest;
import com.taller2.hypechatapp.services.OrganizationService;

import java.util.ArrayList;
import java.util.List;

public class SendInvitationsActivity extends AppCompatActivity {

    RecyclerView emailsRecyclerView;
    InvitationsAdapter invitationsAdapter;
    List<String> emailsList=new ArrayList<>();
    private MaterialButton sendInvitationsButton;
    private OrganizationService organizationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);

        //Add first line to the list
        emailsList.add("");
        organizationService = new OrganizationService();


        setUpUI();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_invitations);
        setSupportActionBar(toolbar);

        emailsRecyclerView = findViewById(R.id.invitations_email_list);
        invitationsAdapter = new InvitationsAdapter(emailsList);
        emailsRecyclerView.setAdapter(invitationsAdapter);

        emailsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        emailsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        sendInvitationsButton=findViewById(R.id.send_invitations_button);
        sendInvitationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<UserInvitationRequest> userInvitationsList = buildUserInvitationsList();
                sendInvitations(userInvitationsList);
            }
        });

    }

    private void sendInvitations(List<UserInvitationRequest> userInvitationsList) {
        //TODO reemplazar con el id correcto de la organizacion
        organizationService.inviteUsers(1, userInvitationsList, new Client() {
            @Override
            public void onResponseSuccess(Object responseBody) {

            }

            @Override
            public void onResponseError(String errorMessage) {

            }

            @Override
            public Context getContext() {
                return SendInvitationsActivity.this;
            }
        });
    }

    private List<UserInvitationRequest> buildUserInvitationsList() {
        List<UserInvitationRequest> userInvitations=new ArrayList<>();
        for (String email:emailsList) {
            if(!TextUtils.isEmpty(email)){
                UserInvitationRequest userInvitationRequest = new UserInvitationRequest();
                userInvitationRequest.setUserEmail(email);
                userInvitations.add(userInvitationRequest);
            }

        }
        return userInvitations;
    }
}
