package com.taller2.hypechatapp.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.ImagePicker;
import com.taller2.hypechatapp.network.model.OrganizationRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class CreateOrganizationStepOneFragment extends Fragment {

    public static final int PICK_IMAGE_REQUEST = 71;

    private OnNextButtonClickListener callback;

    private ImageView profileImageView;
    private MaterialButton pickImageButton;
    private TextView errorText;
    private ImagePicker imagePicker;

    private OrganizationRequest organizationRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View returnView=inflater.inflate(R.layout.create_organization_step1, container, false);

        organizationRequest=(OrganizationRequest)getArguments().getSerializable("organizationRequest");
        setUpImagePicker(returnView);

        setUpUI(returnView);

        return returnView;

    }

    public void setOnNextButtonClickListener(OnNextButtonClickListener callback) {
        this.callback = callback;
    }

    public void setImageNotSelectedError() {
        Toast.makeText(getContext(), "Debe seleccionar una imagen para continuar", Toast.LENGTH_LONG).show();

    }

    public interface OnNextButtonClickListener{
        void onNextButtonClick(OrganizationRequest organizationRequest);

    }


    private void setUpUI(final View returnView) {
        Toolbar toolbar = returnView.findViewById(R.id.toolbar_create_organization);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        MaterialButton nextButton=returnView.findViewById(R.id.new_organization_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            TextInputEditText nameInputText = returnView.findViewById(R.id.organization_name_input);
            TextInputEditText descriptionInputText = returnView.findViewById(R.id.organization_description_input);

            if (!validateUserInput(nameInputText, descriptionInputText))
                return;

            organizationRequest.name=nameInputText.getText().toString();
            organizationRequest.description=descriptionInputText.getText().toString();

            callback.onNextButtonClick(organizationRequest);

            }
        });
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


    private void setUpImagePicker(View returnView) {
        pickImageButton = returnView.findViewById(R.id.pick_profile_image_button);
        profileImageView = returnView.findViewById(R.id.profile_image_view);
        errorText = returnView.findViewById(R.id.profile_image_error);


        imagePicker = new ImagePicker((AppCompatActivity)getActivity(), pickImageButton, profileImageView, errorText);

    }

    public void setImageBitmap(Bitmap profileImageBitmap){
        profileImageView.setImageBitmap(profileImageBitmap);
        profileImageView.setVisibility(View.VISIBLE);
        pickImageButton.setVisibility(View.INVISIBLE);
    }
}
