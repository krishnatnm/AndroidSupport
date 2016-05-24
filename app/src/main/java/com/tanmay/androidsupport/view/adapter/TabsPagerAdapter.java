package com.tanmay.androidsupport.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tanmay.androidsupport.view.activities.TabsAndViewPager;
import com.tanmay.androidsupport.view.fragments.Extra;
import com.tanmay.androidsupport.view.fragments.Home;
import com.tanmay.androidsupport.view.fragments.News;
import com.tanmay.androidsupport.view.fragments.Notifications;
import com.tanmay.androidsupport.view.fragments.Profile;

/**
 * Created by TaNMay on 3/16/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new Home();
            case 1:
                return new News();
            case 2:
                return new Notifications();
            case 3:
                return new Profile();
            default:
                return new Extra();
        }
    }

    @Override
    public int getCount() {
        return TabsAndViewPager.tabCount;
    }
}