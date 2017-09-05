package com.zoho.app.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;
import com.zoho.app.R;
import com.zoho.app.adapter.VideoListAdapter;
import com.zoho.app.custom.CustomProgressDialog;
import com.zoho.app.model.response.SubCategoryModel;
import com.zoho.app.model.response.VideoListModel;
import com.zoho.app.presentor.VideoListPresentor;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.view.VideoListView;

import java.lang.reflect.Field;
import java.util.List;

import static com.zoho.app.R.id.recyclerView;

/**
 * Created by hp on 29-05-2017.
 */

public class VideoListActivity extends BaseActivity implements VideoListView {
    private RecyclerView mRecyclerView;
    private VideoListAdapter adapter;
    private Toolbar toolbar;
    private TextView titleTextView;
    private ImageView backImageView;
    private ImageView bannerImageView;
    private CustomProgressDialog progressDialog;
    private VideoListPresentor presentor;


    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        bindView();
        presentor = new VideoListPresentor(this, this);
        SubCategoryModel subCategoryModel = (SubCategoryModel) getIntent().getExtras().getSerializable(ConstantLib.SUB_MODEL);
        titleTextView.setText(subCategoryModel.getSubCategoryName());
        Picasso.with(bannerImageView.getContext()).load(subCategoryModel.getSubCategooryThumbUrl()).into(bannerImageView);
        presentor.getVideoList(subCategoryModel.getSubCategoryId());
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7077554208908443/4419549137");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.v("Ad","failed");
            }
        });


    }

    private void setAdapter(List<VideoListModel> list) {
        adapter = new VideoListAdapter(this, list);
        mRecyclerView.setAdapter(adapter);
    }

    private void bindView() {
        mRecyclerView = (RecyclerView) findViewById(recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTextView = (TextView) findViewById(R.id.textView_title);
        backImageView = (ImageView) findViewById(R.id.back_imageView);
        bannerImageView = (ImageView) findViewById(R.id.bannerImageView);
        backImageView.setVisibility(View.VISIBLE);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
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
    public void setVideoList(List<VideoListModel> videoList) {
        setAdapter(videoList);
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        searchEditText.setCursorVisible(true);
        searchEditText.setBackgroundResource(R.drawable.search_box);
        searchEditText.setHint("Search");
        try {
            Field mDrawable = SearchView.class.getDeclaredField("mSearchHintIcon");
            mDrawable.setAccessible(true);
            Drawable drawable = (Drawable) mDrawable.get(searchView);
            drawable.setBounds(0, 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

}
