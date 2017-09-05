package com.zoho.app.model.response;

import java.util.List;

/**
 * Created by hp on 11-06-2017.
 */

public class CategoryResponseModel extends BaseResponseModel{
private List<CategoryModel> ResponseData;

    public void setResponseData(List<CategoryModel> responseData) {
        ResponseData = responseData;
    }

    public List<CategoryModel> getResponseData() {
        return ResponseData;
    }
}
