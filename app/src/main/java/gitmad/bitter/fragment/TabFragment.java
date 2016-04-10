package gitmad.bitter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import gitmad.bitter.R;
import gitmad.bitter.model.Post;

import java.util.ArrayList;

public class TabFragment extends Fragment {
    public TabFragment() {
        // Mandatory empty constructor for the UserProfile Fragment Manager
    }

    public static TabFragment newInstance() {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Tabhost setup
        FragmentTabHost tabHost = new FragmentTabHost(getContext());
        tabHost.setup(getContext(), getChildFragmentManager(), R.id
                .realTabContent);

        // Tab setup
        TabHost.TabSpec tab1 = tabHost.newTabSpec("fragmentA").setIndicator
                ("Fragment A");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("fragmentB").setIndicator
                ("Fragment B");

        tabHost.addTab(tab1, SortedPostFragment.class, new Bundle());
        tabHost.addTab(tab2, SortedPostFragment.class, new Bundle());
        tabHost.setCurrentTab(1);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                SortedPostFragment sortedPostsFragment = SortedPostFragment
                        .newInstance(new SortedPostFragment
                                .FeedPostComparator(),
                                new ArrayList<Post>());
                FragmentTransaction transaction = getChildFragmentManager()
                        .beginTransaction();
                transaction.add(R.id.fragment_feed_sorted_posts_frame,
                        sortedPostsFragment).commit();
            }
        });

        return tabHost;
    }
}
