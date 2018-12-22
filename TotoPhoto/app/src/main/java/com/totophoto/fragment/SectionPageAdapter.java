package com.totophoto.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class SectionPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    /**
     * The function add a new fragment
     * @param fragment The fragment to add
     */
    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    /**
     *
     * @param fm
     */
    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}