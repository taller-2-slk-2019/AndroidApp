package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.InvitationClickListener;
import com.taller2.hypechatapp.adapters.InvitationResponseListener;
import com.taller2.hypechatapp.adapters.ReceivedInvitationsAdapter;
import com.taller2.hypechatapp.model.ReceivedInvitation;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.AcceptInvitationRequest;
import com.taller2.hypechatapp.services.OrganizationService;
import com.taller2.hypechatapp.services.UserService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReceivedInvitationsActivity extends BaseActivity implements InvitationClickListener {

    List<ReceivedInvitation> receivedInvitations;
    UserService userService;
    OrganizationService organizationService;

    RecyclerView invitationsRecyclerView;
    ReceivedInvitationsAdapter receivedInvitationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_invitations);
        userService = new UserService();
        organizationService = new OrganizationService();
        setUpUI();
        getUserInvitations();

    }

    private void getUserInvitations() {
        showLoading();
        userService.getReceivedInvitations(new Client<List<ReceivedInvitation>>() {
            @Override
            public void onResponseSuccess(List<ReceivedInvitation> responseBody) {
                hideLoading();
                receivedInvitations = responseBody;
                setUpRecyclerView();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                hideLoading();
                String textToShow = "No fue posible obtener las invitaciones. Intente más tarde.";
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

        loading = findViewById(R.id.loading);
    }

    private void setUpRecyclerView() {
        invitationsRecyclerView = findViewById(R.id.received_invitations_list);
        receivedInvitationsAdapter = new ReceivedInvitationsAdapter(receivedInvitations, this, getApplicationContext());
        invitationsRecyclerView.setAdapter(receivedInvitationsAdapter);
        invitationsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        invitationsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onAcceptClick(final ReceivedInvitation receivedInvitation, final int adapterPosition, final InvitationResponseListener listener) {
        showLoading();

        AcceptInvitationRequest acceptInvitationRequest = new AcceptInvitationRequest();
        acceptInvitationRequest.token = receivedInvitation.token;
        organizationService.acceptInvitation(acceptInvitationRequest, new Client<Void>() {

            @Override
            public void onResponseSuccess(Void responseBody) {
                hideLoading();
                EventBus.getDefault().post(receivedInvitation.organization);

                Toast.makeText(getContext(), "Invitación aceptada", Toast.LENGTH_LONG).show();
                listener.onInvitationResponse(adapterPosition);
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                hideLoading();

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
    public void onRejectClick(String token, final int adapterPosition, final InvitationResponseListener listener) {
        showLoading();

        userService.rejectInvitation(token, new Client<Void>() {

            @Override
            public void onResponseSuccess(Void responseBody) {
                hideLoading();

                Toast.makeText(getContext(), "Invitación rechazada", Toast.LENGTH_LONG).show();
                listener.onInvitationResponse(adapterPosition);
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                hideLoading();

                Toast.makeText(getContext(), "Ocurrió un error al intentar rechazar la invitación." +
                        " Intente más tarde.", Toast.LENGTH_LONG).show();

            }

            @Override
            public Context getContext() {
                return ReceivedInvitationsActivity.this;
            }
        });
    }

}
