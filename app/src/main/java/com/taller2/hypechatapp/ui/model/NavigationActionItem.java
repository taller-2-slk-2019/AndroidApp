package com.taller2.hypechatapp.ui.model;

import com.taller2.hypechatapp.model.NavigationDrawerItemType;
import com.taller2.hypechatapp.model.NavigationDrawerShowable;


public class NavigationActionItem implements NavigationDrawerShowable {

    private String title;
    private int imageId;
    private ActionType actionType;

    public NavigationActionItem(String title, int imageId, ActionType actionType) {
        this.title = title;
        this.imageId = imageId;
        this.actionType = actionType;
    }

    public String getTitle() {
        return title;
    }

    public int getImageId() {
        return imageId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public NavigationDrawerItemType getType() {
        return NavigationDrawerItemType.ITEM_ACTION;
    }

    @Override
    public int compareTo(NavigationDrawerShowable o) {
        return 0;
    }

    public enum ActionType {
        CREATE_CHANNEL,
        CREATE_ORGANIZATION
    }
}
