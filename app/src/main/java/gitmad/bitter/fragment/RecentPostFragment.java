package gitmad.bitter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import gitmad.bitter.R;
import gitmad.bitter.activity.ViewPostActivity;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.data.mock.MockUserProvider;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;
import gitmad.bitter.ui.PostAdapter;

public class RecentPostFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private MockPostProvider postProvider;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecentPostFragment() {
    }

    public static RecentPostFragment newInstance() {
        RecentPostFragment fragment = new RecentPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_posts, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recent_posts_recycler_view);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        postProvider = new MockPostProvider(this.getActivity());
        UserProvider userProvider = new MockUserProvider();

        // specify an adapter (see also next example)

        Post[] posts = getMockPosts();
        Map<Post, User> authorsMap = userProvider.getAuthorsOfPosts(posts);

        ArrayList<Post> postList = new ArrayList<>(posts.length);

        Collections.addAll(postList, posts);

        adapter = new PostAdapter(postList, authorsMap, newFeedInteractionListener());
        recyclerView.setAdapter(adapter);

        return view;
    }


    private PostAdapter.FeedInteractionListener newFeedInteractionListener() {
        return new PostAdapter.FeedInteractionListener() {
            @Override
            public void onPostClicked(Post p, int index) {
                Intent intent = new Intent(getActivity(), ViewPostActivity.class);
                intent.putExtra(ViewPostActivity.KEY_POST_ID, p.getId());
                startActivity(intent);
            }

            @Override
            public void onDownvoteClicked(Post p, int index) {
                postProvider.downvotePost(p.getId());
            }
        };
    }

    private Post[] getMockPosts() {
        return postProvider.getPosts(Integer.MAX_VALUE);
    }
}
