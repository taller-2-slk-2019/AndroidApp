package com.taller2.hypechatapp.ui.activities;

import android.view.View;
import android.widget.ProgressBar;

import com.taller2.hypechatapp.ui.activities.utils.ScreenDisablerHelper;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected ProgressBar loading;

    protected void showLoading() {
        loading.setVisibility(View.VISIBLE);
        ScreenDisablerHelper.disableScreenTouch(getWindow());
    }

    protected void hideLoading() {
        loading.setVisibility(View.GONE);
        ScreenDisablerHelper.enableScreenTouch(getWindow());
    }
}
