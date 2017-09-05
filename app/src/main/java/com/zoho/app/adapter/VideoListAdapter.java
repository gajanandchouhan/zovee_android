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
import com.zoho.app.activity.VideoDetailsActivity;
import com.zoho.app.model.response.VideoListModel;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user on 6/25/2016.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.CustomViewHodler> implements Filterable {
    private List<VideoListModel> filteredList;
    private Context mContext;
    private List<VideoListModel> itemList;

    public VideoListAdapter(Context mContext, List itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.filteredList = new ArrayList<VideoListModel>();
        this.filteredList.addAll(itemList);
    }

    @Override
    public CustomViewHodler onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_videolist, parent, false);
        return new CustomViewHodler(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHodler holder, int position) {
        VideoListModel videoListModel = filteredList.get(position);
        holder.titleTextView.setText(videoListModel.getVideoName());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public MainFilter getFilter() {
        MainFilter mainFilter = null;
        if (mainFilter == null) {
            mainFilter = new MainFilter();
        }
        return mainFilter;
    }


    public class CustomViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumImageView;
        TextView titleTextView;

        public CustomViewHodler(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTextView = (TextView) itemView.findViewById(R.id.textView_videotitle);
            thumImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantLib.VIDEO_MODEL, filteredList.get(getAdapterPosition()));
            Utils.startActivity(mContext, VideoDetailsActivity.class, bundle);
        }
    }

    public class MainFilter extends Filter {

        @Override
        public FilterResults performFiltering(CharSequence constraint) {


            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<VideoListModel> list = itemList;

            int count = list.size();
            final ArrayList<VideoListModel> nlist = new ArrayList<VideoListModel>(count);

            VideoListModel categoryModel;

            for (int i = 0; i < count; i++) {
                categoryModel = list.get(i);
                if (categoryModel != null && categoryModel.getVideoName().toLowerCase().contains(filterString)) {
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
            filteredList = (ArrayList<VideoListModel>) results.values;
            notifyDataSetChanged();
        }

    }
}
