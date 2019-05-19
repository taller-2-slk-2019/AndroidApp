package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.ImagePicker;
import com.taller2.hypechatapp.components.LocationPicker;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.firebase.FirebaseStorageService;
import com.taller2.hypechatapp.firebase.FirebaseStorageUploadInterface;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.OrganizationService;
import com.taller2.hypechatapp.ui.activities.utils.ScreenDisablerHelper;
import com.taller2.hypechatapp.ui.listeners.OnViewTouchListener;

import androidx.appcompat.app.AppCompatActivity;

public class EditOrganizationActivity extends AppCompatActivity implements FirebaseStorageUploadInterface {
    private OrganizationService organizationService;
    private TextView name, description, welcome;
    private FloatingActionButton editImage;
    private ImageView profilePicture;
    private Button updateOrganizationBtn;
    private ImagePicker imagePicker;
    private LocationPicker locationPicker;
    private UserManagerPreferences prefs;
    private Uri imagePath;
    private ProgressBar loading;
    private Organization organization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_organization);
        findViewById(R.id.layoutContainer).setOnTouchListener(new OnViewTouchListener());

        organizationService = new OrganizationService();
        prefs = new UserManagerPreferences(this);

        setUpUI();
    }

    private void setUpUI() {
        loading = findViewById(R.id.loading);

        name = findViewById(R.id.editOrganizationName);
        description = findViewById(R.id.editOrganizationDescription);
        welcome = findViewById(R.id.editOrganizationWelcome);
        profilePicture = findViewById(R.id.editOrganizationImage);
        editImage = findViewById(R.id.editOrganizationEditImageButton);

        imagePicker = new ImagePicker(this, editImage, profilePicture, null);
        updateOrganizationBtn = findViewById(R.id.editOrganizationUpdateButton);
        updateOrganizationBtn.setEnabled(false);

        updateOrganizationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidForm()) {
                    showLoading();
                    if (imagePath != null) {
                        uploadProfileImage();
                    } else {
                        updateOrganization();
                    }
                }
            }
        });

        organizationService.getOrganizationProfile(prefs.getSelectedOrganization(), new Client<Organization>() {
            @Override
            public void onResponseSuccess(Organization org) {
                organization = org;
                name.setText(org.getName());
                description.setText(org.getDescription());
                welcome.setText(org.getWelcome());
                String imageUrl = org.getPicture();
                PicassoLoader.load(getApplicationContext(), String.format("%s?type=large", imageUrl), R.drawable.default_user, profilePicture);

                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.lite_map);
                MaterialButton chooseLocationButton = findViewById(R.id.pick_location_image_button);
                locationPicker = new LocationPicker(EditOrganizationActivity.this,
                        mapFragment, chooseLocationButton, org.getLatitude(), org.getLongitude());

                updateOrganizationBtn.setEnabled(false);
                hideLoading();

                setChangesDetectors();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                String textToShow = "No fue posible obtener el perfil de la organización. Intente más tarde.";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public Context getContext() {
                return EditOrganizationActivity.this;
            }
        });
    }

    private boolean isValidForm() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Ingrese un nombre de la organización");
            name.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(description.getText().toString())) {
            description.setError("Ingrese una descripción");
            description.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(welcome.getText().toString())) {
            welcome.setError("Ingrese un mensaje de bienvenida");
            welcome.requestFocus();
            return false;
        }

        return true;
    }

    private void updateOrganization() {
        organization.setName(name.getText().toString());
        organization.setDescription(description.getText().toString());
        organization.setWelcome(welcome.getText().toString());

        organizationService.updateOrganization(prefs.getSelectedOrganization(),
                organization, new Client<Void>() {
                    @Override
                    public void onResponseSuccess(Void nothing) {
                        hideLoading();
                        updateOrganizationBtn.setEnabled(false);
                        imagePath = null;
                        setChangesDetectors();
                        Toast.makeText(getContext(), "Organización actualizada!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponseError(boolean connectionError, String errorMessage) {
                        Toast.makeText(getContext(), "Error al actualizar la organización", Toast.LENGTH_LONG).show();
                        hideLoading();
                    }

                    @Override
                    public Context getContext() {
                        return EditOrganizationActivity.this;
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imagePath = imagePicker.analyzeResult(this, data);
            updateOrganizationBtn.setEnabled(true);
            imagePicker.showPicker();
        } else if (requestCode == LocationPicker.LOCATION_PICKER_REQUEST_CODE &&
                resultCode == LocationPicker.LOCATION_PICKER_RESULT_CODE) {
            LatLng location = locationPicker.analyzeResults(data);
            organization.setLatitude(location.latitude);
            organization.setLongitude(location.longitude);
            updateOrganizationBtn.setEnabled(true);
        }
    }

    private void uploadProfileImage() {
        FirebaseStorageService storage = new FirebaseStorageService();
        storage.uploadLocalImage(this, imagePath);
    }

    @Override
    public void onFileUploaded(String downloadUrl, String type) {
        organization.setPicture(downloadUrl);
        updateOrganization();
    }

    @Override
    public void onFileUploadError(Exception exception) {
        Toast.makeText(this, "Error subiendo la imágen", Toast.LENGTH_LONG).show();
    }

    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
        ScreenDisablerHelper.disableScreenTouch(getWindow());
    }

    public void hideLoading() {
        loading.setVisibility(View.GONE);
        ScreenDisablerHelper.enableScreenTouch(getWindow());
    }

    public void setChangesDetectors() {
        name.addTextChangedListener(new ChangesDetection(organization.getName()));
        description.addTextChangedListener(new ChangesDetection(organization.getDescription()));
        welcome.addTextChangedListener(new ChangesDetection(organization.getWelcome()));
    }

    public class ChangesDetection implements TextWatcher {
        private final String original;

        public ChangesDetection(String original) {
            this.original = original;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals(original)) {
                updateOrganizationBtn.setEnabled(true);
            }
        }
    }
}
