package gitmad.bitter.data;

import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

/**
 * Created by brian on 8/27/15.
 */
public interface PostProvider {
    /**
     * @param numPosts number of posts to retreive
     * @return the posts available to this PostProvider
     */
    public Post[] getPosts(int numPosts);

    /**
     * @param id the id of the desired post.
     * @return the post with the specified id.
     * @throws IllegalArgumentException if a post with the id specified is not found.
     */
    public Post getPost(int id) throws IllegalArgumentException;

    /**
     * @Param post the Post we are adding
     */
    public Post addPost(String postText);

    /**
     * gets all of the posts made by a particular user
     * @param userId the id of the user whose posts will be returned
     * @return an array of the user's posts
     * @throws IllegalArgumentException if the user does not exist
     */
    public Post[] getPostsByUser(String userId);
}
