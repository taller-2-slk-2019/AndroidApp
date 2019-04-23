package com.taller2.hypechatapp.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.ui.fragments.FailedInvitationsFragment;
import com.taller2.hypechatapp.ui.fragments.SendInvitationsFragment;

import java.util.ArrayList;
import java.util.List;

public class SendInvitationsActivity extends AppCompatActivity implements SendInvitationsFragment.OnSendButtonClickListener {

    private SendInvitationsFragment sendInvitationsFragment;
    private FailedInvitationsFragment failedInvitationsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);

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
