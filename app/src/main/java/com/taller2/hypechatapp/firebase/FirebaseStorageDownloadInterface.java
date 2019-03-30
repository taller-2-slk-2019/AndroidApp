package com.taller2.hypechatapp.firebase;

public interface FirebaseStorageDownloadInterface {

    void onFileDownloaded(String filePath);

    void onFileDownloadError(Exception exception);
}
