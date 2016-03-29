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
import gitmad.bitter.R;
import gitmad.bitter.activity.ViewPostActivity;
import gitmad.bitter.data.CommentProvider;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.mock.MockCommentProvider;
import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.data.mock.MockUserProvider;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;
import gitmad.bitter.ui.PostAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public abstract class SortedPostFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Comparator<Post> comparator;

    private PostProvider postProvider;
    private UserProvider userProvider;
    private CommentProvider commentProvider;
    private SwipeRefreshLayout srl;

    /**
     * Provides a way to implement different ways of sorting compactly
     * as defined by each subclass
     *
     * @param comparator The comparator to be used in sorting the posts
     */
    public SortedPostFragment(Comparator<Post> comparator) {
        this.comparator = comparator;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // TODO change to firebase providers
        postProvider = new MockPostProvider(this.getContext());
        userProvider = new MockUserProvider();
        commentProvider = new MockCommentProvider(this.getContext());

        refreshRecyclerView();

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
        Post[] posts = postProvider.getPosts(Integer.MAX_VALUE);

        Map<Post, User> authorsMap = userProvider.getAuthorsOfPosts(posts);
        ArrayList<Post> postList = new ArrayList<>(posts.length);

        for (Post p : posts) {
            postList.add(p);
        }
        Collections.sort(postList, comparator);
        adapter = new PostAdapter(postList, authorsMap,
                newFeedInteractionListener(), postProvider, commentProvider);
        recyclerView.setAdapter(adapter);
    }
}
