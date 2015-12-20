package gitmad.bitter.model;

/**
 * Created by brian on 9/21/15.
 */
public class Post {
    private int id;
    private String text;
    private long timestamp;
    private int downvotes;
    private String authorId;

    public Post(int id, String text, long timestamp, int downvotes, String authorId) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.downvotes = downvotes;
        this.authorId = authorId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public int decrementDownvotes() {
        return --downvotes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }
}
