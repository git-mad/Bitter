package gitmad.bitter;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import gitmad.bitter.data.CommentProvider;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.mock.MockCommentProvider;
import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.data.mock.MockUserProvider;
import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;
/**
 * Testing the MockUserProvider, MockPostProvider, and MockCommentProvider.
 */
public class TestMockDataProvider extends InstrumentationTestCase {
    private UserProvider userProvider;
    private PostProvider postProvider;
    private CommentProvider commentProvider;

//    @Before
    public void setUp() {
        userProvider = new MockUserProvider();
        postProvider = new MockPostProvider(getInstrumentation()
                .getTargetContext());
        commentProvider = new MockCommentProvider(getInstrumentation()
                .getTargetContext());
    }

    public void testAddPost() {
        final String newPostText = "I hate writing test cases, but it kinda " +
                "helps.";

        Post newPost = postProvider.addPostSync(newPostText);

        assertEquals("Should be able to get added post from " +
                        "postProvider#getPost()",
                newPost, postProvider.getPost(newPost.getId()));

        final int numPostsToRetrieve = 1;
        assertEquals("Should be able to get added post from " +
                        "postProvider#getPosts()",
                newPost, postProvider.getPosts(numPostsToRetrieve)[0]);
    }

    public void testDeletePost() {
        final int numPostsToRetrieve = 5;
        List<Post> postList = toArrayList(postProvider.getPosts
                (numPostsToRetrieve));
        Post firstPostInFeed = postList.get(0);
        Post secondPostInFeed = postList.get(1);

        postProvider.deletePost(secondPostInFeed.getId());
        postList.remove(1);

        assertTrue("Feed should have second post removed", Arrays.equals(
                postList.toArray(new Post[postList.size()]), postProvider
                        .getPosts(numPostsToRetrieve - 1)));

        postProvider.deletePost(firstPostInFeed.getId());
        postList.remove(0);

        assertTrue("Feed should have first post removed", Arrays.equals(
                postList.toArray(new Post[postList.size()]), postProvider
                        .getPosts(numPostsToRetrieve - 2)));
    }

    public void testDownvote() {
        final int numPostsToRetrieve = 1;
        Post firstPostInFeed = postProvider.getPosts(numPostsToRetrieve)[0];
        String postId = firstPostInFeed.getId();

        Post downvotedPost = postProvider.downvotePost(postId);

        assertEquals("Returned post should have one less(?) downvote.",
                firstPostInFeed.getDownvotes() - 1, downvotedPost
                        .getDownvotes());
        assertEquals("Stored post should have one less(?) downvote.",
                firstPostInFeed.getDownvotes() - 1, postProvider.getPost
                        (postId).getDownvotes());
    }

    public void testGetCommentsByUser() {
        final String userId = "asdf"; // random choice

        Comment[] comments = commentProvider.getCommentsByUser(userId);

        assertTrue("Should always produce some comments", comments.length > 0);

        for (Comment comment : comments) {
            assertEquals("Comments should all have user Id requested",
                    userId, comment.getAuthorId());
        }
    }

    public void testGetCommentsOnPost() {
        final String postId = "3"; // random choice

        Comment[] comments = commentProvider.getCommentsOnPost(postId);

        assertTrue("Should always produce some comments", comments.length > 0);

        for (Comment comment : comments) {
            assertEquals("Comments should all have post Id requested",
                    postId, comment.getPostId());
        }
    }

    public void testGetConsistentPostAuthors() {
        final int numPosts = 10;
        Post[] posts = postProvider.getPosts(numPosts);
        User[] users = new User[posts.length];

        for (int i = 0; i < posts.length; i++) {
            users[i] = userProvider.getAuthorOfPost(posts[i]);
        }

        // check if the userProvider returns the same mock users per post //

        for (int i = 0; i < posts.length; i++) {
            assertEquals("Users should be the same for the same post.",
                    users[i], userProvider.getAuthorOfPost(posts[i]));
        }
    }

    public void testGetPosts() {
        final int numPosts = 5;
        Post[] postsArray = postProvider.getPosts(numPosts);

        assertEquals("Posts array should contain number of posts queried",
                numPosts, postsArray.length);
        for (int i = 0; i < numPosts; i++) {
            assertNotNull("no index in array returned should be null",
                    postsArray[i]);
        }
    }

    private <T> List<T> toArrayList(T[] array) {
        List<T> list = new ArrayList<>();

        Collections.addAll(list, array);

        return list;
    }
}