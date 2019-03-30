package com.taller2.hypechatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taller2.hypechatapp.R;

import androidx.annotation.NonNull;

public class LoginActivity extends Activity {

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;

    private TextView emailText;
    private TextView passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        setFacebookLogin();
        setNormalLogin();
    }

    private void setNormalLogin(){
        Button loginButton = findViewById(R.id.login_button);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        // Callback registration
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUserInput()) {
                    return;
                }

                handleNormalLogin();
            }
        });
    }

    private void setFacebookLogin(){
        LoginButton loginButton = findViewById(R.id.login_button_facebook);
        loginButton.setReadPermissions("email", "public_profile");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook login", "facebook:onSuccess:" + loginResult);
                handleFacebookLogin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Facebook login", "facebook:onCancel");
                //TODO error
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("Facebook login", "facebook:onError", exception);
                //TODO error
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userLoggedIn();
    }

    public void userLoggedIn(){
        Log.i("LoginActivity","User is logged in");
        // TODO do something when logged in
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookLogin(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.i("Firebase log in", "handling facebook access token");
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.i("Firebase log in", "signInWithCredential:success");
                        userLoggedIn();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Firebase log in", "signInWithCredential:failure", task.getException());
                        //TODO error
                    }
                }
            });
    }

    private void handleNormalLogin() {
        Log.i("Firebase log in", "handling normal log in");
        mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.i("Firebase log in", "signInWithEmail:success");
                        userLoggedIn();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Firebase log in", "signInWithCredential:failure", task.getException());
                        //TODO error
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
