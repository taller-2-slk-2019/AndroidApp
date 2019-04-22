package com.taller2.hypechatapp.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.network.model.UserInvitationRequest;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.OrganizationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SendInvitationsActivity extends AppCompatActivity implements SendInvitationsFragment.OnSendButtonClickListener {

    private SendInvitationsFragment sendInvitationsFragment;
    private FailedInvitationsFragment failedInvitationsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);

        //Add first line to the list
        //emailsList.add("");
        //organizationService = new OrganizationService();
        //preferences = new UserManagerPreferences(this);

        setUpFragment(savedInstanceState);
    }

    private void setUpFragment(@Nullable Bundle savedInstanceState) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container_frame_layout) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            sendInvitationsFragment = new SendInvitationsFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            sendInvitationsFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_frame_layout, sendInvitationsFragment).commit();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof SendInvitationsFragment){
            SendInvitationsFragment sendInvitationsFragment=(SendInvitationsFragment) fragment;
            sendInvitationsFragment.setOnSendButtonClickListener(this);
        }

    }

    @Override
    public void onSendButtonClick(List<String> failedInvitations) {

        failedInvitationsFragment=new FailedInvitationsFragment();
        getIntent().putStringArrayListExtra("failedInvitations", new ArrayList<>(failedInvitations));
        failedInvitationsFragment.setArguments(getIntent().getExtras());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container_frame_layout,failedInvitationsFragment );
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

}
