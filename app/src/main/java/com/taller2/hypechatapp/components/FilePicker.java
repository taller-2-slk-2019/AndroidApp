package com.taller2.hypechatapp.components;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

public class FilePicker {

    public static final int PICK_FILE_REQUEST = 7;

    public static void chooseFile(AppCompatActivity activity) {
        activity.startActivityForResult(getFilePickerIntent(), PICK_FILE_REQUEST);
    }

    private static Intent getFilePickerIntent() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, "Seleccionar Archivo");
    }

    public static Uri getFilePath(Intent data) {
        return data.getData();
    }
}
