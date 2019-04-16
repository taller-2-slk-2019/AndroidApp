package com.taller2.hypechatapp.model;

public enum NavigationDrawerItemType {
    CHANNELS("CHN"),
    MESSAGES("MESSAGES"),
    ITEM_ACTION("ACT");

    private String code;

    NavigationDrawerItemType(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
