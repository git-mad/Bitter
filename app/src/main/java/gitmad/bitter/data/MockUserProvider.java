package gitmad.bitter.data;

import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

/**
 * A UserProvider implementation that returns fake users, for testing and development purposes.
 * It will consistently return the same fake user for given input.
 */
public class MockUserProvider implements UserProvider {

    private static final User[] fakeUsers = {
            new User("NotGBurdell", "asdf"),
            new User("ASDF", "fdsa"),
            new User("George Washington", "fjdlwe-adf"),
            new User("George Forman", "afdsksd-asdf")
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
    public User getAuthorOfComment(Comment comment) {
        return fakeUsers[userIndexFromIdHash(comment.getAuthorId())];
    }

    /**
     * gets the index of a consistent user from the fakeUsers array, by taking a hash of a user id.
     * This way, one will always be able to retrieve the same fake user using a given fake id.
     * @param userId a non-null String. Does not actually have to match a user id.
     * @return the index of a user in the fakeUsers array.
     */
    private int userIndexFromIdHash(String userId) {
        return userId.hashCode() % fakeUsers.length;
    }
}
