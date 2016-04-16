package gitmad.bitter.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import gitmad.bitter.R;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.firebase.FirebasePostProvider;
import gitmad.bitter.data.firebase.auth.FirebaseAuthManager;
import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.model.FirebaseImage;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private PostProvider postProvider;
    private List<Post> postList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserFragment() {
    }

    @SuppressWarnings("unused")
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

        FirebaseAuthManager authenticator = new FirebaseAuthManager(
                getActivity());
        authenticator.authenticate();
        GetPostFirebaseTask asyncTask = new GetPostFirebaseTask(view);
        asyncTask.execute(authenticator.getUid());

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
                case 1:
                    return SortedPostFragment.newInstance(new SortedPostFragment
                            .RecentPostComparator(), postList);
                case 2:
                    return SortedPostFragment.newInstance(new SortedPostFragment
                            .TopPostComparator(), postList);
                case 3:
                    return SortedPostFragment.newInstance(new SortedPostFragment
                            .FavoritePostComparator(), postList);
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

    private class GetPostFirebaseTask extends AsyncTask<String, String,
            List<Post>> {
        View view;

        public GetPostFirebaseTask(View view) {
            this.view = view;
        }

        protected List<Post> doInBackground(String... params) {
            FirebasePostProvider firebasePostProvider = new
                    FirebasePostProvider();
            postList = Arrays.asList(firebasePostProvider.getPostsByUser
                    (params[0]));
            return postList;
        }

        protected void onPostExecute(List<Post> posts) {
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter
                    (getChildFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) view.findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(mViewPager);
        }
    }
}
