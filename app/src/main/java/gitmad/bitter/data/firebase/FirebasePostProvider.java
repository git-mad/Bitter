package gitmad.bitter.data.firebase;

import android.util.Log;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.firebase.auth.FirebaseNotAuthenticatedException;
import gitmad.bitter.model.Post;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Provides methods to access data from the Bitter Firebase repo
 * <p/>
 * FirebasePostProvider is not thread safe. Create one for each thread on
 * which it is needed.
 */
public class FirebasePostProvider implements PostProvider {

    public static final String FIREBASE_POSTS_URL = "https://bitter-gitmad" +
            ".firebaseio.com/posts";
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

    public static String getFirebaseUrlForPost(String postId) {
        return FIREBASE_POSTS_URL + "/" + postId;
    }

    private static long millisSinceEpoch() {
        return new Date().getTime();
    }

    private static Post newDownvotedPost(Post post) {
        return new Post(post.getId(), post.getText(), post.getTimestamp(),
                post.getDownvotes() - 1, post.getAuthorId(), post.getCategory
                ());
    }

    private static Post[] parsePostsFromDataSnapshot(DataSnapshot
                                                             dataSnapshot) {
        List<Post> postsList = new LinkedList<>();

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            postsList.add(0, postSnapshot.getValue(Post.class));
        }

        return postsList.toArray(new Post[postsList.size()]);
    }

    @Override
    public Post addPostAsync(String postText) { //TODO should pass in post,
        // rather than text
        Firebase newPostRef = firebasePostsRef.push();

        String newPostId = newPostRef.getKey();
        long timestamp = millisSinceEpoch();
        final int zeroDownvotes = 0;

        Post post = new Post(newPostId, postText, timestamp, zeroDownvotes,
                getLoggedInUserId(), "sports");

        newPostRef.setValue(post);

        return post;
    }

    public Post addPostSync(String postText) { //TODO should pass in post,
        // rather than text
        Firebase newPostRef = firebasePostsRef.push();

        String newPostId = newPostRef.getKey();
        long timestamp = millisSinceEpoch();
        final int zeroDownvotes = 0;

        Post post = new Post(newPostId, postText, timestamp, zeroDownvotes,
                getLoggedInUserId(), "sports");

        final CountDownLatch latch = new CountDownLatch(1);

        newPostRef.setValue(post, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase
                    firebase) {
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
    public Post deletePost(String postId) {
        Firebase firebasePostRef = new Firebase(getFirebaseUrlForPost(postId));

        FirebaseSyncRequester<Post> postRequester = new FirebaseSyncRequester
                (firebasePostRef, Post.class);

        if (!postRequester.exists()) {
            throw new IllegalArgumentException("Post to delete does not exist");
        }

        Post postToDelete = postRequester.get();

        final CountDownLatch latch = new CountDownLatch(1);

        firebasePostRef.removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase
                    firebase) {
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

    @Override
    public Post downvotePost(String postId) {
        Firebase firebasePostRef = new Firebase(getFirebaseUrlForPost(postId));

        FirebaseSyncRequester<Post> postRequester = new
                FirebaseSyncRequester<>(firebasePostRef, Post.class);

        if (!postRequester.exists()) {
            throw new IllegalArgumentException("Post to downvote does not " +
                    "exist");
        }

        Post downvotedPost = newDownvotedPost(postRequester.get());

        firebasePostRef.setValue(downvotedPost);

        return downvotedPost;
    }

    @Override
    public Post getPost(String id) throws IllegalArgumentException {
        Firebase firebasePostRef = new Firebase(getFirebaseUrlForPost(id));

        String firebaseUrl = getFirebaseUrlForPost(id);

        FirebaseSyncRequester<Post> firebaseSyncRequester = new
                FirebaseSyncRequester<>(firebasePostRef, Post.class);

        if (!firebaseSyncRequester.exists()) {
            throw new IllegalArgumentException("Post with id " + id + " does " +
                    "not exist.");
        }

        return firebaseSyncRequester.get();
    }

    @Override
    public Post[] getPosts(int numPosts) {
        Query query = firebasePostsRef.orderByChild("timestamp").limitToLast
                (numPosts);

        countDownLatch = new CountDownLatch(1);

        ValueEventListener callbackListener =
                newValueEventListenerForSynchronizingCallback();
        query.addListenerForSingleValueEvent(callbackListener);

        waitForCallback();

        if (dataFromCallback != null) {
            return parsePostsFromDataSnapshot(dataFromCallback.get());
        } else {
            return new Post[0];
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

    @Override
    public Post[] getPostsByUser(String userId) {
        Query query = firebasePostsRef.orderByChild("authorId").equalTo(userId);

        countDownLatch = new CountDownLatch(1);

        ValueEventListener callbackListener =
                newValueEventListenerForSynchronizingCallback();
        query.addListenerForSingleValueEvent(callbackListener);

        waitForCallback();

        if (dataFromCallback != null) {
            return parsePostsFromDataSnapshot(dataFromCallback.get());
        } else {
            return new Post[0];
        }
    }

    /**
     * PostsChangedListeners added here will receive callbacks when the
     * posts in the feed change.
     * It will also receive a callback immediately after being added.
     *
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

    private String getLoggedInUserId() {
        return firebasePostsRef.getAuth().getUid();
    }

    private void invokePostChangedListeners() {
//        for (PostsChangedListener postsChangedListener :
// postsChangedListeners) {
//            postsChangedListener.onPostsChanged(currentPostsInFeed);
//        }
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

    private void waitForCallback() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    private Post[] getMostRecent(int numberOfPostsToRetrieve) {
//        Post[] toReturn = new Post[numberOfPostsToRetrieve];
//
//        for (int i = currentPostsInFeed.length - 1, j = 0; i >=
// currentPostsInFeed.length - numberOfPostsToRetrieve; i--, j++) {
//            toReturn[j] = currentPostsInFeed[i];
//        }
//
//        return toReturn;
//    }
}
