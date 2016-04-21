package gitmad.bitter.data.firebase;

import android.support.annotation.NonNull;
import com.firebase.client.Firebase;
import gitmad.bitter.data.CategoryProvider;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Synchronous method calls for accessing User data from Firebase
 */
public class FirebaseUserProvider implements UserProvider {
    public static final String FIREBASE_USERS_URL = "https://bitter-gitmad" +
            ".firebaseio.com/users";

    private static String[] getCommentAuthorIds(Comment[] comments) {
        String[] authors = new String[comments.length];

        for (int i = 0; i < comments.length; i++) {
            authors[i] = comments[i].getAuthorId();
        }

        return authors;
    }

    private static String getFirebaseUrlForUser(String userId) {
        return FIREBASE_USERS_URL + "/" + userId;
    }

    private static String[] getPostAuthorIds(Post[] posts) {
        String[] authors = new String[posts.length];

        for (int i = 0; i < posts.length; i++) {
            authors[i] = posts[i].getAuthorId();
        }

        return authors;
    }

    private static Firebase newFirebaseRefForUser(String userId) {
        return new Firebase(getFirebaseUrlForUser(userId));
    }

    @NonNull
    private static FirebaseSyncRequester<User>[] startRequestersForUsers
            (String[] userIds) {
        FirebaseSyncRequester[] userRequesters = new
                FirebaseSyncRequester[userIds.length];

        for (int i = 0; i < userIds.length; i++) {
            Firebase authorRef = newFirebaseRefForUser(userIds[i]);
            FirebaseSyncRequester<User> userRequester = new
                    FirebaseSyncRequester<>(authorRef, User.class);

            userRequesters[i] = userRequester;
        }

        return userRequesters;
    }

    @Override
    public User getAuthorOfComment(Comment comment) {
        return getUser(comment.getAuthorId());
    }

    @Override
    public User getAuthorOfPost(Post post) {
        return getUser(post.getAuthorId());
    }

    @Override
    public Map<Comment, User> getAuthorsOfComments(Comment... comments) {

        String[] authorIds = getCommentAuthorIds(comments);

        FirebaseSyncRequester<User>[] userRequesters =
                startRequestersForUsers(authorIds);

        Map<Comment, User> authorsMap = new HashMap<>();

        for (int i = 0; i < comments.length; i++) {
            authorsMap.put(comments[i], userRequesters[i].get());
        }

        return authorsMap;
    }

    @Override
    public Map<Post, User> getAuthorsOfPosts(Post... posts) {
        String[] authorIds = getPostAuthorIds(posts);

        FirebaseSyncRequester<User>[] userRequesters =
                startRequestersForUsers(authorIds);

        Map<Post, User> authorsMap = new HashMap<>();

        for (int i = 0; i < posts.length; i++) {
            authorsMap.put(posts[i], userRequesters[i].get());
        }

        return authorsMap;
    }

    @Override
    public User getLoggedInUser() {
        Firebase firebaseUsersRef = new Firebase(FIREBASE_USERS_URL);

        return getUser(firebaseUsersRef.getAuth().getUid());
    }

    @Override
    public Map<String, Integer> getPostCategoryCount(String userUid) {
        PostProvider postProvider = new FirebasePostProvider();

        Map<String, Integer> postCategoryCounts = initCategoryMap();

        Post[] allUserPosts = postProvider.getPostsByUser(userUid);

        for (Post post : allUserPosts) {
            String category = post.getCategory();
            postCategoryCounts.put(category, postCategoryCounts.get(category)
                    + 1);
        }

        return postCategoryCounts;
    }

    @Override
    public User getUser(String userId) {
        Firebase userFirebaseRef = newFirebaseRefForUser(userId);

        FirebaseSyncRequester<User> userRequester = new FirebaseSyncRequester
                (userFirebaseRef, User.class);

        if (!userRequester.exists()) {
            throw new IllegalArgumentException("User does not exist");
        }

        return userRequester.get();
    }

    @NonNull
    private Map<String, Integer> initCategoryMap() {
        Map<String, Integer> postCategoryCounts = new HashMap<>();

        CategoryProvider categoryProvider = new FirebaseCategoryProvider();

        for (String category : categoryProvider.getCategories()) {
            postCategoryCounts.put(category, 0);
        }
        return postCategoryCounts;
    }
}
