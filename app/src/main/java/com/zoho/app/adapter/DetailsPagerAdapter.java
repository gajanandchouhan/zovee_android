package com.zoho.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zoho.app.fragment.DescriptionFragment;

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
            case 1:
                return new DescriptionFragment();
            case 2:
                return new DescriptionFragment();
        }
        return null;
    }


    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}
