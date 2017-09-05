package com.zoho.app.model.response;

import java.util.List;

/**
 * Created by hp on 11-06-2017.
 */

public class VideoListResponseModel extends BaseResponseModel {
    private List<VideoListModel> ResponseData;

    public void setResponseData(List<VideoListModel> responseData) {
        ResponseData = responseData;
    }

    public List<VideoListModel> getResponseData() {
        return ResponseData;
    }
}
