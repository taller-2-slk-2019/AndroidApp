package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.ImagePicker;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.firebase.FirebaseStorageService;
import com.taller2.hypechatapp.firebase.FirebaseStorageUploadInterface;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.UserService;
import com.taller2.hypechatapp.ui.listeners.OnViewTouchListener;

import androidx.annotation.NonNull;

public class RegisterActivity extends BaseActivity implements FirebaseStorageUploadInterface {

    private TextView email;
    private TextView password;
    private TextView name;
    private TextView username;
    private Uri filePath;
    private String imageUrl;
    private ImagePicker imagePicker;

    private TextView errorText;

    private FirebaseAuth mAuth;
    private UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.layoutContainer).setOnTouchListener(new OnViewTouchListener());

        mAuth = FirebaseAuth.getInstance();
        userService = new UserService();

        setupUI();
    }

    private void setupUI() {
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        name = findViewById(R.id.name_register);
        username = findViewById(R.id.username_register);
        MaterialButton pickImageButton = findViewById(R.id.pick_profile_image_button);
        ImageView profileImageView = findViewById(R.id.profile_image_view);
        errorText = findViewById(R.id.error_text);
        imagePicker = new ImagePicker(this, pickImageButton, profileImageView, errorText);

        loading = findViewById(R.id.loading);

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUserInput())
                    return;

                showLoading();
                registerFirebaseUser();
            }
        });
    }

    private void registerFirebaseUser() {
        Log.i("Firebase register", "starting user register");
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.i("Firebase register", "Register succesfull");
                            uploadProfileImage();
                        } else {
                            handleFirebaseException(task.getException());
                            Log.w("Firebase register", "register failed", task.getException());
                        }
                    }
                });
    }

    private void handleFirebaseException(Exception exception) {

        if (exception instanceof FirebaseAuthWeakPasswordException) {
            password.setError(getString(R.string.error_weak_password));
            password.requestFocus();
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            email.setError(getString(R.string.error_invalid_email));
            email.requestFocus();
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            email.setError(getString(R.string.error_user_exists));
            email.requestFocus();
        } else {
            Toast.makeText(this, R.string.error_register_connection, Toast.LENGTH_LONG).show();
            Log.e(getPackageName(), exception.getMessage());
        }

        enableRegisterEdition();
    }

    private void registerUser() {
        User userRequest = new User();
        userRequest.setEmail(email.getText().toString());
        userRequest.setName(name.getText().toString());
        userRequest.setUsername(username.getText().toString());
        userRequest.setToken(FirebaseAuthService.getCurrentUserToken());
        userRequest.setPicture(imageUrl);

        userService.registerUser(userRequest, new Client<User>() {
            @Override
            public void onResponseSuccess(User responseUser) {
                endRegister();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                FirebaseAuthService.getCurrentUser().delete();
                showError(false, connectionError);
            }

            @Override
            public Context getContext() {
                return RegisterActivity.this;
            }
        });

    }

    private void endRegister() {
        FirebaseAuthService.logIn(this);
        Toast.makeText(this, "Se ha registrado con éxito", Toast.LENGTH_LONG).show();
        finish();
    }

    protected void showLoading() {
        errorText.setVisibility(View.INVISIBLE);
        super.showLoading();
    }

    private void showError(boolean firebase, boolean connectionError) {
        errorText.setVisibility(View.VISIBLE);
        if (firebase) {
            errorText.setText(R.string.error_register_firebase);
        } else if (!connectionError) {
            errorText.setText(R.string.error_register);
        } else {
            Toast.makeText(this, R.string.error_register_connection, Toast.LENGTH_LONG).show();
        }

        enableRegisterEdition();
    }

    private void enableRegisterEdition() {
        hideLoading();
        FirebaseAuthService.logOut(this);
    }

    private boolean validateUserInput() {
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Ingrese un email");
            return false;
        }
        if (TextUtils.isEmpty(username.getText().toString())) {
            username.setError("Ingrese un nombre de usuario");
            return false;
        }
        if (username.getText().toString().contains(" ")) {
            username.setError("El nombre de usuario no puede tener espacios");
            return false;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Ingrese una contraseña");
            return false;
        }
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Ingrese un nombre");
            return false;
        }
        return imagePicker.validate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = imagePicker.analyzeResult(this, data);
        }
    }

    @Override
    public void onFileUploaded(String downloadUrl, String type) {
        imageUrl = downloadUrl;
        registerUser();
    }

    @Override
    public void onFileUploadError(Exception exception) {
        showError(false, true);
    }

    private void uploadProfileImage() {
        FirebaseStorageService storage = new FirebaseStorageService();
        storage.uploadLocalImage(this, filePath);
    }
}
