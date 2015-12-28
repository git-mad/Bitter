package gitmad.bitter.data.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import gitmad.bitter.data.CommentProvider;
import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;

import static gitmad.bitter.data.firebase.FirebasePostProvider.getFirebaseUrlForPost;

/**
 * Synchronous method calls for accessing Comment data from Firebase
 */
public class FirebaseCommentProvider implements CommentProvider {

    private static final String FIREBASE_COMMENTS_URL = "https://bitter-gitmad.firebaseio.com/users";

    private CountDownLatch countDownLatch;
    private AtomicReference<DataSnapshot> dataFromCallbackAtomicRef;

    private Firebase commentsFirebaseRef;

    private FirebaseSyncRequester<Post> requesterForCheckingPost;

    public FirebaseCommentProvider() {
        dataFromCallbackAtomicRef = new AtomicReference<>();
        commentsFirebaseRef = new Firebase(FIREBASE_COMMENTS_URL);

        checkAuthentication();
    }

    @Override
    public Comment getComment(String commentId) {
        FirebaseSyncRequester<Comment> commentRequester = newFirebaseSyncRequesterForComment(commentId);

        if (!commentRequester.exists()) {
            throw new IllegalArgumentException("Comment does not exist");
        }

        return commentRequester.get();
    }

    @Override
    public Comment[] getCommentsByUser(String authorId) {
        Query query = commentsFirebaseRef.orderByChild("authorId").equalTo(authorId);

        return executeCommentQuerySynchronously(query);
    }

    @Override
    public Comment[] getCommentsOnPost(String postId) {
        Query query = commentsFirebaseRef.orderByChild("postId").equalTo(postId);

        return executeCommentQuerySynchronously(query);
    }

    @Override
    public Comment addComment(String commentText, String postId) {

        startCheckingIfPostExists(postId);

        Firebase newCommentRef = commentsFirebaseRef.push();

        if (!finishCheckingIfPostExists()) {
            newCommentRef.removeValue();

            throw new IllegalArgumentException("Post being commented on does not exist.");
        }

        String newCommentId = newCommentRef.getKey();
        long timestamp = new Date().getTime();
        int zeroDownvotes = 0;

        Comment newComment = new Comment(newCommentId, postId, getLoggedInUserId(), commentText, timestamp, zeroDownvotes);

        newCommentRef.setValue(newComment);

        return newComment;
    }

    @Override
    public Comment deleteComment(String commentId) {
        Firebase firebaseCommentRef = newFirebaseRefForComment(commentId);
        FirebaseSyncRequester<Comment> commentRequester = new FirebaseSyncRequester<>(firebaseCommentRef);

        Comment commentToReturn = null;

        if (commentRequester.exists()) {
            firebaseCommentRef.removeValue();
            commentToReturn = commentRequester.get();
        } else {
            throw new IllegalArgumentException("Comment with id " + commentId + " does not exist.");
        }

        return commentToReturn;
    }

    @Override
    public Comment downvoteComment(String commentId) {
        Firebase commentFirebaseRef = newFirebaseRefForComment(commentId);
        FirebaseSyncRequester<Comment> commentRequester = new FirebaseSyncRequester<>(commentFirebaseRef);

        Comment downvotedComment = null;

        if (commentRequester.exists()) {
            downvotedComment = newDownvotedComment(commentRequester.get());
            commentFirebaseRef.setValue(newDownvotedComment(downvotedComment));
        } else {
            throw new IllegalArgumentException("Comment with id " + commentId + " does not exist");
        }

        return downvotedComment;
    }

    private ValueEventListener getValueEventListenerForSynchronizingCallbacks() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataFromCallbackAtomicRef.set(dataSnapshot);

                countDownLatch.countDown();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Bitter", "Could not connect to firebase.");
            }
        };
    }

    private Comment newDownvotedComment(Comment commentToDownvote) {
        return new Comment(commentToDownvote.getId(), commentToDownvote.getPostId(), commentToDownvote.getAuthorId(),
                commentToDownvote.getText(), commentToDownvote.getTimestamp(),
                commentToDownvote.getDownvotes() - 1);
    }

    @NonNull
    private Comment[] parseCommentsFromCallbackData() {
        DataSnapshot commentCallbackData = dataFromCallbackAtomicRef.get();

        Comment[] comments = new Comment[(int) commentCallbackData.getChildrenCount()];

        Iterator<DataSnapshot> userDataIterator = commentCallbackData.getChildren().iterator();
        for (int i = 0; userDataIterator.hasNext(); i++) {
            comments[i] = userDataIterator.next().getValue(Comment.class);
        }
        return comments;
    }

    private void waitForCallback() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getLoggedInUserId() {
        return commentsFirebaseRef.getAuth().getUid();
    }

    /**
     * Begins the asynchronous operation of downloading a post using the FirebaseSyncRequester
     * @param postId the id of the post to download.
     */
    private void startCheckingIfPostExists(String postId) {
        Firebase postBeingCommentedOnRef = new Firebase(getFirebaseUrlForPost(postId));
        requesterForCheckingPost = new FirebaseSyncRequester<>(postBeingCommentedOnRef);
    }

    /**
     * Determines if the post that was passed to FirebaseCommentProvider#startCheckingIfPostExists()
     * does exists.
     * This method waits for the post to be downloaded.
     * @return true if it exists, false otherwise
     */
    private boolean finishCheckingIfPostExists() {
        return requesterForCheckingPost.exists();
    }

    private Firebase newFirebaseRefForComment(String commentId) {
        return new Firebase(getFirebaseUrlForComment(commentId));
    }

    private FirebaseSyncRequester<Comment> newFirebaseSyncRequesterForComment(String commentId) {
        return new FirebaseSyncRequester<>(newFirebaseRefForComment(commentId));
    }

    private void checkAuthentication() {
        if (commentsFirebaseRef.getAuth() == null) {
            throw new FirebaseNotAuthenticatedException("Must be authenticated with firebase");
        }
    }

    @NonNull
    private Comment[] executeCommentQuerySynchronously(Query query) {
        countDownLatch = new CountDownLatch(1);

        query.addListenerForSingleValueEvent(getValueEventListenerForSynchronizingCallbacks());

        waitForCallback();

        return parseCommentsFromCallbackData();
    }

    private static String getFirebaseUrlForComment(String commentId) {
        return FIREBASE_COMMENTS_URL + "/" + commentId;
    }
}
