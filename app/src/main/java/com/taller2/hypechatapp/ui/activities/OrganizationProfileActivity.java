package com.taller2.hypechatapp.ui.activities;

import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.OrganizationService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class OrganizationProfileActivity extends AppCompatActivity {
    OrganizationService organizationService;
    TextView name, description, welcomeMessage;
    ProgressDialog dialog;
    ImageView profilePicture;

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
        welcomeMessage = findViewById(R.id.organization_welcome_message);
        profilePicture = findViewById(R.id.organization_picture);

        organizationService = new OrganizationService();

        int organizationId = 1; // TODO Reemplzar por el Id correspondiente a la organizacion

        organizationService.getOrganizationProfile(organizationId, new Client<Organization>() {

            @Override
            public void onResponseSuccess(Organization responseBody) {
                name.setText(responseBody.getName());
                description.setText(responseBody.getDescription());
                welcomeMessage.setText(responseBody.getWelcome());
                String url = responseBody.getPicture();
                PicassoLoader.load(getApplicationContext(), url, profilePicture);
                dialog.dismiss();
            }

            @Override
            public void onResponseError(String errorMessage) {
                //dialog.dismiss();
                //setAlertDialog();
                String textToShow;
                if(!TextUtils.isEmpty(errorMessage)){
                    textToShow=errorMessage;
                } else {
                    textToShow="No fue posible obtener el perfil de la organización. Intente más tarde.";
                }
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public Context getContext() { return OrganizationProfileActivity.this; }
        });
    }

    private void setAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Alerta");
        alertDialog.setMessage("Se produjo un error al obtener la información de la organización");
        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create().show();
    }
}
