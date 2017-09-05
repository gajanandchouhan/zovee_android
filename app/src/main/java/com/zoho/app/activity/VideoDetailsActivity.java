package com.zoho.app.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.zoho.app.R;
import com.zoho.app.custom.CustomProgressDialog;
import com.zoho.app.model.response.VideoDetailsModel;
import com.zoho.app.model.response.VideoListModel;
import com.zoho.app.model.response.YoutubeDetailsModel;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.presentor.VideoDetailPresentor;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.PdfCreater;
import com.zoho.app.utils.Utils;
import com.zoho.app.view.VideoDetailView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hp on 29-05-2017.
 */

public class VideoDetailsActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener, VideoDetailView {
    private TextView descTextView;
    private TextView titleTextView;
    private YouTubePlayerView youTubeView;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private ImageView backImageView;
    private VideoListModel videoListModel;
    private CustomProgressDialog progressDialog;
    private String videoId;
    private ImageView downloadImgeView;
    private String videoDesc;
    private String videoTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videodetails);
        titleTextView = (TextView) findViewById(R.id.textView_title);
        videoListModel = (VideoListModel) getIntent().getExtras().getSerializable(ConstantLib.VIDEO_MODEL);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        descTextView = (TextView) findViewById(R.id.textView_descript);
        backImageView = (ImageView) findViewById(R.id.back_imageView);
        downloadImgeView = (ImageView) findViewById(R.id.download_imageView);
        backImageView.setVisibility(View.VISIBLE);
        downloadImgeView.setVisibility(View.VISIBLE);
        descTextView.setMovementMethod(new ScrollingMovementMethod());
        // Initializing video player with developer key
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView.setText(videoListModel.getVideoName());
        downloadImgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoDesc != null) {
                    if (Utils.hasPermissions(VideoDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        PdfCreater.createPdf(VideoDetailsActivity.this, videoDesc, videoTitle + ".pdf");
                    else {
                        ActivityCompat.requestPermissions(VideoDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
                    }
                }

            }
        });
        if (!CheckNetworkState.isOnline(this)) {
            Utils.showToast(this, getString(R.string.no_internet));
            return;
        }
        final String url = "https://www.googleapis.com/youtube/v3/videos?id=" + videoListModel.getYoutubeId() + "&key=" + ConstantLib.DEVELOPER_KEY + "&fields=items(id,snippet(description,channelId,title,categoryId),statistics)&part=snippet,statistics";
        //  new VideoDetailPresentor(this, this).getVideoDetails(videoListModel.getVideoId());
        AsyncTaskCompat.executeParallel(new AsyncTask<Object, Object, String>() {
            CustomProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new CustomProgressDialog(VideoDetailsActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.show();
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Object... params) {
                try {

                    URL mUrl = new URL(url);
                    HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
                    httpConnection.setRequestMethod("GET");
                    httpConnection.setRequestProperty("Content-length", "0");
                    httpConnection.setUseCaches(false);
                    httpConnection.setAllowUserInteraction(false);
                    httpConnection.setConnectTimeout(100000);
                    httpConnection.setReadTimeout(100000);

                    httpConnection.connect();

                    int responseCode = httpConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        return sb.toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String o) {
                Log.v("VideoDetails", o.toString());
                try {
                    if (o != null) {
                        YoutubeDetailsModel youtubeDetailsModel = new Gson().fromJson(o, YoutubeDetailsModel.class);
                        youTubeView.initialize(ConstantLib.DEVELOPER_KEY, VideoDetailsActivity.this);
                        if (youtubeDetailsModel.getItems() != null && youtubeDetailsModel.getItems().size() > 0) {
                            descTextView.setText(youtubeDetailsModel.getItems().get(0).getSnippet().getDescription());
                            videoDesc = youtubeDetailsModel.getItems().get(0).getSnippet().getDescription();
                            videoTitle = youtubeDetailsModel.getItems().get(0).getSnippet().getTitle();
                        }
                        progressDialog.dismiss();
                    } else {
                        Utils.showToast(VideoDetailsActivity.this, getString(R.string.server_error));
                    }

                } catch (Exception e) {

                }

                super.onPostExecute(o);
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            player.loadVideo(videoListModel.getYoutubeId());

            // Hiding player controls
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), youTubeInitializationResult.toString());
            Utils.showToast(this, errorMessage);
        }
    }


    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialog(this);
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void setVideoDetail(VideoDetailsModel videoList) {
        descTextView.setText(Html.fromHtml(videoList.getVideoHTML()));
        youTubeView.initialize(ConstantLib.DEVELOPER_KEY, this);
        videoId = videoList.getVideoYoutubeId();
        videoDesc = videoList.getVideoHTML();
        videoTitle = videoList.getVideoName();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


}
