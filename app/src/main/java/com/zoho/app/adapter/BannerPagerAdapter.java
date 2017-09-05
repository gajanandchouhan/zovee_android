package com.zoho.app.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zoho.app.R;

import java.util.List;

/**
 * Created by hp on 17-08-2017.
 */

public class BannerPagerAdapter extends PagerAdapter {
    private Context mContext;
    List<String> banners;
    public BannerPagerAdapter(Context context, List<String> banners) {
        mContext = context;
        this.banners=banners;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_banner, collection, false);
        ImageView imageView=(ImageView) layout.findViewById(R.id.imageView);
        Picasso.with(mContext).load(banners.get(position)).into(imageView);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}
