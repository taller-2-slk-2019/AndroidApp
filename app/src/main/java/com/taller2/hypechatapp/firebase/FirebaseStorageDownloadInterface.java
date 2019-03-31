package com.taller2.hypechatapp.firebase;

import android.content.Context;

public interface FirebaseStorageDownloadInterface {

    void onFileDownloaded(String filePath);

    void onFileDownloadError(Exception exception);

    Context getContext();
}
