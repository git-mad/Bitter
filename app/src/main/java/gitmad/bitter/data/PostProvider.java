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
    public Post getPost(String id) throws IllegalArgumentException;

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

    /**
     * adds a downvote to a post
     * @param postId the id of the post to be downvoted
     * @return a new Post Object reflecting the new downvote count
     */
    public Post downvotePost(String postId);

    /**
     * removes a post from the database, if it is owned by the currently logged in user.
     * @param postId the id of the post to be deleted
     * @return a Post Object containing the Post's last state.
     */
    public Post deletePost(String postId);
}
