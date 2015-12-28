package gitmad.bitter.data;

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
     * retrieves the User that created the Comment
     * @param comment comment in question
     * @return the User that authored the Comment
     */
    public User getAuthorOfComment(Comment comment);

    /**
     * retrieves the User that is currently logged in to the app
     * @return the current user, or null if none is logged in.
     */
    public User getLoggedInUser();
}
