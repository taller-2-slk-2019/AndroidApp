package com.taller2.hypechatapp.ui.activities;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.services.ConversationService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CreateConversationActivity extends AppCompatActivity {

    private ConversationService conversationService;
    private ProgressBar loading;
    private Integer organizationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        organizationId = getIntent().getIntExtra("ORGANIZATION_ID", -1);
        setContentView(R.layout.activity_create_conversation);

        conversationService = new ConversationService();

        setUpView();
    }

    private void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar_create_conversation);
        setSupportActionBar(toolbar);

        loading = findViewById(R.id.loading);
    }

}
