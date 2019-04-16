package com.taller2.hypechatapp.firebase;

public interface FirebaseStorageUploadInterface {

    void onFileUploaded(String downloadUrl, String type);

    void onFileUploadError(Exception exception);
}
