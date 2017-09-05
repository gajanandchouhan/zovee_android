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
public class ShowMoreAdapter extends RecyclerView.Adapter<ShowMoreAdapter.CustomViewHodler> implements Filterable {
    private Context mContext;
    private List<SubCategoryModel> itemList;
    private List<SubCategoryModel> filteredList;

    public ShowMoreAdapter(Context mContext, List itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
        filteredList = new ArrayList<>();
        filteredList.addAll(itemList);

        Collections.sort(filteredList, new Comparator<SubCategoryModel>() {

            @Override
            public int compare(SubCategoryModel lhs, SubCategoryModel rhs) {
                //here getTitle() method return app name...
                return lhs.getSubCategoryName().compareTo(rhs.getSubCategoryName());

            }
        });
    }

    @Override
    public CustomViewHodler onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_showmore, parent, false);
        return new CustomViewHodler(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHodler holder, int position) {
        SubCategoryModel subCategoryModel = filteredList.get(position);
        holder.titleTv.setText(subCategoryModel.getSubCategoryName());
        holder.descTv.setText(subCategoryModel.getSubCategoryDescription());
        Picasso.with(holder.thumImageView.getContext()).load(subCategoryModel.getSubCategooryThumbUrl()).into(holder.thumImageView);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }


    public class CustomViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView descTv;
        ImageView thumImageView;
        TextView titleTv;

        public CustomViewHodler(View view) {
            super(view);
            view.setOnClickListener(this);
            titleTv = (TextView) view.findViewById(R.id.title);
            descTv = (TextView) view.findViewById(R.id.desc);
            thumImageView = (ImageView) view.findViewById(R.id.thumbnail);

        }

        @Override
        public void onClick(View v) {
            SubCategoryModel subCategoryModel = filteredList.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantLib.SUB_MODEL, subCategoryModel);
            Utils.startActivity(mContext, VideoListActivity.class, bundle);
        }

    }

    @Override
    public MainFilter getFilter() {
        MainFilter filter = null;
        if (filter == null) {
            filter = new MainFilter();
        }
        return filter;
    }


    public class MainFilter extends Filter {

        @Override
        public FilterResults performFiltering(CharSequence constraint) {


            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<SubCategoryModel> list = itemList;

            int count = list.size();
            final ArrayList<SubCategoryModel> nlist = new ArrayList<SubCategoryModel>(count);

            SubCategoryModel categoryModel;

            for (int i = 0; i < count; i++) {
                categoryModel = list.get(i);
                if (categoryModel != null && categoryModel.getSubCategoryName().toLowerCase().contains(filterString)) {
                    nlist.add(categoryModel);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<SubCategoryModel>) results.values;
            notifyDataSetChanged();
        }

    }
}
