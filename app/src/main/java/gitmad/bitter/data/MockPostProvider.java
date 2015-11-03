package gitmad.bitter.data;

import android.content.Context;

import java.util.Arrays;

import gitmad.bitter.R;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

/**
 * Created by brian on 8/27/15.
 */
public class MockPostProvider implements PostProvider {

    private Context context;
    private Post[] posts;

    public MockPostProvider(Context context) {
        this.context = context;
    }

    @Override
    public Post[] getPosts() {
        if(posts == null){
            String[] postsText = context.getResources().getStringArray(R.array.mock_posts);
            User user = new User();
            user.setName("NOTgBurdell");
            posts = new Post[postsText.length];
            for (int i = 0; i < postsText.length; i++) {
                posts[i] = new Post();
                posts[i].setText(postsText[i]);
                posts[i].setUser(user);
                posts[i].setId(i);
            }
        }
        return posts;
    }

    @Override
    public Post getPost(int id) throws IllegalArgumentException {
        User user = new User();
        user.setName("NOTgBurdell");
        if(posts == null){
            getPosts();
        }
        if (posts[id]==null) {
            throw new IllegalArgumentException("could not find post with id " + id);
        }
        return posts[id];
    }

    //NOT stored in resources, just in memory.
    //Also
    @Override
    public void addPost(Post post){
        posts = Arrays.copyOf(posts, posts.length + 1);
        post.setId(posts.length - 1);
        posts[posts.length - 1] = post;
    }
}
