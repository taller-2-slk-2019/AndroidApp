package com.taller2.hypechatapp.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.ImagePicker;
import com.taller2.hypechatapp.network.model.OrganizationRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.content.Intent.FLAG_ACTIVITY_FORWARD_RESULT;

public class CreateOrganizationActivityStepOne extends AppCompatActivity {

    private Uri filePath;
    private ImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_organization_step1);
        imagePicker = new ImagePicker();

        setUpUI();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_create_organization);
        setSupportActionBar(toolbar);

        MaterialButton nextButton=findViewById(R.id.new_organization_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            TextInputEditText nameInputText = findViewById(R.id.organization_name_input);
            TextInputEditText descriptionInputText = findViewById(R.id.organization_description_input);

            if (!validateUserInput(nameInputText, descriptionInputText))
                return;

            OrganizationRequest newOrganization = createNewOrganizationRequest(nameInputText, descriptionInputText);

            Intent intent = new Intent(CreateOrganizationActivityStepOne.this, CreateOrganizationActivityStepTwo.class);
            intent.putExtra("newOrganization",newOrganization);
            intent.addFlags(FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
            finish();
            }
        });

        MaterialButton pickImageButton = findViewById(R.id.pick_profile_image_button);
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        ImageView pickImageView = findViewById(R.id.profile_image_view);
        pickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private OrganizationRequest createNewOrganizationRequest(TextInputEditText nameInputText, TextInputEditText descriptionInputText) {
        OrganizationRequest newOrganization = new OrganizationRequest();
        newOrganization.name=nameInputText.getText().toString();
        newOrganization.description=descriptionInputText.getText().toString();
        if (filePath != null) {
            newOrganization.picture=filePath.getPath();
        }
        return newOrganization;
    }

    private boolean validateUserInput(TextInputEditText nameInputText, TextInputEditText descriptionInputText) {
        if(TextUtils.isEmpty(nameInputText.getText().toString())){
            nameInputText.setError("Ingrese el nombre de la organización");
            return false;
        }

        if(TextUtils.isEmpty(descriptionInputText.getText().toString())){
            descriptionInputText.setError("Ingrese una descripción para la organización");
            return false;
        }
        return true;
    }

    private void chooseImage() {
        startActivityForResult(imagePicker.getImagePickerIntent(), imagePicker.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == imagePicker.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = imagePicker.analyzeResult(this, data);
        }
    }
}
