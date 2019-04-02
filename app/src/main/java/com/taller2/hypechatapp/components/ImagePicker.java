package com.taller2.hypechatapp.components;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.taller2.hypechatapp.R;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class ImagePicker {

    public static final int PICK_IMAGE_REQUEST = 71;

    public ImagePicker(final AppCompatActivity activity){
        MaterialButton pickImageButton = activity.findViewById(R.id.pick_profile_image_button);
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(activity);
            }
        });

        ImageView pickImageView = activity.findViewById(R.id.profile_image_view);
        pickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(activity);
            }
        });
    }

    private void chooseImage(AppCompatActivity activity) {
        activity.startActivityForResult(getImagePickerIntent(), PICK_IMAGE_REQUEST);
    }

    public Intent getImagePickerIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, "Seleccionar Imagen");
    }


    public Uri analyzeResult(AppCompatActivity activity, Intent data) {
        Uri filePath = data.getData();
        try {
            Bitmap profileImageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), filePath);

            ImageView profileImageView = activity.findViewById(R.id.profile_image_view);
            if (profileImageView != null) {
                profileImageView.setImageBitmap(profileImageBitmap);
                profileImageView.setVisibility(View.VISIBLE);
            }

            MaterialButton pickImageButton = activity.findViewById(R.id.pick_profile_image_button);
            if (pickImageButton != null) {
                pickImageButton.setVisibility(View.INVISIBLE);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return filePath;
    }
}
