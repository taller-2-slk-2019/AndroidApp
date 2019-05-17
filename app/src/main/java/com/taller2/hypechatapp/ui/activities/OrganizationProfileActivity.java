package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.OrganizationService;
import com.taller2.hypechatapp.ui.activities.utils.ScreenDisablerHelper;

import androidx.appcompat.app.AppCompatActivity;

public class OrganizationProfileActivity extends AppCompatActivity {
    private OrganizationService organizationService;
    private TextView name, description, welcomeMessage;
    private ProgressBar loading;
    private ImageView profilePicture;
    private UserManagerPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_profile);
        organizationService = new OrganizationService();
        prefs = new UserManagerPreferences(this);

        setUpUI();
    }

    private void setUpUI() {

        name = findViewById(R.id.organizationProfileName);
        description = findViewById(R.id.organizationProfileDescription);
        welcomeMessage = findViewById(R.id.organizationProfileWelcome);
        profilePicture = findViewById(R.id.organizationProfileImage);
        loading = findViewById(R.id.loading);

        showLoading();
        organizationService.getOrganizationProfile(prefs.getSelectedOrganization(), new Client<Organization>() {

            @Override
            public void onResponseSuccess(Organization responseBody) {
                hideLoading();
                name.setText(responseBody.getName());
                description.setText(responseBody.getDescription());
                welcomeMessage.setText(responseBody.getWelcome());
                String url = responseBody.getPicture();
                PicassoLoader.load(getApplicationContext(), url, profilePicture);
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                String textToShow = "No fue posible obtener el perfil de la organización. Intente más tarde.";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                hideLoading();
            }

            @Override
            public Context getContext() {
                return OrganizationProfileActivity.this;
            }
        });
    }

    private void showLoading() {
        loading.setVisibility(View.VISIBLE);
        ScreenDisablerHelper.disableScreenTouch(getWindow());
    }

    private void hideLoading() {
        loading.setVisibility(View.GONE);
        ScreenDisablerHelper.enableScreenTouch(getWindow());
    }

}
