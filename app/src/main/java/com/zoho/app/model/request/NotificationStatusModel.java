package com.zoho.app.model.request;

/**
 * Created by gajanand on 22/11/17.
 */

public class NotificationStatusModel {
    private int Id;
    private int Notification;

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public void setNotification(int notification) {
        Notification = notification;
    }

    public int getNotification() {
        return Notification;
    }
}
