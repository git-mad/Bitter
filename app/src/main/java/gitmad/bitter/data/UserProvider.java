package gitmad.bitter.data;

import java.util.Map;

import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

/**
 * Created by brian on 9/21/15.
 */
public interface UserProvider {

    /**
     * gets the specified user
     * @param userId id of the user to retrieve
     * @return A user object, or null if no User exists for id
     */
    public User getUser(String userId);

    /**
     * retrieves the User that created the post
     * @param post post in question
     * @return the user that authored the post
     */
    public User getAuthorOfPost(Post post);

    /**
     * for each post, this method downloads the User that
     * authored it and then maps the post to the author.
     * @param posts the posts in question
     * @return the map of all the posts and their authors.
     */
    public Map<Post, User> getAuthorsOfPosts(Post... posts);

    /**
     * retrieves the User that created the Comment
     * @param comment comment in question
     * @return the User that authored the Comment
     */
    public User getAuthorOfComment(Comment comment);

    /**
     * for each comment, downloads the User that authored it and
     * then maps the comment to the author.
     * @param comments comments in question
     * @return the map of the comments and their authors
     */
    public Map<Comment, User> getAuthorsOfComments(Comment... comments);

    /**
     * retrieves the User that is currently logged in to the app
     * @return the current user, or null if none is logged in.
     */
    public User getLoggedInUser();
}
