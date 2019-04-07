package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.services.OrganizationService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CreateOrganizationActivityStepTwo extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final int STEP_CODE = 300;
    OrganizationRequest newOrganization;
    OrganizationService organizationService;

    private static final int REQUEST_CODE = 2;
    public static final int RESULT_CODE = 400;

    private MapFragment mapFragment;
    private GoogleMap map;
    private static final int DEFAULT_ZOOM = 14;

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

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.lite_map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.GONE);


        MaterialButton chooseLocationButton=findViewById(R.id.pick_location_image_button);
        chooseLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrganizationActivityStepTwo.this,
                        ChooseLocationActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        MaterialButton endButton=findViewById(R.id.new_organization_end_button);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText welcomeMessageInputText=findViewById(R.id.organization_welcome_input);

                if(!validateUserInput(welcomeMessageInputText))
                    return;

                newOrganization.welcome=welcomeMessageInputText.getText().toString();

                ProgressBar loadingView = findViewById(R.id.loading_create_orga_step2);
                loadingView.setVisibility(View.VISIBLE);

                createNewOrganization(newOrganization);
            }
        });

    }

    private void createNewOrganization(OrganizationRequest newOrganization) {
        ProgressBar loadingView = findViewById(R.id.loading_create_orga_step2);
        loadingView.setVisibility(View.INVISIBLE);

        organizationService.createOrganization(newOrganization, new Client<Organization>(){

            @Override
            public void onResponseSuccess(Organization organization) {
                ProgressBar loadingView = findViewById(R.id.loading_create_orga_step2);
                loadingView.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Woow! Organización creada con el id: " + organization.getId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateOrganizationActivityStepTwo.this, ChannelChatActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onResponseError(String errorMessage) {
                ProgressBar loadingView = findViewById(R.id.loading_create_orga_step2);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
                LatLng selectedLocation=(LatLng) data.getParcelableExtra("selectedLocation");
                newOrganization.latitude=selectedLocation.latitude;
                newOrganization.longitude=selectedLocation.longitude;

                map.clear();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        selectedLocation, DEFAULT_ZOOM));
                map.addMarker(new MarkerOptions().position(selectedLocation));

                mapFragment.getView().setVisibility(View.VISIBLE);

                MaterialButton chooseLocationButton=findViewById(R.id.pick_location_image_button);
                chooseLocationButton.setVisibility(View.INVISIBLE);

            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setOnMapClickListener(this);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent intent = new Intent(CreateOrganizationActivityStepTwo.this,
                ChooseLocationActivity.class);
        intent.putExtra("startLocation",
                new LatLng(newOrganization.latitude,newOrganization.longitude));
        startActivityForResult(intent,REQUEST_CODE);
    }
}
