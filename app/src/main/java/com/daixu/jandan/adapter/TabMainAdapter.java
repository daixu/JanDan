package com.daixu.jandan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Liompei
 * time : 2017/11/7 13:16
 * 1137694912@qq.com
 * remark:
 */

public class TabMainAdapter extends FragmentPagerAdapter {

    private String[] tabNames;
    private Fragment[] mFragments;

    public TabMainAdapter(FragmentManager fm, String[] tabNames, Fragment[] fragments) {
        super(fm);
        this.tabNames = tabNames;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return tabNames.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }
}
