package gitmad.bitter.data;

import java.util.List;

/**
 * Created by brian on 8/27/15.
 */
public interface PostProvider {
    /**
     * @return all of the posts available to this PostProvider
     */
    public String[] getPosts();

    /**
     * @param index the id or index of the desired post
     * @return the post with the index or id specified.
     * @throws IllegalArgumentException if a post with the id or index specified is not found
     */
    public String getPost(int index) throws IllegalArgumentException;
}
