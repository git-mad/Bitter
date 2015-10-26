// Copyright 2004-present Facebook. All Rights Reserved.

package gitmad.bitter.ui;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

;

/**
 * Created by Clayton on 10/7/15.
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener {

    private Fragment mFragment;
    private final String mTag;
    private final Class<T> mClass;
    private final Activity mActivity;

    public TabListener(Activity activity, String tag, Class<T> claz){
        mActivity = activity;
        mTag = tag;
        mClass = claz;
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft){
        if (mFragment == null){
            mFragment = Fragment.instantiate(mActivity, mClass.getName());
            ft.add(android.R.id.content, mFragment, mTag);
        } else {
            ft.attach(mFragment);
        }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft){
        if (mFragment != null){
            ft.detach(mFragment);
        }
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft){

    }
}
