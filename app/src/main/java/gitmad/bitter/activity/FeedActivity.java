package gitmad.bitter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import gitmad.bitter.R;
import gitmad.bitter.fragment.AuthorPostDialogFragment;
import gitmad.bitter.data.MockPostProvider;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;
import gitmad.bitter.ui.PostAdapter;


public class FeedActivity extends Activity implements AuthorPostDialogFragment.OnPostCreatedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private MockPostProvider postProvider;

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

        adapter = new PostAdapter(posts);
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

        return super.onOptionsItemSelected(item);
    }

    private Post[] getMockPosts() {
        return postProvider.getPosts();
    }

    @Override
    public void onPostCreated(Post post) {
        //TODO assign user, add to user
        //TODO save in post database
        //TODO add post to RecyclerView adapter and refresh
    }
}
