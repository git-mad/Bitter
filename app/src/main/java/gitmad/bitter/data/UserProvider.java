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
     * @param userId id of the user to retreive
     * @return A user object, or null if no User exists for id
     */
    public User getUser(String userId);

    /**
     * retreives the User that created the post
     * @param post post in question
     * @return the user that authored the post
     */
    public User getAuthorOfPost(Post post);

    /**
     * retreives the User that created the Comment
     * @param comment comment in question
     * @return the User that authored the Comment
     */
    public User getAuthorOfComment(Comment comment);
}
