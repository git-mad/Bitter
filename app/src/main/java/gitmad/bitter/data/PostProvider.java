package gitmad.bitter.data;

import gitmad.bitter.model.Post;

/**
 * Created by brian on 8/27/15.
 */
public interface PostProvider {
    /**
     * @return all of the posts available to this PostProvider
     */
    public Post[] getPosts();

    /**
     * @param id the id of the desired post.
     * @return the post with the specified id.
     * @throws IllegalArgumentException if a post with the id specified is not found.
     */
    public Post getPost(int id) throws IllegalArgumentException;

    /**
     * @Param post the Post we are adding
     */
    public void addPost(Post post);
}
