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
import gitmad.bitter.data.firebase.auth.FirebaseNotAuthenticatedException;
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

//    private Post[] currentPostsInFeed;
    private List<PostsChangedListener> postsChangedListeners;

    public FirebasePostProvider() {
        dataFromCallback = new AtomicReference<>();

        firebasePostsRef = new Firebase(FIREBASE_POSTS_URL);

        checkAuthentication();

        postsChangedListeners = new LinkedList<>();

//        setFirebaseListener();
    }

    @Override
    public Post[] getPosts(int numPosts) {
        Query query = firebasePostsRef.orderByChild("timestamp").limitToLast(numPosts);

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
    public Post getPost(String id) throws IllegalArgumentException {
        Firebase firebasePostRef = new Firebase(getFirebaseUrlForPost(id));

        String firebaseUrl = getFirebaseUrlForPost(id);

        FirebaseSyncRequester firebaseSyncRequester = new FirebaseSyncRequester(firebasePostRef);

        if (!firebaseSyncRequester.exists()) {
            throw new IllegalArgumentException("Post with id " + id + " does not exist.");
        }

        return firebaseSyncRequester.getPost();
    }

    @Override
    public Post addPostAsync(String postText) {
        Firebase newPostRef = firebasePostsRef.push();

        String newPostId = newPostRef.getKey();
        long timestamp = millisSinceEpoch();
        final int zeroDownvotes = 0;

        Post post = new Post(newPostId, postText, timestamp, zeroDownvotes, getLoggedInUserId());

        newPostRef.setValue(post);

        return post;
    }

    public Post addPostSync(String postText) {
        Firebase newPostRef = firebasePostsRef.push();

        String newPostId = newPostRef.getKey();
        long timestamp = millisSinceEpoch();
        final int zeroDownvotes = 0;

        Post post = new Post(newPostId, postText, timestamp, zeroDownvotes, getLoggedInUserId());

        final CountDownLatch latch = new CountDownLatch(1);

        newPostRef.setValue(post, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    throw firebaseError.toException();
                }

                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

        FirebaseSyncRequester postRequester = new FirebaseSyncRequester(firebasePostRef);

        if (!postRequester.exists()) {
            throw new IllegalArgumentException("Post to downvote does not exist");
        }

        Post downvotedPost = newDownvotedPost(postRequester.getPost());

        firebasePostRef.setValue(downvotedPost);

        return downvotedPost;
    }

    @Override
    public Post deletePost(String postId) {
        Firebase firebasePostRef = new Firebase(getFirebaseUrlForPost(postId));

        FirebaseSyncRequester postRequester = new FirebaseSyncRequester(firebasePostRef);

        if (!postRequester.exists()) {
            throw new IllegalArgumentException("Post to delete does not exist");
        }

        Post postToDelete = postRequester.getPost();

        final CountDownLatch latch = new CountDownLatch(1);

        firebasePostRef.removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    throw firebaseError.toException();
                }

                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return postToDelete;
    }

    /**
     * PostsChangedListeners added here will receive callbacks when the
     * posts in the feed change.
     * It will also receive a callback immediately after being added.
     * @param listener listener to receive updates
     */
    public void addPostsChangedListener(PostsChangedListener listener) {
        throw new UnsupportedOperationException();
//        postsChangedListeners.add(listener);

//        listener.onPostsChanged(currentPostsInFeed);
    }

    public void removePostsChangedListener(PostsChangedListener listener) {
        throw new UnsupportedOperationException();
//        postsChangedListeners.remove(listener);
    }

    private void checkAuthentication() {
        if (firebasePostsRef == null || firebasePostsRef.getAuth() == null) {
            throw new FirebaseNotAuthenticatedException();
        }
    }

//    private void setFirebaseListener() {
//        Query postsQuery = firebasePostsRef
//                .orderByChild("timestamp")
//                .limitToLast(numberOfPostsToRetrieve);
//
//        countDownLatch = new CountDownLatch(1);
//
//        postsQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                currentPostsInFeed = parsePostsFromDataSnapshot(dataSnapshot);
//
//                countDownLatch.countDown();
//
//                invokePostChangedListeners();
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                Log.e("Bitter", "Firebase could not connect");
//            }
//        });
//
//        waitForCallback();
//    }

    private void invokePostChangedListeners() {
//        for (PostsChangedListener postsChangedListener : postsChangedListeners) {
//            postsChangedListener.onPostsChanged(currentPostsInFeed);
//        }
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
            postsList.add(0, postSnapshot.getValue(Post.class));
        }

        return postsList.toArray(new Post[postsList.size()]);
    }

    private static long millisSinceEpoch() {
        return new Date().getTime();
    }

    private static Post newDownvotedPost(Post post) {
        return new Post(post.getId(), post.getText(), post.getTimestamp(), post.getDownvotes() - 1, post.getAuthorId());
    }

//    private Post[] getMostRecent(int numberOfPostsToRetrieve) {
//        Post[] toReturn = new Post[numberOfPostsToRetrieve];
//
//        for (int i = currentPostsInFeed.length - 1, j = 0; i >= currentPostsInFeed.length - numberOfPostsToRetrieve; i--, j++) {
//            toReturn[j] = currentPostsInFeed[i];
//        }
//
//        return toReturn;
//    }
}
