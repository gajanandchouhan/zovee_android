package com.zoho.app.model.response;

import java.io.Serializable;

/**
 * Created by hp on 11-06-2017.
 */

public class VideoListModel implements Serializable {
    private int VideoId;
    private String VideoName;
    private String YoutubeId;

    public int getVideoId() {
        return VideoId;
    }

    public void setVideoId(int videoId) {
        VideoId = videoId;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public String getYoutubeId() {
        return YoutubeId;
    }

    public void setYoutubeId(String YoutubeId) {
        this.YoutubeId = YoutubeId;
    }
}
