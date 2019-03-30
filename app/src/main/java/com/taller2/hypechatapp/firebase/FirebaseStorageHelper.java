package com.taller2.hypechatapp.firebase;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import androidx.annotation.NonNull;

public class FirebaseStorageHelper {

    private FirebaseStorage firebaseStorage;

    public FirebaseStorageHelper(){
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public void uploadLocalFile(final FirebaseStorageUploadInterface caller, Uri filePath){
        this.upload(caller, "files/", filePath);
    }

    public void uploadLocalImage(final FirebaseStorageUploadInterface caller, Uri imagePath){
        this.upload(caller, "images/", imagePath);
    }

    public void downloadFile(final FirebaseStorageDownloadInterface caller, String url){
        this.download(caller, url, null);
    }

    public void downloadImage(final FirebaseStorageDownloadInterface caller, String url){
        this.download(caller, url, ".jpg");
    }

    private void download(final FirebaseStorageDownloadInterface caller, String url, String extension){
        final StorageReference storage = firebaseStorage.getReferenceFromUrl(url);

        try {
            if (extension == null){
                extension = storage.getName().split(".")[1];
            }
            final File localFile = File.createTempFile(storage.getName(), extension);

            storage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Log.i("Firebase file download", localFile.getAbsolutePath());
                    if (caller != null){
                        caller.onFileDownloaded(localFile.getAbsolutePath());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.e("Firebase download error", exception.toString());
                    if (caller != null){
                        caller.onFileDownloadError(exception);
                    }
                }
            });

        } catch (IOException e) {
            Log.e("Firebase download error", e.toString());
            if (caller != null){
                caller.onFileDownloadError(e);
            }
        }
    }

    private void upload(final FirebaseStorageUploadInterface caller, String folder, Uri file){
        final StorageReference storage = firebaseStorage.getReference().child(folder + UUID.randomUUID().toString() + file.getLastPathSegment());
        UploadTask uploadTask = storage.putFile(file);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storage.getDownloadUrl();
            }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    // Get a URL to the uploaded content
                    Uri downloadUrl = task.getResult();
                    Log.i("Firebase file uploaded", downloadUrl.toString());

                    if (caller != null){
                        caller.onFileUploaded(downloadUrl.toString());
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.e("Firebase file upload", exception.getMessage());

                    if (caller != null){
                        caller.onFileUploadError(exception);
                    }
                }
            });
    }


}
