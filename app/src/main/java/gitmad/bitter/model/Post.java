package gitmad.bitter.model;

/**
 * Created by brian on 9/21/15.
 */
public class Post {
    private String id;
    private String text;
    private long timestamp;
    private int downvotes;
    private String authorId;

    public Post(String id, String text, long timestamp, int downvotes, String authorId) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.downvotes = downvotes;
        this.authorId = authorId;
    }

    public Post() {
    }

    public String getAuthorId() {
        return authorId;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Post)) {
            return false;
        }

        Post other = (Post) o;
        return other.getId().equals(getId());
    }

    @Override
    public String toString() {
        return "id:" + getId();
    }
}
