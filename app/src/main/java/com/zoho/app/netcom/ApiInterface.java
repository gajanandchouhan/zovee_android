package com.zoho.app.netcom;

import com.zoho.app.model.request.IdViewModel;
import com.zoho.app.model.response.CategoryResponseModel;
import com.zoho.app.model.response.VideoDetailsResponseModel;
import com.zoho.app.model.response.VideoListResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by hp on 03-06-2017.
 */

public interface ApiInterface {
    @POST("getCategories")
    Call<CategoryResponseModel> getCategories();
    @POST("getVideos")
    Call<VideoListResponseModel> getVideos(@Body IdViewModel idViewModel);
    @POST("getVideoDetails")
    Call<VideoDetailsResponseModel> getVideoDetails(@Body IdViewModel idViewModel);

}
