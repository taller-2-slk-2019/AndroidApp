package com.taller2.hypechatapp.firebase;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

public class FirebaseStorageService {
    public static final String TYPE_FILE = "file";
    public static final String TYPE_IMAGE = "image";

    private FirebaseStorage firebaseStorage;

    public FirebaseStorageService(){
        firebaseStorage = FirebaseStorage.getInstance();
    }

    // Upload files or images

    public void uploadLocalImage(final FirebaseStorageUploadInterface caller, Uri filePath){
        this.upload(caller, filePath, TYPE_IMAGE);
    }

    public void uploadLocalFile(final FirebaseStorageUploadInterface caller, Uri filePath){
        this.upload(caller, filePath, TYPE_FILE);
    }

    private void upload(final FirebaseStorageUploadInterface caller, Uri file, final String type){
        String fileName = UUID.randomUUID().toString() + file.getLastPathSegment();
        final StorageReference storage = firebaseStorage.getReference().child(fileName.replace("/", "a"));
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
                    caller.onFileUploaded(downloadUrl.toString(), type);
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

    // Download files

    public void downloadFile(final FirebaseStorageDownloadInterface caller, String url){
        this.download(caller, url);
    }

    private void download(final FirebaseStorageDownloadInterface caller, String url){
        final StorageReference storage = firebaseStorage.getReferenceFromUrl(url);

        File dir = caller.getContext().getFilesDir();
        final File localFile = new File(dir.getAbsolutePath() + '/' + storage.getName());

        if (localFile.isFile() && localFile.length() > 0){
            // File already exists, do not download again
            onFileDownloaded(storage, caller, localFile);
            return;
        }

        storage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Log.i("Firebase file download", localFile.getAbsolutePath());
                onFileDownloaded(storage, caller, localFile);
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
    }

    private Uri getLocalFileUri(Context context, File localFile){
        return FileProvider.getUriForFile(context, "com.taller2.hypechatapp.fileProvider", localFile);
    }

    private void onFileDownloaded(StorageReference storage, final FirebaseStorageDownloadInterface caller, final File localFile){
        storage.getMetadata().addOnSuccessListener(
                new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        String contentType = storageMetadata.getContentType();
                        caller.onFileDownloaded(getLocalFileUri(caller.getContext(), localFile), contentType);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("Firebase download error", exception.toString());
                if (caller != null){
                    caller.onFileDownloadError(exception);
                }
            }
        });
    }


}
