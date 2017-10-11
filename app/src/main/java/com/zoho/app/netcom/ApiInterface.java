package com.zoho.app.netcom;

import com.zoho.app.model.request.ChangePasswordRequestModel;
import com.zoho.app.model.request.EmailViewModel;
import com.zoho.app.model.request.IdViewModel;
import com.zoho.app.model.request.LoginRequestModel;
import com.zoho.app.model.response.BaseResponseModel;
import com.zoho.app.model.response.CategoryResponseModel;
import com.zoho.app.model.response.LoginResponseModel;
import com.zoho.app.model.response.SignupResponseModel;
import com.zoho.app.model.response.VideoDetailsResponseModel;
import com.zoho.app.model.response.VideoListResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    //Multipart params :- FirstName,LastName,Email,Password,CompanyName,ProfilePicture
    @Multipart
    @POST("signUp")
    Call<LoginResponseModel> doSignup(@Part("FirstName") RequestBody fName, @Part("LastName") RequestBody lName,
                                      @Part("Email") RequestBody email, @Part("Password") RequestBody password,
                                      @Part("CompanyName") RequestBody companyName, @Part MultipartBody.Part file,
                                      @Part("DeviceToken") RequestBody token, @Part("SocialId") RequestBody socialId,
                                      @Part("RegistrationBy") RequestBody regType);

    @POST("login")
    Call<LoginResponseModel> doLogin(@Body LoginRequestModel loginRequestModel);

    @POST("ChangePassword")
    Call<BaseResponseModel> changePassword(@Body ChangePasswordRequestModel changePasswordRequestModel);

    @POST("ForgotPassword")
    Call<BaseResponseModel> forgotPassword(@Body EmailViewModel emailViewModel);

    @Multipart
    @POST("updateUserProfile")
    Call<LoginResponseModel> updateProfile(@Part("FirstName") RequestBody fName, @Part("LastName") RequestBody lName,
                                           @Part("UserId") RequestBody uid,
                                           @Part("CompanyName") RequestBody companyName, @Part MultipartBody.Part file);


}
