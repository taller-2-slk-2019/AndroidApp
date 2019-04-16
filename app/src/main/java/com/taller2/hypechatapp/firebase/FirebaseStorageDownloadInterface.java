package com.taller2.hypechatapp.firebase;

import android.content.Context;
import android.net.Uri;

public interface FirebaseStorageDownloadInterface {

    void onFileDownloaded(Uri filePath, String contentType);

    void onFileDownloadError(Exception exception);

    Context getContext();
}
