package com.zoho.app.model.request;

/**
 * Created by hp on 02-10-2017.
 */

public class LoginRequestModel {
    private String Email;
    private String Password;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
