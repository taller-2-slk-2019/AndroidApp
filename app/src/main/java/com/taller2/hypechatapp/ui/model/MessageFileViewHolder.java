package com.taller2.hypechatapp.ui.model;

import android.content.Context;
import android.view.View;
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
    private Context context;

    public MessageFileViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    @Override
    protected void initialize(View view){
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
                new FirebaseStorageService().downloadFile(MessageFileViewHolder.this, fileUrl);
            }
        });
    }

    @Override
    protected int getViewId() {
        return R.layout.message_file;
    }

    @Override
    public void onFileDownloaded(String filePath) {
        messageFile.setClickable(true);
        Toast.makeText(getContext(), filePath, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFileDownloadError(Exception exception) {
        String textToShow = "No se pudo descargar el archivo";
        Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
        messageFile.setClickable(true);
    }

    @Override
    public Context getContext() {
        return context;
    }
}
