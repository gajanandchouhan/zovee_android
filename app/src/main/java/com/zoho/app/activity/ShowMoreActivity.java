package com.zoho.app.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.zoho.app.model.response.CategoryModel;
import com.zoho.app.model.response.SubCategoryModel;
import com.zoho.app.utils.ConstantLib;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by hp on 29-05-2017.
 */

public class ShowMoreActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private ShowMoreAdapter adapter;
    private Toolbar toolbar;
    private TextView titleTextView;
    private ImageView backImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmore);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        bindView();
        if (getIntent().getExtras() != null) {
            CategoryModel categoryModel = (CategoryModel) getIntent().getExtras().getSerializable(ConstantLib.KEY_CATEGORY);
            if (categoryModel.getCategoryName() != null) {
                titleTextView.setText(categoryModel.getCategoryName());
            }
            setAdapter(categoryModel.getSubCategoryList());
        }

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setAdapter(List<SubCategoryModel> subCategoryList) {
        adapter = new ShowMoreAdapter(this, subCategoryList);
        mRecyclerView.setAdapter(adapter);
    }

    private void bindView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTextView = (TextView) findViewById(R.id.textView_title);
        backImageView = (ImageView) findViewById(R.id.back_imageView);
        backImageView.setVisibility(View.VISIBLE);
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
