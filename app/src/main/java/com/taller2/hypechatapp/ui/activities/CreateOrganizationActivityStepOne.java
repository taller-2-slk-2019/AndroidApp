package com.taller2.hypechatapp.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.network.model.OrganizationRequest;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.content.Intent.FLAG_ACTIVITY_FORWARD_RESULT;

public class CreateOrganizationActivityStepOne extends AppCompatActivity {

    private static final int REQUEST_CODE = 2;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private Bitmap profileImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_organization_step1);

        setUpUI();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_create_organization);
        setSupportActionBar(toolbar);

        MaterialButton nextButton=findViewById(R.id.new_organization_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            TextInputEditText nameInputText=findViewById(R.id.organization_name_input);
            TextInputEditText descriptionInputText=findViewById(R.id.organization_description_input);

            if (!validateUserInput(nameInputText, descriptionInputText))
                return;

            OrganizationRequest newOrganization=createNewOrganizationRequest(nameInputText, descriptionInputText);

            Intent intent=new Intent(CreateOrganizationActivityStepOne.this, CreateOrganizationActivityStepTwo.class);
            intent.putExtra("newOrganization",newOrganization);
            intent.addFlags(FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
            finish();
            }
        });

        MaterialButton pickImageButton=findViewById(R.id.pick_profile_image_button);
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        ImageView pickImageView=findViewById(R.id.profile_image_view);
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                profileImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ImageView profileImageView=findViewById(R.id.profile_image_view);
                profileImageView.setImageBitmap(profileImageBitmap);
                profileImageView.setVisibility(View.VISIBLE);
                MaterialButton pickImageButton=findViewById(R.id.pick_profile_image_button);
                pickImageButton.setVisibility(View.INVISIBLE);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
