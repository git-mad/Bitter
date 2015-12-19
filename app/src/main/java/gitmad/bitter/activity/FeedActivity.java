package gitmad.bitter.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import gitmad.bitter.R;
import gitmad.bitter.data.MockPostProvider;
import gitmad.bitter.fragment.AuthorPostDialogFragment;
import gitmad.bitter.model.Post;
import gitmad.bitter.ui.PostAdapter;


public class FeedActivity extends AppCompatActivity implements AuthorPostDialogFragment.OnPostCreatedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private MockPostProvider postProvider;

    //TODO: Add a way to get to UserActivity
    //TODO: Change to AppCompact
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        recyclerView = (RecyclerView) findViewById(R.id.feed_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        postProvider = new MockPostProvider(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // specify an adapter (see also next example)

        Post[] posts = getMockPosts();

        ArrayList<Post> postList = new ArrayList<>(posts.length);

        for (Post p : posts) {
            postList.add(p);
        }

        adapter = new PostAdapter(postList);
        recyclerView.setAdapter(adapter);

    }

    private Post[] getMockPosts() {
        return postProvider.getPosts();
    }

    @Override
    public void onPostCreated(Post post) {

        //TODO re-read from provider
        postProvider.addPost(post);
        ((PostAdapter) adapter).add(post);
        recyclerView.swapAdapter(adapter, false);
    }
}
