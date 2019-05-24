package com.taller2.hypechatapp.ui.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.firebase.FirebaseStorageDownloadInterface;
import com.taller2.hypechatapp.firebase.FirebaseStorageService;
import com.taller2.hypechatapp.model.Message;

import androidx.annotation.NonNull;

public class MessageFileViewHolder extends MessageViewHolder implements FirebaseStorageDownloadInterface {

    private TextView messageFile;
    private String fileUrl;
    private ProgressBar loading;

    public MessageFileViewHolder(@NonNull View itemView) {
        super(itemView);
        loading = itemView.findViewById(R.id.loading);
    }

    @Override
    protected void initialize(View view) {
        messageFile = view.findViewById(R.id.message_file);
    }

    @Override
    public void setMessage(Message message) {
        super.setMessage(message);
        fileUrl = message.data;

        messageFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageFile.setClickable(false);
                loading.setVisibility(View.VISIBLE);
                new FirebaseStorageService().downloadFile(MessageFileViewHolder.this, fileUrl);
            }
        });
    }

    @Override
    protected int getViewId() {
        return R.layout.message_file;
    }

    @Override
    public void onFileDownloaded(Uri filePath, String contentType) {
        messageFile.setClickable(true);
        loading.setVisibility(View.INVISIBLE);

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(filePath, contentType);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    @Override
    public void onFileDownloadError(Exception exception) {
        String textToShow = "No se pudo descargar el archivo";
        Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
        messageFile.setClickable(true);
        loading.setVisibility(View.INVISIBLE);
    }

    @Override
    public Context getContext() {
        return context;
    }
}
