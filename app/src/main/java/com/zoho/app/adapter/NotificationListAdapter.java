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

import com.zoho.app.R;
import com.zoho.app.activity.VideoListActivity;
import com.zoho.app.fcm.NotificationDataModel;
import com.zoho.app.model.response.SubCategoryModel;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user on 6/25/2016.
 */
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.CustomViewHodler> {
    private Context mContext;
    private List<NotificationDataModel> itemList;

    public NotificationListAdapter(Context mContext, List itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public CustomViewHodler onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new CustomViewHodler(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHodler holder, int position) {
        holder.titleTv.setText(itemList.get(position).getTitle());
        holder.descTv.setText(itemList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class CustomViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView descTv;
        TextView titleTv;

        public CustomViewHodler(View view) {
            super(view);
            view.setOnClickListener(this);
            titleTv = (TextView) view.findViewById(R.id.textView_title);
            descTv = (TextView) view.findViewById(R.id.textView_msg);

        }

        @Override
        public void onClick(View v) {
            NotificationDataModel model = itemList.get(getAdapterPosition());
            SubCategoryModel subCategoryModel = new SubCategoryModel();
            subCategoryModel.setSubCategoryId((long) model.getSubCategoryId());
            subCategoryModel.setSubCategoryName(model.getSubcategoryName());
            subCategoryModel.setSubCategoryDescription(model.getSubCategoryDescription());
            subCategoryModel.setSubCategooryThumbUrl(model.getSubCategoryThumbUrl());
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantLib.SUB_MODEL, subCategoryModel);
            Utils.startActivity(mContext, VideoListActivity.class, bundle);
        }

    }

}
