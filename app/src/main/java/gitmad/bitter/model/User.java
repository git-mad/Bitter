package gitmad.bitter.model;

import java.util.Collection;

/**
 * Created by brian on 9/21/15.
 */
public class User {
    private String name;
    private Collection<Post> posts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Post> getPosts() {
        return posts;
    }

    public void setPosts(Collection<Post> posts) {
        this.posts = posts;
    }
}
