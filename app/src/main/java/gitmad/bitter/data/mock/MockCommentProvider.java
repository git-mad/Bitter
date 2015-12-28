package gitmad.bitter.data.mock;

import android.content.Context;

import java.util.Date;
import java.util.UUID;

import gitmad.bitter.R;
import gitmad.bitter.data.CommentProvider;
import gitmad.bitter.model.Comment;

/**
 * A CommentProvider that returns mock data
 */
public class MockCommentProvider implements CommentProvider {

    private static final int IMPOSSIBLE_POST_ID = -1;
    private static final String IMPOSSIBLE_USER_ID = "";

    private Context context;
    private String[] commentsText;

    public MockCommentProvider(Context context) {
        this.context = context;

        commentsText = readMockCommentTextFromResource(R.array.mock_comments);
    }

    @Override
    public Comment getComment(int id) {
        return createRandomCommentOnPost(commentsText[id], id, IMPOSSIBLE_POST_ID);
    }

    @Override
    public Comment[] getCommentsByUser(String userId) {
        Comment[] comments = new Comment[commentsText.length];

        for (int i = 0; i < commentsText.length; i++) {
            comments[i] = createRandomCommentByUser(commentsText[i], i, userId);
        }

        return comments;
    }

    @Override
    public Comment[] getCommentsOnPost(int postId) {
        Comment[] comments = new Comment[commentsText.length];

        for (int i = 0; i < commentsText.length; i++) {
            comments[i] = createRandomCommentOnPost(commentsText[i], i, postId);
        }

        return comments;
    }

    @Override
    public Comment addComment(String commentText, int postId) {
        throw new UnsupportedOperationException("Not implemented on mock data provider");
    }

    @Override
    public Comment deleteComment(int commentId) {
        throw new UnsupportedOperationException("Not implemented on mock data provider");
    }

    @Override
    public Comment downvoteComment(int commentId) {
        throw new UnsupportedOperationException("Not implemented on mock data provider");
    }

    private String[] readMockCommentTextFromResource(int arrayResource) {
        return context.getResources().getStringArray(arrayResource);
    }

    private Comment createRandomCommentOnPost(String text, int commentId, int postId) {
        String randomAuthorId = UUID.randomUUID().toString();
        long commentCreatedTimestamp = new Date().getTime();

        return new Comment(commentId, postId, randomAuthorId, text, commentCreatedTimestamp, MockPostProvider.getRandomDownvoteCount());
    }

    private Comment createRandomCommentByUser(String text, int commentId, String userId) {
        long commentCreatedTimestamp = new Date().getTime();

        return new Comment(commentId, IMPOSSIBLE_POST_ID, userId, text, commentCreatedTimestamp, MockPostProvider.getRandomDownvoteCount());
    }


}
