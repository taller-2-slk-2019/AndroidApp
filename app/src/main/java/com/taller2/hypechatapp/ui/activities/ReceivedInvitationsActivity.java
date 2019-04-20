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
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.InvitationClickListener;
import com.taller2.hypechatapp.adapters.InvitationResponseListener;
import com.taller2.hypechatapp.adapters.ReceivedInvitationsAdapter;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.AcceptInvitationRequest;
import com.taller2.hypechatapp.network.model.ReceivedInvitation;
import com.taller2.hypechatapp.network.model.SuccessResponse;
import com.taller2.hypechatapp.services.OrganizationService;
import com.taller2.hypechatapp.services.UserService;

import java.util.List;

public class ReceivedInvitationsActivity extends AppCompatActivity implements InvitationClickListener {

    private ProgressBar loadingView;
    RecyclerView invitationsRecyclerView;
    ReceivedInvitationsAdapter receivedInvitationsAdapter;
    List<ReceivedInvitation> receivedInvitations;
    UserService userService;
    OrganizationService organizationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_invitations);
        userService=new UserService();
        organizationService=new OrganizationService();
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
        receivedInvitationsAdapter = new ReceivedInvitationsAdapter(receivedInvitations,this);
        invitationsRecyclerView.setAdapter(receivedInvitationsAdapter);
        invitationsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        invitationsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onAcceptClick(String token, final int adapterPosition, final InvitationResponseListener listener) {
        loadingView.setVisibility(View.VISIBLE);
        //Disable the screen, so no other invitation can be clicked
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        AcceptInvitationRequest acceptInvitationRequest=new AcceptInvitationRequest();
        acceptInvitationRequest.token=token;
        organizationService.acceptInvitation(acceptInvitationRequest, new Client<SuccessResponse>(){

            @Override
            public void onResponseSuccess(SuccessResponse responseBody) {
                loadingView.setVisibility(View.INVISIBLE);
                //Re-enable the screen
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                Toast.makeText(getContext(), "Invitación aceptada", Toast.LENGTH_LONG).show();
                listener.onInvitationResponseOK(adapterPosition);
            }

            @Override
            public void onResponseError(String errorMessage) {
                loadingView.setVisibility(View.INVISIBLE);
                //Re-enable the screen
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                Toast.makeText(getContext(), "Ocurrió un error al intentar aceptar la invitación." +
                        " Intente más tarde.", Toast.LENGTH_LONG).show();

            }

            @Override
            public Context getContext() {
                return ReceivedInvitationsActivity.this;
            }
        });
    }

    @Override
    public void onRejectClick(String token) {

    }
}
