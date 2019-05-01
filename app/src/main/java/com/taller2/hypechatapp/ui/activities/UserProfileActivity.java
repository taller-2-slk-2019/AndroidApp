package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.model.UserStatistics;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.UserService;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserProfileActivity extends AppCompatActivity {
    private UserService userService;
    private TextInputEditText name, username;
    private TextView email;
    private FloatingActionButton editProfileAction;
    private ImageView profilePicture;
    private String imageUrl;
    private TextView msgSent, organizations;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userService = new UserService();

        setUpUI();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_view_profile);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        name = findViewById(R.id.name_profile);
        username = findViewById(R.id.username_profile);
        email = findViewById(R.id.txt_user_email);
        profilePicture = findViewById(R.id.profile_image_view);

        editProfileAction = findViewById(R.id.floating_btn_edit);
        editProfileAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
                startActivity(intent);
            }
        });

        msgSent = findViewById(R.id.txt_msg_sent);
        organizations = findViewById(R.id.txt_organizations);
        updateUserInfo();

        userService.getStatistics(new Client<UserStatistics>() {
            @Override
            public void onResponseSuccess(UserStatistics stats) {
                msgSent.setText(String.valueOf(stats.messagesSent));
                organizations.setText(getOrganizationText(stats.organizations));
            }

            @Override
            public void onResponseError(boolean conenctionError, String errorMessage) {
                Toast.makeText(getContext(), "No pudimos obtener tus estadísticas =(", Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                 return UserProfileActivity.this;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserInfo();
    }

    private void updateUserInfo() {
        userService.getUser(new Client<User>() {

            @Override
            public void onResponseSuccess(User responseBody) {
                name.setText(responseBody.getName());
                username.setText(responseBody.getUsername());
                email.setText(responseBody.getEmail());
                imageUrl = responseBody.getPicture();
                PicassoLoader.load(getApplicationContext(), String.format("%s?type=large", imageUrl), R.drawable.default_user, profilePicture);
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {

                String textToShow = "No fue posible obtener el perfil del usuario. Intente más tarde.";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public Context getContext() {
                return UserProfileActivity.this;
            }
        });
    }

    private String getOrganizationText(List<String> organizations) {
        String separator = "";

        StringBuilder sb = new StringBuilder();
        for (String organizationName: organizations) {
            sb.append(separator);
            sb.append(organizationName);
            separator = "\n";
        }

        return sb.toString();
    }
}
