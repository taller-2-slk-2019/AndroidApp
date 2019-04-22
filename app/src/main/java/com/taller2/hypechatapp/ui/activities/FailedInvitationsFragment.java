package com.taller2.hypechatapp.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.FailedInvitationsAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FailedInvitationsFragment extends Fragment {

    View returnView;
    RecyclerView failedInvitationsRecyclerView;
    List<String> failedInvitations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        returnView=inflater.inflate(R.layout.fragment_failed_invitations, container, false);

        failedInvitations=getArguments().getStringArrayList("failedInvitations");

        setUpUI();
        return returnView;
    }

    private void setUpUI() {
        Toolbar toolbar = returnView.findViewById(R.id.toolbar_invitations_error);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        failedInvitationsRecyclerView = returnView.findViewById(R.id.failed_invitations_list);

        FailedInvitationsAdapter adapter = new FailedInvitationsAdapter(failedInvitations);

        failedInvitationsRecyclerView.setAdapter(adapter);

        failedInvitationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        failedInvitationsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
