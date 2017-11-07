package com.zoho.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zoho.app.fragment.CommentsFragment;
import com.zoho.app.fragment.DescriptionFragment;
import com.zoho.app.fragment.LikesFragment;

/**
 * Created by hp on 17-08-2017.
 */

public class DetailsPagerAdapter extends FragmentPagerAdapter {
    public DetailsPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DescriptionFragment();
          /*  case 1:
                return new LikesFragment();
            case 2:
                return new CommentsFragment();*/
        }
        return null;
    }


    @Override
    public int getCount() {
        return 1;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Description";
           /* case 1:
                return "Likes";
            case 2:
                return "Comments";*/
        }
        return "";
    }

}
