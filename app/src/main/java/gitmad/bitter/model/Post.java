package gitmad.bitter.model;

/**
 * Created by brian on 9/21/15.
 */
public class Post {
    private User user;
    private String text;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
