package com.zoho.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoho.app.R;
import com.zoho.app.fragment.DescriptionFragment;
import com.zoho.app.fragment.DetailsFragment;
import com.zoho.app.model.response.VideoListModel;
import com.zoho.app.utils.ConstantLib;

/**
 * Created by hp on 29-05-2017.
 */

public class VideoDetailsActivity extends AppCompatActivity {
    private TextView titleTextView;
    private ImageView backImageView;
    private VideoListModel videoListModel;
    private ImageView downloadImgeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videodetails);
        titleTextView = (TextView) findViewById(R.id.textView_title);
        videoListModel = (VideoListModel) getIntent().getExtras().getSerializable(ConstantLib.VIDEO_MODEL);
        backImageView = (ImageView) findViewById(R.id.back_imageView);
        downloadImgeView = (ImageView) findViewById(R.id.download_imageView);
        backImageView.setVisibility(View.VISIBLE);
        downloadImgeView.setVisibility(View.VISIBLE);
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


            }
        });
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(ConstantLib.VIDEO_MODEL,videoListModel);
        detailsFragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.frame, detailsFragment);
        ft.commit();
    }


}
