package gitmad.bitter.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import gitmad.bitter.R;
import gitmad.bitter.data.MockPostProvider;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.model.Post;

public class ViewPostActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        Intent intent = getIntent();
        String id = intent.getStringExtra("POST_ID");

        PostProvider postProvider = new MockPostProvider(this);
        Post post = postProvider.getPost(Integer.parseInt(id));

        TextView postBody = (TextView) findViewById(R.id.postContent);
        TextView user = (TextView) findViewById(R.id.posterUsername);

        postBody.setText(post.getText());
        user.setText(post.getUser().getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
