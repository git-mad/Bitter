package gitmad.bitter.activity;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.support.v7.app.AppCompatActivity;
=======
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
>>>>>>> refs/remotes/origin/view-post
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import gitmad.bitter.R;
<<<<<<< HEAD
import gitmad.bitter.data.MockPostProvider;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.model.Post;
=======
import gitmad.bitter.model.Comment;
import gitmad.bitter.model.User;
import gitmad.bitter.ui.CommentAdapter;
>>>>>>> refs/remotes/origin/view-post

public class ViewPostActivity extends AppCompatActivity {

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

<<<<<<< HEAD
        postBody.setText(post.getText());
        user.setText(post.getUser().getName());
=======
        postBody.setText(postContent);
        user.setText(userName);

        Comment[] comments = getMockComments();

        CommentAdapter adapter = new CommentAdapter(this, comments);
        ListView listView = (ListView) findViewById(R.id.comments_list_view);
        listView.setAdapter(adapter);

        final TextInputLayout commentWrapper = (TextInputLayout) findViewById(R.id.comment_text_wrapper);
        commentWrapper.setHint("Bitch about it!");
>>>>>>> refs/remotes/origin/view-post
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

    private Comment[] getMockComments() {
        String[] commentsText = getResources().getStringArray(R.array.mock_comments);
        User user = new User();
        user.setName("NOTgBurdell");
        Comment[] comments = new Comment[commentsText.length];
        for (int i = 0; i < commentsText.length; i++) {
            comments[i] = new Comment();
            comments[i].setText(commentsText[i]);
            comments[i].setUser(user);
        }
        return comments;
    }
}
