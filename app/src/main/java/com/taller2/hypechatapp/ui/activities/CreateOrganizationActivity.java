package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.firebase.FirebaseStorageService;
import com.taller2.hypechatapp.firebase.FirebaseStorageUploadInterface;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.services.OrganizationService;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class CreateOrganizationActivity extends AppCompatActivity implements
        CreateOrganizationStepOneFragment.OnNextButtonClickListener,
        CreateOrganizationStepTwoFragment.OnFinishButtonClickListener, FirebaseStorageUploadInterface {

    public static final int PICK_IMAGE_REQUEST = 71;
    private OrganizationRequest organizationRequest;
    private OrganizationService organizationService;
    private Uri filePath = null;
    private CreateOrganizationStepOneFragment createOrganizationStepOneFragment;
    private CreateOrganizationStepTwoFragment createOrganizationStepTwoFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_organization);
        setUpFragment(savedInstanceState);

        setUpInitials();
    }

    private void setUpInitials() {
        organizationService=new OrganizationService();
    }

    private void setUpFragment(@Nullable Bundle savedInstanceState) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

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
            organizationRequest=new OrganizationRequest();
            getIntent().putExtra("organizationRequest",organizationRequest);
            createOrganizationStepOneFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, createOrganizationStepOneFragment).commit();
        }
    }


    @Override
    public void onNextButtonClick(OrganizationRequest organizationRequest) {
        if(filePath==null){
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for(Fragment fragment:fragments){
                if(fragment instanceof CreateOrganizationStepOneFragment){
                    CreateOrganizationStepOneFragment createOrganizationStepOneFragment =
                            (CreateOrganizationStepOneFragment) fragment;

                    createOrganizationStepOneFragment.setImageNotSelectedError();
                    return;
                }
            }
        }
        this.organizationRequest=organizationRequest;
        // Create fragment and give it an argument for the selected article
        createOrganizationStepTwoFragment = new CreateOrganizationStepTwoFragment();
        getIntent().putExtra("organizationRequest", this.organizationRequest);
        createOrganizationStepTwoFragment.setArguments(getIntent().getExtras());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, createOrganizationStepTwoFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof CreateOrganizationStepOneFragment){
            CreateOrganizationStepOneFragment createOrganizationStepOneFragment=(CreateOrganizationStepOneFragment) fragment;
            createOrganizationStepOneFragment.setOnNextButtonClickListener(this);
        }

        if(fragment instanceof CreateOrganizationStepTwoFragment){
            CreateOrganizationStepTwoFragment createOrganizationStepTwoFragment=(CreateOrganizationStepTwoFragment) fragment;
            createOrganizationStepTwoFragment.setOnFinishButtonClickListener(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = analyzeResult(data);
        }

    }

    public Uri analyzeResult(Intent data) {
        Uri filePath = data.getData();
        try {
            Bitmap profileImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for(Fragment fragment:fragments){
                if(fragment instanceof CreateOrganizationStepOneFragment){
                    CreateOrganizationStepOneFragment createOrganizationStepOneFragment =
                            (CreateOrganizationStepOneFragment) fragment;

                    createOrganizationStepOneFragment.setImageBitmap(profileImageBitmap);
                }
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return filePath;
    }

    @Override
    public void onFinishButtonClick(OrganizationRequest organizationRequest) {

        ProgressBar loadingView = findViewById(R.id.loading_create_orga);
        loadingView.setVisibility(View.VISIBLE);

        this.organizationRequest=organizationRequest;

        //First, we want to upload the image to Firebase, then we can create the organization
        uploadProfileImage();

    }

    private void createNewOrganization(OrganizationRequest newOrganization) {

        organizationService.createOrganization(newOrganization, new Client<Organization>(){

            @Override
            public void onResponseSuccess(Organization organization) {
                ProgressBar loadingView = findViewById(R.id.loading_create_orga);
                loadingView.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Woow! Organización creada con el id: " + organization.getId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateOrganizationActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onResponseError(String errorMessage) {
                ProgressBar loadingView = findViewById(R.id.loading_create_orga);
                loadingView.setVisibility(View.INVISIBLE);
                String textToShow;
                if(!TextUtils.isEmpty(errorMessage)){
                    textToShow=errorMessage;
                } else {
                    textToShow="No fue posible crear una organización. Intente más tarde.";
                }
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public Context getContext() {
                return CreateOrganizationActivity.this;
            }
        });
    }

    @Override
    public void onFileUploaded(String downloadUrl) {
        organizationRequest.picture = downloadUrl;

        //Once we have the picture uploaded we can call the organization creation service
        createNewOrganization(this.organizationRequest);
    }

    @Override
    public void onFileUploadError(Exception exception) {
        Toast.makeText(CreateOrganizationActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
        finish();
    }

    private void uploadProfileImage(){
        FirebaseStorageService storage = new FirebaseStorageService();
        storage.uploadLocalFile(this, filePath);
    }

    @Override
    public void onBackPressed() {
        filePath=null;
        super.onBackPressed();
    }
}
