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

public class UserProfileTabFragment extends Fragment {
    public UserProfileTabFragment() {
        // Mandatory empty constructor for the UserProfile Fragment Manager
    }

    public static UserProfileTabFragment newInstance() {
        UserProfileTabFragment fragment = new UserProfileTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_tabs, container, false);

        // Tabhost setup
        FragmentTabHost tabHost = (FragmentTabHost) view.findViewById(R.id
                .user_profile_tabhost);
        tabHost.setup(getContext(), getChildFragmentManager(), R.id
                .realTabContent);

        // Tab setup
        TabHost.TabSpec tab1 = tabHost.newTabSpec("cat1").setIndicator
                ("Category 1");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("cat2").setIndicator
                ("Category 2");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("cat3").setIndicator
                ("Category 3");

        tabHost.addTab(tab1, SortedPostFragment.class, new Bundle());
        tabHost.addTab(tab2, SortedPostFragment.class, new Bundle());
        tabHost.addTab(tab3, SortedPostFragment.class, new Bundle());
        tabHost.setCurrentTab(0);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                SortedPostFragment sortedPostsFragment = SortedPostFragment
                        .newInstance(new SortedPostFragment
                                .FeedPostComparator(),
                                new ArrayList<Post>());
                FragmentTransaction transaction = getChildFragmentManager()
                        .beginTransaction();
                transaction.add(R.id.realTabContent,
                        sortedPostsFragment).commit();
            }
        });

        return view;
    }
}
