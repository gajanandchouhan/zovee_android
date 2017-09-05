package com.zoho.app.model;

/**
 * Created by user on 6/25/2016.
 */
public class DrawerItem {
    private String title;
    private int icon;

    public DrawerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
