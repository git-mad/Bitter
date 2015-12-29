package gitmad.bitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import gitmad.bitter.fragment.ViewPostFragment;

/**
 * Activity that embeds the ViewPostFragment to display a post in detail.
 */
public class ViewPostActivity extends AppCompatActivity {

    public static final String KEY_POST_ID = "postIdkey";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            addFragmentToContentView();
        }
    }

    private void addFragmentToContentView() {
        ViewPostFragment viewPostFragment = ViewPostFragment.newInstance(getPostIdFromIntent());

        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, viewPostFragment)
                .commit();
    }

    private String getPostIdFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(KEY_POST_ID)) {
            return getIntent().getStringExtra(KEY_POST_ID);
        } else  {
            throw new IllegalStateException("ViewPostActivity was not passed a postId.");
        }
    }
}
