package gitmad.bitter.data.firebase;

import com.firebase.client.Firebase;

import gitmad.bitter.data.UserProvider;
import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

/**
 * Synchronous method calls for accessing User data from Firebase
 */
public class FirebaseUserProvider implements UserProvider {
    public static final String FIREBASE_USERS_URL = "https://bitter-gitmad.firebaseio.com/users";

    @Override
    public User getUser(String userId) {
        Firebase userFirebaseRef = new Firebase(getFirebaseUrlForUser(userId));

        FirebaseSyncRequester<User> userRequester = new FirebaseSyncRequester<>(userFirebaseRef);

        if (!userRequester.exists()) {
            throw new IllegalArgumentException("User does not exist");
        }

        return userRequester.get();
    }

    @Override
    public User getAuthorOfPost(Post post) {
        return getUser(post.getAuthorId());
    }

    @Override
    public User getAuthorOfComment(Comment comment) {
        return getUser(comment.getAuthorId());
    }

    @Override
    public User getLoggedInUser() {
        Firebase firebaseUsersRef = new Firebase(FIREBASE_USERS_URL);

        return getUser(firebaseUsersRef.getAuth().getUid());
    }

    private static String getFirebaseUrlForUser(String userId) {
        return FIREBASE_USERS_URL + "/" + userId;
    }
}
