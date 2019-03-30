package com.taller2.hypechatapp.firebase;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import androidx.annotation.NonNull;

public class FirebaseStorageHelper {

    private StorageReference storageRef;

    public FirebaseStorageHelper(){
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void uploadLocalFile(FirebaseStorageInterface caller, Uri filePath){
        this.upload(caller, "files/", filePath);
    }

    public void uploadLocalImage(final FirebaseStorageInterface caller, Uri imagePath){
        this.upload(caller, "images/", imagePath);
    }

    private void upload(final FirebaseStorageInterface caller, String folder, Uri file){
        final StorageReference storage = storageRef.child(folder + UUID.randomUUID().toString() + file.getLastPathSegment());
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
