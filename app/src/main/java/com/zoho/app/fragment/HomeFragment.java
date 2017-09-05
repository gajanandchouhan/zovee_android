package com.zoho.app.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoho.app.R;
import com.zoho.app.adapter.MainAdapter;
import com.zoho.app.custom.CustomProgressDialog;
import com.zoho.app.model.response.CategoryModel;
import com.zoho.app.presentor.HomePresentor;
import com.zoho.app.view.HomeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 31-05-2017.
 */

public class HomeFragment extends Fragment implements HomeView {
    private CustomProgressDialog progressDialog;
    RecyclerView mRecyclerView;
    private MainAdapter adapter;
    private View view;
    private HomePresentor presentor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        bindView();
        presentor = new HomePresentor(getActivity(), this);
        presentor.getCategories();
        return view;
    }


    private void setAdapter(List<CategoryModel> categoryList) {
        adapter = new MainAdapter(getActivity(), categoryList);
        mRecyclerView.setAdapter(adapter);
    }

    private void bindView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialog(getActivity());
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void setCategoryList(List<CategoryModel> categoryList) {
        if (categoryList != null && categoryList.size() > 0) {
            setAdapter(categoryList);
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public void filterCategoryList(CharSequence text) {
        adapter.getFilter().filter(text);
    }
}
