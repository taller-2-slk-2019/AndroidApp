package com.taller2.hypechatapp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.SendInvitationsAdapter;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.network.model.UserInvitationRequest;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.OrganizationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SendInvitationsFragment extends Fragment {

    private OnSendButtonClickListener callback;
    private View returnView;
    private RecyclerView emailsRecyclerView;
    private SendInvitationsAdapter sendInvitationsAdapter;
    private List<String> emailsList=new ArrayList<>();
    private MaterialButton sendInvitationsButton;
    private OrganizationService organizationService;
    private ProgressBar loadingView;
    private UserManagerPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        returnView=inflater.inflate(R.layout.fragment_send_invitations, container, false);

        //Add first line to the list
        if(emailsList.isEmpty()){
            emailsList.add("");
        }
        organizationService = new OrganizationService();
        preferences = new UserManagerPreferences(getActivity());

        setUpUI();
        return returnView;
    }

    private void setUpUI() {
        Toolbar toolbar = returnView.findViewById(R.id.toolbar_invitations);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        emailsRecyclerView = returnView.findViewById(R.id.invitations_email_list);
        sendInvitationsAdapter = new SendInvitationsAdapter(emailsList);
        emailsRecyclerView.setAdapter(sendInvitationsAdapter);

        emailsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        emailsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        sendInvitationsButton=returnView.findViewById(R.id.send_invitations_button);
        sendInvitationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInvitationRequest userInvitationRequest = buildUserInvitationsRequest();
                sendInvitations(userInvitationRequest);
            }
        });

        loadingView = returnView.findViewById(R.id.loading);
    }

    private void sendInvitations(UserInvitationRequest userInvitationRequest) {

        loadingView.setVisibility(View.VISIBLE);

        organizationService.inviteUsers(preferences.getSelectedOrganization(), userInvitationRequest, new Client<List<String>>() {
            @Override
            public void onResponseSuccess(List<String> failedInvitations) {
                loadingView.setVisibility(View.INVISIBLE);
                if (failedInvitations.isEmpty()){
                    Toast.makeText(getContext(), "Invitaciones Enviadas!!!", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
                    callback.onSendButtonClick(failedInvitations);
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
                getActivity().finish();
            }

            @Override
            public Context getContext() {
                return getActivity();
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

    public interface OnSendButtonClickListener{
        void onSendButtonClick(List<String> failedInvitations);

    }

    public void setOnSendButtonClickListener(OnSendButtonClickListener callback){
        this.callback=callback;
    }
}
