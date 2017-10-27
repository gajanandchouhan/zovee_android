package com.zoho.app.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoho.app.R;
import com.zoho.app.adapter.ShowMoreAdapter;
import com.zoho.app.fragment.MyAccountFragment;
import com.zoho.app.model.response.CategoryModel;
import com.zoho.app.model.response.SubCategoryModel;
import com.zoho.app.utils.ConstantLib;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by hp on 29-05-2017.
 */

public class MyAccountActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView titleTextView;
    private ImageView backImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        bindView();
        titleTextView.setText(getString(R.string.my_acnt));
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private void bindView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTextView = (TextView) findViewById(R.id.textView_title);
        backImageView = (ImageView) findViewById(R.id.back_imageView);
        backImageView.setVisibility(View.VISIBLE);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.frame, new MyAccountFragment());
        ft.commit();
    }


}
