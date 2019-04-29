package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.UserService;
import com.taller2.hypechatapp.ui.activities.utils.ScreenDisablerHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private UserService userService;

    private TextInputEditText emailText;
    private TextInputEditText passwordText;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        userService = new UserService();
        loading = findViewById(R.id.loading);

        setFacebookLogin();
        setNormalLogin();
    }

    private void setNormalLogin(){
        Button loginButton = findViewById(R.id.login_button);
        emailText = findViewById(R.id.email_text);
        passwordText = findViewById(R.id.password_text);

        TextView noAccountText = findViewById(R.id.no_account_text);
        noAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Callback registration
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!validateUserInput()) {
                return;
            }
            loading();
            firebaseNormalLogin();
            }
        });
    }

    private void setFacebookLogin(){
        LoginButton fbLoginButton = findViewById(R.id.login_button_facebook);
        fbLoginButton.setReadPermissions("email", "public_profile");

        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
            }
        });

        // Callback registration
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook login", "facebook:onSuccess:" + loginResult);
                firebaseFacebookLogin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Facebook login", "facebook:onCancel");
                showError(true);
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("Facebook login", "facebook:onError", exception);
                showError(true);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuthService.isUserLoggedIn()){
            userLoggedIn();
        }
    }

    private void loading(){
        passwordText.setError(null);
        loading.setVisibility(View.VISIBLE);
        ScreenDisablerHelper.disableScreenTouch(getWindow());
    }

    private void hideLoading(){
        loading.setVisibility(View.INVISIBLE);
        ScreenDisablerHelper.enableScreenTouch(getWindow());
    }

    private void showError(boolean facebookError){
        if (facebookError){
            Toast.makeText(this, R.string.fb_log_in_error, Toast.LENGTH_LONG).show();
        } else {
            passwordText.setError(getResources().getString(R.string.log_in_error));
            passwordText.requestFocus();
        }

        hideLoading();
        FirebaseAuthService.logOut(this);
    }

    public void userLoggedIn(){
        Log.i("LoginActivity","User is logged in with token: " + FirebaseAuthService.getCurrentUserToken());
        FirebaseAuthService.logIn(this);
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

    public void facebookUserLoggedIn(){
        FirebaseUser user = FirebaseAuthService.getCurrentUser();
        User userRequest = new User();
        userRequest.setEmail(user.getEmail());
        userRequest.setName(user.getDisplayName());
        userRequest.setToken(FirebaseAuthService.getCurrentUserToken());
        userRequest.setPicture(user.getPhotoUrl().toString());

        userService.registerUser(userRequest, new Client<User>(){
            @Override
            public void onResponseSuccess(User responseUser) {
                userLoggedIn();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                showError(true);
            }

            @Override
            public Context getContext() {
                return LoginActivity.this;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // For Facebook log in
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseFacebookLogin(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.i("Firebase log in", "handling facebook access token");
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i("Firebase log in", "Log in succesfull");
                        facebookUserLoggedIn();
                    } else {
                        Log.w("Firebase log in", "Log in failed", task.getException());
                        showError(true);
                    }
                }
            });
    }

    private void firebaseNormalLogin() {
        Log.i("Firebase log in", "handling normal log in");
        mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i("Firebase log in", "Log in succesfull");
                        userLoggedIn();
                    } else {
                        Log.w("Firebase log in", "Log in failed", task.getException());
                        if (task.getException() instanceof FirebaseNetworkException){
                            hideLoading();
                            Toast.makeText(LoginActivity.this,
                                    getResources().getString(R.string.error_login_connection), Toast.LENGTH_LONG).show();
                        } else {
                            showError(false);
                        }
                    }
                }
            });
    }

    private boolean validateUserInput() {
        if(TextUtils.isEmpty(emailText.getText().toString())){
            emailText.setError("Ingrese un email");
            return false;
        }

        if(TextUtils.isEmpty(passwordText.getText().toString())){
            passwordText.setError("Ingrese una contraseña");
            return false;
        }

        return true;
    }

}
