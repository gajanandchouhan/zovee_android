package com.zoho.app.model.response;

/**
 * Created by hp on 11-06-2017.
 */

public class VideoDetailsModel {
    private String VideoName;
    private String VideoUrl;
    private String VideoYoutubeId;
    private String VideoHTML;

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getVideoYoutubeId() {
        return VideoYoutubeId;
    }

    public void setVideoYoutubeId(String videoYoutubeId) {
        VideoYoutubeId = videoYoutubeId;
    }

    public String getVideoHTML() {
        return VideoHTML;
    }

    public void setVideoHTML(String videoHTML) {
        VideoHTML = videoHTML;
    }
}
