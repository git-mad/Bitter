package gitmad.bitter.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gitmad.bitter.R;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.firebase.FirebasePostProvider;
import gitmad.bitter.data.firebase.FirebaseUserProvider;
import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.model.Post;

import java.util.Arrays;
import java.util.List;

public class UserFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private PostProvider postProvider;
    private UserProvider userProvider;
    private List<Post> postList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserFragment() {
    }

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /*
 * onAttach(Context) is not called on pre API 23 versions of Android and
 * onAttach(Activity) is deprecated
 * Use onAttachToContext instead
 */
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this
                .getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);


        // TODO change to FireBase
        postProvider = new FirebasePostProvider();
        userProvider = new FirebaseUserProvider();
        // FIXME change to get posts by user
        //postList = Arrays.asList(postProvider.getPostsByUser(params[0]));
        postList = Arrays.asList(postProvider.getPosts(Integer.MAX_VALUE));

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().findViewById(R.id.appBar).setElevation(8);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public String getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Profile";
                case 1:
                    return "Recent";
                case 2:
                    return "Top";
                case 3:
                    return "Favorite";
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return UserProfileFragment.newInstance();
//                case 1:
//                    return SortedPostFragment.newInstance(new SortedPostFragment
//                            .RecentPostComparator(), postList);
//                case 2:
//                    return SortedPostFragment.newInstance(new SortedPostFragment
//                            .TopPostComparator(), postList);
//                case 3:
//                    return SortedPostFragment.newInstance(new SortedPostFragment
//                            .FavoritePostComparator(), postList);
            }
            return null;
        }
    }

    /*
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().findViewById(R.id.appBar).setElevation(0);
            System.out.println("Elevation is 0!");
        }
    }

    private class GetPostsAsyncTask extends AsyncTask<Integer, Void, Post[]> {

        String[] authorNames;

        @Override
        protected Post[] doInBackground(Integer... params) {
            int numPostsToRetrieve = params[0];
            Post[] posts = postProvider.getPosts(numPostsToRetrieve);

            authorNames = new String[numPostsToRetrieve];

            for (int i = 0; i < posts.length; i++) {
                authorNames[i] = userProvider.getAuthorOfPost(posts[i]).getName();
            }

            return posts;
        }

        @Override
        protected void onPostExecute(Post[] posts) {
            super.onPostExecute(posts);
            final List<Post> postList = Arrays.asList(posts);
            final List<String> authorNamesList = Arrays.asList(authorNames);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SortedPostFragment sortedPostsFragment = SortedPostFragment
                            .newInstance(new SortedPostFragment.FeedPostComparator(),
                                    postList, authorNamesList);
                    FragmentTransaction transaction = getChildFragmentManager()
                            .beginTransaction();
                    transaction.add(R.id.fragment_feed_sorted_posts_frame,
                            sortedPostsFragment).commit();
                }
            });
        }
    }
}
