package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.ImagePicker;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.firebase.FirebaseStorageService;
import com.taller2.hypechatapp.firebase.FirebaseStorageUploadInterface;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.UserService;
import com.taller2.hypechatapp.ui.listeners.OnViewTouchListener;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

public class EditUserProfileActivity extends BaseActivity implements FirebaseStorageUploadInterface {
    private UserService userService;
    private TextInputEditText name, username, password, newPassword;
    private TextView email;
    private FloatingActionButton editImage;
    private ImageView profilePicture;
    private Button updateUserBtn;
    private ImagePicker imagePicker;
    private Uri filePath;
    private String imageUrl;
    private List<String> currentUserValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        findViewById(R.id.layoutContainer).setOnTouchListener(new OnViewTouchListener());

        setUpUI();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_edit_profile);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        loading = findViewById(R.id.loading);
        showLoading();

        name = findViewById(R.id.name_profile);
        username = findViewById(R.id.username_profile);
        email = findViewById(R.id.txt_user_email);
        profilePicture = findViewById(R.id.profile_image_view);
        editImage = findViewById(R.id.floating_btn_edit);
        password = findViewById(R.id.password_text);
        newPassword = findViewById(R.id.password_text_confirm);

        imagePicker = new ImagePicker(this, editImage, profilePicture, null);
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

                hideLoading();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {

                String textToShow = "No fue posible obtener el perfil del usuario. Intente más tarde.";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public Context getContext() {
                return EditUserProfileActivity.this;
            }
        });

        updateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidForm(username, name)) {
                    showLoading();
                    if (filePath != null) {
                        uploadProfileImage();
                    } else {
                        updateUser();
                    }
                }
            }
        });

        name.addTextChangedListener(new ChangesDetection());
        username.addTextChangedListener(new ChangesDetection());
        password.addTextChangedListener(new ChangesDetection());
        newPassword.addTextChangedListener(new ChangesDetection());
    }

    private boolean isValidForm(TextInputEditText username, TextInputEditText name) {
        if (TextUtils.isEmpty(username.getText().toString())) {
            username.setError(getString(R.string.input_username_error));
            return false;
        }

        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError(getString(R.string.input_name_error));
            return false;
        }

        String passwordStr = password.getText().toString();
        String newPasswordStr = newPassword.getText().toString();

        if ((passwordStr.isEmpty() && !newPasswordStr.isEmpty()) || (!passwordStr.isEmpty() && newPasswordStr.isEmpty())) {
            password.setError("Debe completar Contraseña y Nueva contraseña.");
            return false;
        }

        return true;
    }

    private void updatePreviousValues() {
        currentUserValues = Arrays.asList(name.getText().toString(), username.getText().toString());
    }

    private void updateUser() {
        final User userRequest = new User();
        userRequest.setName(name.getText().toString());
        userRequest.setUsername(username.getText().toString());
        userRequest.setPicture(imageUrl);
        username.setError(null);

        if (isChangePasswordOk(password, newPassword)) {

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential credential = EmailAuthProvider
                    .getCredential(user.getEmail(), password.getText().toString());

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(this.getClass().getName(), "Password updated");
                                            updateUser(userRequest);

                                        } else {
                                            Toast.makeText(EditUserProfileActivity.this, "Error: la nueva contraseña debe tener 8 caracteres", Toast.LENGTH_LONG).show();
                                            hideLoading();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(EditUserProfileActivity.this, "Error: la contraseña ingresada es incorrecta", Toast.LENGTH_LONG).show();
                                hideLoading();
                            }
                        }
                    });
        } else {
            updateUser(userRequest);
        }

    }

    private void updateUser(User userRequest) {
        userService.updateUser(userRequest, new Client<Void>() {
            @Override
            public void onResponseSuccess(Void nothing) {
                hideLoading();
                updateUserBtn.setEnabled(false);
                updatePreviousValues();
                Toast.makeText(getContext(), "Usuario actualizado ok!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                if (connectionError) {
                    Toast.makeText(getContext(), "Error al actualizar usuario", Toast.LENGTH_LONG).show();
                } else {
                    username.setError(getString(R.string.error_username_exists));
                    username.requestFocus();
                }
                hideLoading();
            }

            @Override
            public Context getContext() {
                return EditUserProfileActivity.this;
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


    private boolean isChangePasswordOk(EditText password, EditText newPassword) {
        String passwordStr = password.getText().toString();
        String newPasswordStr = newPassword.getText().toString();

        return !newPasswordStr.isEmpty();
    }


    public class ChangesDetection implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (currentUserValues != null && !currentUserValues.contains(s.toString())) {
                updateUserBtn.setEnabled(true);
            }
        }
    }
}
