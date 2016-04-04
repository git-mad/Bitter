package gitmad.bitter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import gitmad.bitter.R;
import gitmad.bitter.activity.ViewPostActivity;
import gitmad.bitter.data.CommentProvider;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.mock.MockCommentProvider;
import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.data.mock.MockUserProvider;
import gitmad.bitter.model.Post;
import gitmad.bitter.ui.PostAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortedPostFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Comparator<Post> comparator;

    private PostProvider postProvider;
    private UserProvider userProvider;
    private List<Post> posts;
    private CommentProvider commentProvider;
    private SwipeRefreshLayout srl;

    //TODO: Remove this eventually, along with first constructor, along with associated if statement
    private boolean manAddPost = true;


    /**
     * Required default constructor?
     */
    public SortedPostFragment() {
        this.manAddPost = true;
    }

    /**
     * Provides a way to implement different ways of sorting compactly
     * as defined by each subclass
     *
     * @param comparator The comparator to be used in sorting the posts
     * @parma name the name of this post
     */
    public SortedPostFragment(Comparator<Post> comparator) {
        this.comparator = comparator;
        manAddPost = true;
        posts = new ArrayList<>();
    }

    /**
     * Alternate comparator, to switch to?
     * @param comparator
     * @param posts
     */
    public SortedPostFragment(Comparator<Post> comparator, List<Post> posts) {
        this.comparator = comparator;
        this.posts = posts;
        this.manAddPost = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sorted_posts,
                container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id
                .sorted_posts_recycler_view);

        srl = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecyclerView();
                recyclerView.invalidate();
                srl.setRefreshing(false);
            }
        });

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        // TODO change to firebase providers
        postProvider = new MockPostProvider(this.getContext());
        userProvider = new MockUserProvider();
        commentProvider = new MockCommentProvider(this.getContext());

        refreshRecyclerView();

        if (manAddPost) {
            Post[] postArray = postProvider.getPosts(Integer.MAX_VALUE);

            for (Post p : postArray) {
                posts.add(p);
            }
        }

        Collections.sort(posts, comparator);
        adapter = new PostAdapter(posts, newFeedInteractionListener(), postProvider, userProvider, commentProvider);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private PostAdapter.FeedInteractionListener newFeedInteractionListener() {
        return new PostAdapter.FeedInteractionListener() {
            @Override
            public void onPostClicked(Post p, int index) {
                Intent intent = new Intent(getActivity(), ViewPostActivity
                        .class);
                intent.putExtra(ViewPostActivity.KEY_POST_ID, p.getId());
                startActivity(intent);
            }

            @Override
            public void onDownvoteClicked(Post p, int index) {
                postProvider.downvotePost(p.getId());
            }
        };
    }

    private void refreshRecyclerView() {
        // TODO pass in posts as an array
        // FIXME limit posts by user
        List<Post> posts = Arrays.asList(postProvider.getPosts(Integer.MAX_VALUE));
        Collections.sort(posts, comparator);
        adapter = new PostAdapter(posts, newFeedInteractionListener(),
                postProvider, userProvider, commentProvider);
        recyclerView.setAdapter(adapter);
    }
}
