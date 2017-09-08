package com.zoho.app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.zoho.app.R;
import com.zoho.app.activity.ShowMoreActivity;
import com.zoho.app.custom.AutoScrollViewPager;
import com.zoho.app.model.response.CategoryModel;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user on 6/25/2016.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context mContext;
    private List<CategoryModel> itemList;
    private List<CategoryModel> filteredList;
    private static final int HEADER_VIEW = 1;
    private static final int ITEM_VIEW = 2;

    public MainAdapter(Context mContext, List itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
        filteredList = new ArrayList<>();
        filteredList.addAll(itemList);
        filteredList.add(0, null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header_main, parent, false);
            return new HeaderViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);
            return new CustomViewHodler(itemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CustomViewHodler) {
            CustomViewHodler customViewHodler = (CustomViewHodler) holder;
            CategoryModel categoryModel = filteredList.get(position);
            customViewHodler.tvTitle.setText(categoryModel.getCategoryName());
            if (categoryModel.getSubCategoryList() != null && categoryModel.getSubCategoryList().size() > 2) {
                customViewHodler.showMoreTextView.setVisibility(View.VISIBLE);
                //    customViewHodler.leftArrowImageView.setVisibility(View.VISIBLE);
                // customViewHodler.rightArrow.setVisibility(View.VISIBLE);
            } else {
                customViewHodler.showMoreTextView.setVisibility(View.GONE);
                //   customViewHodler.leftArrowImageView.setVisibility(View.GONE);
                // customViewHodler.rightArrow.setVisibility(View.GONE);
            }
            customViewHodler.mRecyclerView.setAdapter(new SubCategoryAdapter(mContext, categoryModel.getSubCategoryList()));
        }

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (filteredList.get(position) == null) {
            return HEADER_VIEW;
        }
        return ITEM_VIEW;
    }

    @Override
    public MainFilter getFilter() {
        MainFilter filter = null;
        if (filter == null) {
            filter = new MainFilter();
        }
        return filter;
    }

    public class CustomViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerView mRecyclerView;
        TextView tvTitle;
        TextView showMoreTextView;
        ImageView leftArrowImageView;
        ImageView rightArrow;

        public CustomViewHodler(View itemView) {
            super(itemView);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView_cat);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_cat_title);
            showMoreTextView = (TextView) itemView.findViewById(R.id.textView_more);
            leftArrowImageView = (ImageView) itemView.findViewById(R.id.imageView_arrow_left);
            rightArrow = (ImageView) itemView.findViewById(R.id.imageView_arrow_right);
            showMoreTextView.setOnClickListener(this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        }

        @Override
        public void onClick(View v) {
            CategoryModel categoryModel = filteredList.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantLib.KEY_CATEGORY, categoryModel);
            Utils.startActivity(mContext, ShowMoreActivity.class, bundle);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        AutoScrollViewPager autoScrollViewPager;
        CirclePageIndicator indicator;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            autoScrollViewPager = (AutoScrollViewPager) itemView.findViewById(R.id.autoPager);
            autoScrollViewPager.startAutoScroll();
            autoScrollViewPager.setInterval(3000);
            autoScrollViewPager.setCycle(true);
            autoScrollViewPager.setStopScrollWhenTouch(true);
            autoScrollViewPager.setAdapter(new BannerPagerAdapter(mContext, itemList.get(0).getMasterBanners()));
            indicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
            indicator.setViewPager(autoScrollViewPager);
        }
    }

    public class MainFilter extends Filter {

        @Override
        public FilterResults performFiltering(CharSequence constraint) {


            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<CategoryModel> list = itemList;

            int count = list.size();
            final ArrayList<CategoryModel> nlist = new ArrayList<CategoryModel>(count);

            CategoryModel categoryModel;

            for (int i = 0; i < count; i++) {
                categoryModel = list.get(i);
                if (categoryModel != null && categoryModel.getCategoryName().toLowerCase().contains(filterString)) {
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
            filteredList = (ArrayList<CategoryModel>) results.values;
            filteredList.add(0, null);
            notifyDataSetChanged();
        }

    }
}
