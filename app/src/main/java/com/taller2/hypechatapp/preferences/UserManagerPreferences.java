package com.taller2.hypechatapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManagerPreferences {

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREFER_NAME = "Reg";
    private static final int DEFAULT_ID = 0;

    // All Shared Preferences Keys
    private static final String KEY_ORGANIZATION_SELECTED = "organizationSelected";
    private static final String KEY_CHANNEL_SELECTED = "channelSelected";

    public UserManagerPreferences(Context context) {
        pref = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveSelectedOrganization(Integer organizationId) {
        editor.putInt(KEY_ORGANIZATION_SELECTED, organizationId);
        editor.commit();
    }

    public void saveSelectedChannel(Integer channelId) {
        editor.putInt(KEY_CHANNEL_SELECTED + getSelectedOrganization(), channelId);
        editor.commit();
    }

    public Integer getSelectedOrganization() {
        return pref.getInt(KEY_ORGANIZATION_SELECTED, DEFAULT_ID);
    }

    public Integer getSelectedChannel() {
        return pref.getInt(KEY_CHANNEL_SELECTED + getSelectedOrganization(), DEFAULT_ID);
    }

    public void clearSelectedChannel() {
        editor.putInt(KEY_CHANNEL_SELECTED + getSelectedOrganization(), DEFAULT_ID);
        editor.commit();
    }
}