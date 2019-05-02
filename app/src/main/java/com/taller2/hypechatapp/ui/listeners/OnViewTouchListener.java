package com.taller2.hypechatapp.ui.listeners;

import android.view.MotionEvent;
import android.view.View;

import com.taller2.hypechatapp.ui.activities.utils.KeyboardHelper;

public class OnViewTouchListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        KeyboardHelper.hideKeyboard(view);
        view.setFocusable(true);
        view.requestFocus();
        return false;
    }
}
