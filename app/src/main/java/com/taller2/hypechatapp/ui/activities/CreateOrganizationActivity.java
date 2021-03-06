package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.firebase.FirebaseStorageService;
import com.taller2.hypechatapp.firebase.FirebaseStorageUploadInterface;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.OrganizationService;
import com.taller2.hypechatapp.ui.activities.utils.ScreenDisablerHelper;
import com.taller2.hypechatapp.ui.fragments.CreateOrganizationStepOneFragment;
import com.taller2.hypechatapp.ui.fragments.CreateOrganizationStepTwoFragment;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class CreateOrganizationActivity extends BaseActivity implements
        CreateOrganizationStepOneFragment.OnNextButtonClickListener,
        CreateOrganizationStepTwoFragment.OnFinishButtonClickListener, FirebaseStorageUploadInterface {

    private OrganizationRequest organizationRequest;
    private OrganizationService organizationService;
    private Uri filePath = null;
    private CreateOrganizationStepOneFragment createOrganizationStepOneFragment;
    private CreateOrganizationStepTwoFragment createOrganizationStepTwoFragment;
    private UserManagerPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);
        preferences = new UserManagerPreferences(this);
        setUpFragment(savedInstanceState);
        setUpUI();
        setUpInitials();
    }

    private void setUpUI() {
        loading = findViewById(R.id.loading);
    }

    private void setUpInitials() {
        organizationService = new OrganizationService();
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
            createOrganizationStepOneFragment = new CreateOrganizationStepOneFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            organizationRequest = new OrganizationRequest();
            getIntent().putExtra("organizationRequest", organizationRequest);
            createOrganizationStepOneFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_frame_layout, createOrganizationStepOneFragment).commit();
        }
    }


    @Override
    public void onNextButtonClick(OrganizationRequest organizationRequest, Uri filePath) {
        this.filePath = filePath;
        this.organizationRequest = organizationRequest;
        // Create fragment and give it an argument for the selected article
        createOrganizationStepTwoFragment = new CreateOrganizationStepTwoFragment();
        getIntent().putExtra("organizationRequest", this.organizationRequest);
        createOrganizationStepTwoFragment.setArguments(getIntent().getExtras());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container_frame_layout, createOrganizationStepTwoFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof CreateOrganizationStepOneFragment) {
            CreateOrganizationStepOneFragment createOrganizationStepOneFragment = (CreateOrganizationStepOneFragment) fragment;
            createOrganizationStepOneFragment.setOnNextButtonClickListener(this);
        }

        if (fragment instanceof CreateOrganizationStepTwoFragment) {
            CreateOrganizationStepTwoFragment createOrganizationStepTwoFragment = (CreateOrganizationStepTwoFragment) fragment;
            createOrganizationStepTwoFragment.setOnFinishButtonClickListener(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Fix to call the fragment on the activity result of picking an image
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                f.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onFinishButtonClick(OrganizationRequest organizationRequest) {

        showLoading();

        this.organizationRequest = organizationRequest;

        //First, we want to upload the image to Firebase, then we can create the organization
        uploadProfileImage();

    }

    private void createNewOrganization(OrganizationRequest newOrganization) {

        organizationService.createOrganization(newOrganization, new Client<Organization>() {

            @Override
            public void onResponseSuccess(Organization organization) {
                hideLoading();
                Toast.makeText(getContext(), "Woow! Organización creada", Toast.LENGTH_LONG).show();
                preferences.saveSelectedOrganization(organization.getId());
                Intent intent = new Intent(CreateOrganizationActivity.this, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                hideLoading();
                String textToShow = "No fue posible crear una organización. Intente más tarde.";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return CreateOrganizationActivity.this;
            }
        });
    }

    @Override
    public void onFileUploaded(String downloadUrl, String type) {
        organizationRequest.picture = downloadUrl;

        //Once we have the picture uploaded we can call the organization creation service
        createNewOrganization(this.organizationRequest);
    }

    @Override
    public void onFileUploadError(Exception exception) {
        Toast.makeText(CreateOrganizationActivity.this, "No fue posible crear una organización. Intente más tarde.", Toast.LENGTH_LONG).show();
        ScreenDisablerHelper.enableScreenTouch(getWindow());
        finish();
    }

    private void uploadProfileImage() {
        FirebaseStorageService storage = new FirebaseStorageService();
        storage.uploadLocalImage(this, filePath);
    }

    @Override
    public void onBackPressed() {
        filePath = null;
        super.onBackPressed();
    }
}
