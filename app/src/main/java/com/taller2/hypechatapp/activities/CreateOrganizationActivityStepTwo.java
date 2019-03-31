package com.taller2.hypechatapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.OrganizationService;
import com.taller2.hypechatapp.network.model.OrganizationRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CreateOrganizationActivityStepTwo extends AppCompatActivity {

    OrganizationRequest newOrganization;
    OrganizationService organizationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_organization_step2);


        setUpInitials();

        setUpUI();

    }

    private void setUpInitials() {
        newOrganization=(OrganizationRequest) getIntent().getSerializableExtra("newOrganization");
        organizationService=new OrganizationService();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_create_organization2);
        setSupportActionBar(toolbar);

        MaterialButton endButton=findViewById(R.id.new_organization_end_button);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText welcomeMessageInputText=findViewById(R.id.organization_welcome_input);

                if(!validateUserInput(welcomeMessageInputText))
                    return;
                /*TODO Choose a location*/
                newOrganization.setLongitude(10.0);
                newOrganization.setLatitude(20.0);

                /*TODO Set the user id*/
                newOrganization.setCreatorId(1);

                newOrganization.setWelcome(welcomeMessageInputText.getText().toString());

                createNewOrganization(newOrganization);
            }
        });

    }

    private void createNewOrganization(OrganizationRequest newOrganization) {
        organizationService.createOrganization(newOrganization, new Client<Organization>(){

            @Override
            public void onResponseSuccess(Organization responseBody) {
                Toast.makeText(getContext(), "Se ha creado la Organización con id:"+responseBody.getId(),
                        Toast.LENGTH_LONG).show();
                /* TODO Hacer que vuelva al menú principal o que salte a la pantalla de invitacion a usuarios*/
            }

            @Override
            public void onResponseError(String errorMessage) {

            }

            @Override
            public Context getContext() {
                return CreateOrganizationActivityStepTwo.this;
            }
        });
    }

    private boolean validateUserInput(TextInputEditText welcomeMessageInputText) {
        if(TextUtils.isEmpty(welcomeMessageInputText.getText().toString())){
            welcomeMessageInputText.setError("Ingrese el mensaje de bienvenida");
            return false;
        }
        return true;
    }
}
