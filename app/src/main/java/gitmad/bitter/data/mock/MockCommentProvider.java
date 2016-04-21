package gitmad.bitter.data.mock;

import android.content.Context;
import gitmad.bitter.R;
import gitmad.bitter.data.CommentProvider;
import gitmad.bitter.model.Comment;

import java.util.Date;
import java.util.UUID;

/**
 * A CommentProvider that returns mock data
 */
public class MockCommentProvider implements CommentProvider {

    private static final String IMPOSSIBLE_POST_ID = "-2";
    private static final String IMPOSSIBLE_USER_ID = "";

    private Context context;
    private String[] commentsText;

    public MockCommentProvider(Context context) {
        this.context = context;

        commentsText = readMockCommentTextFromResource(R.array.mock_comments);
    }

    @Override
    public Comment addCommentAsync(String commentText, String postId) {
        throw new UnsupportedOperationException("Not implemented on mock data" +
                " provider");
    }

    @Override
    public Comment addCommentSync(String commentText, String postId) {
        throw new UnsupportedOperationException("Not implemented on mock data" +
                " provider");
    }

    @Override
    public Comment deleteComment(String commentId) {
        throw new UnsupportedOperationException("Not implemented on mock data" +
                " provider");
    }

    @Override
    public Comment downvoteComment(String commentId) {
        throw new UnsupportedOperationException("Not implemented on mock data" +
                " provider");
    }

    @Override
    public Comment getComment(String commentId) {
        int commentIndex = Integer.parseInt(commentId);
        return createRandomCommentOnPost(commentsText[commentIndex],
                commentId, IMPOSSIBLE_POST_ID);
    }

    @Override
    public Comment[] getCommentsByUser(String userId) {
        Comment[] comments = new Comment[commentsText.length];

        for (int i = 0; i < commentsText.length; i++) {
            comments[i] = createRandomCommentByUser(commentsText[i], Integer
                    .toString(i), userId);
        }

        return comments;
    }

    @Override
    public Comment[] getCommentsOnPost(String postId) {
        Comment[] comments = new Comment[commentsText.length];

        for (int i = 0; i < commentsText.length; i++) {
            comments[i] = createRandomCommentOnPost(commentsText[i], Integer
                    .toString(i), postId);
        }

        return comments;
    }

    private Comment createRandomCommentByUser(String text, String commentId,
                                              String userId) {
        long commentCreatedTimestamp = new Date().getTime();

        return new Comment(commentId, IMPOSSIBLE_POST_ID, userId, text,
                commentCreatedTimestamp, MockPostProvider
                .getRandomDownvoteCount());
    }

    private Comment createRandomCommentOnPost(String text, String commentId,
                                              String postId) {
        String randomAuthorId = UUID.randomUUID().toString();
        long commentCreatedTimestamp = new Date().getTime();

        return new Comment(commentId, postId, randomAuthorId, text,
                commentCreatedTimestamp, MockPostProvider
                .getRandomDownvoteCount());
    }

    private String[] readMockCommentTextFromResource(int arrayResource) {
        return context.getResources().getStringArray(arrayResource);
    }


}
