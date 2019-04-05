package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.ImagePicker;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.firebase.FirebaseStorageService;
import com.taller2.hypechatapp.firebase.FirebaseStorageUploadInterface;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.UserService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements FirebaseStorageUploadInterface {

    private TextView email;
    private TextView password;
    private TextView name;
    private TextView username;
    private Uri filePath;
    private String imageUrl;
    private ImagePicker imagePicker;
    private Button registerButton;

    private TextView errorText;
    private ProgressBar loading;

    private FirebaseAuth mAuth;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        userService = new UserService();

        setupUI();
    }

    private void setupUI() {
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        name = findViewById(R.id.name_register);
        username = findViewById(R.id.username_register);
        imagePicker = new ImagePicker(this);

        loading = findViewById(R.id.loading);
        errorText = findViewById(R.id.error_text);

        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUserInput())
                    return;

                loading();
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
                            Log.w("Firebase register", "register failed", task.getException());
                            showError(true);
                        }
                    }
                });
    }

    private void userRegistered() {
        User userRequest = new User();
        userRequest.setEmail(email.getText().toString());
        userRequest.setName(name.getText().toString());
        userRequest.setUsername(username.getText().toString());
        userRequest.setToken(FirebaseAuthService.getCurrentUserToken());
        userRequest.setPicture(imageUrl);

        userService.registerUser(userRequest, new Client<User>(){
            @Override
            public void onResponseSuccess(User responseUser) {
                endRegister();
            }

            @Override
            public void onResponseError(String errorMessage) {
                FirebaseAuthService.getCurrentUser().delete();
                showError(false);
            }

            @Override
            public Context getContext() {
                return RegisterActivity.this;
            }
        });

    }

    private void endRegister() {
        Toast.makeText(this, "Se ha registrado con éxito", Toast.LENGTH_LONG).show();
        finish();
    }

    private void loading() {
        loading.setVisibility(View.VISIBLE);
        registerButton.setClickable(false);
        errorText.setText("");
        imagePicker.disable();
    }

    private void showError(boolean firebase) {
        if (firebase){
            errorText.setText(R.string.error_register_firebase);
        } else {
            errorText.setText(R.string.error_register);
        }
        loading.setVisibility(View.INVISIBLE);
        registerButton.setClickable(true);
        imagePicker.enable();
        FirebaseAuthService.logOut();
    }

    private boolean validateUserInput() {
        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError("Ingrese un email");
            return false;
        }
        if(TextUtils.isEmpty(username.getText().toString())){
            username.setError("Ingrese un nombre de usuario");
            return false;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            password.setError("Ingrese una contraseña");
            return false;
        }
        if(TextUtils.isEmpty(name.getText().toString())){
            name.setError("Ingrese un nombre");
            return false;
        }
        return imagePicker.validate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ImagePicker.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = imagePicker.analyzeResult(this, data);
        }
    }

    @Override
    public void onFileUploaded(String downloadUrl) {
        imageUrl = downloadUrl;
        userRegistered();
    }

    @Override
    public void onFileUploadError(Exception exception) {
        showError(false);
    }

    private void uploadProfileImage(){
        FirebaseStorageService storage = new FirebaseStorageService();
        storage.uploadLocalFile(this, filePath);
    }
}
