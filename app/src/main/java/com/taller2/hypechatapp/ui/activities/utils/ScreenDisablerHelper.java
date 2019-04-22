package com.taller2.hypechatapp.ui.activities.utils;

import android.view.Window;
import android.view.WindowManager;

public class ScreenDisablerHelper {

    public static void disableScreenTouch(Window window){
        //Disable the screen touch, so no other button can be clicked
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void enableScreenTouch(Window window){
        //Enable the screen touch
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
