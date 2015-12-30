package gitmad.bitter.data;

import gitmad.bitter.model.Comment;

/**
 * Created by brian on 12/18/15.
 */
public interface CommentProvider {
    /**
     * retrieves a comment by its id
     * @param id the id of the comment to retrieve
     * @return the comment with the matching id, or null if the comment does not exist
     */
    Comment getComment(String id);

    /**
     * retrieves all comments authored by user.
     * @param authorId id of the author of the comments
     * @return an array of all the comments made by the author, or null if the user does not exist
     */
    Comment[] getCommentsByUser(String authorId);

    /**
     * retrieves all comments made on the post
     * @param postId the id of the post in question
     * @return an array of comments for the post, or null if the post does not exist
     */
    Comment[] getCommentsOnPost(String postId);

    /**
     * Adds a comment to a post
     * @param commentText the text of the comment to add.
     * @param postId the id of the post commented on.
     * @return the Comment Object added to the post.
     */
    Comment addComment(String commentText, String postId);

    /**
     * removes a comment from the feed
     * @param commentId the id of the comment to remove
     * @return the comment removed
     */
    Comment deleteComment(String commentId);

    /**
     * decrements(?) the downvote count on a comment
     * @param commentId the id of the comment to downvote
     * @return the downvoted comment.
     */
    Comment downvoteComment(String commentId);

}
