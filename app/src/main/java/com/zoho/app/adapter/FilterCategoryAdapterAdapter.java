package com.zoho.app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zoho.app.R;
import com.zoho.app.activity.VideoListActivity;
import com.zoho.app.model.response.CategoryModel;
import com.zoho.app.model.response.SubCategoryModel;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by user on 6/25/2016.
 */
public class FilterCategoryAdapterAdapter extends RecyclerView.Adapter<FilterCategoryAdapterAdapter.CustomViewHodler> {
    private Context mContext;
    private List<CategoryModel> itemList;

    public FilterCategoryAdapterAdapter(Context mContext, List itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public CustomViewHodler onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filter, parent, false);
        return new CustomViewHodler(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHodler holder, int position) {
        CategoryModel subCategoryModel = itemList.get(position);
        holder.titleTv.setText(subCategoryModel.getCategoryName());
        holder.itemView.setSelected(subCategoryModel.isSelected());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public List<CategoryModel> getList() {
        return itemList;
    }


    public class CustomViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView checkImageView;
        TextView titleTv;

        public CustomViewHodler(View view) {
            super(view);
            view.setOnClickListener(this);
            titleTv = (TextView) view.findViewById(R.id.title);
            checkImageView = (ImageView) view.findViewById(R.id.check);

        }

        @Override
        public void onClick(View v) {
            CategoryModel categoryModel = itemList.get(getAdapterPosition());
            categoryModel.setSelected(!(categoryModel.isSelected()));
            notifyDataSetChanged();
        }

    }
}
