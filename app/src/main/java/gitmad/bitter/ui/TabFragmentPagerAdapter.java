// Copyright 2004-present Facebook. All Rights Reserved.

package gitmad.bitter.ui;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import gitmad.bitter.fragment.FavoritePostFragment;
import gitmad.bitter.fragment.RecentPostFragment;
import gitmad.bitter.fragment.TopPostFragment;
import gitmad.bitter.fragment.UserProfileFragment;

/**
 * Created by Clayton on 10/7/15.
 */
public class TabFragmentPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

    protected static String tabTitles[] = new String[]{"My Profile", "Recent Post", "Favorite Posts", "Top Posts"};
    private Context context;

    public TabFragmentPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new UserProfileFragment();
            case 1:
                return new RecentPostFragment();
            case 2:
                return new FavoritePostFragment();
            case 3:
                return new TopPostFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}
