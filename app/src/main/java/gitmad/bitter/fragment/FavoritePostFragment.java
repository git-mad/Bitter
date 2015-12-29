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
import java.util.HashMap;
import java.util.Map;

import gitmad.bitter.R;
import gitmad.bitter.activity.ViewPostActivity;
import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.data.mock.MockUserProvider;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;
import gitmad.bitter.ui.PostAdapter;

public class FavoritePostFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private PostProvider postProvider;

    private UserProvider userProvider;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoritePostFragment() {
    }

    public static FavoritePostFragment newInstance() {
        FavoritePostFragment fragment = new FavoritePostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeMockPostProviders();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_posts, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.favorite_posts_recycler_view);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)

        Post[] postsArray = getMockPosts();

        ArrayList<Post> postList = new ArrayList<>(postsArray.length);

        for (Post p : postsArray) {
            postList.add(p);
        }

        Map<Post, User> authorsOfPosts = getAuthorsOfPosts(postsArray);

        adapter = new PostAdapter(postList, authorsOfPosts, newFeedInteractionListener());
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

    private void initializeMockPostProviders() {
        postProvider = new MockPostProvider(this.getActivity());
        userProvider = new MockUserProvider();
    }

    private Post[] getMockPosts() {
        return postProvider.getPosts(Integer.MAX_VALUE);
    }

    private Map<Post, User> getAuthorsOfPosts(Post[] posts) {
        Map<Post, User> postAuthors = new HashMap<>();

        for (Post post : posts) {
            User authorOfPost = userProvider.getAuthorOfPost(post);

            postAuthors.put(post, authorOfPost);
        }

        return postAuthors;
    }
}
