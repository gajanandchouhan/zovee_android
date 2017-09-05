package com.zoho.app.model.response;

/**
 * Created by hp on 11-06-2017.
 */

public class BaseResponseModel {
    private String ResponseCode;
    private String ResponseMessage;

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }
}
