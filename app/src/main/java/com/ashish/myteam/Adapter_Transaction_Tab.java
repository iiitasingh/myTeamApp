package com.ashish.myteam;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Adapter_Transaction_Tab extends FragmentPagerAdapter {

    private int numTabs;
    public Adapter_Transaction_Tab(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
        ;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment =  new Trans_Debit();
                break;
            case 1:
                fragment = new Trans_Credit();
                break;
            default:
                fragment = null;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
