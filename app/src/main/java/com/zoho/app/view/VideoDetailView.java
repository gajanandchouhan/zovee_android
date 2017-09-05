package com.zoho.app.view;

import com.zoho.app.model.response.VideoDetailsModel;
import com.zoho.app.model.response.VideoListModel;

import java.util.List;

/**
 * Created by hp on 15-06-2017.
 */

public interface VideoDetailView {
    void showProgress();

    void setVideoDetail(VideoDetailsModel videoList);

    void hideProgress();
}
