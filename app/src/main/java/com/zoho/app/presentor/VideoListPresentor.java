package com.zoho.app.presentor;

import android.content.Context;

import com.zoho.app.R;
import com.zoho.app.model.request.IdViewModel;
import com.zoho.app.model.response.CategoryResponseModel;
import com.zoho.app.model.response.VideoListResponseModel;
import com.zoho.app.netcom.ApiClient;
import com.zoho.app.netcom.ApiInterface;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.Utils;
import com.zoho.app.view.VideoListView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hp on 11-06-2017.
 */

public class VideoListPresentor {
    private Context mContext;
    private VideoListView videoListView;

    public VideoListPresentor(Context mContext, VideoListView videoListView) {
        this.mContext = mContext;
        this.videoListView = videoListView;
    }

    public void getVideoList(int id) {
        if (!CheckNetworkState.isOnline(mContext)) {
            Utils.showToast(mContext, mContext.getString(R.string.no_internet));
            return;
        }
        IdViewModel idViewModel=new IdViewModel();
        idViewModel.setId(id);
        videoListView.showProgress();
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<VideoListResponseModel> videos = apiInterface.getVideos(idViewModel);
        videos.enqueue(new Callback<VideoListResponseModel>() {
            @Override
            public void onResponse(Call<VideoListResponseModel> call, Response<VideoListResponseModel> response) {
                videoListView.hideProgress();
                if (response.body() != null) {
                    VideoListResponseModel videoListResponseModel = response.body();
                    if (videoListResponseModel.getResponseCode().equals(ConstantLib.RESPONSE_SUCCESS)) {
                    videoListView.setVideoList(videoListResponseModel.getResponseData());
                    } else {
                        Utils.showToast(mContext, videoListResponseModel.getResponseMessage());
                    }
                } else {
                    Utils.showToast(mContext, mContext.getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<VideoListResponseModel> call, Throwable t) {
                videoListView.hideProgress();
                Utils.showToast(mContext, mContext.getString(R.string.server_error));
            }
        });

    }
}
