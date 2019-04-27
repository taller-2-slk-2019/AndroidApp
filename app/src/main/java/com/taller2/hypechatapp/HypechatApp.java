package com.taller2.hypechatapp;

import android.app.Application;

import io.github.kbiakov.codeview.classifier.CodeProcessor;

public class HypechatApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Allows Code view to detect code language
        CodeProcessor.init(this);
    }
}
