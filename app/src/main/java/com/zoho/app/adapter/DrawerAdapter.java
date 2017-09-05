package com.zoho.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoho.app.R;
import com.zoho.app.model.DrawerItem;

import java.util.List;


/**
 * Created by user on 6/25/2016.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.CustomViewHodler> {
    private final DrawerClickListener clickListener;
    private Context mContext;
    private List<DrawerItem> itemList;
    private int selectedPosition = 0;

    public DrawerAdapter(Context mContext, List<DrawerItem> itemList, DrawerClickListener clickListener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.clickListener = clickListener;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public CustomViewHodler onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drawer, parent, false);

        return new CustomViewHodler(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHodler holder, int position) {
        DrawerItem item = itemList.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.iconImageView.setImageResource(item.getIcon());
        if (selectedPosition == position) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class CustomViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView;
        ImageView iconImageView;

        public CustomViewHodler(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.textView_title);
            iconImageView = (ImageView) itemView.findViewById(R.id.imageView_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getAdapterPosition());
        }
    }

    public interface DrawerClickListener {
        void onClick(int position);
    }

}
