package com.zoho.app.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;
import com.zoho.app.R;
import com.zoho.app.adapter.DetailsPagerAdapter;
import com.zoho.app.custom.CustomProgressDialog;
import com.zoho.app.model.response.VideoListModel;
import com.zoho.app.model.response.YoutubeDetailsModel;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.PdfCreater;
import com.zoho.app.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hp on 01-10-2017.
 */

public class DetailsFragment extends Fragment {
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private VideoListModel videoListModel;
    private CustomProgressDialog progressDialog;
    private String videoId;
    private String videoDesc;
    private String videoTitle;
    private TabLayout tabLayout;
    private ViewPager pager;
    FragmentActivity mContext;
    private YouTubePlayer YPlayer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof FragmentActivity) {
            mContext = (FragmentActivity) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detailss, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        videoListModel = (VideoListModel) getArguments().getSerializable(ConstantLib.VIDEO_MODEL);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(tabListner);
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(new DetailsPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(pager);
        if (!CheckNetworkState.isOnline(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.no_internet));
            return;
        }
        final String url = "https://www.googleapis.com/youtube/v3/videos?id=" + videoListModel.getYoutubeId() + "&key=" + ConstantLib.DEVELOPER_KEY + "&fields=items(id,snippet(description,channelId,title,categoryId),statistics)&part=snippet,statistics";
        //  new VideoDetailPresentor(this, this).getVideoDetails(videoListModel.getVideoId());
       new AsyncTask<Object, Object, String>() {
            CustomProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new CustomProgressDialog(getActivity());
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
                        initializePlayer();
                        if (youtubeDetailsModel.getItems() != null && youtubeDetailsModel.getItems().size() > 0) {
                            // descTextView.setText(youtubeDetailsModel.getItems().get(0).getSnippet().getDescription());
                            videoDesc = youtubeDetailsModel.getItems().get(0).getSnippet().getDescription();
                            videoTitle = youtubeDetailsModel.getItems().get(0).getSnippet().getTitle();
                            pager.setAdapter(new DetailsPagerAdapter(getChildFragmentManager()));
                        }
                        progressDialog.dismiss();
                    } else {
                        Utils.showToast(getActivity(), getString(R.string.server_error));
                    }

                } catch (Exception e) {

                }

                super.onPostExecute(o);
            }
        }.execute();
    }

    private void initializePlayer() {

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(ConstantLib.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    YPlayer = youTubePlayer;
                    YPlayer.setFullscreen(false);

/*
                    YPlayer.loadVideo("2zNSgSzhBfM");
*/
                    YPlayer.cueVideo(videoListModel.getYoutubeId());
                    /*YPlayer.play();*/
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }


    TabLayout.OnTabSelectedListener tabListner = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };


    public void downloadPdf() {
        if (videoDesc != null) {

            PdfCreater.createPdf(getActivity(), videoDesc, videoTitle + ".pdf");

        }
    }

    public String getVideoDesc() {
        return videoDesc;
    }


}


