package gitmad.bitter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import gitmad.bitter.ui.PostAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortedPostFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // Must be passed in as a bundle, so here are some default values
    private Comparator<Post> comparator = new Comparator<Post>() {
        @Override
        public int compare(Post lhs, Post rhs) {
            return 0;
        }
    };
    private List<Post> posts = new ArrayList<>();

    private PostProvider postProvider;
    private UserProvider userProvider;
    private CommentProvider commentProvider;
    private SwipeRefreshLayout srl;

    public SortedPostFragment() {
        // Required default constructor for the Fragment Manager to
        // instantiate a Sorted Post Fragment
    }

    // TODO post and comparator args
    public static SortedPostFragment newInstance(Comparator<Post>
                                                         postComparator,
                                                 List<Post> posts) {
        SortedPostFragment fragment = new SortedPostFragment();

        // FIXME use Bundle instead of setters and getters
        fragment.comparator = postComparator;
        fragment.posts = posts;

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        Collections.sort(posts, comparator);
        adapter = new PostAdapter(posts, newFeedInteractionListener(),
                postProvider, userProvider, commentProvider);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private PostAdapter.FeedInteractionListener newFeedInteractionListener() {
        return new PostAdapter.FeedInteractionListener() {
            @Override
            public void onDownvoteClicked(Post p, int index) {
                postProvider.downvotePost(p.getId());
            }

            @Override
            public void onPostClicked(Post p, int index) {
                Intent intent = new Intent(getActivity(), ViewPostActivity
                        .class);
                intent.putExtra(ViewPostActivity.KEY_POST_ID, p.getId());
                Log.d("POST_ID", p.getId());
                startActivity(intent);
            }
        };
    }

    private void refreshRecyclerView() {
        Collections.sort(posts, comparator);
        adapter = new PostAdapter(posts, newFeedInteractionListener(),
                postProvider, userProvider, commentProvider);
        recyclerView.setAdapter(adapter);
    }

    public static class TopPostComparator implements Comparator<Post> {
        @Override
        public int compare(Post lhs, Post rhs) {
            return lhs.getDownvotes() - rhs.getDownvotes();
        }
    }

    public static class RecentPostComparator implements Comparator<Post> {
        @Override
        public int compare(Post lhs, Post rhs) {
            return Long.compare(lhs.getTimestamp(), rhs.getTimestamp());
        }
    }

    public static class FeedPostComparator implements Comparator<Post> {
        @Override
        public int compare(Post lhs, Post rhs) {
            // TODO feed post algorithm implementation
            return lhs.getDownvotes() - rhs.getDownvotes();
        }
    }

    public static class FavoritePostComparator implements Comparator<Post> {
        @Override
        public int compare(Post lhs, Post rhs) {
            // TODO favorite post algorithm implementation
            return lhs.getText().compareTo(rhs.getText());
        }
    }
}
