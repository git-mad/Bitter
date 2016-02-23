package gitmad.bitter.data.mock;

import java.util.HashMap;
import java.util.Map;

import gitmad.bitter.data.UserProvider;
import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

/**
 * A UserProvider implementation that returns fake users, for testing and development purposes.
 * It will consistently return the same fake user for given input.
 */
public class MockUserProvider implements UserProvider {

    private static final User locallyLoggedInUser = new User("me123", "asdlfkafd-adf");

    private static final User[] fakeUsers = {
            new User("NotGBurdell", "asdf"),
            new User("ASDF", "fdsa"),
            new User("George Washington", "fjdlwe-adf"),
            new User("George Forman", "afdsksd-asdf"),
            locallyLoggedInUser
    };

    @Override
    public User getUser(String userId) {
        return fakeUsers[userIndexFromIdHash(userId)];
    }

    @Override
    public User getAuthorOfPost(Post post) {
        return fakeUsers[userIndexFromIdHash(post.getAuthorId())];
    }

    @Override
    public Map<Post, User> getAuthorsOfPosts(Post... posts) {
        Map<Post, User> authorsMap = new HashMap<>();

        for (Post post : posts) {
            authorsMap.put(post, getAuthorOfPost(post));
        }

        return authorsMap;
    }

    @Override
    public User getAuthorOfComment(Comment comment) {
        return fakeUsers[userIndexFromIdHash(comment.getAuthorId())];
    }

    @Override
    public Map<Comment, User> getAuthorsOfComments(Comment... comments) {
        Map<Comment, User> authorsMap = new HashMap<>();

        for (Comment comment : comments) {
            authorsMap.put(comment, getAuthorOfComment(comment));
        }

        return authorsMap;
    }

    @Override
    public User getLoggedInUser() {
        return locallyLoggedInUser;
    }

    @Override
    public Map<String, Integer> getPostCatergoryCount(String userUid) {
        throw new UnsupportedOperationException();
    }

    /**
     * gets the index of a consistent user from the fakeUsers array, by taking a hash of a user id.
     * This way, one will always be able to retrieve the same fake user using a given fake id.
     * @param userId a non-null String. Does not actually have to match a user id.
     * @return the index of a user in the fakeUsers array.
     */
    private int userIndexFromIdHash(String userId) {
        return Math.abs(userId.hashCode()) % fakeUsers.length;
    }
}
