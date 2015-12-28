package gitmad.bitter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gitmad.bitter.R;
import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.model.Post;
import gitmad.bitter.ui.PostAdapter;


public class FeedFragment extends Fragment implements AuthorPostDialogFragment.OnPostCreatedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private MockPostProvider postProvider;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedFragment() {
    }

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
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
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.feed_recycler_view);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        postProvider = new MockPostProvider(this.getContext());

        // specify an adapter (see also next example)
        Post[] posts = getMockPosts();
        ArrayList<Post> postList = new ArrayList<>(posts.length);

        for (Post p : posts) {
            postList.add(p);
        }
        adapter = new PostAdapter(postList, );
        recyclerView.setAdapter(adapter);

        return view;
    }

    private Post[] getMockPosts() {
        return postProvider.getPosts(Integer.MAX_VALUE);
    }

    @Override
    public void onPostCreated(String postText) {

        //TODO re-read from provider
        Post newPost = postProvider.addPost(postText);
        ((PostAdapter) adapter).add(newPost);
        recyclerView.swapAdapter(adapter, false);
    }
}
