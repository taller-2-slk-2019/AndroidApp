package com.taller2.hypechatapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.OrganizationService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class OrganizationProfileActivity extends AppCompatActivity {
    OrganizationService organizationService;
    TextView name, description, welcome_message;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_profile);

        setUpUI();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_organization_profile);
        setSupportActionBar(toolbar);

        dialog = new ProgressDialog(OrganizationProfileActivity.this);
        dialog.setMessage("Cargando...");
        dialog.show();

        name = findViewById(R.id.organization_name);
        description = findViewById(R.id.organization_description);
        welcome_message = findViewById(R.id.organization_welcome_message);

        organizationService = new OrganizationService();

        int organizationId = 1; // TODO Reemplzar por el Id correspondiente a la organizacion

        organizationService.getOrganization(organizationId, new Client<Organization>() {

            @Override
            public void onResponseSuccess(Organization responseBody) {
                name.setText(responseBody.getName());
                description.setText(responseBody.getDescription());
                welcome_message.setText(responseBody.getWelcome());
                dialog.dismiss();
            }

            @Override
            public void onResponseError(String errorMessage) {

            }

            @Override
            public Context getContext() { return OrganizationProfileActivity.this; }
        });
    }
}
