package com.zoho.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zoho.app.R;
import com.zoho.app.adapter.FilterCategoryAdapterAdapter;
import com.zoho.app.model.response.CategoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 11-08-2017.
 */

public class FilterDialog extends Dialog {


    private final OnCategorySelectedListner listner;
    private final Context mContext;
    private final List<CategoryModel> categoryList;
    private RecyclerView recyclerView;
    FilterCategoryAdapterAdapter adapterAdapter;

    public FilterDialog(Context context, List<CategoryModel> categoryModelList, OnCategorySelectedListner listner) {
        super(context);
        this.listner = listner;
        this.mContext = context;
        this.categoryList = categoryModelList;
        initView();
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_filter);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapterAdapter = new FilterCategoryAdapterAdapter(mContext, categoryList);
        recyclerView.setAdapter(adapterAdapter);
        findViewById(R.id.textView_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.textView_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                List<CategoryModel> selectedList = new ArrayList<>();
                List<CategoryModel> list = adapterAdapter.getList();
                for (CategoryModel categoryModel : list) {
                    if (categoryModel.isSelected()) {
                        selectedList.add(categoryModel);
                    }
                }
                listner.onCategorySelected(selectedList);
                dismiss();


            }
        });

    }

    public interface OnCategorySelectedListner {
        void onCategorySelected(List<CategoryModel> selectedList);
    }
}
