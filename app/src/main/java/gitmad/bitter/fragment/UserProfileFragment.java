package gitmad.bitter.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import gitmad.bitter.R;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.firebase.FirebaseUserProvider;
import gitmad.bitter.data.firebase.auth.FirebaseAuthManager;
import gitmad.bitter.model.FirebaseImage;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserProfileFragment extends Fragment {
    public UserProfileFragment() {
        // Mandatory empty constructor for the UserProfile Fragment Manager
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile,
                container, false);

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

        FirebaseAuthManager authenticator = new FirebaseAuthManager(
                getActivity());
        authenticator.authenticate();
        GetUserFirebaseTask asyncTask = new GetUserFirebaseTask(view);
        asyncTask.execute(authenticator.getUid());

        return view;
    }

    private class GetUserFirebaseTask extends AsyncTask<String, String, User> {
        private User userData;
        private View view;

        public GetUserFirebaseTask(View view) {
            this.view = view;
        }

        @Override
        protected User doInBackground(String... params) {
            UserProvider provider = new FirebaseUserProvider();
            userData = provider.getUser(params[0]);
            return userData;
        }

        protected void onPostExecute(User myUser) {
            // FIXME get the image from the user object
            ImageView pic = (ImageView) view.findViewById(R.id
                    .user_profile_pic);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap
                    .tejunareddy);
            Bitmap conv_bm = FirebaseImage.toRoundedRectBitmap(bm, 500);
            pic.setImageBitmap(conv_bm);

            // TODO change these when we change the user profile layout

//        TextView userName = (TextView) view.findViewById(
//                R.id.user_profile_username);
//        userName.setText(myUser.getName());
//
//        TextView userSalt = (TextView) view.findViewById(
//                R.id.user_profile_salt);
//        userSalt.setText("Salt: " + String.valueOf(myUser.getSalt()));
//
//        TextView countPosts = (TextView) view.findViewById(R.id
//                .user_profile_posts);
//        countPosts.setText("Total Posts: " + String.valueOf(myUser.getPosts
// ()));
//
//        TextView totalVotes = (TextView) view.findViewById(R.id
//                .user_profile_votes);
//        totalVotes.setText("Total Votes: " + String.valueOf(myUser
//                .getTotalVotes()));
//
//        TextView totalComments = (TextView) view.findViewById(R.id
//                .user_profile_comments);
//        totalComments.setText("Total Comments: " + String.valueOf(myUser
//                .getTotalComments()));
//
//        TextView userSinceDate = (TextView) view.findViewById(R.id
//                .user_profile_user_since);
//        userSinceDate.setText("User Since: " + myUser.getUserSince());
        }
    }

}
