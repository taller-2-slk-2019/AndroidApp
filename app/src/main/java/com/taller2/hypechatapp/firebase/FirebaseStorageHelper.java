package com.taller2.hypechatapp.firebase;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import androidx.annotation.NonNull;

public class FirebaseStorageHelper {

    private StorageReference storageRef;

    public FirebaseStorageHelper(){
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void uploadLocalFile(FirebaseStorageInterface caller, String filePath){
        Uri file = Uri.fromFile(new File(filePath));
        StorageReference filesRef = storageRef.child("files");
        this.upload(caller, filesRef, file);
    }

    public void uploadLocalImage(final FirebaseStorageInterface caller, String imagePath){
        Uri file = Uri.fromFile(new File(imagePath));
        StorageReference imagesRef = storageRef.child("images");
        this.upload(caller, imagesRef, file);
    }

    private void upload(final FirebaseStorageInterface caller, StorageReference storage, Uri file){
        storage.putFile(file)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    Uri downloadUrl = taskSnapshot.getStorage().getDownloadUrl().getResult();
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
