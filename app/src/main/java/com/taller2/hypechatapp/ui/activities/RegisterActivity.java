package com.taller2.hypechatapp.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.taller2.hypechatapp.services.UserService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements FirebaseStorageUploadInterface {

    private TextView email;
    private TextView password;
    private TextView name;
    private Uri filePath;
    private String imageUrl;
    private ImagePicker imagePicker;
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
        imagePicker = new ImagePicker(this);

        Button registerButton = findViewById(R.id.register_button);
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
                            showError();
                        }
                    }
                });
    }

    private void userRegistered() {
        User userRequest = new User();
        userRequest.setEmail(email.getText().toString());
        userRequest.setName(name.getText().toString());
        userRequest.setToken(FirebaseAuthService.getCurrentUserToken());
        userRequest.setPicture(imageUrl);

        endRegister(); // TODO delete this
        /* TODO enable this when fixed in server side (working ok in branch auth)
        userService.registerUser(userRequest, new Client<User>(){
            @Override
            public void onResponseSuccess(User responseUser) {
                endRegister();
            }

            @Override
            public void onResponseError(String errorMessage) {
                showError();
            }

            @Override
            public Context getContext() {
                return RegisterActivity.this;
            }
        });*/

    }

    private void endRegister() {
        finish();
    }

    private void loading() {
        // TODO do something here
    }

    private void showError() {
        // TODO do something here
        FirebaseAuthService.logOut();
    }

    private boolean validateUserInput() {
        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError("Ingrese un email");
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
        return filePath != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == imagePicker.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
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
        showError();
    }

    private void uploadProfileImage(){
        FirebaseStorageService storage = new FirebaseStorageService();
        storage.uploadLocalFile(this, filePath);
    }
}
