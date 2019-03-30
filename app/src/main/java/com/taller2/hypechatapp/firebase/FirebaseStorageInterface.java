package com.taller2.hypechatapp.firebase;

public interface FirebaseStorageInterface {

    void onFileUploaded(String downloadUrl);

    void onFileUploadError(Exception exception);
}
