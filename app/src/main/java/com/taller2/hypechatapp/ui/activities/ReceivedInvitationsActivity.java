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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.ReceivedInvitationsAdapter;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.ReceivedInvitation;
import com.taller2.hypechatapp.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class ReceivedInvitationsActivity extends AppCompatActivity {

    private ProgressBar loadingView;
    RecyclerView invitationsRecyclerView;
    ReceivedInvitationsAdapter receivedInvitationsAdapter;
    List<ReceivedInvitation> receivedInvitations;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_invitations);
        userService=new UserService();
        setUpUI();
        getUserInvitations();

    }

    private void getUserInvitations() {
        loadingView.setVisibility(View.VISIBLE);
        userService.getReceivedInvitations(new Client<List<ReceivedInvitation>>() {
            @Override
            public void onResponseSuccess(List<ReceivedInvitation> responseBody) {
                loadingView.setVisibility(View.INVISIBLE);
                receivedInvitations=responseBody;
                setUpRecyclerView();
            }

            @Override
            public void onResponseError(String errorMessage) {
                loadingView.setVisibility(View.INVISIBLE);
                String textToShow;
                if(!TextUtils.isEmpty(errorMessage)){
                    textToShow=errorMessage;
                } else {
                    textToShow="No fue posible obtener las invitaciones. Intente más tarde.";
                }
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public Context getContext() {
                return ReceivedInvitationsActivity.this;
            }
        });
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_received_invitations);
        setSupportActionBar(toolbar);

        loadingView = findViewById(R.id.loading);
    }

    private void setUpRecyclerView(){
        invitationsRecyclerView = findViewById(R.id.received_invitations_list);
        receivedInvitationsAdapter = new ReceivedInvitationsAdapter(receivedInvitations);
        invitationsRecyclerView.setAdapter(receivedInvitationsAdapter);
        invitationsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        invitationsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
