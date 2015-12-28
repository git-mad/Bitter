package gitmad.bitter.data.firebase;

import gitmad.bitter.model.Post;

/**
 * Receives updates when the posts in the feed change.
 */
public interface PostsChangedListener {
    public void onPostsChanged(Post[] posts);
}
