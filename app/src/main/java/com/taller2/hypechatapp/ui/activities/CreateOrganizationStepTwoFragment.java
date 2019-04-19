package com.taller2.hypechatapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.taller2.hypechatapp.network.model.OrganizationRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class CreateOrganizationStepTwoFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private static final int REQUEST_CODE = 2;
    public static final int RESULT_CODE = 400;

    private OnFinishButtonClickListener callback;
    private OrganizationRequest organizationRequest;
    private MapFragment mapFragment;
    private GoogleMap map;
    private static final int DEFAULT_ZOOM = 14;
    private View returnView;
    private TextView locationError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        returnView = inflater.inflate(R.layout.create_organization_step2, container, false);

        organizationRequest=(OrganizationRequest)getArguments().getSerializable("organizationRequest");

        setUpUI();
        return returnView;
    }


    private void setUpUI() {
        Toolbar toolbar = returnView.findViewById(R.id.toolbar_create_organization2);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.lite_map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.GONE);

        locationError = returnView.findViewById(R.id.location_error_text);

        MaterialButton chooseLocationButton=returnView.findViewById(R.id.pick_location_image_button);
        chooseLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        ChooseLocationActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        MaterialButton endButton=returnView.findViewById(R.id.new_organization_end_button);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText welcomeMessageInputText=returnView.findViewById(R.id.organization_welcome_input);

                if(!validateUserInput(welcomeMessageInputText))
                    return;

                organizationRequest.welcome=welcomeMessageInputText.getText().toString();

                callback.onFinishButtonClick(organizationRequest);

            }
        });
    }

    private boolean validateUserInput(TextInputEditText welcomeMessageInputText) {
        if(TextUtils.isEmpty(welcomeMessageInputText.getText().toString())){
            welcomeMessageInputText.setError("Ingrese el mensaje de bienvenida");
            return false;
        }

        if(organizationRequest.latitude == null || organizationRequest.longitude == null){
            locationError.setVisibility(View.VISIBLE);
            return false;
        } else {
            locationError.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setOnMapClickListener(this);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent intent = new Intent(getActivity(),
                ChooseLocationActivity.class);
        intent.putExtra("startLocation",
                new LatLng(organizationRequest.latitude,organizationRequest.longitude));
        startActivityForResult(intent,REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
                LatLng selectedLocation=(LatLng) data.getParcelableExtra("selectedLocation");
                organizationRequest.latitude=selectedLocation.latitude;
                organizationRequest.longitude=selectedLocation.longitude;

                map.clear();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        selectedLocation, DEFAULT_ZOOM));
                map.addMarker(new MarkerOptions().position(selectedLocation));

                mapFragment.getView().setVisibility(View.VISIBLE);

                MaterialButton chooseLocationButton=returnView.findViewById(R.id.pick_location_image_button);
                chooseLocationButton.setVisibility(View.INVISIBLE);

            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    public interface OnFinishButtonClickListener{
        void onFinishButtonClick(OrganizationRequest organizationRequest);
    }

    public void setOnFinishButtonClickListener(OnFinishButtonClickListener callback){
        this.callback=callback;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.lite_map);
        if(mapFragment!=null){
            getActivity().getFragmentManager().beginTransaction().remove(mapFragment).commit();
        }
    }
}
