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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.ImagePicker;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.firebase.FirebaseStorageService;
import com.taller2.hypechatapp.firebase.FirebaseStorageUploadInterface;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.UserService;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserProfileActivity extends AppCompatActivity implements FirebaseStorageUploadInterface {
    private UserService userService;
    private TextInputEditText name, username;
    private TextView email;
    private FloatingActionButton editImage;
    private ImageView profilePicture;
    private Button updateUserBtn;
    private ImagePicker imagePicker;
    private Uri filePath;
    private String imageUrl;
    private ProgressBar loading;
    private List<String> currentUserValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        setUpUI();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_create_channel);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        name = findViewById(R.id.name_profile);
        username = findViewById(R.id.username_profile);
        email = findViewById(R.id.txt_user_email);
        profilePicture = findViewById(R.id.profile_image_view);
        editImage = findViewById(R.id.floating_btn_edit);

        imagePicker =  new ImagePicker(this, editImage, profilePicture, null);
        updateUserBtn = findViewById(R.id.btn_update_user);
        updateUserBtn.setEnabled(false);

        userService = new UserService();
        userService.getUser(new Client<User>() {

            @Override
            public void onResponseSuccess(User responseBody) {
                name.setText(responseBody.getName());
                username.setText(responseBody.getUsername());
                email.setText(responseBody.getEmail());
                imageUrl = responseBody.getPicture();
                PicassoLoader.load(getApplicationContext(), String.format("%s?type=large", imageUrl), R.drawable.default_user, profilePicture);

                updateUserBtn.setEnabled(false);
                updatePreviousValues();

                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onResponseError(String errorMessage) {

                String textToShow;
                if (!TextUtils.isEmpty(errorMessage)) {
                    textToShow = errorMessage;
                } else {
                    textToShow = "No fue posible obtener el perfil del usuario. Intente más tarde.";
                }

                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public Context getContext() {
                return UserProfileActivity.this;
            }
        });

        updateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(true);
                if (filePath != null) {
                    uploadProfileImage();
                } else {
                    updateUser();
                }
            }
        });

        name.addTextChangedListener(new ChangesDetection());
        username.addTextChangedListener(new ChangesDetection());
    }

    private void updatePreviousValues() {
        currentUserValues = Arrays.asList(name.getText().toString(), username.getText().toString());
    }

    private void updateUser() {
        User userRequest = new User();
        userRequest.setName(name.getText().toString());
        userRequest.setUsername(username.getText().toString());
        userRequest.setToken(FirebaseAuthService.getCurrentUserToken());
        userRequest.setPicture(imageUrl);

        userService.updateUser(userRequest, new Client<Void>() {
            @Override
            public void onResponseSuccess(Void nothing) {
                showLoading(false);
                updateUserBtn.setEnabled(false);
                updatePreviousValues();
                Toast.makeText(getContext(), "Usuario actualizado ok!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponseError(String errorMessage) {
                Toast.makeText(getContext(), "Error al actualizar usuario", Toast.LENGTH_LONG).show();
                showLoading(false);
            }

            @Override
            public Context getContext() {
                return UserProfileActivity.this;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = imagePicker.analyzeResult(this, data);
            updateUserBtn.setEnabled(true);
        }
    }

    private void uploadProfileImage() {
        FirebaseStorageService storage = new FirebaseStorageService();
        storage.uploadLocalImage(this, filePath);
    }

    @Override
    public void onFileUploaded(String downloadUrl, String type) {
        this.imageUrl = downloadUrl;
        updateUser();
    }

    @Override
    public void onFileUploadError(Exception exception) {
        Toast.makeText(this, "Error subiendo la imágen", Toast.LENGTH_LONG).show();
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            loading.setVisibility(View.VISIBLE);
            updateUserBtn.setClickable(false);
            imagePicker.disable();
        } else {
            loading.setVisibility(View.INVISIBLE);
            updateUserBtn.setClickable(true);
            imagePicker.enable();
        }
    }


    public class ChangesDetection implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            if (currentUserValues != null && !currentUserValues.contains(s.toString())) { updateUserBtn.setEnabled(true); }
        }
    }
}
