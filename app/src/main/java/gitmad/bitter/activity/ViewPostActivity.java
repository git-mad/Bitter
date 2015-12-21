package gitmad.bitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import gitmad.bitter.R;
import gitmad.bitter.fragment.ViewPostFragment;

/**
 * Activity that embeds the ViewPostFragment to display a post in detail.
 */
public class ViewPostActivity extends AppCompatActivity {

    public static final String KEY_POST_ID = "postIdkey";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        if (savedInstanceState == null) {
            addFragmentToContentView();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void addFragmentToContentView() {
        ViewPostFragment viewPostFragment = ViewPostFragment.newInstance(getPostIdFromIntent());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.viewPostFrameLayout, viewPostFragment).commit();
    }

    private int getPostIdFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(KEY_POST_ID)) {
            return getIntent().getIntExtra(KEY_POST_ID, -1);
        } else  {
            throw new IllegalStateException("ViewPostActivity was not passed a postId.");
        }
    }
}
