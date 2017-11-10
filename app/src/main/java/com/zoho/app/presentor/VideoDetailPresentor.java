package com.zoho.app.presentor;

import android.content.Context;

import com.zoho.app.R;
import com.zoho.app.model.request.IdViewModel;
import com.zoho.app.model.response.VideoDetailsResponseModel;
import com.zoho.app.model.response.VideoListResponseModel;
import com.zoho.app.netcom.ApiClient;
import com.zoho.app.netcom.ApiInterface;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.Utils;
import com.zoho.app.view.VideoDetailView;
import com.zoho.app.view.VideoListView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hp on 11-06-2017.
 */

public class VideoDetailPresentor {
    private Context mContext;
    private VideoDetailView videoListView;

    public VideoDetailPresentor(Context mContext, VideoDetailView videoListView) {
        this.mContext = mContext;
        this.videoListView = videoListView;
    }

    public void getVideoDetails(long id) {
        IdViewModel idViewModel=new IdViewModel();
        idViewModel.setId(id);
        videoListView.showProgress();
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<VideoDetailsResponseModel> videos = apiInterface.getVideoDetails(idViewModel);
        videos.enqueue(new Callback<VideoDetailsResponseModel>() {
            @Override
            public void onResponse(Call<VideoDetailsResponseModel> call, Response<VideoDetailsResponseModel> response) {
                videoListView.hideProgress();
                if (response.body() != null) {
                    VideoDetailsResponseModel videoListResponseModel = response.body();
                    if (videoListResponseModel.getResponseCode().equals(ConstantLib.RESPONSE_SUCCESS)) {
                    videoListView.setVideoDetail(videoListResponseModel.getResponseData());
                    } else {
                        Utils.showToast(mContext, videoListResponseModel.getResponseMessage());
                    }
                } else {
                    Utils.showToast(mContext, mContext.getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<VideoDetailsResponseModel> call, Throwable t) {
                videoListView.hideProgress();
                Utils.showToast(mContext, mContext.getString(R.string.server_error));
            }
        });

    }
}
