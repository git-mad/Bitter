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
import java.util.Comparator;
import java.util.Map;

import gitmad.bitter.R;
import gitmad.bitter.activity.ViewPostActivity;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.data.mock.MockUserProvider;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;
import gitmad.bitter.ui.PostAdapter;

//TODO fix downvote issues with recycler view

public abstract class SortedPostFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Comparator<Post> comparator;

    private PostProvider postProvider;
    private UserProvider userProvider;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sorted_posts, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.sorted_posts_recycler_view);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // FIXME from backend does not work
        // postProvider = new FirebasePostProvider();
        // userProvider = new FirebaseUserProvider();
        postProvider = new MockPostProvider(this.getContext()); //From mock
        userProvider = new MockUserProvider();

        Post[] posts = postProvider.getPosts(Integer.MAX_VALUE);

        Map<Post, User> authorsMap = userProvider.getAuthorsOfPosts(posts);
        ArrayList<Post> postList = new ArrayList<>(posts.length);

        for (Post p : posts) {
            postList.add(p);
        }
        Collections.sort(postList, comparator);

        adapter = new PostAdapter(postList, authorsMap, newFeedInteractionListener(), postProvider);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        Post[] posts = postProvider.getPosts(Integer.MAX_VALUE);
        ArrayList<Post> postList = new ArrayList<>(posts.length);

        for (Post p : posts) {
            postList.add(p);
        }
        Collections.sort(postList, comparator);

        for(int i = 0; i < postList.size(); i++) {
            adapter.notifyItemChanged(i, postList.get(i));
        }
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
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
}
