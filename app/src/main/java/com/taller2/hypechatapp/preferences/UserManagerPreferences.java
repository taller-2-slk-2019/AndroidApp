package com.taller2.hypechatapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManagerPreferences {

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    private Context context;

    // Shared preferences mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREFER_NAME = "Reg";

    // All Shared Preferences Keys
    public static final String KEY_ORGANIZATION_SELECTED = "organizationSelected";

    public UserManagerPreferences(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveSelectedOrganization(Integer organizationId) {
        editor.putInt(KEY_ORGANIZATION_SELECTED, organizationId);
        // commit changes
        editor.commit();
    }

    public Integer getSelectedOrganization() {
        return pref.getInt(KEY_ORGANIZATION_SELECTED, -1);
    }

}