package com.taller2.hypechatapp.components;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class ImagePicker {

    public static final int PICK_IMAGE_REQUEST = 71;

    private ImageView profileImageView;
    private MaterialButton pickImageButton;
    private Uri filePath;
    private TextView errorText;

    public ImagePicker(final AppCompatActivity activity, MaterialButton pickImageButton,
                       ImageView profileImageView, TextView errorText){
        this.errorText=errorText;
        this.pickImageButton=pickImageButton;
        this.profileImageView=profileImageView;
        pickImageButton.setOnClickListener(new View.OnClickListener() {
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

    private static Intent getImagePickerIntent(){
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
            pickImageButton.setVisibility(View.INVISIBLE);
            errorText.setVisibility(View.INVISIBLE);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return filePath;
    }

    public static Uri getFilePath(Intent data){
        return data.getData();
    }

    public void disable() {
        profileImageView.setClickable(false);
        pickImageButton.setClickable(false);
    }

    public void enable() {
        profileImageView.setClickable(true);
        pickImageButton.setClickable(true);
    }

    public boolean validate(){
        boolean valid = filePath != null;
        errorText.setVisibility(valid ? View.INVISIBLE : View.VISIBLE);
        return valid;
    }
}
