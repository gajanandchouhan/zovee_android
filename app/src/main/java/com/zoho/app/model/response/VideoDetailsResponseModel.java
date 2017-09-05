package com.zoho.app.model.response;

/**
 * Created by hp on 11-06-2017.
 */

public class VideoDetailsResponseModel extends BaseResponseModel {

    private VideoDetailsModel ResponseData;

    public void setResponseData(VideoDetailsModel responseData) {
        ResponseData = responseData;
    }

    public VideoDetailsModel getResponseData() {
        return ResponseData;
    }
}
