package com.zoho.app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.zoho.app.utils.Utils;

/**
 * Created by hp on 29-05-2017.
 */

public class VideoDetailsActivity extends AppCompatActivity {
    private TextView titleTextView;
    private ImageView backImageView;
    private VideoListModel videoListModel;
    private ImageView downloadImgeView;
    private DetailsFragment detailsFragment;

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
        detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantLib.VIDEO_MODEL, videoListModel);
        detailsFragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.frame, detailsFragment);
        ft.commit();
        downloadImgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.hasPermissions(VideoDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    detailsFragment.downloadPdf();
                } else {
                    ActivityCompat.requestPermissions(VideoDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
                }

            }

        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==111&&grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            detailsFragment.downloadPdf();
            // permission was granted, yay! Do the
            // contacts-related task you need to do.

        }
    }
}
