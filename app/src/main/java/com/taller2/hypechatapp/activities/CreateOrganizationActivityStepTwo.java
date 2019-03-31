package com.taller2.hypechatapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.taller2.hypechatapp.network.OrganizationService;
import com.taller2.hypechatapp.network.model.OrganizationRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

public class CreateOrganizationActivityStepTwo extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

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
                /*TODO Choose a location*/
                //newOrganization.setLongitude(10.0);
                //newOrganization.setLatitude(20.0);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
                LatLng selectedLocation=(LatLng) data.getParcelableExtra("selectedLocation");
                newOrganization.setLatitude(selectedLocation.latitude);
                newOrganization.setLongitude(selectedLocation.longitude);

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
                new LatLng(newOrganization.getLatitude(),newOrganization.getLongitude()));
        startActivityForResult(intent,REQUEST_CODE);
    }
}
