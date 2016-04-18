package gitmad.bitter.data;

import gitmad.bitter.model.Post;

/**
 * Created by brian on 8/27/15.
 */
public interface PostProvider {
    /**
     * returns immediately
     *
     * @param postText the text of the Post we are adding
     */
    Post addPostAsync(String postText);

    /**
     * @param postText the text of the Post we are adding
     */
    Post addPostSync(String postText);

    /**
     * removes a post from the database, if it is owned by the currently
     * logged in user.
     * synchronous
     *
     * @param postId the id of the post to be deleted
     * @return a Post Object containing the Post's last state.
     */
    Post deletePost(String postId);

    /**
     * adds a downvote to a post
     *
     * @param postId the id of the post to be downvoted
     * @return a new Post Object reflecting the new downvote count
     */
    Post downvotePost(String postId);

    /**
     * @param id the id of the desired post.
     * @return the post with the specified id.
     * @throws IllegalArgumentException if a post with the id specified is
     *                                  not found.
     */
    Post getPost(String id) throws IllegalArgumentException;

    /**
     * @param numPosts number of posts to retreive
     * @return the posts available to this PostProvider
     */
    Post[] getPosts(int numPosts);

    /**
     * gets all of the posts made by a particular user
     *
     * @param userId the id of the user whose posts will be returned
     * @return an array of the user's posts
     * @throws IllegalArgumentException if the user does not exist
     */
    Post[] getPostsByUser(String userId);
}
