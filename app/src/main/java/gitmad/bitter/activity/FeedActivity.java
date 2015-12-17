package gitmad.bitter.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import gitmad.bitter.R;
import gitmad.bitter.fragment.AuthorPostDialogFragment;
import gitmad.bitter.data.MockPostProvider;
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

        // specify an adapter (see also next example)

        Post[] posts = getMockPosts();

        ArrayList<Post> postList = new ArrayList<>(posts.length);

        for (Post p : posts) {
            postList.add(p);
        }

        adapter = new PostAdapter(postList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_bitch) {
            new AuthorPostDialogFragment().show(getFragmentManager(), "AuthorBitch");
            return true;
        }

        if (id == R.id.start_User){
            Intent intent = new Intent(this,UserActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
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
