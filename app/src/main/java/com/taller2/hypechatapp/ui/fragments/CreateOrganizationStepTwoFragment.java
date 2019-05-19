package com.taller2.hypechatapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.LocationPicker;
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.ui.listeners.OnViewTouchListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class CreateOrganizationStepTwoFragment extends Fragment {

    private OnFinishButtonClickListener callback;
    private OrganizationRequest organizationRequest;
    private View returnView;
    private TextView locationError;
    private LocationPicker locationPicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        returnView = inflater.inflate(R.layout.create_organization_step2, container, false);
        returnView.findViewById(R.id.layoutContainer).setOnTouchListener(new OnViewTouchListener());

        organizationRequest = (OrganizationRequest) getArguments().getSerializable("organizationRequest");

        setUpUI();
        return returnView;
    }


    private void setUpUI() {
        Toolbar toolbar = returnView.findViewById(R.id.toolbar_create_organization2);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.lite_map);
        MaterialButton chooseLocationButton = returnView.findViewById(R.id.pick_location_image_button);
        locationPicker = new LocationPicker(getActivity(), mapFragment, chooseLocationButton);

        locationError = returnView.findViewById(R.id.location_error_text);

        MaterialButton endButton = returnView.findViewById(R.id.new_organization_end_button);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText welcomeMessageInputText = returnView.findViewById(R.id.organization_welcome_input);

                if (!validateUserInput(welcomeMessageInputText))
                    return;

                organizationRequest.welcome = welcomeMessageInputText.getText().toString();

                callback.onFinishButtonClick(organizationRequest);

            }
        });
    }

    private boolean validateUserInput(TextInputEditText welcomeMessageInputText) {
        if (TextUtils.isEmpty(welcomeMessageInputText.getText().toString())) {
            welcomeMessageInputText.setError("Ingrese el mensaje de bienvenida");
            return false;
        }

        if (organizationRequest.latitude == null || organizationRequest.longitude == null) {
            locationError.setVisibility(View.VISIBLE);
            return false;
        } else {
            locationError.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == LocationPicker.LOCATION_PICKER_REQUEST_CODE &&
                    resultCode == LocationPicker.LOCATION_PICKER_RESULT_CODE) {
                LatLng selectedLocation = locationPicker.analyzeResults(data);
                organizationRequest.latitude = selectedLocation.latitude;
                organizationRequest.longitude = selectedLocation.longitude;
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    public interface OnFinishButtonClickListener {
        void onFinishButtonClick(OrganizationRequest organizationRequest);
    }

    public void setOnFinishButtonClickListener(OnFinishButtonClickListener callback) {
        this.callback = callback;
    }
}
