package com.zoho.app.model.response;

/**
 * Created by hp on 01-10-2017.
 */

public class LoginResponseModel extends BaseResponseModel {
    private LoginResponseData ResponseData;

    public LoginResponseData getResponseData() {
        return ResponseData;
    }

    public void setResponseData(LoginResponseData responseData) {
        ResponseData = responseData;
    }
}
