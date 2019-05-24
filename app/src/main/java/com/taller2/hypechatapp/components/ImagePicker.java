package com.taller2.hypechatapp.components;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class ImagePicker {

    public static final int PICK_IMAGE_REQUEST = 71;

    private ImageView profileImageView;
    private View pickImage;
    private Uri filePath;
    private TextView errorText;

    public ImagePicker(final AppCompatActivity activity, View pickImage,
                       ImageView profileImageView, TextView errorText) {
        this.errorText = errorText;
        this.pickImage = pickImage;
        this.profileImageView = profileImageView;
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(activity);
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(activity);
            }
        });
    }

    public static void chooseImage(AppCompatActivity activity) {
        activity.startActivityForResult(getImagePickerIntent(), PICK_IMAGE_REQUEST);
    }

    private static Intent getImagePickerIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, "Seleccionar Imagen");
    }


    public Uri analyzeResult(AppCompatActivity activity, Intent data) {
        filePath = getFilePath(data);
        try {
            Bitmap profileImageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), filePath);

            profileImageView.setImageBitmap(profileImageBitmap);
            profileImageView.setVisibility(View.VISIBLE);
            pickImage.setVisibility(View.INVISIBLE);
            if (errorText != null) {
                errorText.setVisibility(View.INVISIBLE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }

    public static Uri getFilePath(Intent data) {
        return data.getData();
    }

    public void disable() {
        profileImageView.setClickable(false);
        pickImage.setClickable(false);
    }

    public void enable() {
        profileImageView.setClickable(true);
        pickImage.setClickable(true);
    }

    public boolean validate() {
        boolean valid = filePath != null;
        errorText.setVisibility(valid ? View.INVISIBLE : View.VISIBLE);
        return valid;
    }

    public void showPicker() {
        pickImage.setVisibility(View.VISIBLE);
    }
}
