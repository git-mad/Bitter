package gitmad.bitter;

import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.firebase.client.Firebase;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import gitmad.bitter.data.CategoryProvider;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.firebase.FirebaseCategoryProvider;
import gitmad.bitter.data.firebase.FirebasePostProvider;
import gitmad.bitter.data.firebase.FirebaseSyncRequester;
import gitmad.bitter.data.firebase.FirebaseUserProvider;
import gitmad.bitter.data.firebase.auth.FirebaseAuthManager;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

/**
 * Created by brian on 2/28/16.
 */
public class TestFirebaseCategories extends ApplicationTestCase<BitterApplication> {

    CategoryProvider categoriesProvider;
    UserProvider userProvider;
    PostProvider postProvider;

    FirebaseAuthManager authManager;


    public TestFirebaseCategories() {
        super(BitterApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createApplication();

        authenticateFirebase();

        initProviders();
    }

    @SmallTest
    public void testGetCategories() {
        Firebase categoriesRef = new Firebase(FirebaseCategoryProvider.CATEGORIES_URL);

        FirebaseSyncRequester<Map> categoriesRequester = new FirebaseSyncRequester<>(categoriesRef, Map.class);

        Map<String, Boolean> categoriesMap = categoriesRequester.get();

        String[] categoriesFromProvider = categoriesProvider.getCategories();

        assertEquals("should be same length", categoriesMap.size(), categoriesFromProvider.length);


        Iterator<String> categoryIterator = categoriesMap.keySet().iterator();

        for (int i = 0; categoryIterator.hasNext() && i < categoriesFromProvider.length; i++) {
            assertEquals("categories should be equal", categoryIterator.next(), categoriesFromProvider[i]);
        }
    }

//    public void testGetUserPostCategories() {
//        removePastPosts();
//
//        String[] categories = categoriesProvider.getCategories();
//
//        int numPosts = 10;
//
//        Post[] postsAdded = new Post[numPosts];
//
//        for (int i = 0; i < numPosts; i++) {
//            int randomIndex = (int) (Math.random() * categories.length);
//            String randomCategory = categories[randomIndex];
//
//            Post postAdded = postProvider.addPostSync(newPostWithCategory(randomCategory));
//
//            postsAdded[i] = postAdded;
//        }
//
//        //TODO record post categories and compare when returned
//    }

    private void removePastPosts() {
        User loggedInUser = userProvider.getLoggedInUser();

        Post[] previousPostsByUser = postProvider.getPostsByUser(loggedInUser.getId());

        for (Post p : previousPostsByUser) {
            postProvider.deletePost(p.getId());
        }
    }

    private Post newPostWithCategory(String postCategory) {
        long timestamp = new Date().getTime();
        int zeroDownvotes = 0;

        return  new Post(null, randomText(), timestamp, zeroDownvotes,
                userProvider.getLoggedInUser().getId(), postCategory);
    }

    private String randomText() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        int textLength = 10;

        StringBuilder randomText = new StringBuilder();

        for (int i = 0; i < textLength; i++) {
            int randomIndex = (int) (Math.random() * alphabet.length());
            randomText.append(alphabet.charAt(randomIndex));
        }

        return randomText.toString();
    }

    private void initProviders() {
        categoriesProvider = new FirebaseCategoryProvider();
        userProvider = new FirebaseUserProvider();
        postProvider = new FirebasePostProvider();
    }

    private void authenticateFirebase() {
        authManager = new FirebaseAuthManager(getApplication());
        if (!authManager.isAuthed()) {
            authManager.authenticate();
        }
    }
}
