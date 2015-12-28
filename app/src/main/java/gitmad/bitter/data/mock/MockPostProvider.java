package gitmad.bitter.data.mock;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import gitmad.bitter.R;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.model.Post;

/**
 * This class will mocks a post provider. It does not interact with
 * any server backend. The post ids are indices starting at zero.
 * Their timestamps are roughly equal to the time at which each is
 * instantiated.
 * Downvotes and Author Ids are random. Random author ids should still work
 * consistently with the MockUserProvider.
 */
public class MockPostProvider implements PostProvider {

    private Context context;
    private Map<Integer, Post> posts;

    private boolean isLoggingEnabled;

    private int nextId;

    public MockPostProvider(Context context) {
        this.context = context;

        initializeMockPosts();

        setLoggingEnabled(true);
    }

    @Override
    public Post[] getPosts(int numPostsRequested) {
        logMessageIfEnabled("MockPostProvider#getPosts()");

        int numPostsToReturn;
        if (numPostsRequested > posts.size()) {
            numPostsToReturn = posts.size();
        } else {
            numPostsToReturn = numPostsRequested;
        }

        Post[] postArray = new Post[numPostsToReturn];

        for (int i = 0; i < numPostsToReturn; i++) {
            postArray[i] = posts.get(i);
        }

        return postArray;
    }

    @Override
    public Post getPost(String id) throws IllegalArgumentException {
        logMessageIfEnabled(String.format("MockPostProvider#getPost(%d)", id));

        if (Integer.parseInt(id) >= posts.size()) {
            throw new IllegalArgumentException("No post with Id specified");
        }
        return posts.get(id);
    }

    @Override
    public Post addPost(String postText) {
        logMessageIfEnabled(String.format("MockPostProvider#addPost(%s)", postText));

        int nextId = getNextId();
        Post newPost = createPostWithText(postText, Integer.toString(nextId), getMockLoggedInUserId());
        posts.put(nextId, newPost);
        return newPost;
    }

    @Override
    public Post[] getPostsByUser(String userId) {
        logMessageIfEnabled(String.format("MockPostProvider#getPostsByUser(%s)", userId));

        List<Post> postList = new LinkedList<>();

        for (Integer key : posts.keySet()) {
            Post post = posts.get(key);

            if (post.getAuthorId().equals(userId)) {
                postList.add(post);
            }
        }

        return postList.toArray(new Post[postList.size()]);
    }

    @Override
    public Post downvotePost(String postId) {
        logMessageIfEnabled(String.format("MockPostProvider#downvotePost(%d)", postId));

        Post p = getPost(postId);

        Post downvotedPost = new Post(p.getId(), p.getText(), p.getTimestamp(),
                p.getDownvotes() - 1, p.getAuthorId());

        posts.put(Integer.parseInt(downvotedPost.getId()), downvotedPost);

        return downvotedPost;
    }

    @Override
    public Post deletePost(String postId) {
        logMessageIfEnabled(String.format("MockPostProvider#deletePost(%d)", postId));

        return posts.remove(postId);
    }

    /**
     * if set to true, all operations are logged to the debug stream.
     * @param enabled whether logging should be enabled or not.
     */
    public void setLoggingEnabled(boolean enabled) {
        isLoggingEnabled = enabled;
    }

    private void initializeMockPosts() {
        String[] postsText = readMockPostTextFromResource(R.array.mock_posts);

        posts = new HashMap<>();

        /*
            create new posts with ids 0...postsText.length, and use these id's as keys
            in the Map.
         */
        for (int i = 0; i < postsText.length; i++) {
            posts.put(i, createRandomPostWithText(postsText[i], Integer.toString(i)));
        }
    }

    private String[] readMockPostTextFromResource(int arrayResource) {
        return context.getResources().getStringArray(arrayResource);
    }

    private Post createRandomPostWithText(String text, String postId) {
        String randomAuthorId = UUID.randomUUID().toString();
        long postCreatedTimestamp = new Date().getTime();

        return new Post(postId, text, postCreatedTimestamp, getRandomDownvoteCount(), randomAuthorId);
    }

    private Post createPostWithText(String text, String postId, String authorId) {
        long postCreatedTimestamp = new Date().getTime();
        final int zeroDownvotes = 0;
        return new Post(postId, text, postCreatedTimestamp, zeroDownvotes, authorId);
    }

    public static int getRandomDownvoteCount() {
        return (int) (Math.random() * 20);
    }

    private int getNextId() {
        return nextId++;
    }

    private String getMockLoggedInUserId() {
        return new MockUserProvider().getLoggedInUser().getId();
    }

    private void logMessageIfEnabled(String message) {
        if (isLoggingEnabled) {
            Log.d("Bitter", message);
        }
    }
}
