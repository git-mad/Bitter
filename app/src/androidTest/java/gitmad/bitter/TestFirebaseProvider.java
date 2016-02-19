package gitmad.bitter;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.authentication.AuthenticationManager;

import org.junit.After;
import org.junit.Before;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import gitmad.bitter.data.CommentProvider;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.firebase.FirebaseCommentProvider;
import gitmad.bitter.data.firebase.FirebasePostProvider;
import gitmad.bitter.data.firebase.FirebaseUserProvider;
import gitmad.bitter.data.firebase.auth.FirebaseAuthManager;
import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

/**
 * Created by brian on 12/29/15.
 */
public class TestFirebaseProvider extends ApplicationTestCase<BitterApplication> {

    private static final String[] FAKE_POSTS_TEXT = {
            "aasdff dsafasdf aij; jvodisja",
            "bfasdojofaifinvoisievda",
            "casdfajojovjoise",
            "dasdjf;lajdkjv;as;dkf"
    };

    private PostProvider postProvider;
    private UserProvider userProvider;
    private CommentProvider commentProvider;

    public TestFirebaseProvider() {
        super(BitterApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();

        authenticate();

        initializeFirebaseProviders();
    }

    private void authenticate() {
        FirebaseAuthManager authManager = new FirebaseAuthManager(getApplication());
        if (!authManager.isAuthed()) {
            authManager.authenticate();
        }
    }

    @SmallTest
    public void testGettingPosts() {
        final int numPosts = 10;
        Post[] posts = postProvider.getPosts(numPosts);

        for (int i = 0; i < posts.length; i++) {
            assertNotNull(posts[i]);
        }
    }

    @SmallTest
    public void testAddingAndGettingPosts() {
        Stack<Post> newPostsStack = addFakePosts();

        Post[] postsFromFirebase = postProvider.getPosts(newPostsStack.size());

        assertEquals("Problem getting posts; array from database has wrong size",
                newPostsStack.size(), postsFromFirebase.length);


        for (Post postFromFirebase: postsFromFirebase) {
            assertEquals("Post not correctly added or not in correct order\n"
                            + "posts from Firebase: " + Arrays.toString(postsFromFirebase)
                            + "\n" + "posts before sending: " + newPostsStack.toString(),
                    postFromFirebase, newPostsStack.pop());
        }
    }

    @SmallTest
    public void testGetPostsByUser() {
        Stack<Post> fakePostsAdded = addFakePosts();

        User loggedInUser = userProvider.getLoggedInUser();

        Post[] postsByLoggedInUser = postProvider.getPostsByUser(loggedInUser.getId());

        for (Post postQueried : postsByLoggedInUser) {
            Post postAdded = fakePostsAdded.pop();

            assertEquals("Posts not equal or not in correct order", postAdded, postQueried);
        }
    }

    @SmallTest
    public void testGetSinglePost() {
        Post postAdded = postProvider.addPost(FAKE_POSTS_TEXT[0]);

        Post postQueried = postProvider.getPost(postAdded.getId());

        assertEquals("posts not equal", postAdded, postQueried);
    }

    @SmallTest
    public void testDownvotePost() {
        Post postAdded = postProvider.addPost(FAKE_POSTS_TEXT[0]);

        Post postDownvoted = postProvider.downvotePost(postAdded.getId());

        assertEquals("downvoted post should have one less(?) downvote than original",
                postAdded.getDownvotes() - 1, postDownvoted.getDownvotes());
    }

    @SmallTest
    public void testAddAndGetComment() {
        Post post = postProvider.addPost(FAKE_POSTS_TEXT[0]);

        Comment addedComment = commentProvider.addComment(FAKE_POSTS_TEXT[0], post.getId());

        Comment queriedComment = commentProvider.getComment(addedComment.getId());

        assertEquals("Comment queried should be the same as added", addedComment, queriedComment);
    }

    @SmallTest
    public void testGetCommentByPost() {
        Post post = postProvider.addPost(FAKE_POSTS_TEXT[0]);

        Stack<Comment> addedComments = addFakeCommentsToPost(post);

        Comment[] queriedComments = commentProvider.getCommentsOnPost(post.getId());

        for (Comment queriedCommment : queriedComments) {
            Comment addedComment = addedComments.pop();

            assertEquals("Comments not equal or out of order", addedComment, queriedCommment);
        }
    }

    @SmallTest
    public void testGetCommentsByUser() {
        Post firstPost = postProvider.addPost(FAKE_POSTS_TEXT[0]);
        Post secondPost = postProvider.addPost(FAKE_POSTS_TEXT[1]);

        Stack<Comment> commentsOnFirstPost = addFakeCommentsToPost(firstPost);
        Stack<Comment> commentsOnSecondPost = addFakeCommentsToPost(secondPost);

        List<Comment> allCommentsAdded = new LinkedList<>(commentsOnFirstPost);
        allCommentsAdded.addAll(commentsOnSecondPost);

        Comment[] queriedComments = commentProvider.getCommentsByUser(userProvider.getLoggedInUser().getId());

        assertEquals("incorrect number of comments returned",
                allCommentsAdded.size(), queriedComments.length);

        for (Comment comment : queriedComments) {
            if (allCommentsAdded.contains(comment)) {
                allCommentsAdded.remove(comment);
            } else {
                fail("Returned a comment that was not added.");
            }
        }

        assertEquals("did not return all comments, some expected where not found.", 0, allCommentsAdded.size());
    }

    @NonNull
    private Stack<Comment> addFakeCommentsToPost(Post post) {
        Stack<Comment> addedComments = new Stack<>();
        for (String commentText : FAKE_POSTS_TEXT) {
            Comment newComment = commentProvider.addComment(commentText, post.getId());
            addedComments.push(newComment);
        }
        return addedComments;
    }

    @After
    public void takeDown() {
        List<Post> fakePosts = getFakePosts();

        removeCommentsFromPosts(fakePosts);

        removePosts(fakePosts);
    }

    private void removePosts(List<Post> fakePosts) {
        for (Post post : fakePosts) {
            postProvider.deletePost(post.getId());
        }
    }

    private List<Post> getFakePosts() {
        Post[] pastFiftyPosts = postProvider.getPosts(50);
        List<Post> fakePosts = new LinkedList<>();

        for (Post post : pastFiftyPosts) {
            if (hasFakePostText(post.getText())) {
                fakePosts.add(post);
            }
        }

        return fakePosts;
    }

    private void removeCommentsFromPosts(List<Post> posts) {
        for (Post post : posts) {
            Comment[] commentsToRemove = commentProvider.getCommentsOnPost(post.getId());

            removeComments(commentsToRemove);
        }
    }

    private void removeComments(Comment[] commentsToRemove) {
        for (Comment comment : commentsToRemove) {
            commentProvider.deleteComment(comment.getId());
        }
    }

    private void initializeFirebaseProviders() {
        postProvider = new FirebasePostProvider();
        userProvider = new FirebaseUserProvider();
        commentProvider = new FirebaseCommentProvider();
    }

    private boolean hasFakePostText(String postText) {
        for (String fakePostText : FAKE_POSTS_TEXT) {
            if (postText.equals(fakePostText)) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    private Stack<Post> addFakePosts() {
        Stack<Post> newPostsStack = new Stack<>();

        for (int i = 0; i < 10; i++) {
            for (String postText : FAKE_POSTS_TEXT) {
                Post newPost = ((FirebasePostProvider) postProvider).addPostSync(postText);

                newPostsStack.push(newPost);
            }
        }
        return newPostsStack;
    }

}
