package gitmad.bitter.model;

/**
 * Created by brian on 9/21/15.
 */
public class Post {
    private User user;
    private String text;
    private int downvotes = 0;
    private int id;

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

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public void setId(int id) { this.id = id; }

    public int getId() {
        return id;
    }
}
