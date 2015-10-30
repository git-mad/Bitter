package gitmad.bitter.data;

import android.content.Context;

import gitmad.bitter.R;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

/**
 * Created by brian on 8/27/15.
 */
public class MockPostProvider implements PostProvider {

    private Context context;

    public MockPostProvider(Context context) {
        this.context = context;
    }

    @Override
    public Post[] getPosts() {
        String[] postsText = context.getResources().getStringArray(R.array.mock_posts);
        User user = new User();
        user.setName("NOTgBurdell");
        Post[] posts = new Post[postsText.length];
        for (int i = 0; i < postsText.length; i++) {
            posts[i] = new Post();
            posts[i].setText(postsText[i]);
            posts[i].setUser(user);
            posts[i].setId(i);
        }
        return posts;
    }

    @Override
    public Post getPost(int id) throws IllegalArgumentException {
        User user = new User();
        user.setName("NOTgBurdell");
        Post[] posts = getPosts();
        if (posts[id]==null) {
            throw new IllegalArgumentException("could not find post with id " + id);
        }
        return posts[id];
    }
}
