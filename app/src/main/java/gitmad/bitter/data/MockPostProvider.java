package gitmad.bitter.data;

import android.content.Context;

import gitmad.bitter.R;

/**
 * Created by brian on 8/27/15.
 */
public class MockPostProvider implements PostProvider {

    private Context context;

    public MockPostProvider(Context context) {
        this.context = context;
    }

    @Override
    public String[] getPosts() {
        return context.getResources().getStringArray(R.array.mock_posts);
    }

    @Override
    public String getPost(int id) throws IllegalArgumentException {
        String[] posts = context.getResources().getStringArray(R.array.mock_posts);
        if (id < 0 || id >= posts.length) {
            throw new IllegalArgumentException("could not find post with id " + id);
        }
        return posts[id];
    }
}
