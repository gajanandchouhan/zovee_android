package com.zoho.app.model.request;

/**
 * Created by hp on 11-11-2017.
 */

public class HelpRequestModel {

    private int UserId;
    private String Subject;
    private String Message;

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getSubject() {
        return Subject;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessage() {
        return Message;
    }
}
