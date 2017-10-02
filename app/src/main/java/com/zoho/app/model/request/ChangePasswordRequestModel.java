package com.zoho.app.model.request;

/**
 * Created by hp on 02-10-2017.
 */

public class ChangePasswordRequestModel {


    public int UserId;
    public String CurrentPassword;
    public String NewPassword;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getCurrentPassword() {
        return CurrentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        CurrentPassword = currentPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }
}
