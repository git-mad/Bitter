package gitmad.bitter.activity;

/**
 * Created by Clayton on 10/7/2015.
 */
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.app.FragmentManager;


import gitmad.bitter.fragment.UserProfileFragment;
import gitmad.bitter.ui.TabFragmentPagerAdapter;
import gitmad.bitter.R;

public class TabViewActivity extends AppCompatActivity implements UserProfileFragment.OnFragmentInteractionListener{

//    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.tabs_list_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabFragmentPagerAdapter tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), TabViewActivity.this);
        viewPager.setAdapter(tabFragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

    }

    @Override
    public void onFragmentInteraction(String uri) {
        int d = 0;
    }
}
