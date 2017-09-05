package com.zoho.app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zoho.app.R;
import com.zoho.app.activity.VideoListActivity;
import com.zoho.app.model.response.SubCategoryModel;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.Utils;

import java.util.List;


/**
 * Created by user on 6/25/2016.
 */
public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.CustomViewHodler> {
    private Context mContext;
    private List<SubCategoryModel> itemList;

    public SubCategoryAdapter(Context mContext, List itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public CustomViewHodler onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_category, parent, false);
        return new CustomViewHodler(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHodler holder, int position) {
        SubCategoryModel subCategoryModel = itemList.get(position);
        holder.titleTv.setText(subCategoryModel.getSubCategoryName());
        Picasso.with(holder.thumImageView.getContext()).load(subCategoryModel.getSubCategooryThumbUrl()).into(holder.thumImageView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class CustomViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumImageView;
        TextView titleTv;

        public CustomViewHodler(View view) {
            super(view);
            view.setOnClickListener(this);
            titleTv = (TextView) view.findViewById(R.id.title);
            thumImageView = (ImageView) view.findViewById(R.id.thumbnail);

        }

        @Override
        public void onClick(View v) {
            SubCategoryModel subCategoryModel = itemList.get(getAdapterPosition());
            Bundle bundle=new Bundle();
            bundle.putSerializable(ConstantLib.SUB_MODEL,subCategoryModel);
            Utils.startActivity(mContext, VideoListActivity.class, bundle);
        }
    }


}
