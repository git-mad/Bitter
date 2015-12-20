package gitmad.bitter.data;

import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

/**
 * Created by brian on 12/18/15.
 */
public interface CommentsProvider {
    /**
     * retrieves a comment by its id
     * @param id the id of the comment to retrieve
     * @return the comment with the matching id, or null if the comment does not exist
     */
    public Comment getComment(int id);

    /**
     * retrieves all comments authored by user.
     * @param author author of the comments returned
     * @return an array of all the comments made by the author, or null if the user does not exist
     */
    public Comment[] getCommentsByUser(User author);

    /**
     * retrieves all comments made on the post
     * @param post the post in question
     * @return an array of comments for the post, or null if the post does not exist
     */
    public Comment[] getCommentsOnPost(Post post);
}
