package gitmad.bitter.data.firebase;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import gitmad.bitter.data.PostProvider;
import gitmad.bitter.model.Post;

/**
 * Provides methods to access data from the Bitter Firebase repo
 *
 * FirebasePostProvider is not thread safe. Create one for each thread on which it is needed.
 */
public class FirebasePostProvider implements PostProvider {

    public static final String FIREBASE_POSTS_URL = "https://bitter-gitmad.firebaseio.com/posts";
    private static final int numberOfPostsToRetrieve = 40;

    private Firebase firebasePostsRef;

    private CountDownLatch countDownLatch;
    private AtomicReference<DataSnapshot> dataFromCallback;

    private Post[] currentPostsInFeed;
    private List<PostsChangedListener> postsChangedListeners;

    public FirebasePostProvider() {
        dataFromCallback = new AtomicReference<>();

        firebasePostsRef = new Firebase(FIREBASE_POSTS_URL);

        checkAuthentication();

        setFirebaseListener();
    }

    @Override
    public Post[] getPosts(int numPosts) {
        int lengthOfArrayToReturn = Math.min(numPosts, currentPostsInFeed.length);
        return Arrays.copyOf(currentPostsInFeed, lengthOfArrayToReturn);
    }

    @Override
    public Post getPost(String id) throws IllegalArgumentException {
        Firebase firebasePostRef = new Firebase(getFirebaseUrlForPost(id));

        FirebaseSyncRequester<Post> firebaseSyncRequester = new FirebaseSyncRequester<>(firebasePostRef);

        if (!firebaseSyncRequester.exists()) {
            throw new IllegalArgumentException("Post with id " + id + " does not exist.");
        }

        return firebaseSyncRequester.get();
    }

    @Override
    public Post addPost(String postText) {
        Firebase newPostRef = firebasePostsRef.push();

        String newPostId = newPostRef.getKey();
        long timestamp = millisSinceEpoch();
        final int zeroDownvotes = 0;

        Post post = new Post(newPostId, postText, timestamp, zeroDownvotes, getLoggedInUserId());

        newPostRef.setValue(post);

        return post;
    }

    @Override
    public Post[] getPostsByUser(String userId) {
        Query query = firebasePostsRef.orderByChild("authorId").equalTo(userId);

        countDownLatch = new CountDownLatch(1);

        ValueEventListener callbackListener = newValueEventListenerForSynchronizingCallback();
        query.addListenerForSingleValueEvent(callbackListener);

        waitForCallback();

        if (dataFromCallback != null) {
            return parsePostsFromDataSnapshot(dataFromCallback.get());
        } else {
            return new Post[0];
        }
    }

    @Override
    public Post downvotePost(String postId) {
        Firebase firebasePostRef = new Firebase(getFirebaseUrlForPost(postId));

        FirebaseSyncRequester<Post> postRequester = new FirebaseSyncRequester<>(firebasePostRef);

        if (!postRequester.exists()) {
            throw new IllegalArgumentException("Post to downvote does not exist");
        }

        Post downvotedPost = newDownvotedPost(postRequester.get());

        firebasePostRef.setValue(downvotedPost);

        return downvotedPost;
    }

    @Override
    public Post deletePost(String postId) {
        Firebase firebasePostRef = new Firebase(getFirebaseUrlForPost(postId));

        FirebaseSyncRequester<Post> postRequester = new FirebaseSyncRequester<>(firebasePostRef);

        if (!postRequester.exists()) {
            throw new IllegalArgumentException("Post to delete does not exist");
        }

        Post postToDelete = postRequester.get();

        firebasePostRef.removeValue();

        return postToDelete;
    }

    /**
     * PostsChangedListeners added here will receive callbacks when the
     * posts in the feed change.
     * It will also receive a callback immediately after being added.
     * @param listener listener to receive updates
     */
    public void addPostsChangedListener(PostsChangedListener listener) {
        postsChangedListeners.add(listener);

        listener.onPostsChanged(currentPostsInFeed);
    }

    public void removePostsChangedListener(PostsChangedListener listener) {
        postsChangedListeners.remove(listener);
    }

    private void checkAuthentication() {
        if (firebasePostsRef == null || firebasePostsRef.getAuth() == null) {
            throw new FirebaseNotAuthenticatedException();
        }
    }

    private void setFirebaseListener() {
        Query postsQuery = firebasePostsRef.limitToFirst(numberOfPostsToRetrieve);

        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentPostsInFeed = parsePostsFromDataSnapshot(dataSnapshot);

                invokePostChangedListeners();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Bitter", "Firebase could not connect");
            }
        });
    }

    private void invokePostChangedListeners() {
        for (PostsChangedListener postsChangedListener : postsChangedListeners) {
            postsChangedListener.onPostsChanged(currentPostsInFeed);
        }
    }

    private String getLoggedInUserId() {
        return firebasePostsRef.getAuth().getUid();
    }

    private void waitForCallback() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ValueEventListener newValueEventListenerForSynchronizingCallback() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataFromCallback.set(dataSnapshot);

                countDownLatch.countDown();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Bitter", "Could not connect to Firebase");
            }
        };
    }

    public static String getFirebaseUrlForPost(String postId) {
        return FIREBASE_POSTS_URL + "/" + postId;
    }

    private static Post[] parsePostsFromDataSnapshot(DataSnapshot dataSnapshot) {
        List<Post> postsList = new LinkedList<>();

        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            postsList.add(postSnapshot.getValue(Post.class));
        }

        return postsList.toArray(new Post[postsList.size()]);
    }

    private static long millisSinceEpoch() {
        return new Date().getTime();
    }

    private static Post newDownvotedPost(Post post) {
        return new Post(post.getId(), post.getText(), post.getTimestamp(), post.getDownvotes() - 1, post.getAuthorId());
    }
}
