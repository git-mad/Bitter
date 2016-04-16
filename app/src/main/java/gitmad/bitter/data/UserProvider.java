package gitmad.bitter.data;

import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

import java.util.Map;

/**
 * Created by brian on 9/21/15.
 */
public interface UserProvider {

    /**
     * retrieves the User that created the Comment
     *
     * @param comment comment in question
     * @return the User that authored the Comment
     */
    User getAuthorOfComment(Comment comment);

    /**
     * retrieves the User that created the post
     *
     * @param post post in question
     * @return the user that authored the post
     */
    User getAuthorOfPost(Post post);

    /**
     * for each comment, downloads the User that authored it and
     * then maps the comment to the author.
     *
     * @param comments comments in question
     * @return the map of the comments and their authors
     */
    Map<Comment, User> getAuthorsOfComments(Comment[] comments);

    /**
     * for each post, this method downloads the User that
     * authored it and then maps the post to the author.
     *
     * @param posts the posts in question
     * @return the map of all the posts and their authors.
     */
    Map<Post, User> getAuthorsOfPosts(Post[] posts);

    /**
     * retrieves the User that is currently logged in to the app
     *
     * @return the current user, or null if none is logged in.
     */
    User getLoggedInUser();

    /**
     * retrieves a map that contains each of the post categories, mapped to
     * the number
     * of posts the user has made in each category
     *
     * @param userUid the id of the user whose category counts should be
     *                retrieved.
     * @return
     */
    Map<String, Integer> getPostCategoryCount(String userUid);

    /**
     * gets the specified user
     *
     * @param userId id of the user to retrieve
     * @return A user object, or null if no User exists for id
     */
    User getUser(String userId);
}
