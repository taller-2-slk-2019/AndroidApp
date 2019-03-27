package com.taller2.hypechatapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.UserService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserProfileActivity extends AppCompatActivity {
    UserService userService;
    TextView name, surname, email;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        setUpUI();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_user_profile);
        setSupportActionBar(toolbar);

        progressbar = (ProgressBar) findViewById(R.id.progressBar);

        name = findViewById(R.id.user_name);
        surname = findViewById(R.id.user_surname);
        email = findViewById(R.id.user_email);

        userService = new UserService();

        int userId = 1; // Reemplzar por el Id correspondiente al usuario

        userService.getUser(userId, new Client<User>() {

            @Override
            public void onResponseSuccess(User responseBody) {
                name.append(responseBody.getName());
                surname.append(responseBody.getSurname());
                email.append(responseBody.getEmail());
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onResponseError(String errorMessage) {

            }

            @Override
            public Context getContext() {
                return UserProfileActivity.this;
            }
        });
    }
}
