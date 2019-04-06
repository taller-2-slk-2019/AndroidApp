package com.taller2.hypechatapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.UserService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserProfileActivity extends AppCompatActivity {
    UserService userService;
    TextView name, username, email;
    ProgressDialog dialog;
    ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        setUpUI();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_user_profile);
        setSupportActionBar(toolbar);

        dialog = new ProgressDialog(UserProfileActivity.this);
        dialog.setMessage("Cargando...");
        dialog.show();

        name = findViewById(R.id.user_name);
        username = findViewById(R.id.user_username);
        email = findViewById(R.id.user_email);
        profilePicture = findViewById(R.id.user_picture);

        userService = new UserService();
        userService.getUser(new Client<User>() {

            @Override
            public void onResponseSuccess(User responseBody) {
                name.append(responseBody.getName());
                username.append(responseBody.getUsername());
                email.append(responseBody.getEmail());
                String url = responseBody.getPicture();
                PicassoLoader.load(getApplicationContext(), url, profilePicture);
                dialog.dismiss();
            }

            @Override
            public void onResponseError(String errorMessage) {

                //dialog.dismiss();

                String textToShow;
                if(!TextUtils.isEmpty(errorMessage)){
                    textToShow=errorMessage;
                } else {
                    textToShow="No fue posible obtener el perfil del usuario. Intente más tarde.";
                }
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public Context getContext() {
                return UserProfileActivity.this;
            }
        });
    }
}
