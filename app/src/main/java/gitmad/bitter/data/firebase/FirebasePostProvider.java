package gitmad.bitter.data.firebase;

import com.firebase.client.Firebase;

import gitmad.bitter.data.PostProvider;
import gitmad.bitter.model.Post;

/**
 * Created by brian on 12/24/15.
 */
public class FirebasePostProvider implements PostProvider {

    Firebase postsRef;

    @Override
    public Post[] getPosts(int numPosts) {
        return new Post[0];
    }

    @Override
    public Post getPost(int id) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Post addPost(String postText) {
        return null;
    }

    @Override
    public Post[] getPostsByUser(String userId) {
        return new Post[0];
    }

    @Override
    public Post downvotePost(int postId) {
        return null;
    }

    @Override
    public Post deletePost(int postId) {
        return null;
    }
}
