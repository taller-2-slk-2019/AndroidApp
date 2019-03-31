package com.taller2.hypechatapp.firebase;

public interface FirebaseStorageUploadInterface {

    void onFileUploaded(String downloadUrl);

    void onFileUploadError(Exception exception);
}
