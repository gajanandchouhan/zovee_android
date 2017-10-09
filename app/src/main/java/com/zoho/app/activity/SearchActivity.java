package com.zoho.app.activity;

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

public class SearchActivity extends AppCompatActivity implements VideoListView {

    private RecyclerView mRecyclerView;
    private VideoListAdapter adapter;
    private Toolbar toolbar;
    private TextView titleTextView;
    private ImageView backImageView;
    private CustomProgressDialog progressDialog;
    private VideoListPresentor presentor;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        bindView();
        presentor = new VideoListPresentor(this, this);
        //SubCategoryModel subCategoryModel = (SubCategoryModel) getIntent().getExtras().getSerializable(ConstantLib.SUB_MODEL);
        titleTextView.setText("Search");
        presentor.getVideoList(0);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setAdapter(List<VideoListModel> list) {
        adapter = new VideoListAdapter(this, list,false);
        mRecyclerView.setAdapter(adapter);
    }

    private void bindView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTextView = (TextView) findViewById(R.id.textView_title);
        backImageView = (ImageView) findViewById(R.id.back_imageView);
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
        myActionMenuItem.expandActionView();
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
